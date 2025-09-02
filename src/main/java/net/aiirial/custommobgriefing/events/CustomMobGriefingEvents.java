package net.aiirial.custommobgriefing.events;

import net.aiirial.custommobgriefing.CustomMobGriefing;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

public class CustomMobGriefingEvents {

    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        Entity source = explosion.getDirectSourceEntity();
        if (source == null) return;

        if (!(source.level() instanceof ServerLevel serverLevel)) return;

        // Nur Blockschaden verhindern, Spieler bleiben betroffen
        if ((source instanceof Creeper && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.CREEPER_GRIEFING)) ||
                (source instanceof WitherBoss && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.WITHER_GRIEFING)) ||
                (source instanceof EnderDragon && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERDRAGON_GRIEFING))) {
            event.getAffectedBlocks().clear(); // Blockschaden löschen
        }
    }

    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Entity projectile = event.getProjectile();
        if (!(projectile.level() instanceof ServerLevel serverLevel)) return;

        // Blockschaden verhindern, Spieler bleiben betroffen
        if (projectile instanceof Fireball &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.GHAST_GRIEFING)) {
            if (event.getRayTraceResult().getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                event.setCanceled(true);
            }
        }

        if (projectile instanceof WitherSkull &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.WITHER_GRIEFING)) {
            if (event.getRayTraceResult().getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                event.setCanceled(true);
            }
        }
    }

    public static void onMobBlockBreak(LivingDestroyBlockEvent event) {
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        Block block = event.getState().getBlock();

        // Zombies/Vindicators Tür / Turtle Egg
        if ((entity instanceof Zombie || entity instanceof Vindicator) &&
                (isWoodDoor(block) || block == Blocks.TURTLE_EGG) &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ZOMBIE_GRIEFING)) {
            event.setCanceled(true);
        }

        // Enderman
        if (entity instanceof EnderMan &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERMAN_GRIEFING)) {
            event.setCanceled(true);
        }

        // Ravager
        if (entity instanceof Ravager &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.RAVAGER_GRIEFING)) {
            event.setCanceled(true);
        }

        // Silverfish
        if (entity instanceof Silverfish &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.SILVERFISH_GRIEFING)) {
            event.setCanceled(true);
        }

        // EnderDragon (Blockzerstörung durch Flug verhindern)
        if (entity instanceof EnderDragon &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERDRAGON_GRIEFING)) {
            event.setCanceled(true);
        }
    }


    private static boolean isWoodDoor(Block block) {
        return block == Blocks.OAK_DOOR
                || block == Blocks.BIRCH_DOOR
                || block == Blocks.SPRUCE_DOOR
                || block == Blocks.JUNGLE_DOOR
                || block == Blocks.ACACIA_DOOR
                || block == Blocks.DARK_OAK_DOOR;
    }
}
