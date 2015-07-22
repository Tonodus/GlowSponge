package net.glowstone.console;

import jline.console.ConsoleReader;
import net.glowstone.GlowServer;
import net.glowstone.text.TextUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.source.ConsoleSource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Console {
    private static final String CONSOLE_DATE = "HH:mm:ss";
    private static final String FILE_DATE = "yyyy/MM/dd HH:mm:ss";
    private static final Logger logger = Logger.getLogger("");

    private final GlowServer server;

    private ConsoleReader reader;
    private final ConsoleSource consoleSource;

    private boolean useJLine = false;
    private boolean running = true;

    public Console(GlowServer server) {
        this.server = server;

        // install Ansi code handler, which makes colors work on Windows
        AnsiConsole.systemInstall();

        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        // add log handler which writes to console
        logger.addHandler(new FancyConsoleHandler());

        // reader must be initialized before standard streams are changed
        try {
            reader = new ConsoleReader();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception initializing console reader", ex);
        }
        reader.addCompleter(new CommandCompleter(server));

        // set system output streams
        System.setOut(new PrintStream(new LoggerOutputStream(Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(Level.WARNING), true));

        consoleSource = new GlowConsoleSource(this);
    }

    public void startConsole(boolean jLine) {
        this.useJLine = jLine;

        Thread thread = new ConsoleCommandThread();
        thread.setName("ConsoleCommandThread");
        thread.setDaemon(true);
        thread.start();
    }

    public void startFile(String logfile) {
        File parent = new File(logfile).getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            logger.warning("Could not create log folder: " + parent);
        }
        Handler fileHandler = new RotatingFileHandler(logfile);
        fileHandler.setFormatter(new DateOutputFormatter(FILE_DATE));
        logger.addHandler(fileHandler);
    }

    public void stop() {
        running = false;
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
            handler.close();
        }
    }

    public void sendMessage(Text text) {
        StringBuilder builder = new StringBuilder();

        for (Text.Literal literal : TextUtils.toLiterals(text)) {
            if (useJLine) {
                if (literal.getColor() != TextColors.NONE) {
                    builder.append(getFormatCode(literal));
                }
            }

            builder.append(literal.getContent());
        }

        logger.info(builder.toString());
    }

    private CharSequence getFormatCode(Text.Literal toFormat) {
        TextColor color = toFormat.getColor();

        if (color == TextColors.BLACK) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString();
        }
        if (color == TextColors.DARK_BLUE) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString();
        }
        if (color == TextColors.DARK_GREEN) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString();
        }
        if (color == TextColors.DARK_AQUA) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString();
        }
        if (color == TextColors.DARK_RED) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString();
        }
        if (color == TextColors.DARK_PURPLE) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString();
        }
        if (color == TextColors.GOLD) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString();
        }
        if (color == TextColors.GRAY) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString();
        }
        if (color == TextColors.DARK_GRAY) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString();
        }
        if (color == TextColors.BLUE) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString();
        }
        if (color == TextColors.GREEN) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString();
        }
        if (color == TextColors.AQUA) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString();
        }
        if (color == TextColors.RED) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString();
        }
        if (color == TextColors.LIGHT_PURPLE) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString();
        }
        if (color == TextColors.YELLOW) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString();
        }
        if (color == TextColors.WHITE) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString();
        }

        if (toFormat.getStyle().isObfuscated().or(false)) {
            return Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString();
        }
        if (toFormat.getStyle().isBold().or(false)) {
            return Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString();
        }
        if (toFormat.getStyle().hasStrikethrough().or(false)) {
            return Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString();
        }
        if (toFormat.getStyle().hasUnderline().or(false)) {
            return Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString();
        }
        if (toFormat.getStyle().isItalic().or(false)) {
            return Ansi.ansi().a(Ansi.Attribute.ITALIC).toString();
        }
        if (color == TextColors.RESET) {
            return Ansi.ansi().a(Ansi.Attribute.RESET).toString();
        }
        return "";
    }

    public ConsoleSource getConsoleSource() {
        return consoleSource;
    }

    private class CommandTask implements Runnable {
        private final String command;

        public CommandTask(String command) {
            this.command = command;
        }

        @Override
        public void run() {
            server.getGame().getCommandDispatcher().process(consoleSource, command);
        }
    }

    private class ConsoleCommandThread extends Thread {
        @Override
        public void run() {
            String command = "";
            while (running) {
                try {
                    if (useJLine) {
                        command = reader.readLine(">", null);
                    } else {
                        command = reader.readLine();
                    }

                    if (command == null || command.trim().length() == 0)
                        continue;

                    server.getGame().getScheduler().getTaskBuilder().execute(new CommandTask(command.trim())).submitServer();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Error while reading commands", ex);
                }
            }
        }
    }

    private static class LoggerOutputStream extends ByteArrayOutputStream {
        private final String separator = System.getProperty("line.separator");
        private final Level level;

        public LoggerOutputStream(Level level) {
            super();
            this.level = level;
        }

        @Override
        public synchronized void flush() throws IOException {
            super.flush();
            String record = this.toString();
            super.reset();

            if (record.length() > 0 && !record.equals(separator)) {
                logger.logp(level, "LoggerOutputStream", "log" + level, record);
            }
        }
    }

    private class FancyConsoleHandler extends ConsoleHandler {
        public FancyConsoleHandler() {
            setFormatter(new DateOutputFormatter(CONSOLE_DATE));
            setOutputStream(System.out);
        }

        @Override
        public synchronized void flush() {
            try {
                if (useJLine) {
                    reader.print(ConsoleReader.RESET_LINE + "");
                    reader.flush();
                    super.flush();
                    try {
                        reader.drawLine();
                    } catch (Throwable ex) {
                        reader.getCursorBuffer().clear();
                    }
                    reader.flush();
                } else {
                    super.flush();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "I/O exception flushing console output", ex);
            }
        }
    }

    private static class RotatingFileHandler extends StreamHandler {
        private final SimpleDateFormat dateFormat;
        private final String template;
        private final boolean rotate;
        private String filename;

        public RotatingFileHandler(String template) {
            this.template = template;
            rotate = template.contains("%D");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            filename = calculateFilename();
            updateOutput();
        }

        private void updateOutput() {
            try {
                setOutputStream(new FileOutputStream(filename, true));
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Unable to open " + filename + " for writing", ex);
            }
        }

        private void checkRotate() {
            if (rotate) {
                String newFilename = calculateFilename();
                if (!filename.equals(newFilename)) {
                    filename = newFilename;
                    // note that the console handler doesn't see this message
                    super.publish(new LogRecord(Level.INFO, "Log rotating to: " + filename));
                    updateOutput();
                }
            }
        }

        private String calculateFilename() {
            return template.replace("%D", dateFormat.format(new Date()));
        }

        @Override
        public synchronized void publish(LogRecord record) {
            if (!isLoggable(record)) {
                return;
            }
            checkRotate();
            super.publish(record);
            super.flush();
        }

        @Override
        public synchronized void flush() {
            checkRotate();
            super.flush();
        }
    }

    private class DateOutputFormatter extends Formatter {
        private final SimpleDateFormat date;

        public DateOutputFormatter(String pattern) {
            this.date = new SimpleDateFormat(pattern);
        }

        @Override
        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();

            builder.append(date.format(record.getMillis()));
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(formatMessage(record));
            builder.append('\n');

            if (record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                builder.append(writer.toString());
            }

            return builder.toString();
        }
    }
}
