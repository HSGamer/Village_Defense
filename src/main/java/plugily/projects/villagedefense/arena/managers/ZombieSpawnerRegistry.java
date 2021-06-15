package plugily.projects.villagedefense.arena.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import plugily.projects.villagedefense.Main;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.managers.spawner.BabyZombieSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.FastZombieSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.GolemBusterSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.HalfInvisibleZombieSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.HardZombieSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.KnockbackResistantZombieSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.PlayerBusterSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.SoftHardZombieSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.VillagerBusterSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.VillagerSlayerSpawner;
import plugily.projects.villagedefense.arena.managers.spawner.ZombieSpawner;
import plugily.projects.villagedefense.arena.options.ArenaOption;

/**
 * The registry for all {@link ZombieSpawner}
 */
public class ZombieSpawnerRegistry {
  private final List<ZombieSpawner> zombieSpawnerList = new ArrayList<>();
  private final Main plugin;

  public ZombieSpawnerRegistry(Main plugin) {
    this.plugin = plugin;

    zombieSpawnerList.add(new FastZombieSpawner());
    zombieSpawnerList.add(new VillagerSlayerSpawner());
    zombieSpawnerList.add(new KnockbackResistantZombieSpawner());
    zombieSpawnerList.add(new HardZombieSpawner());
    zombieSpawnerList.add(new SoftHardZombieSpawner());
    zombieSpawnerList.add(new PlayerBusterSpawner());
    zombieSpawnerList.add(new GolemBusterSpawner());
    zombieSpawnerList.add(new VillagerBusterSpawner());
    zombieSpawnerList.add(new BabyZombieSpawner());
    zombieSpawnerList.add(new HalfInvisibleZombieSpawner());
  }

  /**
   * Spawn the zombies at the arena
   *
   * @param random the random instance
   * @param arena  the arena
   */
  public void spawnZombies(Random random, Arena arena) {
    int wave = arena.getWave();
    int spawn = arena.getWave();
    int zombiesLimit = plugin.getConfig().getInt("Zombies-Limit", 75);
    if (zombiesLimit < wave) {
      spawn = (int) Math.ceil(zombiesLimit / 2.0);
    }

    arena.addOptionValue(ArenaOption.ZOMBIE_SPAWN_COUNTER, 1);
    if (arena.getOption(ArenaOption.ZOMBIE_SPAWN_COUNTER) == 20) {
      arena.setOptionValue(ArenaOption.ZOMBIE_SPAWN_COUNTER, 0);
    }

    for (ZombieSpawner zombieSpawner : zombieSpawnerList) {
      zombieSpawner.spawnZombie(random, arena, spawn);
    }
  }

  /**
   * Get the list of zombie spawners
   *
   * @return the list of zombie spawners
   */
  public List<ZombieSpawner> getZombieSpawnerList() {
    return zombieSpawnerList;
  }

  /**
   * Get the zombie spawner by its name
   *
   * @param name the name
   * @return the zombie spawner
   */
  public Optional<ZombieSpawner> getSpawnerByName(String name) {
    return zombieSpawnerList.stream()
        .filter(zombieSpawner -> zombieSpawner.getName().equals(name))
        .findFirst();
  }
}