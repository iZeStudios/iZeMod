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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List;

public final class ServerPinger extends ClickableWidget {

    private static final MultiplayerServerListWidget WIDGET = new MultiplayerServerListWidget(null, null, 0, 0, 0, 0);
    public static final int WIDTH = 300;
    public static final int HEIGHT = 60;

    private final MultiplayerScreen multiplayerScreen = new MultiplayerScreen(null) {

        @Override
        public void setTooltip(final List<OrderedText> tooltip, final TooltipPositioner positioner, final boolean focused) {
            ServerPinger.this.tooltip = tooltip;
            ServerPinger.this.positioner = positioner;
        }

        @Override
        public ServerList getServerList() {
            return new ServerList(MinecraftClient.getInstance()) {

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public void saveFile() {
                }
            };
        }
    };
    private MultiplayerServerListWidget.ServerEntry serverEntry;
    private MultiplayerServerListWidget.ServerEntry previousServerEntry;

    private List<OrderedText> tooltip;
    private TooltipPositioner positioner;

    public ServerPinger(final int x, final int y) {
        super(x, y, WIDTH, HEIGHT, Text.empty());
    }

    public void updateServer(final String address) {
        final ServerInfo serverInfo = new ServerInfo("", address, ServerInfo.ServerType.OTHER);
        serverInfo.setStatus(ServerInfo.Status.INITIAL);

        multiplayerScreen.getServerListPinger().cancel();
        serverEntry = WIDGET.new ServerEntry(multiplayerScreen, serverInfo);
        if (previousServerEntry == null) {
            previousServerEntry = serverEntry;
        }
    }

    @Override
    protected void renderWidget(final DrawContext context, final int mouseX, final int mouseY, final float delta) {
        if (serverEntry == null || previousServerEntry == null) {
            return;
        }

        final ServerInfo.Status status = serverEntry.getServer().getStatus();
        if (status == ServerInfo.Status.INITIAL || status == ServerInfo.Status.PINGING) {
            // The server entry is only pinged when rendered, but we only want to show it when it's successful, therefore
            // render it off-screen to avoid showing it when it's not successful
            serverEntry.render(context, 0, context.getScaledWindowHeight(), context.getScaledWindowWidth(), 0, 0, mouseX, mouseY, false, delta);
        } else if (status == ServerInfo.Status.SUCCESSFUL) {
            previousServerEntry = serverEntry;
        } else if (status == ServerInfo.Status.UNREACHABLE || status == ServerInfo.Status.INCOMPATIBLE) {
            return;
        }

        if (previousServerEntry.getServer().getStatus() != ServerInfo.Status.SUCCESSFUL) {
            return;
        }

        final MultiplayerServerListWidget.ServerEntry entry = status != ServerInfo.Status.SUCCESSFUL ? previousServerEntry : serverEntry;
        context.fill(getX(), getY(), getX() + WIDTH, getY() + HEIGHT, IzeModImpl.INSTANCE.getThemeColor().getRGB());
        entry.render(context, 0, getY() + 2, getX() + 2, WIDTH + 1, HEIGHT, mouseX, mouseY, false, delta);
        if (tooltip != null) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, positioner, mouseX, mouseY);
            tooltip = null;
        }

        final String brand = I18n.translate("screens.directconnect.brand");
        final String version = I18n.translate("screens.directconnect.version");
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Formatting.RED + brand + ": " + Formatting.AQUA + entry.getServer().version.getString(), getX() + 2, getY() + 40, -1);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Formatting.RED + version + ": " + Formatting.AQUA + entry.getServer().protocolVersion, getX() + 2, getY() + 50, -1);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        return false;
    }

    @Override
    protected void appendClickableNarrations(final NarrationMessageBuilder builder) {
    }

}
