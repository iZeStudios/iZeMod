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

package net.izestudios.izemod.component.discord;

import java.time.Instant;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;
import net.izestudios.izemod.api.discord.DiscordRPC;

public final class DiscordRPCImpl implements DiscordRPC {

    public static final DiscordRPCImpl INSTANCE = new DiscordRPCImpl();
    private static final long APPLICATION_ID = 1352601841376165908L;
    private long startTime = -1L;

    @Override
    public void start() {
        if (startTime != -1L) {
            return;
        }

        if (!DiscordIPC.start(APPLICATION_ID, () -> System.out.println("Logged in account: " + DiscordIPC.getUser().username))) {
            System.out.println("Discord RPC failed to start");
            return;
        }

        startTime = Instant.now().getEpochSecond();
        update(null, null);
    }

    @Override
    public void stop() {
        if (startTime == -1L) {
            return;
        }

        DiscordIPC.stop();
        startTime = -1L;
    }

    @Override
    public void update(final String first, final String second) {
        if (startTime == -1L) {
            return;
        }

        final RichPresence presence = new RichPresence();
        presence.setDetails(first);
        presence.setState(second);
        presence.setLargeImage("icon", "© iZeStudios");
        presence.setStart(startTime);

        DiscordIPC.setActivity(presence);
    }

}
