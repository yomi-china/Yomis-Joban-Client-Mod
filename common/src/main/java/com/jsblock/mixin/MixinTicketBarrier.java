package com.jsblock.mixin;

import com.jsblock.Joban;
import mtr.data.Station;
import mtr.data.TicketSystem;
import mtr.mappings.Text;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This is a mixin for the MTR TicketSystem<br>
 * Used for detecting when a player exits a station
 * @author LX86
 * @since 1.1.4
 */
@Mixin(TicketSystem.class)
public class MixinTicketBarrier {
    private static final int BASE_FARE = 2;
    private static final int ZONE_FARE = 1;

    @Inject(at = @At("HEAD"), method = "onExit")
    private static void onExit(Station station, Player player, Score balanceScore, Score entryZoneScore, boolean remindIfNoRecord, CallbackInfoReturnable<Boolean> cir) {
        if(!Joban.discountMap.containsKey(player.getUUID())) return;

        final int entryZone = entryZoneScore.getScore();
        final boolean evasion = entryZone == 0;
        /* Imagine getting a discount for evading */
        if(evasion) return;

        final int fare = BASE_FARE + ZONE_FARE * Math.abs(station.zone - decodeZone(entryZone));
        final int finalFare = isConcessionary(player) ? (int) Math.ceil(fare / 2F) : fare;
        final int discounts = Joban.discountMap.getInt(player.getUUID());

        /* Don't let the discount add any money */
        final int addition = Math.min(discounts, finalFare);
        balanceScore.add(addition);

        if(discounts >= 0) {
            player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.saved", addition), false);
        } else {
            player.displayClientMessage(Text.translatable("gui.jsblock.faresaver.saved_sarcasm", addition), false);
        }
        Joban.discountMap.removeInt(player.getUUID());
    }

    private static int decodeZone(int zone) {
        return zone > 0 ? zone - 1 : zone;
    }

    private static boolean isConcessionary(Player player) {
        return player.isCreative();
    }
}
