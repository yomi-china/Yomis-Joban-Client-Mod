package com.jsblock.fabric;

import mtr.MTR;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public class JobanImpl {
    public static String getMTRVersion() {
        try {
            Optional<ModContainer> mtr = FabricLoader.getInstance().getModContainer(MTR.MOD_ID);
            if (mtr.isPresent()) {
                String mtrVersion = mtr.get().getMetadata().getVersion().toString();
                return mtrVersion.replace(mtrVersion.split("-")[0] + "-", "");
            } else {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
    }
}
