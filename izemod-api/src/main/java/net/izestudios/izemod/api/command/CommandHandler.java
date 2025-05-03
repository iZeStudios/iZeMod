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

package net.izestudios.izemod.api.command;

import org.jetbrains.annotations.NotNull;

/**
 * Endpoint to handle commands.
 */
public interface CommandHandler {

    /**
     * Adds a command to the handler.
     *
     * @param command The command to add.
     */
    void addCommand(final @NotNull AbstractCommand command);

    /**
     * Removes a command from the handler.
     *
     * @param command The command to remove.
     */
    void removeCommand(final @NotNull AbstractCommand command);

    /**
     * Removes a command from the handler by one of the aliases. If the alias is not found, nothing happens.
     *
     * @param alias The alias of the command to remove.
     */
    void removeCommand(final @NotNull String alias);

}
