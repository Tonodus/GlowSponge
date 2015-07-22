package net.glowstone.net.handler.login;

import com.flowpowered.networking.MessageHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.glowstone.GlowServer;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.entity.meta.profile.PlayerProperty;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.login.EncryptionKeyResponseMessage;
import net.glowstone.util.UuidUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class EncryptionKeyResponseHandler implements MessageHandler<GlowSession, EncryptionKeyResponseMessage> {

    @Override
    public void handle(GlowSession session, EncryptionKeyResponseMessage message) {
        final PrivateKey privateKey = session.getServer().getKeyPair().getPrivate();

        // create rsaCipher
        Cipher rsaCipher;
        try {
            rsaCipher = Cipher.getInstance("RSA");
        } catch (GeneralSecurityException ex) {
            GlowServer.logger.log(Level.SEVERE, "Could not initialize RSA cipher", ex);
            session.disconnect("Unable to initialize RSA cipher.");
            return;
        }

        // decrypt shared secret
        SecretKey sharedSecret;
        try {
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            sharedSecret = new SecretKeySpec(rsaCipher.doFinal(message.getSharedSecret()), "AES");
        } catch (Exception ex) {
            GlowServer.logger.log(Level.WARNING, "Could not decrypt shared secret", ex);
            session.disconnect("Unable to decrypt shared secret.");
            return;
        }

        // decrypt verify token
        byte[] verifyToken;
        try {
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            verifyToken = rsaCipher.doFinal(message.getVerifyToken());
        } catch (Exception ex) {
            GlowServer.logger.log(Level.WARNING, "Could not decrypt verify token", ex);
            session.disconnect("Unable to decrypt verify token.");
            return;
        }

        // check verify token
        if (!Arrays.equals(verifyToken, session.getVerifyToken())) {
            session.disconnect("Invalid verify token.");
            return;
        }

        // initialize stream encryption
        session.enableEncryption(sharedSecret);

        // create hash for auth
        String hash;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(session.getSessionId().getBytes());
            digest.update(sharedSecret.getEncoded());
            digest.update(session.getServer().getKeyPair().getPublic().getEncoded());

            // BigInteger takes care of sign and leading zeroes
            hash = new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            GlowServer.logger.log(Level.SEVERE, "Unable to generate SHA-1 digest", ex);
            session.disconnect("Failed to hash login data.");
            return;
        }

        // start auth thread
        Thread clientAuthThread = new Thread(new ClientAuthRunnable(session, session.getVerifyUsername(), hash));
        clientAuthThread.setName("ClientAuthThread{" + session.getVerifyUsername() + "}");
        clientAuthThread.start();
    }

    private static class ClientAuthRunnable implements Runnable {

        private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined";

        private final GlowSession session;
        private final String username;
        private final String postURL;

        private ClientAuthRunnable(GlowSession session, String username, String hash) {
            this.session = session;
            this.username = username;
            this.postURL = BASE_URL + "?username=" + username + "&serverId=" + hash;
        }

        @Override
        public void run() {
            try {
                // authenticate
                URLConnection connection = new URL(postURL).openConnection();
                JsonObject json;
                try (InputStream is = connection.getInputStream()) {
                    try {
                        json = (JsonObject) new JsonParser().parse(new InputStreamReader(is));
                    } catch (JsonParseException e) {
                        GlowServer.logger.warning("Username \"" + username + "\" failed to authenticate!");
                        session.disconnect("Failed to verify username!");
                        return;
                    }
                }

                final String name = json.get("name").getAsString();
                final String id = json.get("id").getAsString();

                // parse UUID
                final UUID uuid;
                try {
                    uuid = UuidUtils.fromFlatString(id);
                } catch (IllegalArgumentException ex) {
                    GlowServer.logger.log(Level.SEVERE, "Returned authentication UUID invalid: " + id, ex);
                    session.disconnect("Invalid UUID.");
                    return;
                }

                final JsonArray propsArray = json.get("properties").getAsJsonArray();

                // parse properties
                final List<PlayerProperty> properties = new ArrayList<>(propsArray.size());
                for (Object obj : propsArray) {
                    JsonObject propJson = (JsonObject) obj;
                    String propName = propJson.get("name").getAsString();
                    String value = propJson.get("value").getAsString();
                    String signature = propJson.get("signature").getAsString();
                    properties.add(new PlayerProperty(propName, value, signature));
                }
                /*
                final AsyncPlayerPreLoginEvent event = EventFactory.onPlayerPreLogin(name, session.getAddress(), uuid);
                if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
                    session.disconnect(event.getKickMessage(), true);
                    return;
                }*/

                // spawn player
                session.getServer().getGame().getScheduler().getTaskBuilder().execute(new Runnable() {
                    @Override
                    public void run() {
                        session.setPlayer(new PlayerProfile(name, uuid, properties));
                    }
                }).submitServer();
            } catch (Exception e) {
                GlowServer.logger.log(Level.SEVERE, "Error in authentication thread", e);
                session.disconnect("Internal error during authentication.", true);
            }
        }
    }
}
