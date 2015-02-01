package net.glowstone.service;

import com.google.common.base.Optional;
import net.glowstone.GlowGame;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.ProvisioningException;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.ServiceReference;

public class GlowServiceManager implements ServiceManager {
    private final GlowGame game;

    public GlowServiceManager(GlowGame game) {
        this.game = game;
    }

    @Override
    public <T> void setProvider(Object plugin, Class<T> service, T provider) throws ProviderExistsException {

    }

    @Override
    public <T> Optional<T> provide(Class<T> service) {
        return null;
    }

    @Override
    public <T> ServiceReference<T> potentiallyProvide(Class<T> service) {
        return null;
    }

    @Override
    public <T> T provideUnchecked(Class<T> service) throws ProvisioningException {
        return null;
    }
}
