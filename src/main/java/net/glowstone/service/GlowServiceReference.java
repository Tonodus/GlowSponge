package net.glowstone.service;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.spongepowered.api.service.ServiceReference;

import java.util.ArrayList;
import java.util.List;

public class GlowServiceReference<T> implements ServiceReference<T> {
    private final Object sync = new Object();

    private final Class<T> clazz;
    private Optional<T> ref;
    private final List<Predicate<T>> runnables = new ArrayList<>();

    public GlowServiceReference(Class<T> clazz) {
        this.ref = Optional.absent();
        this.clazz = clazz;
    }

    @Override
    public Optional<T> ref() {
        return ref;
    }

    @Override
    public T await() throws InterruptedException {
        synchronized (sync) {
            while (!ref.isPresent()) {
                sync.wait();
            }
        }

        return ref.get();
    }

    public void notifyPresence(T newRef) {
        if (ref.isPresent()) {
            throw new IllegalStateException("ServiceReference already filled!");
        }

        synchronized (sync) {
            ref = Optional.of(newRef);
            sync.notifyAll();
        }

        for (Predicate<T> run : runnables) {
            run.apply(newRef);
        }

        runnables.clear();
    }

    @Override
    public void executeWhenPresent(Predicate<T> run) {
        runnables.add(run);
    }

    public boolean doesListenFor(Class service) {
        return service == clazz;
    }
}
