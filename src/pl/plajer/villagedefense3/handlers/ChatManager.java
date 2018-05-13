/*
 * Village Defense 3 - Protect villagers from hordes of zombies
 * Copyright (C) 2018  Plajer's Lair - maintained by Plajer
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

package pl.plajer.villagedefense3.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.plajer.villagedefense3.arena.Arena;
import pl.plajer.villagedefense3.language.LanguageManager;
import pl.plajer.villagedefense3.utils.MessageUtils;
import pl.plajer.villagedefense3.utils.Util;

/**
 * Created by Tom on 27/07/2014.
 */
public class ChatManager {

    public static String PLUGIN_PREFIX;

    public ChatManager(String prefix) {
        PLUGIN_PREFIX = prefix;
    }

    public static String colorRawMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String colorMessage(String message) {
        try {
            return ChatColor.translateAlternateColorCodes('&', LanguageManager.getLanguageMessage(message));
        } catch(NullPointerException e1) {
            e1.printStackTrace();
            MessageUtils.errorOccured();
            Bukkit.getConsoleSender().sendMessage("Game message not found!");
            Bukkit.getConsoleSender().sendMessage("Please regenerate your language.yml file! If error still occurs report it to the developer!");
            Bukkit.getConsoleSender().sendMessage("Access string: " + message);
            return "ERR_MESSAGE_NOT_FOUND";
        }
    }

    public static String formatMessage(Arena arena, String message, Player[] players) {
        String returnString = message;
        for(Player player : players) {
            returnString = returnString.replaceFirst("%PLAYER%", player.getName());
        }
        returnString = colorRawMessage(formatPlaceholders(returnString, arena));
        return returnString;
    }

    public static String formatMessage(Arena arena, String message, int integer) {
        String returnString = message;
        returnString = returnString.replaceAll("%NUMBER%", Integer.toString(integer));
        returnString = colorRawMessage(formatPlaceholders(returnString, arena));
        return returnString;
    }

    public static String formatMessage(Arena arena, String message, Player player) {
        String returnString = message;
        returnString = returnString.replaceAll("%PLAYER%", player.getName());
        returnString = colorRawMessage(formatPlaceholders(returnString, arena));
        return returnString;
    }

    private static String formatPlaceholders(String message, Arena arena) {
        String returnstring = message;
        returnstring = returnstring.replaceAll("%TIME%", Integer.toString(arena.getTimer()));
        returnstring = returnstring.replaceAll("%FORMATTEDTIME%", Util.formatIntoMMSS((arena.getTimer())));
        returnstring = returnstring.replaceAll("%PLAYERSIZE%", Integer.toString(arena.getPlayers().size()));
        returnstring = returnstring.replaceAll("%MAXPLAYERS%", Integer.toString(arena.getMaximumPlayers()));
        returnstring = returnstring.replaceAll("%MINPLAYERS%", Integer.toString(arena.getMinimumPlayers()));
        return returnstring;
    }

    public static void broadcastAction(Arena a, Player p, ActionType action){
        String message;
        switch(action){
            case JOIN:
                message = formatMessage(a, ChatManager.colorMessage("In-Game.Messages.Join"), p);
                break;
            case LEAVE:
                message = formatMessage(a, ChatManager.colorMessage("In-Game.Messages.Leave"), p);
                break;
            case DEATH:
                message = formatMessage(a, ChatManager.colorMessage("In-Game.Messages.Death"), p);
                break;
            default: return; //likely won't ever happen
        }
        for(Player player : a.getPlayers()) {
            player.sendMessage(PLUGIN_PREFIX + message);
        }
    }

    public enum ActionType {
        JOIN, LEAVE, DEATH
    }

}

