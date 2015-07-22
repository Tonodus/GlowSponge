package net.glowstone.data.manipulator;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.manipulator.ListData;

import java.util.ArrayList;
import java.util.List;

public abstract class GlowListData<E, T extends ListData<E, T>> extends GlowDataManipulator<T> implements ListData<E, T> {
    protected List<E> list;

    public GlowListData(Class<T> manipulatorClass) {
        super(manipulatorClass);
        this.list = new ArrayList<>();
    }

    @Override
    public List<E> getAll() {
        return ImmutableList.copyOf(list);
    }

    @Override
    public Optional<E> get(int index) {
        return Optional.fromNullable(list.get(index));
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public T set(E... elements) {
        list.clear();
        for (E element : elements) {
            list.add(element);
        }
        return (T) this;
    }

    @Override
    public T set(Iterable<E> elements) {
        list.clear();
        for (E element : elements) {
            list.add(element);
        }
        return (T) this;
    }

    @Override
    public T set(int index, E element) {
        list.set(index, element);
        return (T) this;
    }

    @Override
    public T add(E element) {
        list.add(element);
        return (T) this;
    }

    @Override
    public T add(int index, E element) {
        list.add(index, element);
        return (T) this;
    }

    @Override
    public T remove(int index) {
        list.remove(index);
        return (T) this;
    }
}
