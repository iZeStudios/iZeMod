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

package net.izestudios.izemod.component.command.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UUIDCommand extends AbstractCommand {

    String uuid;

    public UUIDCommand() {
        super(Text.translatable("commands.uuid"), "uuid");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {


        builder.then(argument("name", StringArgumentType.string()).executes(commandContext -> {
            String name = StringArgumentType.getString(commandContext, "name");
            uuid = fetchUUID(name);
            if(uuid.equals("")){
                printErrorMessage(Text.translatable("commands.uuid.invalid"));
                return FAILURE;
            }else {
                MinecraftClient.getInstance().keyboard.setClipboard(uuid);
                printSuccessMessage(Text.translatable("commands.uuid.success"));
                return SUCCESS;
            }
        })).executes(commandContext -> {
            uuid = MinecraftClient.getInstance().player.getUuid().toString().replace("-", "");
            if(uuid.equals("")){
                printErrorMessage(Text.translatable("commands.uuid.invalid"));
                return FAILURE;
            }else {
                MinecraftClient.getInstance().keyboard.setClipboard(uuid);
                printSuccessMessage(Text.translatable("commands.uuid.success"));
                return SUCCESS;
            }
        });
    }

    private String fetchUUID(final String name){
        String uuid;
        JsonObject jp;

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            jp = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        } catch (IOException e) {
            return "";
        }
        if(jp.has("id")){
            uuid = jp.get("id").getAsString();
        }else {
            uuid = "";
        }

        return uuid;
    }
}
