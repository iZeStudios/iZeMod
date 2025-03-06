/*
 * This file is part of iZeMod - https://github.com/iZeStudios/iZeMod
 * Copyright (C) 2025 iZeStudios and GitHub contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.izestudios.izemod.injection.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    NumberFormat numformat = NumberFormat.getInstance();
    SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");

    @Shadow
    @Final
    private MinecraftClient client;



    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        numformat.setMaximumFractionDigits(2);

        final MutableText fps = Text.translatable("ingame.hud.fps", client.getCurrentFps())
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText coords_x = Text.translatable("ingame.hud.x", numformat.format(client.player.getX()))
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText coords_y = Text.translatable("ingame.hud.y", numformat.format(client.player.getY()))
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText coords_z = Text.translatable("ingame.hud.z", numformat.format(client.player.getZ()))
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText biome = Text.translatable("ingame.hud.biome", client.world.getBiome(client.player.getBlockPos()).getIdAsString())
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText ping = Text.translatable("ingame.hud.ping", client.getNetworkHandler().getPlayerListEntry(client.player.getUuid()).getLatency())
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText players = Text.translatable("ingame.hud.players", client.getNetworkHandler().getPlayerList().size(), client.getCurrentServerEntry() == null ? 0 : client.getCurrentServerEntry().players.max())
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText date = Text.translatable("ingame.hud.date", dateformat.format(new Date()))
            .styled(style -> style.withColor(Formatting.AQUA));
        final MutableText time = Text.translatable("ingame.hud.time", timeformat.format(new Date()))
            .styled(style -> style.withColor(Formatting.AQUA));


        context.drawTextWithShadow(client.textRenderer, fps, 2, 20, -1);
        context.drawTextWithShadow(client.textRenderer, coords_x, 2, 30, -1);
        context.drawTextWithShadow(client.textRenderer, coords_y, 2, 40, -1);
        context.drawTextWithShadow(client.textRenderer, coords_z, 2, 50, -1);
        context.drawTextWithShadow(client.textRenderer, biome, 2, 60, -1);
        context.drawTextWithShadow(client.textRenderer, ping, 2, 70, -1);
        context.drawTextWithShadow(client.textRenderer, players, 2, 80, -1);
        context.drawTextWithShadow(client.textRenderer, date, 2, 90, -1);
        context.drawTextWithShadow(client.textRenderer, time, 2, 100, -1);


    }


}
