package com.jsblock;

import mtr.CreativeModeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * List of categories that will be shown in the creative inventory
 * @since 1.0.5
 * @author LX86
 */
public interface ItemGroups {
    CreativeModeTabs.Wrapper MAIN = new CreativeModeTabs.Wrapper(new ResourceLocation(Joban.MOD_ID, "core"), () -> new ItemStack(Blocks.HELPLINE_3.get()));
    CreativeModeTabs.Wrapper PIDS = new CreativeModeTabs.Wrapper(new ResourceLocation(Joban.MOD_ID, "pids"), () -> new ItemStack(Blocks.PIDS_RV_TCL.get()));
    CreativeModeTabs.Wrapper CEILING = new CreativeModeTabs.Wrapper(new ResourceLocation(Joban.MOD_ID, "ceiling"), () -> new ItemStack(Blocks.STATION_CEILING_1.get()));
}