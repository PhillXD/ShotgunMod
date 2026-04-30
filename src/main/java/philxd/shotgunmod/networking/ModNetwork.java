package philxd.shotgunmod.networking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import philxd.shotgunmod.Config;
import philxd.shotgunmod.Shotgunmod;
import philxd.shotgunmod.items.Shotgun;
import philxd.shotgunmod.networking.packet.ReloadC2SPacket;
import philxd.shotgunmod.sound.ModSounds;
import software.bernie.geckolib.animatable.GeoItem;

@EventBusSubscriber(modid = Shotgunmod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1"); // version string

        registrar.playToServer(
                ReloadC2SPacket.TYPE,
                ReloadC2SPacket.STREAM_CODEC,
                ModNetwork::handleReload
        );
    }

    private static void handleReload(ReloadC2SPacket packet, IPayloadContext context) {
        // enqueueWork runs on the main server thread — safe to touch game state
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack stack = null;

            if (player.getMainHandItem().getItem() instanceof Shotgun)
                stack = player.getMainHandItem();
            else if (player.getOffhandItem().getItem() instanceof Shotgun)
                stack = player.getOffhandItem();

            if (stack == null) return;
            if (player.getCooldowns().isOnCooldown(stack.getItem())) return;

            player.getCooldowns().addCooldown(stack.getItem(), Config.RELOAD_COOLDOWN.get());
            stack.setDamageValue(0);

            // Trigger the reload animation server-side
            ServerLevel level = (ServerLevel) player.level();
            ((Shotgun) stack.getItem()).triggerAnim(
                    player,
                    GeoItem.getOrAssignId(stack, level),
                    "controller",
                    "reload"
            );

            level.playSound(null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.RELOAD_SOUND.get(),
                    SoundSource.PLAYERS,
                    1f,
                    player.getRandom().nextFloat() * 0.5f + 0.75f
            );
        });
    }
}
