package net.glowstone.console;

import com.google.common.base.Optional;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.source.ConsoleSource;

import java.util.List;
import java.util.Set;

public class GlowConsoleSource implements ConsoleSource {
    private final Console console;

    public GlowConsoleSource(Console console) {
        this.console = console;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(Text... messages) {
        for (Text text : messages) {
            console.sendMessage(text);
        }
    }

    @Override
    public void sendMessage(Iterable<Text> messages) {
        for (Text text : messages) {
            console.sendMessage(text);
        }
    }

    @Override
    public MessageSink getMessageSink() {
        return null;
    }

    @Override
    public void setMessageSink(MessageSink sink) {

    }

    @Override
    public String getIdentifier() {
        return "CONSOLE";
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.of((CommandSource) this);
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return null;
    }

    @Override
    public SubjectData getSubjectData() {
        return null;
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return null;
    }

    @Override
    public boolean hasPermission(Set<Context> contexts, String permission) {
        return true;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return null;
    }

    @Override
    public boolean isChildOf(Subject parent) {
        return false;
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return false;
    }

    @Override
    public List<Subject> getParents() {
        return null;
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return null;
    }

    @Override
    public Set<Context> getActiveContexts() {
        return null;
    }
}
