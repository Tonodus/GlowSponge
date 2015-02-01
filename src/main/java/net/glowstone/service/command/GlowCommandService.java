package net.glowstone.service.command;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandMapping;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;
import java.util.Set;

public class GlowCommandService implements CommandService {
    @Override
    public Optional<CommandMapping> register(Object plugin, CommandCallable callable, String... alias) {
        return null;
    }

    @Override
    public Optional<CommandMapping> register(Object plugin, CommandCallable callable, List<String> aliases) {
        return null;
    }

    @Override
    public Optional<CommandMapping> register(Object plugin, CommandCallable callable, List<String> aliases, Function<List<String>, List<String>> callback) {
        return null;
    }

    @Override
    public Optional<CommandMapping> remove(String alias) {
        return null;
    }

    @Override
    public Optional<CommandMapping> removeMapping(CommandMapping mapping) {
        return null;
    }

    @Override
    public Set<PluginContainer> getPluginContainers() {
        return null;
    }

    @Override
    public Set<CommandMapping> getOwnedBy(PluginContainer container) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<? extends CommandMapping> getCommands() {
        return null;
    }

    @Override
    public Set<String> getPrimaryAliases() {
        return null;
    }

    @Override
    public Set<String> getAliases() {
        return null;
    }

    @Override
    public Optional<? extends CommandMapping> get(String alias) {
        return null;
    }

    @Override
    public boolean containsAlias(String alias) {
        return false;
    }

    @Override
    public boolean containsMapping(CommandMapping mapping) {
        return false;
    }

    @Override
    public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
        return false;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    @Override
    public Optional<String> getShortDescription() {
        return null;
    }

    @Override
    public Optional<String> getHelp() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }
}
