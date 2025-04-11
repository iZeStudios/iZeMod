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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class MojangAPI {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/";

    public static String fetchUUID(final String name) {
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(UUID_URL + name)).GET().build();
        try {
            final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body().isEmpty()) {
                return null;
            }

            final JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            final String uuid = json.has("id") ? json.get("id").getAsString() : null;
            if (uuid != null) {
                return uuidWithDashes(uuid);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static String uuidWithDashes(final String uuid) {
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
    }

}
