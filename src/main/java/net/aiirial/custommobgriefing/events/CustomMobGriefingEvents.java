package net.aiirial.custommobgriefing.events;

import net.aiirial.custommobgriefing.CustomMobGriefing;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

public class CustomMobGriefingEvents {

    // 1️⃣ Explosionen verhindern
    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        Entity source = explosion.getDirectSourceEntity();
        if (source == null) return;
        if (!(source.level() instanceof ServerLevel serverLevel)) return;

        if ((source instanceof Creeper && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.CREEPER_GRIEFING)) ||
                (source instanceof WitherBoss && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.WITHER_GRIEFING)) ||
                (source instanceof EnderDragon && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERDRAGON_GRIEFING))) {
            event.getAffectedBlocks().clear();
        }
    }

    // 2️⃣ Projektile (Ghast + Wither)
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Entity projectile = event.getProjectile();
        if (!(projectile.level() instanceof ServerLevel serverLevel)) return;

        if (projectile instanceof Fireball &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.GHAST_GRIEFING) &&
                event.getRayTraceResult().getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            event.setCanceled(true);
        }

        if (projectile instanceof WitherSkull &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.WITHER_GRIEFING) &&
                event.getRayTraceResult().getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            event.setCanceled(true);
        }
    }

    // 3️⃣ Blockzerstörung durch Mobs (Zombies, Ravager, Silverfish, EnderDragon)
    @SubscribeEvent
    public static void onMobBlockBreak(LivingDestroyBlockEvent event) {
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        BlockState state = event.getState();

        // Zombie: Turtle-Eier & Türen schützen
        if (entity instanceof Zombie &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ZOMBIE_GRIEFING)) {
            if (state.is(Blocks.TURTLE_EGG) || state.is(BlockTags.WOODEN_DOORS)) {
                event.setCanceled(true);
            }
        }

        // Ravager: Blätter & Pflanzen schützen
        if (entity instanceof Ravager &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.RAVAGER_GRIEFING)) {
            if (state.is(Blocks.OAK_LEAVES) || state.is(Blocks.BIRCH_LEAVES) ||
                    state.is(Blocks.SPRUCE_LEAVES) || state.is(Blocks.JUNGLE_LEAVES) ||
                    state.is(Blocks.ACACIA_LEAVES) || state.is(Blocks.DARK_OAK_LEAVES) ||
                    state.is(Blocks.MANGROVE_LEAVES) || state.is(Blocks.CHERRY_LEAVES) ||
                    state.is(Blocks.FLOWERING_AZALEA_LEAVES) || state.is(Blocks.AZALEA_LEAVES) ||
                    state.is(Blocks.TALL_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.LARGE_FERN)) {
                event.setCanceled(true);
            }
        }

        // Silverfish: keine Blöcke zerstören
        if (entity instanceof Silverfish &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.SILVERFISH_GRIEFING)) {
            event.setCanceled(true);
        }

        // EnderDragon: Flug-Blockzerstörung verhindern
        if (entity instanceof EnderDragon &&
                !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERDRAGON_GRIEFING)) {
            event.setCanceled(true);
        }
    }

    // 4️⃣ Universelles Griefing Event (inkl. Endermen, Zombies, etc.)
    @SubscribeEvent
    public static void onEntityMobGriefing(EntityMobGriefingEvent event) {
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        if (entity instanceof Creeper && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.CREEPER_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof WitherBoss && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.WITHER_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof EnderDragon && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERDRAGON_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof Ghast && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.GHAST_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof EnderMan && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ENDERMAN_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof Ravager && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.RAVAGER_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof Silverfish && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.SILVERFISH_GRIEFING)) {
            event.setCanGrief(false);
        }
        if (entity instanceof Zombie && !serverLevel.getGameRules().getBoolean(CustomMobGriefing.ZOMBIE_GRIEFING)) {
            event.setCanGrief(false);
        }
    }
}
