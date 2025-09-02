package net.aiirial.custommobgriefing;

import net.aiirial.custommobgriefing.config.CustomMobGriefingConfig;
import net.aiirial.custommobgriefing.events.CustomMobGriefingEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CustomMobGriefing.MOD_ID)
public class CustomMobGriefing {

    public static final String MOD_ID = "custommobgriefing";

    // Gamerules
    public static GameRules.Key<GameRules.BooleanValue> CREEPER_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> WITHER_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> ENDERDRAGON_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> GHAST_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> ENDERMAN_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> RAVAGER_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> SILVERFISH_GRIEFING;
    public static GameRules.Key<GameRules.BooleanValue> ZOMBIE_GRIEFING;

    public CustomMobGriefing() {
        // Events für Mob-Griefing registrieren
        NeoForge.EVENT_BUS.addListener(CustomMobGriefingEvents::onExplosionDetonate);
        NeoForge.EVENT_BUS.addListener(CustomMobGriefingEvents::onProjectileImpact);
        NeoForge.EVENT_BUS.addListener(CustomMobGriefingEvents::onMobBlockBreak);

        // Server Events registrieren (z. B. ServerStarting)
        NeoForge.EVENT_BUS.register(this);

        // Gamerules registrieren
        registerGameRules();
    }

    private void registerGameRules() {
        CREEPER_GRIEFING = register("griefingCreeper", "creeper");
        WITHER_GRIEFING = register("griefingWither", "wither");
        ENDERDRAGON_GRIEFING = register("griefingEnderDragon", "ender_dragon");
        GHAST_GRIEFING = register("griefingGhast", "ghast");
        ENDERMAN_GRIEFING = register("griefingEnderman", "enderman");
        RAVAGER_GRIEFING = register("griefingRavager", "ravager");
        SILVERFISH_GRIEFING = register("griefingSilverfish", "silverfish");
        ZOMBIE_GRIEFING = register("griefingZombie", "zombie");
    }

    private GameRules.Key<GameRules.BooleanValue> register(String name, String mobKey) {
        return GameRules.register(
                name,
                GameRules.Category.MOBS,
                GameRules.BooleanValue.create(
                        CustomMobGriefingConfig.isGriefingAllowed(mobKey),
                        (MinecraftServer server, GameRules.BooleanValue value) -> {
                            // Wird aufgerufen, wenn die Gamerule geändert wird
                            if (server != null) {
                                for (ServerLevel level : server.getAllLevels()) {
                                    syncGameRules(level);
                                }
                            }
                        }
                )
        );
    }

    // Sync Gamerules → JSON-Config überschreiben
    public static void syncGameRules(ServerLevel level) {
        CustomMobGriefingConfig.setGriefingAllowed("creeper", level.getGameRules().getBoolean(CREEPER_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("wither", level.getGameRules().getBoolean(WITHER_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("ender_dragon", level.getGameRules().getBoolean(ENDERDRAGON_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("ghast", level.getGameRules().getBoolean(GHAST_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("enderman", level.getGameRules().getBoolean(ENDERMAN_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("ravager", level.getGameRules().getBoolean(RAVAGER_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("silverfish", level.getGameRules().getBoolean(SILVERFISH_GRIEFING));
        CustomMobGriefingConfig.setGriefingAllowed("zombie", level.getGameRules().getBoolean(ZOMBIE_GRIEFING));
    }

    // Beim Serverstart → Config-Werte in Gamerules übernehmen
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            level.getGameRules().getRule(CREEPER_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("creeper"), server);
            level.getGameRules().getRule(WITHER_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("wither"), server);
            level.getGameRules().getRule(ENDERDRAGON_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("ender_dragon"), server);
            level.getGameRules().getRule(GHAST_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("ghast"), server);
            level.getGameRules().getRule(ENDERMAN_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("enderman"), server);
            level.getGameRules().getRule(RAVAGER_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("ravager"), server);
            level.getGameRules().getRule(SILVERFISH_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("silverfish"), server);
            level.getGameRules().getRule(ZOMBIE_GRIEFING).set(CustomMobGriefingConfig.isGriefingAllowed("zombie"), server);
        }
    }
}
