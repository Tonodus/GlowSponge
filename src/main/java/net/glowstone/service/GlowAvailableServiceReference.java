package net.glowstone.service;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.spongepowered.api.service.ServiceReference;

public class GlowAvailableServiceReference<T> implements ServiceReference<T> {
    private final T value;

    public GlowAvailableServiceReference(T value) {
        this.value = value;
    }

    @Override
    public Optional<T> ref() {
        return Optional.of(value);
    }

    @Override
    public T await() throws InterruptedException {
        return value;
    }

    @Override
    public void executeWhenPresent(Predicate<T> run) {
        run.apply(value);
    }
}
