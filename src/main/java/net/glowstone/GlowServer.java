package net.glowstone;

import com.google.common.base.Optional;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import net.glowstone.entity.EntityIdManager;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.CraftingManager;
import net.glowstone.io.PlayerDataService;
import net.glowstone.net.GlowNetworkServer;
import net.glowstone.net.SessionRegistry;
import net.glowstone.net.query.QueryServer;
import net.glowstone.net.rcon.RconServer;
import net.glowstone.scheduler.GlowScheduler;
import net.glowstone.scheduler.WorldScheduler;
import net.glowstone.util.*;
import net.glowstone.util.bans.GlowBanList;
import net.glowstone.util.bans.UuidListFile;
import org.apache.commons.lang3.Validate;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.ChannelRegistrationException;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.WorldGenerator;

import java.io.File;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyPair;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The core class of the Glowstone server.
 * @author Graham Edgecombe
 */
public final class GlowServer implements Server {

    /**
     * The logger for this class.
     */
    public static final Logger logger = Logger.getLogger("Minecraft");

    /**
     * The game version supported by the server.
     */
    public static final String GAME_VERSION = "1.8";

    /**
     * The protocol version supported by the server.
     */
    public static final int PROTOCOL_VERSION = 47;

    /**
     * A list of all the active {@link net.glowstone.net.GlowSession}s.
     */
    private final SessionRegistry sessions = new SessionRegistry();

    /**
     * The console manager of this server.
     */
    private final ConsoleManager consoleManager = new ConsoleManager(this);

    /**
     * The help map for the server.
     */
    private final GlowHelpMap helpMap = new GlowHelpMap(this);

    /**
     * The crafting manager for this server.
     */
    private final CraftingManager craftingManager = new CraftingManager();

    /**
     * The configuration for the server.
     */
    private final ServerConfig config;

    /**
     * The list of OPs on the server.
     */
    private final UuidListFile opsList;

    /**
     * The list of players whitelisted on the server.
     */
    private final UuidListFile whitelist;

    /**
     * The BanList for player names.
     */
    private GlowBanList nameBans;

    /**
     * The BanList for IP addresses.
     */
    private GlowBanList ipBans;

    /**
     * The EntityIdManager for this server.
     */
    private final EntityIdManager entityIdManager = new EntityIdManager();

    /**
     * The world this server is managing.
     */
    private final WorldScheduler worlds = new WorldScheduler();

    /**
     * The task scheduler used by this server.
     */
    private final GlowScheduler scheduler = new GlowScheduler(this, worlds);

    /**
     * Whether the server is shutting down
     */
    private boolean isShuttingDown = false;

    /**
     * Whether the whitelist is in effect.
     */
    private boolean whitelistEnabled;

    /**
     * The size of the area to keep protected around the spawn point.
     */
    private int spawnRadius;

    /**
     * The ticks until a player who has not played the game has been kicked, or 0.
     */
    private int idleTimeout;

    /**
     * A RSA key pair used for encryption and authentication
     */
    private final KeyPair keyPair = SecurityUtils.generateKeyPair();

    /**
     * The network server used for network communication
     */
    private final GlowNetworkServer networkServer = new GlowNetworkServer(this);

    /**
     * The query server for this server, or null if disabled.
     */
    private QueryServer queryServer;

    /**
     * The Rcon server for this server, or null if disabled.
     */
    private RconServer rconServer;

    /**
     * The default icon, usually blank, used for the server list.
     */
    private GlowServerIcon defaultIcon;

    /**
     * The server port.
     */
    private int port;

    /**
     * A set of all online players.
     */
    private final Set<GlowPlayer> onlinePlayers = new HashSet<>();

    /**
     * A view of all online players.
     */
    private final Set<GlowPlayer> onlineView = Collections.unmodifiableSet(onlinePlayers);

    private final GlowGame game;
    private Message motd;

    /**
     * Creates a new server.
     */
    public GlowServer(GlowGame game, ServerConfig config) {
        this.game = game;
        this.config = config;
        // stuff based on selected config directory
        opsList = new UuidListFile(config.getFile("ops.json"));
        whitelist = new UuidListFile(config.getFile("whitelist.json"));
        //nameBans = new GlowBanList(this, BanList.Type.NAME);
        //ipBans = new GlowBanList(this, BanList.Type.IP);

        loadConfig();
    }

