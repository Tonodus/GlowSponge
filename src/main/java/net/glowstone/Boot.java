package net.glowstone;

public final class Boot {
    public static void main(String... args) {
        System.out.println("Starting server...");
        GlowGame game = new GlowGame();
        game.startGame();
    }
   /*
     * Creates a new server on TCP port 25565 and starts listening for
     * connections.
     * @param args The command-line arguments.

    public static void main(String[] args) {
        try {
            // parse arguments and read config
            final ServerConfig config = parseArguments(args);
            if (config == null) {
                return;
            }

            ConfigurationSerialization.registerClass(GlowOfflinePlayer.class);
            GlowPotionEffect.register();
            GlowEnchantment.register();

            // start server
            final GlowServer server = new GlowServer(config);
            server.start();
            server.bind();
            server.bindQuery();
            server.bindRcon();
            logger.info("Ready for connections.");
        } catch (BindException ex) {
            // descriptive bind error messages
            logger.severe("The server could not bind to the requested address.");
            if (ex.getMessage().startsWith("Cannot assign requested address")) {
                logger.severe("The 'server.ip' in your configuration may not be valid.");
                logger.severe("Unless you are sure you need it, try removing it.");
                logger.severe(ex.toString());
            } else if (ex.getMessage().startsWith("Address already in use")) {
                logger.severe("The address was already in use. Check that no server is");
                logger.severe("already running on that port. If needed, try killing all");
                logger.severe("Java processes using Task Manager or similar.");
                logger.severe(ex.toString());
            } else {
                logger.log(Level.SEVERE, "An unknown bind error has occurred.", ex);
            }
            System.exit(1);
        } catch (Throwable t) {
            // general server startup crash
            logger.log(Level.SEVERE, "Error during server startup.", t);
            System.exit(1);
        }
    }

    private static ServerConfig parseArguments(String[] args) {
        final Map<ServerConfig.Key, Object> parameters = new EnumMap<>(ServerConfig.Key.class);
        String configDirName = "config";
        String configFileName = "glowstone.yml";

        // Calculate acceptable parameters
        for (int i = 0; i < args.length; i++) {
            final String opt = args[i];

            if (!opt.startsWith("-")) {
                System.err.println("Ignored invalid option: " + opt);
                continue;
            }

            // Help and version
            if ("--help".equals(opt) || "-h".equals(opt) || "-?".equals(opt)) {
                System.out.println("Available command-line options:");
                System.out.println("  --help, -h, -?                 Shows this help message and exits.");
                System.out.println("  --version, -v                  Shows version information and exits.");
                System.out.println("  --configdir <directory>        Sets the configuration directory.");
                System.out.println("  --configfile <file>            Sets the configuration file.");
                System.out.println("  --port, -p <port>              Sets the server listening port.");
                System.out.println("  --host, -H <ip | hostname>     Sets the server listening address.");
                System.out.println("  --onlinemode, -o <onlinemode>  Sets the server's online-mode.");
                System.out.println("  --jline <true/false>           Enables or disables JLine console.");
                System.out.println("  --plugins-dir, -P <directory>  Sets the plugin directory to use.");
                System.out.println("  --worlds-dir, -W <directory>   Sets the world directory to use.");
                System.out.println("  --update-dir, -U <directory>   Sets the plugin update folder to use.");
                System.out.println("  --max-players, -M <director>   Sets the maximum amount of players.");
                System.out.println("  --world-name, -N <name>        Sets the main world name.");
                System.out.println("  --log-pattern, -L <pattern>    Sets the log file pattern (%D for date).");
                return null;
            } else if ("--version".equals(opt) || "-v".equals(opt)) {
                System.out.println("Glowstone version: " + GlowServer.class.getPackage().getImplementationVersion());
                System.out.println("Bukkit version:    " + GlowServer.class.getPackage().getSpecificationVersion());
                System.out.println("Minecraft version: " + GAME_VERSION + " protocol " + PROTOCOL_VERSION);
                return null;
            }

            // Below this point, options require parameters
            if (i == args.length - 1) {
                System.err.println("Ignored option specified without value: " + opt);
                continue;
            }

            switch (opt) {
                case "--configdir":
                    configDirName = args[++i];
                    break;
                case "--configfile":
                    configFileName = args[++i];
                    break;
                case "--port":
                case "-p":
                    parameters.put(ServerConfig.Key.SERVER_PORT, Integer.valueOf(args[++i]));
                    break;
                case "--host":
                case "-H":
                    parameters.put(ServerConfig.Key.SERVER_IP, args[++i]);
                    break;
                case "--onlinemode":
                case "-o":
                    parameters.put(ServerConfig.Key.ONLINE_MODE, Boolean.valueOf(args[++i]));
                    break;
                case "--jline":
                    parameters.put(ServerConfig.Key.USE_JLINE, Boolean.valueOf(args[++i]));
                    break;
                case "--plugins-dir":
                case "-P":
                    parameters.put(ServerConfig.Key.PLUGIN_FOLDER, args[++i]);
                    break;
                case "--worlds-dir":
                case "-W":
                    parameters.put(ServerConfig.Key.WORLD_FOLDER, args[++i]);
                    break;
                case "--update-dir":
                case "-U":
                    parameters.put(ServerConfig.Key.UPDATE_FOLDER, args[++i]);
                    break;
                case "--max-players":
                case "-M":
                    parameters.put(ServerConfig.Key.MAX_PLAYERS, Integer.valueOf(args[++i]));
                    break;
                case "--world-name":
                case "-N":
                    parameters.put(ServerConfig.Key.LEVEL_NAME, args[++i]);
                    break;
                case "--log-pattern":
                case "-L":
                    parameters.put(ServerConfig.Key.LOG_FILE, args[++i]);
                    break;
                default:
                    System.err.println("Ignored invalid option: " + opt);
            }
        }

        final File configDir = new File(configDirName);
        final File configFile = new File(configDir, configFileName);

        return new ServerConfig(configDir, configFile, parameters);
    }*/
}
