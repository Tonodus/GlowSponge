package net.glowstone.resourcepack;

import com.google.common.base.Optional;
import org.spongepowered.api.resourcepack.ResourcePack;

import java.net.MalformedURLException;
import java.net.URL;

public class GlowResourcePack implements ResourcePack {
    private final String hash;
    private final String url;

    public GlowResourcePack(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Optional<String> getHash() {
        return Optional.fromNullable(hash);
    }
}
