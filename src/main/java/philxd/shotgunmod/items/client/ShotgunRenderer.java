package philxd.shotgunmod.items.client;

import philxd.shotgunmod.items.Shotgun;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ShotgunRenderer extends GeoItemRenderer<Shotgun>{
    public <I extends Shotgun> ShotgunRenderer() {
        super(new ShotgunModel());
    }
}
