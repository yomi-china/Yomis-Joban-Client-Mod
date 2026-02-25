package com.jsblock.mixin;

import com.jsblock.Compatibilities;
import com.jsblock.Joban;
import com.jsblock.screen.IncompatibleVersionScreen;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    private static boolean shown = false;

    @Inject(at = @At("TAIL"), method = "tick")
    private void init(CallbackInfo ci)
    {
        if(shown) return;

        String minVersion = Joban.MIN_MTR_VERSION;
        String curVersion = Joban.getMTRVersion();
        if(Compatibilities.lowerThanMin(minVersion, curVersion)) {
            UtilitiesClient.setScreen(Minecraft.getInstance(), new IncompatibleVersionScreen(minVersion, curVersion));
        }
        shown = true;
    }
}
