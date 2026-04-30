package philxd.shotgunmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import philxd.shotgunmod.Shotgunmod;
import philxd.shotgunmod.items.Shotgun;
import philxd.shotgunmod.networking.packet.ReloadC2SPacket;
import philxd.shotgunmod.util.KeyBinding;

@EventBusSubscriber(modid = Shotgunmod.MODID, value = Dist.CLIENT)
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
}