    /**
     * Starts this server.
     */
    public void start() {
        // Determine console mode and start reading input
        consoleManager.startConsole(config.getBoolean(ServerConfig.Key.USE_JLINE));
        consoleManager.startFile(config.getString(ServerConfig.Key.LOG_FILE));

        if (getProxySupport()) {
            if (getOnlineMode()) {
                logger.warning("Proxy support is enabled, but online mode is enabled.");
            } else {
                logger.info("Proxy support is enabled.");
            }
        } else if (!getOnlineMode()) {
            logger.warning("The server is running in offline mode! Only do this if you know what you're doing.");
        }

        // Load player lists
        opsList.load();
        whitelist.load();
        nameBans.load();
        ipBans.load();

        // Start loading plugins
        //TODO

        // Create worlds
        //TODO

        // Finish loading plugins
        //TODO

        scheduler.start();


    }

    public void bindAll() throws BindException {
        bind();
        bindQuery();
        bindRcon();
    }

    /**
     * Binds this server to the address specified in the configuration.
     */
    private void bind() throws BindException {
        SocketAddress address = getBindAddress(ServerConfig.Key.SERVER_PORT);

        logger.info("Binding to address: " + address + "...");
        ChannelFuture future = networkServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();
        if (!channel.isActive()) {
            Throwable cause = future.cause();
            if (cause instanceof BindException) {
                throw (BindException) cause;
            }
            throw new RuntimeException("Failed to bind to address", cause);
        }

        logger.info("Successfully bound to: " + channel.localAddress());
        port = ((InetSocketAddress) channel.localAddress()).getPort();
    }

    /**
     * Binds the query server to the address specified in the configuration.
     */
    private void bindQuery() {
        if (!config.getBoolean(ServerConfig.Key.QUERY_ENABLED)) {
            return;
        }

        SocketAddress address = getBindAddress(ServerConfig.Key.QUERY_PORT);
        queryServer = new QueryServer(this, config.getBoolean(ServerConfig.Key.QUERY_PLUGINS));

        logger.info("Binding query to address: " + address + "...");
        ChannelFuture future = queryServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();
        if (!channel.isActive()) {
            logger.warning("Failed to bind query. Address already in use?");
        }
    }

    /**
     * Binds the rcon server to the address specified in the configuration.
     */
    private void bindRcon() {
        if (!config.getBoolean(ServerConfig.Key.RCON_ENABLED)) {
            return;
        }

        SocketAddress address = getBindAddress(ServerConfig.Key.RCON_PORT);
        rconServer = new RconServer(this, config.getString(ServerConfig.Key.RCON_PASSWORD));

        logger.info("Binding rcon to address: " + address + "...");
        ChannelFuture future = rconServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();
        if (!channel.isActive()) {
            logger.warning("Failed to bind rcon. Address already in use?");
        }
    }

    /**
     * Get the SocketAddress to bind to for a specified service.
     * @param portKey The configuration key for the port to use.
     * @return The SocketAddress
     */
    private SocketAddress getBindAddress(ServerConfig.Key portKey) {
        String ip = config.getString(ServerConfig.Key.SERVER_IP);
        int port = config.getInt(portKey);
        if (ip.length() == 0) {
            return new InetSocketAddress(port);
        } else {
            return new InetSocketAddress(ip, port);
        }
    }

