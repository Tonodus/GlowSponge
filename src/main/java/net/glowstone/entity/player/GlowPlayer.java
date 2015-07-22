package net.glowstone.entity.player;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.networking.Message;
import com.google.common.base.Optional;
import net.glowstone.block.entity.GlowTileEntity;
import net.glowstone.constants.GlowBlockEntity;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.meta.ClientSettings;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.inventory.InventoryMonitor;
import net.glowstone.io.PlayerDataService;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.login.LoginSuccessMessage;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.game.*;
import net.glowstone.net.protocol.ProtocolType;
import net.glowstone.resourcepack.GlowResourcePack;
import net.glowstone.util.StatisticMap;
import net.glowstone.util.TextMessage;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.world.ChunkManager;
import net.glowstone.world.GlowChunk;
import net.glowstone.world.GlowWeather;
import net.glowstone.world.GlowWorld;
import org.bukkit.World.Environment;
import org.bukkit.util.BlockVector;
import org.spongepowered.api.data.manipulator.DisplayNameData;
import org.spongepowered.api.data.manipulator.entity.*;
import org.spongepowered.api.data.manipulators.entities.*;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.tab.TabList;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.Location;

import java.util.*;

public final class GlowPlayer extends GlowHumanEntity implements Player {

    /**
     * A static entity id to use when telling the client about itself.
     */
    private static final int SELF_ID = 0;

    /**
     * This player's session.
     */
    private final GlowSession session;

    /**
     * The entities that the client knows about.
     */
    private final Set<GlowEntity> knownEntities = new HashSet<>();

    /**
     * The entities that are hidden from the client.
     */
    private final Set<UUID> hiddenEntities = new HashSet<>();

    /**
     * The chunks that the client knows about.
     */
    private final Set<GlowChunk.Key> knownChunks = new HashSet<>();

    /**
     * A queue of BlockChangeMessages to be sent.
     */
    private final List<BlockChangeMessage> blockChanges = new LinkedList<>();

    /**
     * A queue of messages that should be sent after block changes are processed.
     * Used for sign updates and other situations where the block must be sent first.
     */
    private final List<Message> afterBlockChanges = new LinkedList<>();

    /**
     * The set of plugin channels this player is listening on
     */
    private final Set<String> listeningChannels = new HashSet<>();

    /**
     * The player's statistics, achievements, and related data.
     */
    private final StatisticMap stats = new StatisticMap();

    /**
     * Whether the player has played before (will be false on first join).
     */
    private final boolean hasPlayedBefore;

    /**
     * The time the player first played, or 0 if unknown.
     */
    private final long firstPlayed;

    /**
     * The time the player last played, or 0 if unknown.
     */
    private final long lastPlayed;

    /**
     * The time the player joined.
     */
    private final long joinTime;

    /**
     * The settings sent by the client.
     */
    private ClientSettings settings = ClientSettings.DEFAULT;

    /**
     * The lock used to prevent chunks from unloading near the player.
     */
    private ChunkManager.ChunkLock chunkLock;

    /**
     * The tracker for changes to the currently open inventory.
     */
    private InventoryMonitor invMonitor;

    /**
     * The display name of this player, for chat purposes.
     */
    private String displayName;

    /**
     * The name a player has in the player list
     */
    private String playerListName;

    /**
     * Cumulative amount of experience points the player has collected.
     */
    private int totalExperience = 0;

    /**
     * The current level (or skill point amount) of the player.
     */
    private int level = 0;

    /**
     * The progress made to the next level, from 0 to 1.
     */
    private float experience = 0;

    /**
     * The human entity's current food level
     */
    private int food = 20;

    /**
     * The player's current exhaustion level.
     */
    private float exhaustion = 0;

    /**
     * The player's current saturation level.
     */
    private float saturation = 0;

    /**
     * Whether to perform special scaling of the player's health.
     */
    private boolean healthScaled = false;

    /**
     * The scale at which to display the player's health.
     */
    private double healthScale = 20;

    /**
     * This player's current time offset.
     */
    private long timeOffset = 0;

