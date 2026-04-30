package philxd.shotgunmod.items.client;

import net.minecraft.resources.ResourceLocation;
import philxd.shotgunmod.Shotgunmod;
import philxd.shotgunmod.items.Shotgun;
import software.bernie.geckolib.model.GeoModel;

public class ShotgunModel extends GeoModel<Shotgun> {
    @Override
    public ResourceLocation getModelResource(Shotgun animatable) {
        return ResourceLocation.fromNamespaceAndPath(Shotgunmod.MODID, "geo/shotgun.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Shotgun animatable) {
        return ResourceLocation.fromNamespaceAndPath(Shotgunmod.MODID, "textures/item/shotgun.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Shotgun animatable) {
        return ResourceLocation.fromNamespaceAndPath(Shotgunmod.MODID, "animations/shotgun.animation.json");
    }
}
