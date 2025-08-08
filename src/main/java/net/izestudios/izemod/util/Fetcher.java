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

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Fetcher {

    public static String fetchDomain2IP(final String domain) {
        final String[] ip = {null};

        Thread thread = new Thread(() -> {
        try {
            ip[0] = InetAddress.getByName(domain).getHostAddress();
        } catch (UnknownHostException ignored) {}
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ignored) {}

        return ip[0];
    }
}