    /**
     * Whether the time offset is relative.
     */
    private boolean timeRelative = true;

    /**
     * The player-specific weather, or null for normal weather.
     */
    private GlowWeather playerWeather = null;

    /**
     * The player's compass target.
     */
    private Location compassTarget;

    /**
     * Whether this player's sleeping state is ignored when changing time.
     */
    private boolean sleepingIgnored;

    /**
     * The bed spawn location of a player
     */
    private Location bedSpawn;

    /**
     * The location of the sign the player is currently editing, or null.
     */
    private Location signLocation;

    /**
     * Whether the player is permitted to fly.
     */
    private boolean canFly;

    /**
     * Whether the player is currently flying.
     */
    private boolean flying;

    /**
     * The player's base flight speed.
     */
    private float flySpeed = 0.1f;

    /**
     * The player's base walking speed.
     */
    private float walkSpeed = 0.2f;

    /**
     * The player's messageSink
     */
    private MessageSink messageSink;

    /**
     * Creates a new player and adds it to the world.
     * @param session The player's session.
     * @param profile The player's profile with name and UUID information.
     * @param reader The PlayerReader to be used to initialize the player.
     */
    public GlowPlayer(GlowServer server, GlowSession session, PlayerProfile profile, PlayerDataService.PlayerReader reader) {
        super(server, initLocation(session, reader), profile, reader);
        setBoundingBox(0.6, 1.8);
        this.session = session;

        chunkLock = world.newChunkLock(getName());

        // enable compression if needed
        int compression = session.getServer().getCompressionThreshold();
        if (compression > 0) {
            session.enableCompression(compression);
        }

        // send login response
        session.send(new LoginSuccessMessage(profile.getUniqueId().toString(), profile.getName()));
        session.setProtocol(ProtocolType.PLAY);

        // send join game
        // in future, handle hardcore, difficulty, and level type
        String type = world.getWorldType().getName().toLowerCase();
        int gameMode = getGameMode().getValue();
        if (server.isHardcore()) {
            gameMode |= 0x8;
        }
        session.send(new JoinGameMessage(SELF_ID, gameMode, world.getEnvironment().getId(), world.getDifficulty().getValue(), session.getServer().getMaxPlayers(), type, world.getGameRuleMap().getBoolean("reducedDebugInfo")));
        setGameModeDefaults();

        // send server brand and supported plugin channels
        session.send(PluginMessage.fromString("MC|Brand", server.getName()));
        sendSupportedChannels();

        // read data from player reader
        hasPlayedBefore = reader.hasPlayedBefore();
        if (hasPlayedBefore) {
            firstPlayed = reader.getFirstPlayed();
            lastPlayed = reader.getLastPlayed();
            bedSpawn = reader.getBedSpawnLocation();
        } else {
            firstPlayed = 0;
            lastPlayed = 0;
        }
        joinTime = System.currentTimeMillis();
        reader.readData(this);
        reader.close();

        // Add player to list of online players
        getServer().setPlayerOnline(this, true);

        // save data back out
        saveData();

        streamBlocks(); // stream the initial set of blocks
        setCompassTarget(world.getSpawnLocation()); // set our compass target
        sendTime();
        sendWeather();
        sendRainDensity();
        sendSkyDarkness();
        sendAbilities();

        invMonitor = new InventoryMonitor(getOpenInventory());
        updateInventory(); // send inventory contents

        // send initial location
        session.send(new PositionRotationMessage(location));

        if (!server.getResourcePackURL().isEmpty()) {
            sendResourcePack(new GlowResourcePack(server.getResourcePackURL(), server.getResourcePackHash()));
        }

        this.messageSink = new MessageSink() {
            final Iterable<CommandSource> me = Arrays.asList((CommandSource) GlowPlayer.this);

            @Override
            public Iterable<CommandSource> getRecipients() {
                return me;
            }
        };
    }

