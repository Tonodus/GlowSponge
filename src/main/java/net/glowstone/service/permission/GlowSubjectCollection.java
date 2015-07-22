package net.glowstone.service.permission;

import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.context.Context;

import java.util.Map;
import java.util.Set;

public class GlowSubjectCollection implements SubjectCollection {
    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public Subject get(String identifier) {
        return null;
    }

    @Override
    public boolean hasRegistered(String identifier) {
        return false;
    }

    @Override
    public Iterable<Subject> getAllSubjects() {
        return null;
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(String permission) {
        return null;
    }

    @Override
    public Map<Subject, Boolean> getAllWithPermission(Set<Context> contexts, String permission) {
        return null;
    }
}
