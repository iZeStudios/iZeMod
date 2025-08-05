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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.izestudios.izemod.api.command.AbstractCommand;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.nio.file.Path;

public class MCCommand extends AbstractCommand {
    public MCCommand() {
        super(Component.translatable("commands.mc"), "mc");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            try {

                openDirectoryWithExplorer(FabricLoader.getInstance().getConfigDir());
                printSuccessMessage(Component.translatable("commands.mc.success"));

                return SUCCESS;
            } catch (IOException e) {
                // In case it can't find the folder, lol.
                printErrorMessage(Component.translatable("commands.mc.failure"));
                return FAILURE;
            }
        });
    }

    private void openDirectoryWithExplorer(final Path directoryPath) throws IOException {
        final String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("explorer.exe", "/select," + directoryPath);
        } else if (os.contains("mac")) {
            processBuilder = new ProcessBuilder("open", directoryPath.toString());
        } else if (os.contains("nix") || os.contains("nux")) {
            processBuilder = new ProcessBuilder("xdg-open", directoryPath.toString());
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
        processBuilder.start();
    }
}
