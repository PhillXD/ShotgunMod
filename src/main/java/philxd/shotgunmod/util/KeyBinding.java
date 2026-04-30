package philxd.shotgunmod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;
import philxd.shotgunmod.Shotgunmod;

@EventBusSubscriber(modid = Shotgunmod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class KeyBinding {
    public static final String KEY_CATEGORY_SHOTGUN = "key.category.shotgunmod.shotgun";
    public static final String KEY_RELOAD = "key.shotgunmod.reload";

    public static final KeyMapping RELOAD_KEY = new KeyMapping(
            KEY_RELOAD,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEY_CATEGORY_SHOTGUN
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.RELOAD_KEY);
    }
}
