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
import net.izestudios.izemod.api.command.AbstractCommand;
import net.minecraft.command.CommandSource;
import org.lwjgl.glfw.GLFW;
import java.util.HashMap;
import java.util.Map;

public final class BindCommand extends AbstractCommand {

    private static final Map<String, Integer> keyBinds = new HashMap<>();

    public BindCommand() {
        super("bind");
    }

    @Override
    public void builder(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("module", string())
            .then(argument("key", string())
                .executes(commandContext -> {
                    String moduleName = getString(commandContext, "module");
                    String keyName = getString(commandContext, "key").toUpperCase();

                    if (keyName.equalsIgnoreCase("NONE")) {
                        keyBinds.remove(moduleName);
                        printSuccessMessage("Removed bind for " + moduleName + ".");
                        return SUCCESS;
                    }

                    int key = GLFW.glfwGetKeyScancode(GLFW.glfwGetKeyName(GLFW.glfwGetKeyScancode(keyName), 0));

                    if (key != -1) {
                        keyBinds.put(moduleName, key);
                        printSuccessMessage(moduleName + " is now bound to " + keyName + ".");
                        return SUCCESS;
                    } else {
                        printErrorMessage("Invalid key: " + keyName);
                        return FAILURE;
                    }
                })
            )
        );
    }

    public static int getKeyBind(String moduleName) {
        return keyBinds.getOrDefault(moduleName, -1);
    }
}
