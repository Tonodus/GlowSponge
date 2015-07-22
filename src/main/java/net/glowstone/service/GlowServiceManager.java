package net.glowstone.service;

import com.google.common.base.Optional;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.ProvisioningException;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.ServiceReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GlowServiceManager implements ServiceManager {
    private final Map<Class<?>, Object> providers = new HashMap<>();
    private final Collection<GlowServiceReference<?>> serviceReferences = new ArrayList<>();

    @Override
    public <T> void setProvider(Object plugin, Class<T> service, T provider) throws ProviderExistsException {
        if (providers.containsKey(service)) {
            throw new ProviderExistsException();
        }

        providers.put(service, provider);

        for (GlowServiceReference reference : serviceReferences) {
            if (reference.doesListenFor(service)) {
                reference.notifyPresence(provider);
            }
        }
    }

    @Override
    public <T> Optional<T> provide(Class<T> service) {
        return Optional.fromNullable((T) providers.get(service));
    }

    @Override
    public <T> ServiceReference<T> potentiallyProvide(Class<T> service) {
        if (providers.containsKey(service)) {
            return new GlowAvailableServiceReference<>((T) providers.get(service));
        } else {
            GlowServiceReference<T> ref = new GlowServiceReference<>(service);
            serviceReferences.add(ref);
            return ref;
        }
    }

    @Override
    public <T> T provideUnchecked(Class<T> service) throws ProvisioningException {
        T t = (T) providers.get(service);
        if (t == null) {
            throw new ProvisioningException(service);
        }

        return t;
    }
}
