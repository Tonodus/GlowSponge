package net.glowstone.id;

public interface IdManager<I, T> {
    T getById(I id);

    I getId(T t);
}
