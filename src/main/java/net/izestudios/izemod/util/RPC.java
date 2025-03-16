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

package net.izestudios.izemod.util;

import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;
import java.time.Instant;

public class RPC {

    public static void startup(long appID, String line1, String line2){


        if (!DiscordIPC.start(appID, () -> System.out.println("Logged in account: " + DiscordIPC.getUser().username))) {
            System.out.println("Failed to start Discord IPC");
            return;
        }

        update(line1, line2);

    }
    public static void update(String line1,String line2){
        RichPresence presence = new RichPresence();
        presence.setDetails(line1);
        presence.setState(line2);
        presence.setLargeImage("large", "© iZeStudios");
        presence.setStart(Instant.now().getEpochSecond());
        DiscordIPC.setActivity(presence);
    }

}
