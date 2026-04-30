package philxd.shotgunmod.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import philxd.shotgunmod.Shotgunmod;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.SOUND_EVENT, Shotgunmod.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SHOOT_SOUND =
            registerSoundEvent("shoot_sound");

    public static final DeferredHolder<SoundEvent, SoundEvent> RELOAD_SOUND =
            registerSoundEvent("reload_sound");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Shotgunmod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }   
}
