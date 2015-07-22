package net.glowstone.service.command;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.*;

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
    public Optional<CommandResult> process(CommandSource source, String arguments) {
        return null;
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    @Override
    public Optional<? extends Text> getShortDescription(CommandSource source) {
        return null;
    }

    @Override
    public Optional<? extends Text> getHelp(CommandSource source) {
        return null;
    }

    @Override
    public Text getUsage(CommandSource source) {
        return null;
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
    public Set<? extends CommandMapping> getAll(String alias) {
        return null;
    }

    @Override
    public Multimap<String, CommandMapping> getAll() {
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
}
