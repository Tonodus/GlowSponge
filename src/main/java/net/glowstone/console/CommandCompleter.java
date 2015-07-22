package net.glowstone.console;

import jline.console.completer.Completer;
import net.glowstone.GlowServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class CommandCompleter implements Completer {
    private final GlowServer server;
    private static final Logger logger = LoggerFactory.getLogger(CommandCompleter.class);

    public CommandCompleter(GlowServer server) {
        this.server = server;
    }

    @Override
    public int complete(final String buffer, int cursor, List<CharSequence> candidates) {
        try {
            List<String> completions = server.getGame().getScheduler().syncIfNeeded(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    return server.getGame().getCommandDispatcher().getSuggestions(server.getConsole(), buffer);
                }
            });
            if (completions == null) {
                return cursor;  // no completions
            }
            candidates.addAll(completions);

            // location to position the cursor at (before autofilling takes place)
            return buffer.lastIndexOf(' ') + 1;
        } catch (Throwable t) {
            logger.warn("Error while tab completing", t);
            return cursor;
        }
    }
}
