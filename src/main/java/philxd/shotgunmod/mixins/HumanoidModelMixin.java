package philxd.shotgunmod.mixins;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import philxd.shotgunmod.items.Shotgun;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void onSetupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch,
                             CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;
        
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand  = player.getOffhandItem();

        boolean mainGun = mainHand.getItem() instanceof Shotgun;
        boolean offGun  = offHand.getItem() instanceof Shotgun;

        if (!mainGun && !offGun) return;

        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        float pitchRad = (float) Math.toRadians(headPitch - 90);
        float yawRad   = (float) Math.toRadians(netHeadYaw);

        boolean isLeftHanded = player.getMainArm() == HumanoidArm.LEFT;
        ModelPart mainArm = isLeftHanded? model.leftArm : model.rightArm;
        ModelPart offArm = isLeftHanded? model.rightArm : model.leftArm;
        
        if (mainGun) {
            mainArm.xRot = pitchRad;
            mainArm.yRot = yawRad;
        }
        if (offGun) {
            offArm.xRot = pitchRad;
            offArm.yRot = yawRad;
        }
    }
}
