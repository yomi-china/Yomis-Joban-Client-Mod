package com.jsblock;

import net.minecraft.resources.ResourceLocation;

/**
 * List of categories that will be shown in the creative inventory
 * @since 1.0.5
 * @author LX86
 */
public interface ItemGroups {
    ResourceLocation MAIN = new ResourceLocation(Joban.MOD_ID, "core");
    ResourceLocation PIDS = new ResourceLocation(Joban.MOD_ID, "pids");
    ResourceLocation CEILING = new ResourceLocation(Joban.MOD_ID, "ceiling");
}
