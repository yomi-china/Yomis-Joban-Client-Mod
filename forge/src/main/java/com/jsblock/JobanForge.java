package com.jsblock;

import com.jsblock.client.JobanCustomResources;
import com.jsblock.mappings.ForgeConfig;
import com.jsblock.mappings.ForgeUtilities;
import mtr.CreativeModeTabs;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import mtr.mappings.RegistryUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Joban.MOD_ID)
public class JobanForge {

	private static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetItem());
	private static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetBlock());
	private static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetBlockEntityType());
	private static final DeferredRegisterHolder<ParticleType<?>> PARTICLE_TYPES = new DeferredRegisterHolder<>(Joban.MOD_ID, ForgeUtilities.registryGetParticleType());

	static {
		Joban.init(JobanForge::registerBlock, JobanForge::registerItem,JobanForge::registerBlockAndItems, JobanForge::registerBlockEntityType, JobanForge::registerParticle);
	}

	public JobanForge() {
		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ForgeUtilities.registerModEventBus(Joban.MOD_ID, eventBus);
		ITEMS.register();
		BLOCKS.register();
		BLOCK_ENTITY_TYPES.register();
		PARTICLE_TYPES.register();
		eventBus.register(MTRForgeRegistry.class);
	}

	private static void registerBlock(String path, RegistryObject<Block> block) {
		BLOCKS.register(path, block::get);
	}

	private static void registerItem(String path, RegistryObject<Item> item) {
		ITEMS.register(path, item::get);
	}

	private static void registerParticle(String resourceLocation, SimpleParticleType particle) {
		PARTICLE_TYPES.register(resourceLocation, Particles.LIGHT_BLOCK::get);
	}

	private static void registerBlockAndItems(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTabWrapper) {
		registerBlock(path, block);
		ITEMS.register(path, () -> {
			final BlockItem blockItem = new BlockItem(block.get(), RegistryUtilities.createItemProperties(creativeModeTabWrapper::get));
			Registry.registerCreativeModeTab(creativeModeTabWrapper.resourceLocation, blockItem);
			return blockItem;
		});
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
	}

	private static class MTRForgeRegistry {

		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			JobanClient.init();
			ForgeConfig.registerConfig();

			ForgeUtilities.registerTextureStitchEvent(textureAtlas -> {
				if (((TextureAtlas)textureAtlas).location().getPath().equals("textures/atlas/blocks.png")) {
					JobanCustomResources.reload(Minecraft.getInstance().getResourceManager());
				}
			});
		}
	}
}
