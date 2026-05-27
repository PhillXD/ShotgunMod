package philxd.shotgunmod;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue RECOIL_STRENGTH = BUILDER.comment("The strength of the recoil").defineInRange("recoilStrength", 1.0, 0.0, Double.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue SHOTGUN_SPREAD = BUILDER.comment("The spread of the shotgun (angle in degrees)").defineInRange("shotgunSpread", 12.6, 0.0, 180);
    public static final ModConfigSpec.DoubleValue KNOCKBACK_STRENGTH = BUILDER.comment("The strength of the knockback").defineInRange("knockbackStrength", 3.0, 0.0, Double.MAX_VALUE);
    public static final ModConfigSpec.IntValue RELOAD_COOLDOWN = BUILDER.comment("The cooldown after reloading").defineInRange("reloadCooldown", 15, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue SHOOT_COOLDOWN = BUILDER.comment("The cooldown after shooting").defineInRange("shootCooldown", 11, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue MISSFIRE_COOLDOWN = BUILDER.comment("The cooldown after a missfire").defineInRange("missfireCooldown", 5, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue SHOW_HUD = BUILDER.comment("Show ammo hud").define("showHud", true);
    public static final ModConfigSpec.DoubleValue HUD_ROTATION_SPEED = BUILDER.comment("The rotation speed of the ammo hud").defineInRange("hudRotationSpeed", 2.0, 0.0, Double.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue HUD_DISTANCE_FROM_CENTER = BUILDER.comment("The distance of the ammo hud from the center of the screen").defineInRange("hudDistanceFromCenter", 30.0, 0.0, Double.MAX_VALUE);
    public static final ModConfigSpec.IntValue HUD_AMMO_SIZE = BUILDER.comment("The size of the ammo hud").defineInRange("hudAmmoSize", 4, 1, 16);
    
    public static final ModConfigSpec.DoubleValue FALL_DAMAGE_REDUCTION = BUILDER.comment("The reduction of fall damage when holding a shotgun in percent (100 = no fall damage)").defineInRange("fallDamageReduction", 40, 0.0, 100.0);
    
    public static final ModConfigSpec.DoubleValue SHOTGUN_BASE_DAMAGE = BUILDER.comment("The base damage of the shotgun").defineInRange("shotgunBaseDamage", 5.0, 0.0, Double.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue DAMAGE_FALLOFF = BUILDER.comment("The damage falloff per block of the shotgun").defineInRange("damageFalloff", 0.625, 0.0, Double.MAX_VALUE);
    
    public static final ModConfigSpec.IntValue MAX_DAMAGE_RANGE = BUILDER.comment("The maximum range it detects for enemy hits").defineInRange("maxDamageRange", 8, 0, Integer.MAX_VALUE);
    
    public static final ModConfigSpec.EnumValue<PearlAction> ENDER_PEARL_ACTION = BUILDER.comment("What should happen to ender pearls when you shoot them").defineEnum("enderPearlAction", PearlAction.SHOOT);
    public static final ModConfigSpec.BooleanValue ENDER_PEARL_THROW_SHOOT = BUILDER.comment("Should ender pearls get boosted velocity when throwing them with shotgun in hand").define("enderPearlThrowShoot", true);
    public static final ModConfigSpec.DoubleValue ENDER_PEARL_BOOST_AMOUNT = BUILDER.comment("The amount of velocity to boost ender pearls when throwing them with shotgun in hand or shooting them").defineInRange("enderPearlBoostAmount", 1.5, 0.0, Double.MAX_VALUE);
    
    public static final ModConfigSpec.BooleanValue AUTO_RELOAD_ON_SHOOT = BUILDER.comment("Automatically reload the shotgun when shooting while it's empty").define("autoReloadOnShoot", false);
    public static final ModConfigSpec.BooleanValue NO_RECOIL_WHEN_SNEAKING = BUILDER.comment("Disable recoil when sneaking").define("noRecoilWhenSneaking", true);
    
    public static double getFallDamageReduction() {
        return 1 - (FALL_DAMAGE_REDUCTION.get() / 100);
    }
    
    public static double getShotgunSpread() {
        return 1-(SHOTGUN_SPREAD.get()/180);
        
    }
    
    static final ModConfigSpec SPEC = BUILDER.build();

    public enum PearlAction {
        DESTROY,
        IGNORE,
        SHOOT
    }
}
