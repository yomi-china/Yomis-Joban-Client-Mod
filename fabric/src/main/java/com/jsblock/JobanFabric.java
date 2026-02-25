package com.jsblock;

import com.jsblock.mappings.FabricRegistryUtilities;
import mtr.CreativeModeTabs;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.RegistryUtilities;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class JobanFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		Joban.init(JobanFabric::registerBlock, JobanFabric::registerItem, JobanFabric::registerBlockItem, JobanFabric::registerBlockEntityType, JobanFabric::RegisterParticle);
	}

	private static void registerBlock(String path, RegistryObject<Block> block) {
		Registry.register(RegistryUtilities.registryGetBlock(), new ResourceLocation(Joban.MOD_ID, path), block.get());
	}

	private static void registerItem(String path, RegistryObject<Item> item) {
		Registry.register(RegistryUtilities.registryGetItem(), new ResourceLocation(Joban.MOD_ID, path), item.get());
	}

	private static void registerBlockItem(String path, RegistryObject<Block> block, CreativeModeTabs.Wrapper creativeModeTab) {
		registerBlock(path, block);
		final BlockItem blockItem = new BlockItem(block.get(), RegistryUtilities.createItemProperties(creativeModeTab::get));
		Registry.register(RegistryUtilities.registryGetItem(), new ResourceLocation(Joban.MOD_ID, path), blockItem);
		FabricRegistryUtilities.registerCreativeModeTab(creativeModeTab.get(), blockItem);
	}

	private static void RegisterParticle(String identifier, SimpleParticleType particle) {
		Registry.register(RegistryUtilities.registryGetParticleType(), new ResourceLocation(Joban.MOD_ID, identifier), particle);
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		Registry.register(RegistryUtilities.registryGetBlockEntityType(), new ResourceLocation(Joban.MOD_ID, path), blockEntityType.get());
	}
}
