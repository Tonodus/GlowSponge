package net.glowstone.event.message;

import com.google.common.base.Optional;
import net.glowstone.event.GlowGameEvent;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

public class GlowCommandEvent extends GlowGameEvent implements CommandEvent {
    private final CommandSource source;
    private final String cmd;
    private final String args;

    private CommandResult result;
    private boolean cancelled;

    public GlowCommandEvent(CommandSource source, String cmd, String args) {
        this.source = source;
        this.cmd = cmd;
        this.args = args;
    }

    @Override
    public CommandSource getSource() {
        return source;
    }

    @Override
    public String getCommand() {
        return cmd;
    }

    @Override
    public String getArguments() {
        return args;
    }

    @Override
    public Optional<CommandResult> getResult() {
        return Optional.fromNullable(result);
    }

    @Override
    public void setResult(CommandResult result) {
        this.result = result;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
