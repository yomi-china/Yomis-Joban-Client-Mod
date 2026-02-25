package com.jsblock.forge;

import mtr.MTR;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class JobanImpl {
    public static String getMTRVersion() {
        try {
            Optional<? extends ModContainer> mtr = ModList.get().getModContainerById(MTR.MOD_ID);
            if (mtr.isPresent()) {
                String mtrVersion = mtr.get().getModInfo().getVersion().toString();
                return mtrVersion.replace(mtrVersion.split("-")[0] + "-", "");
            } else {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
    }
}
