package philxd.shotgunmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import philxd.shotgunmod.Shotgunmod;
import philxd.shotgunmod.items.Shotgun;
import philxd.shotgunmod.networking.packet.ReloadC2SPacket;
import philxd.shotgunmod.util.KeyBinding;

@EventBusSubscriber(modid = Shotgunmod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.level == null) return;

        // consumeClick() handles multiple queued presses correctly
        while (KeyBinding.RELOAD_KEY.consumeClick()) {
            ItemStack stack = null;

            if (player.getMainHandItem().getItem() instanceof Shotgun)
                stack = player.getMainHandItem();
            else if (player.getOffhandItem().getItem() instanceof Shotgun)
                stack = player.getOffhandItem();

            if (stack == null) continue;
            if (player.getCooldowns().isOnCooldown(stack.getItem())) continue;

            // Send to server — server handles cooldown, damage reset, animation, sound
            PacketDistributor.sendToServer(new ReloadC2SPacket());
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        PlayerRenderer renderer = event.getRenderer();
        PlayerModel<AbstractClientPlayer> model = renderer.getModel();

        // Check if they're holding your gun
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean holdingGun = mainHand.getItem() instanceof Shotgun
                || offHand.getItem() instanceof Shotgun;
        if (!holdingGun) return;

        float pitch = event.getEntity().getXRot(); // already synced by vanilla
        float yaw = event.getEntity().getYHeadRot() - event.getEntity().yBodyRot;

        // Convert to radians
        float pitchRad = (float) Math.toRadians(pitch);
        float yawRad   = (float) Math.toRadians(yaw);

        // Rotate the right arm bone (or left for offhand)
        boolean isOffhand = offHand.getItem() instanceof Shotgun;

        if (!isOffhand) {
            model.rightArm.xRot = -pitchRad;
            model.rightArm.yRot = -yawRad;
        } else {
            model.leftArm.xRot = -pitchRad;
            model.leftArm.yRot = yawRad;
        }
    }
}
