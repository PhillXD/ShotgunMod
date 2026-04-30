package philxd.shotgunmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import philxd.shotgunmod.Config;
import philxd.shotgunmod.Shotgunmod;
import philxd.shotgunmod.items.Shotgun;

@EventBusSubscriber(modid = Shotgunmod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AmmoHud {

    private static final ResourceLocation AMMO =
            ResourceLocation.fromNamespaceAndPath(Shotgunmod.MODID, "textures/hud/bullet.png");

    private static float direction = 0f;

    // The layer itself — registered below
    public static final LayeredDraw.Layer HUD_AMMO = (guiGraphics, deltaTracker) -> {
        if (!Config.SHOW_HUD.get()) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        // Don't render when a screen is open (inventory, etc.)
        if (mc.screen != null) return;

        ItemStack shotgunStack = null;
        if (player.getMainHandItem().getItem() instanceof Shotgun)
            shotgunStack = player.getMainHandItem();
        else if (player.getOffhandItem().getItem() instanceof Shotgun)
            shotgunStack = player.getOffhandItem();

        if (shotgunStack == null) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int cx = screenWidth / 2;
        int cy = screenHeight / 2;

        float dt = deltaTracker.getGameTimeDeltaPartialTick(true);
        direction = (direction + dt * Config.HUD_ROTATION_SPEED.get().floatValue()) % 360;

        int ammo = shotgunStack.getMaxDamage() - shotgunStack.getDamageValue();

        for (int i = 0; i < ammo; i++) {
            double ang = ((double) i / ammo) * 2 * Math.PI + (direction / 180.0) * Math.PI;
            int bx = (int) (cx - 2 + Math.sin(ang) * Config.HUD_DISTANCE_FROM_CENTER.get());
            int by = (int) (cy - 2 + Math.cos(ang) * Config.HUD_DISTANCE_FROM_CENTER.get());

            guiGraphics.blit(
                    AMMO,   // texture location
                    bx, by, // screen position
                    0, 0,   // uv offset
                    Config.HUD_AMMO_SIZE.get(), Config.HUD_AMMO_SIZE.get(),   // render width/height
                    4, 4    // texture width/height
            );
        }
    };

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(Shotgunmod.MODID, "ammo_hud"),
                HUD_AMMO
        );
    }
}
