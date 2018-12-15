/*
 * Village Defense 4 - Protect villagers from hordes of zombies
 * Copyright (C) 2018  Plajer's Lair - maintained by Plajer and Tigerpanzer
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

package pl.plajer.villagedefense.user.data;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.plajer.villagedefense.Main;
import pl.plajer.villagedefense.api.StatsStorage;
import pl.plajer.villagedefense.user.User;
import pl.plajer.villagedefense.utils.MessageUtils;
import pl.plajerlair.core.database.MySQLDatabase;

/**
 * @author Plajer
 * <p>
 * Created at 28.09.2018
 */
public class MySQLManager {

  private MySQLDatabase database;

  public MySQLManager(Main plugin) {
    database = plugin.getMySQLDatabase();
    try {
      Connection conn = database.getManager().getConnection();
      conn.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `playerstats` (\n"
          + "  `UUID` text NOT NULL,\n"
          + "  `name` text NOT NULL,\n"
          + "  `kills` int(11) NOT NULL DEFAULT '0',\n"
          + "  `deaths` int(11) NOT NULL DEFAULT '0',\n"
          + "  `highestwave` int(11) NOT NULL DEFAULT '0',\n"
          + "  `gamesplayed` int(11) NOT NULL DEFAULT '0',\n"
          + "  `level` int(11) NOT NULL DEFAULT '0',\n"
          + "  `xp` int(11) NOT NULL DEFAULT '0',\n"
          + "  `orbs` int(11) NOT NULL DEFAULT '0'\n"
          + ");");

      //temporary workaround
      try {
        conn.createStatement().executeUpdate("ALTER TABLE playerstats ADD `name` text NOT NULL");
      } catch (MySQLSyntaxErrorException e) {
        if (!e.getMessage().contains("Duplicate column name")) {
          e.printStackTrace();
        }
      }
      database.getManager().closeConnection(conn);
    } catch (SQLException e) {
      e.printStackTrace();
      MessageUtils.errorOccurred();
      Bukkit.getConsoleSender().sendMessage("Cannot save contents to MySQL database!");
      Bukkit.getConsoleSender().sendMessage("Check configuration of mysql.yml file or disable mysql option in config.yml");
    }
  }

  public void insertPlayer(Player player) {
    database.executeUpdate("INSERT INTO playerstats (UUID,name,xp) VALUES ('" + player.getUniqueId().toString() + "','" + player.getName() + "',0)");
  }

  public void saveStat(User user, StatsStorage.StatisticType stat) {
    database.executeUpdate("UPDATE playerstats SET " + stat.getName() + "=" + user.getStat(stat) + " WHERE UUID='" + user.toPlayer().getUniqueId().toString() + "';");
  }

  public int getStat(User user, StatsStorage.StatisticType stat) {
    ResultSet resultSet = database.executeQuery("SELECT UUID from playerstats WHERE UUID='" + user.toPlayer().getUniqueId().toString() + "'");
    //insert into the database
    try {
      if (!resultSet.next()) {
        insertPlayer(user.toPlayer());
      }
    } catch (SQLException e1) {
      System.out.print("CONNECTION FAILED FOR PLAYER " + user.toPlayer().getName());
    }

    ResultSet set = database.executeQuery("SELECT " + stat.getName() + " FROM playerstats WHERE UUID='" + user.toPlayer().getUniqueId().toString() + "'");
    try {
      if (!set.next()) {
        return 0;
      }
      return set.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

}