    /**
     * Read the location from a PlayerReader for entity initialization. Will
     * fall back to a reasonable default rather than returning null.
     * @param session The player's session.
     * @param reader The PlayerReader to get the location from.
     * @return The location to spawn the player.
     */
    private static Location initLocation(GlowSession session, PlayerDataService.PlayerReader reader) {
        if (reader.hasPlayedBefore()) {
            Location loc = reader.getLocation();
            if (loc != null) {
                return loc;
            }
        }
        return session.getServer().getWorlds().get(0).getSpawnLocation();
    }

    @Override
    public String toString() {
        return "GlowPlayer{name=" + getName() + "}";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    /**
     * Get the network session attached to this player.
     * @return The GlowSession of the player.
     */
    public GlowSession getSession() {
        return session;
    }

    /**
     * Get the join time in milliseconds, to be saved as last played time.
     * @return The player's join time.
     */
    public long getJoinTime() {
        return joinTime;
    }


    @Override
    public EntityType getType() {
        return EntityTypes.PLAYER;
    }

    /**
     * Destroys this entity by removing it from the world and marking it as not
     * being active.
     */
    @Override
    public void remove() {
        knownChunks.clear();
        chunkLock.clear();
        saveData();
        getInventory().removeViewer(this);
        getInventory().getCraftingInventory().removeViewer(this);
        permissions.clearPermissions();
        getServer().setPlayerOnline(this, false);
        super.remove();
    }


    @Override
    public void pulse() {
        super.pulse();

        // stream world
        streamBlocks();
        processBlockChanges();

        // add to playtime
        incrementStatistic(Statistic.PLAY_ONE_TICK);

        // update inventory
        for (InventoryMonitor.Entry entry : invMonitor.getChanges()) {
            sendItemChange(entry.slot, entry.item);
        }

        // send changed metadata
        List<MetadataMap.Entry> changes = metadata.getChanges();
        if (changes.size() > 0) {
            session.send(new EntityMetadataMessage(SELF_ID, changes));
        }

        // update or remove entities
        List<Integer> destroyIds = new LinkedList<>();
        for (Iterator<GlowEntity> it = knownEntities.iterator(); it.hasNext(); ) {
            GlowEntity entity = it.next();
            if (isWithinDistance(entity)) {
                for (Message msg : entity.createUpdateMessage()) {
                    session.send(msg);
                }
            } else {
                destroyIds.add(entity.getEntityId());
                it.remove();
            }
        }
        if (destroyIds.size() > 0) {
            session.send(new DestroyEntitiesMessage(destroyIds));
        }

        // add entities
        for (GlowEntity entity : world.getEntityManager()) {
            if (entity != this && isWithinDistance(entity) &&
                    !knownEntities.contains(entity) && !hiddenEntities.contains(entity.getUniqueId())) {
                knownEntities.add(entity);
                for (Message msg : entity.createSpawnMessage()) {
                    session.send(msg);
                }
            }
        }
    }

    /**
     * Process and send pending BlockChangeMessages.
     */
    private void processBlockChanges() {
        List<BlockChangeMessage> messages = new ArrayList<>(blockChanges);
        blockChanges.clear();

        // separate messages by chunk
        // inner map is used to only send one entry for same coordinates
        Map<GlowChunk.Key, Map<BlockVector, BlockChangeMessage>> chunks = new HashMap<>();
        for (BlockChangeMessage message : messages) {
            GlowChunk.Key key = new GlowChunk.Key(message.getX() >> 4, message.getZ() >> 4);
            Map<BlockVector, BlockChangeMessage> map = chunks.get(key);
            if (map == null) {
                map = new HashMap<>();
                chunks.put(key, map);
            }
            map.put(new BlockVector(message.getX(), message.getY(), message.getZ()), message);
        }

        // send away
        for (Map.Entry<GlowChunk.Key, Map<BlockVector, BlockChangeMessage>> entry : chunks.entrySet()) {
            GlowChunk.Key key = entry.getKey();
            List<BlockChangeMessage> value = new ArrayList<>(entry.getValue().values());

            if (value.size() == 1) {
                session.send(value.get(0));
            } else if (value.size() > 1) {
                session.send(new MultiBlockChangeMessage(key.getX(), key.getZ(), value));
            }
        }

        // now send post-block-change messages
        List<Message> postMessages = new ArrayList<>(afterBlockChanges);
        afterBlockChanges.clear();
        for (Message message : postMessages) {
            session.send(message);
        }
    }

    /**
     * Streams chunks to the player's client.
     */
    private void streamBlocks() {
        Set<GlowChunk.Key> previousChunks = new HashSet<>(knownChunks);
        ArrayList<GlowChunk.Key> newChunks = new ArrayList<>();

        int centralX = location.getBlockX() >> 4;
        int centralZ = location.getBlockZ() >> 4;

        int radius = Math.min(server.getViewDistance(), 1 + settings.getViewDistance());
        for (int x = (centralX - radius); x <= (centralX + radius); x++) {
            for (int z = (centralZ - radius); z <= (centralZ + radius); z++) {
                GlowChunk.Key key = new GlowChunk.Key(x, z);
                if (knownChunks.contains(key)) {
                    previousChunks.remove(key);
                } else {
                    newChunks.add(key);
                }
            }
        }

        // early end if there's no changes
        if (newChunks.size() == 0 && previousChunks.size() == 0) {
            return;
        }

        // sort chunks by distance from player - closer chunks sent first
        Collections.sort(newChunks, new Comparator<GlowChunk.Key>() {
            @Override
            public int compare(GlowChunk.Key a, GlowChunk.Key b) {
                double dx = 16 * a.getX() + 8 - location.getX();
                double dz = 16 * a.getZ() + 8 - location.getZ();
                double da = dx * dx + dz * dz;
                dx = 16 * b.getX() + 8 - location.getX();
                dz = 16 * b.getZ() + 8 - location.getZ();
                double db = dx * dx + dz * dz;
                return Double.compare(da, db);
            }
        });

        // populate then send chunks to the player
        // done in two steps so that all the new chunks are finalized before any of them are sent
        // this prevents sending a chunk then immediately sending block changes in it because
        // one of its neighbors has populated

        // first step: force population then acquire lock on each chunk
        for (GlowChunk.Key key : newChunks) {
            world.getChunkManager().forcePopulation(key.getX(), key.getZ());
            knownChunks.add(key);
            chunkLock.acquire(key);
        }

        // second step: package chunks into bulk packets
        final int maxSize = 0x1fff00;  // slightly under protocol max size of 0x200000
        final boolean skylight = world.getEnvironment() == World.Environment.NORMAL;
        final List<ChunkDataMessage> messages = new LinkedList<>();
        int bulkSize = 6; // size of bulk header

        // split the chunks into bulk packets based on how many fit
        for (GlowChunk.Key key : newChunks) {
            GlowChunk chunk = world.getChunkAt(key.getX(), key.getZ());
            ChunkDataMessage message = chunk.toMessage(skylight);
            // 10 bytes of header in bulk packet, plus data length
            int messageSize = 10 + message.getData().length;

            // if this chunk would make the message too big,
            if (bulkSize + messageSize > maxSize) {
                // send out what we have so far
                session.send(new ChunkBulkMessage(skylight, messages));
                messages.clear();
                bulkSize = 6;
            }

            bulkSize += messageSize;
            messages.add(message);
        }

        // send the leftovers
        if (!messages.isEmpty()) {
            session.send(new ChunkBulkMessage(skylight, messages));
            messages.clear();
        }

        // send visible tile entity data
        for (GlowChunk.Key key : newChunks) {
            GlowChunk chunk = world.getChunkAt(key.getX(), key.getZ());
            for (GlowTileEntity entity : chunk.getRawTileEntities()) {
                entity.update(this);
            }
        }

        // and remove old chunks
        for (GlowChunk.Key key : previousChunks) {
            session.send(ChunkDataMessage.empty(key.getX(), key.getZ()));
            knownChunks.remove(key);
            chunkLock.release(key);
        }

        previousChunks.clear();
    }

    public void saveData(boolean async) {

    }
    //////////////////////////////////////
    // Spawning

    /**
     * Spawn the player at the given location after they have already joined.
     * Used for changing worlds and respawning after death.
     * @param location The location to place the player.
     */
    private void spawnAt(Location location) {
        // switch worlds
        GlowWorld oldWorld = world;
        world.getEntityManager().unregister(this);
        world = (GlowWorld) location.getExtent();
        world.getEntityManager().register(this);

        // switch chunk set
        // no need to send chunk unload messages - respawn unloads all chunks
        knownChunks.clear();
        chunkLock.clear();
        chunkLock = world.newChunkLock(getName());

        // spawn into world
        String type = world.getWorldType().getName().toLowerCase();
        session.send(new RespawnMessage(world.getEnvironment().getId(), world.getDifficulty().getValue(), getGameMode().getValue(), type));
        setRawLocation(location); // take us to spawn position
        streamBlocks(); // stream blocks
        setCompassTarget(world.getSpawnLocation()); // set our compass target
        session.send(new PositionRotationMessage(location));
        sendWeather();
        sendRainDensity();
        sendSkyDarkness();
        sendTime();
        updateInventory();

        // fire world change if needed
        if (oldWorld != world) {
            EventFactory.callEvent(new PlayerChangedWorldEvent(this, oldWorld));
        }
    }

    /**
     * Respawn the player after they have died.
     */
    public void respawn() {
        // restore health
        setHealth(getMaxHealth());

        // determine spawn destination
        boolean spawnAtBed = false;
        Location dest = world.getSpawnLocation();
        if (bedSpawn != null) {
            if (bedSpawn.getBlock().getType() == Material.BED_BLOCK) {
                // todo: spawn next to the bed instead of inside it
                dest = bedSpawn.clone();
                spawnAtBed = true;
            }
        }

        // fire event and perform spawn
        PlayerRespawnEvent event = new PlayerRespawnEvent(this, dest, spawnAtBed);
        EventFactory.callEvent(event);
        if (event.getRespawnLocation().getWorld().equals(getWorld()) && knownEntities.size() > 0) {
            // we need to manually reset all known entities if the player respawns in the same world
            List<Integer> entityIds = new ArrayList<>(knownEntities.size());
            for (GlowEntity e : knownEntities) {
                entityIds.add(e.getEntityId());
            }
            session.send(new DestroyEntitiesMessage(entityIds));
            knownEntities.clear();
        }
        spawnAt(event.getRespawnLocation());

        // just in case any items are left in their inventory after they respawn
        updateInventory();
    }

    /**
     * Checks whether the player can see the given chunk.
     * @return If the chunk is known to the player's client.
     */
    public boolean canSeeChunk(GlowChunk.Key chunk) {
        return knownChunks.contains(chunk);
    }

    /**
     * Checks whether the player can see the given entity.
     * @return If the entity is known to the player's client.
     */
    public boolean canSeeEntity(GlowEntity entity) {
        return knownEntities.contains(entity);
    }


    /**
     * Open the sign editor interface at the specified location.
     * @param loc The location to open the editor at
     */
    public void openSignEditor(Location loc) {
        signLocation = loc.clone();
        signLocation.setX(loc.getBlockX());
        signLocation.setY(loc.getBlockY());
        signLocation.setZ(loc.getBlockZ());
        session.send(new SignEditorMessage(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    /**
     * Check that the specified location matches that of the last opened sign
     * editor, and if so, clears the last opened sign editor.
     * @param loc The location to check
     * @return Whether the location matched.
     */
    public boolean checkSignLocation(Location loc) {
        if (loc.equals(signLocation)) {
            signLocation = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get a UserListItemMessage entry representing adding this player.
     * @return The entry (action ADD_PLAYER) with this player's information.
     */
    public UserListItemMessage.Entry getUserListEntry() {
        TextMessage displayName = null;
        if (playerListName != null && !playerListName.isEmpty()) {
            displayName = new TextMessage(playerListName);
        }
        return UserListItemMessage.add(getProfile(), getGameMode().getValue(), 0, displayName);
    }

    /**
     * Send a UserListItemMessage to every player that can see this player.
     * @param updateMessage The message to send.
     */
    private void updateUserListEntries(UserListItemMessage updateMessage) {
        for (GlowPlayer player : server.getOnlinePlayers()) {
            if (player.canSee(this)) {
                player.getSession().send(updateMessage);
            }
        }
    }

    @Override
    public void setVelocity(Vector3d velocity) {
        super.setVelocity(velocity);
        session.send(new EntityVelocityMessage(SELF_ID, velocity));
    }

    /**
     * Set the client settings for this player.
     * @param settings The new client settings.
     */
    public void setSettings(ClientSettings settings) {
        this.settings = settings;
        metadata.set(MetadataIndex.PLAYER_SKIN_FLAGS, settings.getSkinFlags());
    }

    /**
     * Get this player's client settings.
     * @return The player's client settings.
     */
    public ClientSettings getSettings() {
        return settings;
    }


    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", getName());
        return ret;
    }

    public void sendTileEntityChange(int x, int y, int z, GlowBlockEntity blockEntity, CompoundTag nbt) {
        //TODO: change 0 to ?
        session.send(new UpdateBlockEntityMessage(x, y, z, 0, nbt));
    }

    ///////////////////////////
    // public accessors

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public PlayerConnection getConnection() {
        return null;
    }

    @Override
    public void sendResourcePack(ResourcePack pack) {

    }

    @Override
    public TabList getTabList() {
        return null;
    }

    public void kick(String reason) {
        kick(Texts.of(reason));
    }

    @Override
    public void kick() {

    }

    @Override
    public void kick(Text reason) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {

    }

    @Override
    public JoinData getJoinData() {
        return getData(JoinData.class).get();
    }

    @Override
    public DisplayNameData getDisplayNameData() {
        return getData(DisplayNameData.class).get();
    }

    @Override
    public GameModeData getGameModeData() {
        return getData(GameModeData.class).get();
    }

    @Override
    public void sendMessage(Text... messages) {

    }

    @Override
    public void sendMessage(Iterable<Text> messages) {

    }

    @Override
    public MessageSink getMessageSink() {
        return messageSink;
    }

    @Override
    public void setMessageSink(MessageSink sink) {
        this.messageSink = sink;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass, Vector3d velocity) {
        return null;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.of((Player) this);
    }

    @Override
    public AchievementData getAchievementData() {
        return getData(AchievementData.class).get();
    }

    @Override
    public StatisticData getStatisticData() {
        return getData(StatisticData.class).get();
    }

    @Override
    public BanData getBanData() {
        return getData(BanData.class).get();
    }

    @Override
    public String getIdentifier() {
        return getName();
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.of((CommandSource) this);
    }

    ////////////////////////////////
    // Permission

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
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
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

    //////////////////////
    // Effects

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position) {

    }

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position, int radius) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch, double minVolume) {

    }

    /////////////////////////////
    // Messages and Titles

    @Override
    public void sendMessage(ChatType type, String... message) {
        for (String str : message) {
            sendMessage(type, Texts.of(str));
        }
    }

    @Override
    public void sendMessage(ChatType type, Text... messages) {
        for (Text text : messages) {
            sendMessage(type, text);
        }
    }

    @Override
    public void sendMessage(ChatType type, Iterable<Text> messages) {
        for (Text text : messages) {
            sendMessage(type, text);
        }
    }

    public void sendMessage(ChatType type, Text text) {
        int mode = 0;
        if (type == ChatTypes.CHAT) {
            mode = 0;
        }
        session.send(new ChatMessage(text, mode));
    }

    @Override
    public void sendTitle(Title title) {

    }

    @Override
    public void resetTitle() {
        session.send(new TitleMessage(TitleMessage.Action.RESET));
    }

    @Override
    public void clearTitle() {
        session.send(new TitleMessage(TitleMessage.Action.CLEAR));
    }

    public void sendTime(long age, long time) {
        session.send(new TimeMessage(age, time));
    }

}
