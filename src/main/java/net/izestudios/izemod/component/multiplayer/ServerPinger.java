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

package net.izestudios.izemod.component.multiplayer;

import net.izestudios.izemod.IzeModImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;

public final class ServerPinger extends AbstractWidget {

    private static final ServerSelectionList WIDGET = new ServerSelectionList(null, null, 0, 0, 0, 0);
    public static final int WIDTH = 300;
    public static final int HEIGHT = 60;

    private final JoinMultiplayerScreen multiplayerScreen = new JoinMultiplayerScreen(null) {

        @Override
        public @NotNull ServerList getServers() {
            return new ServerList(Minecraft.getInstance()) {

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public void save() {
                }
            };
        }
    };
    private ServerSelectionList.OnlineServerEntry serverEntry;
    private ServerSelectionList.OnlineServerEntry previousServerEntry;

    public ServerPinger(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, Component.empty());
    }

    public void updateServer(final String address) {
        final ServerData serverInfo = new ServerData("", address, ServerData.Type.OTHER);
        serverInfo.setState(ServerData.State.INITIAL);

        multiplayerScreen.getPinger().removeAll();
        serverEntry = WIDGET.new OnlineServerEntry(multiplayerScreen, serverInfo);
        if (previousServerEntry == null) {
            previousServerEntry = serverEntry;
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (serverEntry == null || previousServerEntry == null) {
            return;
        }

        final ServerData.State status = serverEntry.getServerData().state();
        if (status == ServerData.State.INITIAL || status == ServerData.State.PINGING) {
            // The server entry is only pinged when rendered, but we only want to show it when it's successful, therefore
            // render it off-screen to avoid showing it when it's not successful
            serverEntry.render(guiGraphics, 0, guiGraphics.guiHeight(), guiGraphics.guiWidth(), 0, 0, mouseX, mouseY, false, partialTick);
        } else if (status == ServerData.State.SUCCESSFUL) {
            previousServerEntry = serverEntry;
        } else if (status == ServerData.State.UNREACHABLE || status == ServerData.State.INCOMPATIBLE) {
            return;
        }

        if (previousServerEntry.getServerData().state() != ServerData.State.SUCCESSFUL) {
            return;
        }

        final ServerSelectionList.OnlineServerEntry entry = status != ServerData.State.SUCCESSFUL ? previousServerEntry : serverEntry;
        guiGraphics.fill(getX(), getY(), getX() + WIDTH, getY() + HEIGHT, IzeModImpl.INSTANCE.themeColor().getRGB());
        entry.render(guiGraphics, 0, getY() + 2, getX() + 2, WIDTH + 1, HEIGHT, mouseX, mouseY, false, partialTick);

        final String brand = I18n.get("screens.directconnect.brand");
        final String version = I18n.get("screens.directconnect.version");
        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.RED + brand + ": " + ChatFormatting.AQUA + entry.getServerData().version.getString(), getX() + 2, getY() + 40, -1);
        guiGraphics.drawString(Minecraft.getInstance().font, ChatFormatting.RED + version + ": " + ChatFormatting.AQUA + entry.getServerData().protocol, getX() + 2, getY() + 50, -1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

}
