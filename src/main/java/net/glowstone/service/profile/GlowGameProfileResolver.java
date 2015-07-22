package net.glowstone.service.profile;

import com.google.common.util.concurrent.ListenableFuture;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.service.profile.GameProfileResolver;

import java.util.Collection;
import java.util.UUID;

public class GlowGameProfileResolver implements GameProfileResolver {
    @Override
    public ListenableFuture<GameProfile> get(UUID uniqueId) {
        return get(uniqueId, true);
    }

    @Override
    public ListenableFuture<GameProfile> get(UUID uniqueId, boolean useCache) {
        return null;
    }

    @Override
    public ListenableFuture<GameProfile> get(String name) {
        return get(name, true);
    }

    @Override
    public ListenableFuture<GameProfile> get(String name, boolean useCache) {
        return null;
    }

    @Override
    public ListenableFuture<Collection<GameProfile>> getAllByName(Iterable<String> names, boolean useCache) {
        return null;
    }

    @Override
    public ListenableFuture<Collection<GameProfile>> getAllById(Iterable<UUID> uniqueIds, boolean useCache) {
        return null;
    }

    @Override
    public Collection<GameProfile> getCachedProfiles() {
        return null;
    }

    @Override
    public Collection<GameProfile> match(String lastKnownName) {
        return null;
    }
}