    /**
     * Load the server configuration.
     */
    private void loadConfig() {
        config.load();

        // modifiable values
        spawnRadius = config.getInt(ServerConfig.Key.SPAWN_RADIUS);
        whitelistEnabled = config.getBoolean(ServerConfig.Key.WHITELIST);
        idleTimeout = config.getInt(ServerConfig.Key.PLAYER_IDLE_TIMEOUT);
        craftingManager.initialize();

        // server icon
        defaultIcon = new GlowServerIcon();
        try {
            File file = config.getFile("server-icon.png");
            if (file.isFile()) {
                defaultIcon = new GlowServerIcon(file);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to load server-icon.png", e);
        }
    }


    /**
     * Reloads the server, refreshing settings and plugin information
     */
    public void reload() {
        try {
            // Reload relevant configuration
            loadConfig();
            opsList.load();
            whitelist.load();
            nameBans.load();
            ipBans.load();

            // Reset crafting
            craftingManager.resetRecipes();

            // Load plugins
            //TODO
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Uncaught error while reloading", ex);
        }
    }


    public String toString() {
        return "GlowServer{name=" + getName() + ",version=" + getVersion() + ",minecraftVersion=" + GAME_VERSION + "}";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Access to internals

    /**
     * Gets the session registry.
     * @return The {@link SessionRegistry}.
     */
    public SessionRegistry getSessionRegistry() {
        return sessions;
    }

    /**
     * Gets the entity id manager.
     * @return The {@link EntityIdManager}.
     */
    public EntityIdManager getEntityIdManager() {
        return entityIdManager;
    }

    /**
     * Returns the list of OPs on this server.
     */
    public UuidListFile getOpsList() {
        return opsList;
    }

    /**
     * Returns the list of whitelisted players on this server.
     */
    public UuidListFile getWhitelist() {
        return whitelist;
    }

    /**
     * Returns the folder where configuration files are stored
     */
    public File getConfigDir() {
        return config.getDirectory();
    }

    /**
     * Return the crafting manager.
     * @return The server's crafting manager.
     */
    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    /**
     * The key pair generated at server start up
     * @return The key pair generated at server start up
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * Returns the player data service attached to the first world.
     * @return The server's player data service.
     */
    public PlayerDataService getPlayerDataService() {
        return worlds.getWorlds().get(0).getStorage().getPlayerDataService();
    }

    /**
     * Get the threshold to use for network compression defined in the config.
     * @return The compression threshold, or -1 for no compression.
     */
    public int getCompressionThreshold() {
        return config.getInt(ServerConfig.Key.COMPRESSION_THRESHOLD);
    }

    /**
     * Get whether worlds should keep their spawns loaded by default.
     * @return Whether to keep spawns loaded by default.
     */
    public boolean keepSpawnLoaded() {
        return config.getBoolean(ServerConfig.Key.PERSIST_SPAWN);
    }

    /**
     * Get whether parsing of data provided by a proxy is enabled.
     * @return True if a proxy is providing data to use.
     */
    public boolean getProxySupport() {
        return config.getBoolean(ServerConfig.Key.PROXY_SUPPORT);
    }

    /**
     * Get whether to use color codes in Rcon responses.
     * @return True if color codes will be present in Rcon responses
     */
    public boolean useRconColors() {
        return config.getBoolean(ServerConfig.Key.RCON_COLORS);
    }

    /**
     * Get the resource pack url for this server, or {@code null} if not set.
     * @return The url of the resource pack to use, or {@code null}
     */
    public String getResourcePackURL() {
        return config.getString(ServerConfig.Key.RESOURCE_PACK);
    }

    /**
     * Get the resource pack hash for this server, or the empty string if not set.
     * @return The hash of the resource pack, or the empty string
     */
    public String getResourcePackHash() {
        return config.getString(ServerConfig.Key.RESOURCE_PACK_HASH);
    }

    /**
     * Get whether achievements should be announced.
     * @return True if achievements should be announced in chat.
     */
    public boolean getAnnounceAchievements() {
        return config.getBoolean(ServerConfig.Key.ANNOUNCE_ACHIEVEMENTS);
    }

    /**
     * Sets a player as being online internally.
     * @param player player to set online/offline
     * @param online whether the player is online or offline
     */
    public void setPlayerOnline(GlowPlayer player, boolean online) {
        Validate.notNull(player);
        if (online) {
            onlinePlayers.add(player);
        } else {
            onlinePlayers.remove(player);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Static server properties

    public String getName() {
        return "Glowstone";
    }


    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }


    public String getBukkitVersion() {
        return getClass().getPackage().getSpecificationVersion();
    }


    public Logger getLogger() {
        return logger;
    }


    public boolean isPrimaryThread() {
        return scheduler.isPrimaryThread();
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        return (Collection) onlineView;
    }

    @Override
    public Optional<Player> getPlayer(String name) {
        name = name.toLowerCase();
        Player bestPlayer = null;
        int bestDelta = -1;
        for (Player player : getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(name)) {
                int delta = player.getName().length() - name.length();
                if (bestPlayer == null || delta < bestDelta) {
                    bestPlayer = player;
                }
            }
        }
        return Optional.fromNullable(bestPlayer);
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        for (Player player : getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) {
                return Optional.of(player);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<World> getWorld(String name) {
        return (Optional) Optional.fromNullable(worlds.getWorld(name));
    }

    @Override
    public Optional<World> loadWorld(String worldName) {
        return null;
    }

    @Override
    public boolean unloadWorld(World world) {
        if (world instanceof GlowWorld) {
            GlowWorld glowWorld = (GlowWorld) world;
            if (glowWorld.unload())
                return worlds.removeWorld(glowWorld);
        }

        return false;
    }

    @Override
    public World createWorld(String worldName, WorldGenerator generator, long seed) {
        return null;
    }

    @Override
    public World createWorld(String worldName, WorldGenerator generator) {
        return null;
    }

    @Override
    public World createWorld(String worldName) {
        return null;
    }

    @Override
    public int getRunningTimeTicks() {
        return 0;
    }

    @Override
    public void broadcastMessage(Message message) {
        for (Player player : onlineView)
            player.sendMessage(ChatTypes.CHAT, message);
    }

    @Override
    public Optional<InetSocketAddress> getBoundAddress() {
        return null;
    }

    @Override
    public Optional<World> getWorld(UUID uid) {
        for (GlowWorld world : worlds.getWorlds()) {
            if (uid.equals(world.getUID())) {
                return (Optional) Optional.of(world);
            }
        }
        return Optional.absent();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<World> getWorlds() {
        // Shenanigans needed to cast List<GlowWorld> to List<World>
        return (List) worlds.getWorlds();
    }

    /**
     * Add a world to the internal world collection.
     * @param world The world to add.
     */
    void addWorld(GlowWorld world) {
        worlds.addWorld(world);
    }

    @Override
    public boolean hasWhitelist() {
        return whitelistEnabled;
    }

    @Override
    public void setHasWhitelist(boolean enabled) {
        whitelistEnabled = enabled;
        config.set(ServerConfig.Key.WHITELIST, whitelistEnabled);
        config.save();
    }

    @Override
    public boolean getOnlineMode() {
        return config.getBoolean(ServerConfig.Key.ONLINE_MODE);
    }

    @Override
    public int getMaxPlayers() {
        return config.getInt(ServerConfig.Key.MAX_PLAYERS);
    }


    @Override
    public Message getMotd() {
        return motd;
    }

    public void shutdown() {
        shutdown(null);
    }

    @Override
    public void shutdown(Message kickMessage) {
// Just in case this gets called twice
        if (isShuttingDown) {
            return;
        }
        isShuttingDown = true;
        logger.info("The server is shutting down...");

        // Disable plugins
        //TODO

        // Kick all players (this saves their data too)
        for (GlowPlayer player : onlineView) {
            player.kickPlayer(kickMessage);
        }

        // Stop the network servers - starts the shutdown process
        // It may take a second or two for Netty to totally clean up
        networkServer.shutdown();
        if (queryServer != null) {
            queryServer.shutdown();
        }
        if (rconServer != null) {
            rconServer.shutdown();
        }

        // Save worlds
        for (World world : getWorlds()) {
            logger.info("Saving world: " + world.getName());
            unloadWorld(world);
        }

        // Stop scheduler and console
        scheduler.stop();
        consoleManager.stop();

        // Wait for a while and terminate any rogue threads
        new ShutdownMonitorThread().start();
    }

    @Override
    public void registerChannel(Object plugin, ChannelListener listener, String channel) throws ChannelRegistrationException {

    }

    @Override
    public List<String> getRegisteredChannels() {
        return null;
    }
}
