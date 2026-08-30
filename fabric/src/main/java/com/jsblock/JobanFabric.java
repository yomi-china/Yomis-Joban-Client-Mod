package com.jsblock;

import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.RegistryUtilities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.*;

public class JobanFabric implements ModInitializer {

	private static final Map<ResourceLocation, List<Item>> TAB_ITEMS = new LinkedHashMap<>();

	@Override
	public void onInitialize() {
		Joban.init(JobanFabric::registerBlock, JobanFabric::registerItem, JobanFabric::registerBlockItem, JobanFabric::registerBlockEntityType, JobanFabric::RegisterParticle);

		registerCreativeTab("core", new ItemStack(Blocks.HELPLINE_3.get()));
		registerCreativeTab("pids", new ItemStack(Blocks.PIDS_RV_TCL.get()));
		registerCreativeTab("ceiling", new ItemStack(Blocks.STATION_CEILING_1.get()));
	}

	private static void registerCreativeTab(String path, ItemStack icon) {
		ResourceLocation id = new ResourceLocation(Joban.MOD_ID, path);
		CreativeModeTab tab = FabricItemGroup.builder()
				.icon(() -> icon)
				.title(Component.translatable("itemGroup.jsblock." + path))
				.displayItems((displayContext, entries) -> {
					List<Item> items = TAB_ITEMS.get(id);
					if (items != null) {
						for (Item item : items) {
							entries.accept(item);
						}
					}
				})
				.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
	}

	private static void registerBlock(String path, RegistryObject<Block> block) {
		Registry.register(RegistryUtilities.registryGetBlock(), new ResourceLocation(Joban.MOD_ID, path), block.get());
	}

	private static void registerItem(String path, RegistryObject<Item> item) {
		Registry.register(RegistryUtilities.registryGetItem(), new ResourceLocation(Joban.MOD_ID, path), item.get());
	}

	private static void registerBlockItem(String path, RegistryObject<Block> block, ResourceLocation creativeModeTab) {
		registerBlock(path, block);
		final BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
		Registry.register(RegistryUtilities.registryGetItem(), new ResourceLocation(Joban.MOD_ID, path), blockItem);
		TAB_ITEMS.computeIfAbsent(creativeModeTab, k -> new ArrayList<>()).add(blockItem);
	}

	private static void RegisterParticle(String identifier, SimpleParticleType particle) {
		Registry.register(RegistryUtilities.registryGetParticleType(), new ResourceLocation(Joban.MOD_ID, identifier), particle);
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		Registry.register(RegistryUtilities.registryGetBlockEntityType(), new ResourceLocation(Joban.MOD_ID, path), blockEntityType.get());
	}
}
