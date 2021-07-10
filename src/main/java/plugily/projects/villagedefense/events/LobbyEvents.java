/*
 * Village Defense - Protect villagers from hordes of zombies
 * Copyright (C) 2021  Plugily Projects - maintained by 2Wild4You, Tigerpanzer_02 and contributors
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

package plugily.projects.villagedefense.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import plugily.projects.commonsbox.minecraft.compat.VersionUtils;
import plugily.projects.villagedefense.Main;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaRegistry;
import plugily.projects.villagedefense.arena.ArenaState;

/**
 * Created by Tom on 16/06/2015.
 */
public final class LobbyEvents implements Listener {

  public LobbyEvents(Main plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFoodLose(FoodLevelChangeEvent event) {
    if(event.getEntity().getType() != EntityType.PLAYER) {
      return;
    }
    Player player = (Player) event.getEntity();
    if(ArenaRegistry.getArena(player) == null) {
      return;
    }
    Arena arena = ArenaRegistry.getArena(player);
    if(arena.getArenaState() == ArenaState.STARTING || arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onLobbyDamage(EntityDamageEvent event) {
    if(event.getEntity().getType() != EntityType.PLAYER) {
      return;
    }
    Player player = (Player) event.getEntity();
    Arena arena = ArenaRegistry.getArena(player);
    if(arena == null || arena.getArenaState() == ArenaState.IN_GAME) {
      return;
    }
    event.setCancelled(true);
    player.setHealth(VersionUtils.getMaxHealth(player));
  }

  @EventHandler
  public void onItemFrameRotate(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();
    Arena arena = ArenaRegistry.getArena(player);
    if(arena == null || arena.getArenaState() == ArenaState.IN_GAME) {
      return;
    }
    if(event.getRightClicked() instanceof ItemFrame && ((ItemFrame) event.getRightClicked()).getItem().getType() != Material.AIR) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onHangingBreak(HangingBreakByEntityEvent event) {
    if(event.getEntity().getType() != EntityType.PLAYER) {
      return;
    }
    Player player = (Player) event.getEntity();
    Arena arena = ArenaRegistry.getArena(player);
    if(arena == null || arena.getArenaState() == ArenaState.IN_GAME) {
      return;
    }
    event.setCancelled(true);
  }

}
