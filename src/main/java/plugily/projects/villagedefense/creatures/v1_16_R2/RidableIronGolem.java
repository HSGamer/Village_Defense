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

package plugily.projects.villagedefense.creatures.v1_16_R2;

import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityIronGolem;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.GenericAttributes;
import net.minecraft.server.v1_16_R2.IMonster;
import net.minecraft.server.v1_16_R2.PathfinderGoalDefendVillage;
import net.minecraft.server.v1_16_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R2.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_16_R2.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_16_R2.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

/**
 * Created by Tom on 17/08/2014.
 */
public class RidableIronGolem extends EntityIronGolem {

  public RidableIronGolem(org.bukkit.World world) {
    this(((CraftWorld) world).getHandle());
  }

  public RidableIronGolem(World world) {
    super(EntityTypes.IRON_GOLEM, world);

    GoalSelectorCleaner.clearSelectors(this);

    this.a(1.4F, 2.9F);
    getNavigation().q().b(true);
    goalSelector.a(0, new PathfinderGoalFloat(this));
    goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
    goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
    goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, false, 4, () -> false));
    goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
    goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.6D));
    goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    targetSelector.a(1, new PathfinderGoalDefendVillage(this));
    targetSelector.a(2, new PathfinderGoalHurtByTarget(this));
    targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 0, false, true, (entity) -> entity instanceof IMonster && !entity.isInvisible()));
    setHealth(500);
    p().a(GenericAttributes.FOLLOW_RANGE, 200.0D);
  }

  /*@Override
  public void a(float f, float f1, float f2) {
    EntityLiving entityliving = null;
    for (final Entity e : passengers) {
      if (e instanceof EntityHuman) {
        entityliving = (EntityLiving) e;
        break;
      }
    }
    if (entityliving == null) {
      this.P = 0.5F;
      this.aR = 0.02F;
      o(0.12f);
      this.k((float) 0.12);
      super.a(f, f1, f2);
      return;
    }
    this.lastYaw = this.yaw = entityliving.yaw;
    this.pitch = entityliving.pitch * 0.5F;
    this.setYawPitch(this.yaw, this.pitch);
    this.aO = this.aM = this.yaw;

    f = entityliving.bh * 0.5F * 0.75F;
    f2 = entityliving.bj;
    if (f2 <= 0.0f) {
      f2 *= 0.25F;
    }

    //for 1.13
    entityliving.bj = 0.12f;
    o(0.12f);

    super.a(f, f1, f2);
    P = (float) 1.0;
  }*/ //todo


  /*@Override
  protected void dropDeathLoot(boolean flag, int i) {
    //do not drop death loot
  }*/

}
