package com.jsblock.block;

import com.jsblock.BlockEntityTypes;
import com.jsblock.packet.PacketServer;
import com.jsblock.vermappings.JobanMapping;
import mtr.MTR;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.TickableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Sound Looper Block
 * @author LX86
 * @since 1.0.8
 */
public class SoundLooper extends Block implements EntityBlockMapper {

    public SoundLooper(Properties settings) {
        super(settings);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntitySoundLooper(pos, state);
    }

    @Override
    public <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
        SoundLooper.TileEntitySoundLooper.tick(world, pos, blockEntity);
    }

    @Override
    public BlockEntityType<? extends BlockEntityMapper> getType() {
        return BlockEntityTypes.SOUND_LOOPER_TILE_ENTITY.get();
    }

    /* On player use (Right-clicked without shift)
    need to return an InteractionResult, if it's InteractionResult.FAIL, the hand won't swing
    We just call MTR Method to check whether the player is holding an MTR Brush */
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);

            if (entity instanceof TileEntitySoundLooper) {
                PacketServer.sendSoundLooperScreenS2C((ServerPlayer) player, pos);
            }
        });
    }

    public static class TileEntitySoundLooper extends BlockEntityClientSerializableMapper implements TickableMapper {
        private String soundID = "";
        private BlockPos pos1 = new BlockPos(0, 0, 0);
        private BlockPos pos2 = new BlockPos(0, 0, 0);
        private int repeatTick = 20;
        private float soundVolume = 1;
        private int soundCategory = 0;
        private boolean requireRedstone = false;
        private boolean limitRange = false;

        private boolean useNetworkAudio = false;
        private String networkAudioUrl = "";

        private final String KEY_REPEAT_TICK = "repeat_tick";
        private final String KEY_SOUND_ID = "sound_id";
        private final String KEY_SOUND_CATEGORY = "sound_category";
        private final String KEY_NEED_REDSTONE = "need_redstone";
        private final String KEY_SOUND_VOLUME = "volume";
        private final String KEY_POS1 = "pos_1";
        private final String KEY_POS2 = "pos_2";
        private final String KEY_LIMIT_RANGE = "limit_range";

        private final String KEY_USE_NETWORK_AUDIO = "use_network_audio";
        private final String KEY_NETWORK_AUDIO_URL = "network_audio_url";

        private static final SoundSource[] SOURCE_LIST = {SoundSource.MASTER, SoundSource.MUSIC, SoundSource.WEATHER, SoundSource.AMBIENT, SoundSource.PLAYERS, SoundSource.BLOCKS, SoundSource.VOICE};

        public TileEntitySoundLooper(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SOUND_LOOPER_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            repeatTick = compoundTag.getInt(KEY_REPEAT_TICK);
            soundID = compoundTag.getString(KEY_SOUND_ID);
            soundCategory = compoundTag.getInt(KEY_SOUND_CATEGORY);
            soundVolume = compoundTag.getFloat(KEY_SOUND_VOLUME);
            requireRedstone = compoundTag.getBoolean(KEY_NEED_REDSTONE);
            requireRedstone = compoundTag.getBoolean(KEY_NEED_REDSTONE);
            limitRange = compoundTag.getBoolean(KEY_LIMIT_RANGE);
            pos1 = BlockPos.of(compoundTag.getLong(KEY_POS1));
            pos2 = BlockPos.of(compoundTag.getLong(KEY_POS2));
            useNetworkAudio = compoundTag.getBoolean(KEY_USE_NETWORK_AUDIO);
            networkAudioUrl = compoundTag.getString(KEY_NETWORK_AUDIO_URL);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putInt(KEY_REPEAT_TICK, repeatTick);
            compoundTag.putString(KEY_SOUND_ID, soundID);
            compoundTag.putInt(KEY_SOUND_CATEGORY, soundCategory);
            compoundTag.putFloat(KEY_SOUND_VOLUME, soundVolume);
            compoundTag.putBoolean(KEY_NEED_REDSTONE, requireRedstone);
            compoundTag.putBoolean(KEY_LIMIT_RANGE, limitRange);
            compoundTag.putLong(KEY_POS1, pos1.asLong());
            compoundTag.putLong(KEY_POS2, pos2.asLong());
            compoundTag.putBoolean(KEY_USE_NETWORK_AUDIO, useNetworkAudio);
            compoundTag.putString(KEY_NETWORK_AUDIO_URL, networkAudioUrl);
        }

        @Override
        public void tick() {
            tick(level, worldPosition, this);
        }

        public static <T extends BlockEntityClientSerializableMapper> void tick(Level world, BlockPos pos, BlockEntity entity) {
            if (!(entity instanceof TileEntitySoundLooper)) return;

            int repeatTick = ((TileEntitySoundLooper) entity).getLoopInterval();
            int soundCategory = ((TileEntitySoundLooper) entity).getSoundCategory();
            float soundVolume = ((TileEntitySoundLooper) entity).getSoundVolume();
            boolean requireRedstone = ((TileEntitySoundLooper) entity).getNeedRedstone();
            boolean limitRange = ((TileEntitySoundLooper) entity).getLimitRange();
            String soundID = ((TileEntitySoundLooper) entity).getSoundId();
            BlockPos pos1 = ((TileEntitySoundLooper) entity).getPos1();
            BlockPos pos2 = ((TileEntitySoundLooper) entity).getPos2();

            boolean useNetworkAudio = ((TileEntitySoundLooper) entity).getUseNetworkAudio();
            String networkAudioUrl = ((TileEntitySoundLooper) entity).getNetworkAudioUrl();

            AABB box = new AABB(pos1, pos2);

            if (repeatTick > 0 && MTR.isGameTickInterval(repeatTick) && (useNetworkAudio ? !networkAudioUrl.isEmpty() : !soundID.isEmpty())) {
                if (world.isClientSide()) return;
                if (requireRedstone && !world.hasNeighborSignal(pos)) return;

                if (!useNetworkAudio) {
                    // 原有本地声音播放
                    final ResourceLocation soundLocation = new ResourceLocation(soundID);
                    final SoundSource source = SOURCE_LIST[soundCategory];
                    if (!limitRange) {
                        world.players().forEach(player -> {
                                JobanMapping.sendSoundToPlayer(world, (ServerPlayer) player, soundLocation, source, pos, soundVolume);
                        });
                    } else {
                        world.getEntitiesOfClass(Player.class, box).forEach(player -> {
                                JobanMapping.sendSoundToPlayer(world, (ServerPlayer) player, soundLocation, source, pos, soundVolume);
                        });
                    }
                } else {
                    final SoundSource source = SOURCE_LIST[soundCategory];
                    if (!limitRange) {
                        world.players().forEach(player -> {
                            PacketServer.sendPlayNetworkSoundS2C((ServerPlayer) player, networkAudioUrl, source, pos, soundVolume);
                        });
                    } else {
                        world.getEntitiesOfClass(Player.class, box).forEach(player -> {
                            PacketServer.sendPlayNetworkSoundS2C((ServerPlayer) player, networkAudioUrl, source, pos, soundVolume);
                        });
                    }
                }
            }
        }

        public String getSoundId() {
            return soundID == null ? "" : soundID;
        }

        public int getLoopInterval() {
            return repeatTick;
        }

        public int getSoundCategory() {
            if (soundCategory > SOURCE_LIST.length) {
                soundCategory = 0;
            }
            return soundCategory;
        }

        public void setData(String soundId, int soundCategory, int interval, float volume,
                            boolean requireRedstone, boolean limitRange, BlockPos pos1, BlockPos pos2,
                            boolean useNetworkAudio, String networkAudioUrl) {
            this.soundID = soundId;
            this.repeatTick = interval;
            this.soundCategory = soundCategory;
            this.soundVolume = volume;
            this.requireRedstone = requireRedstone;
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.limitRange = limitRange;
            this.useNetworkAudio = useNetworkAudio;
            this.networkAudioUrl = networkAudioUrl;
            syncData();
        }

        public float getSoundVolume() {
            return soundVolume;
        }

        public boolean getNeedRedstone() {
            return requireRedstone;
        }

        public boolean getLimitRange() {
            return limitRange;
        }

        public BlockPos getPos1() {
            return pos1;
        }

        public BlockPos getPos2() {
            return pos2;
        }

        public boolean getUseNetworkAudio() {
            return useNetworkAudio;
        }

        public String getNetworkAudioUrl() {
            return networkAudioUrl == null ? "" : networkAudioUrl;
        }
    }
}