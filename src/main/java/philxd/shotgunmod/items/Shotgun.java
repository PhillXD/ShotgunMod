package philxd.shotgunmod.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import philxd.shotgunmod.Config;
import philxd.shotgunmod.items.client.ShotgunRenderer;
import philxd.shotgunmod.sound.ModSounds;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;
import java.util.function.Consumer;

public class Shotgun extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    public Shotgun(Properties properties) {
        super(properties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            player.getCooldowns().addCooldown(this, Config.MISSFIRE_COOLDOWN.get());
            return InteractionResultHolder.fail(stack);
        }
        
        player.getCooldowns().addCooldown(this, Config.SHOOT_COOLDOWN.get());

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            
            double recoilStrength = Config.RECOIL_STRENGTH.get();

            player.setDeltaMovement(player.getDeltaMovement().add(
                    -player.getLookAngle().x * recoilStrength *1,
                    -player.getLookAngle().y * recoilStrength * 1,
                    -player.getLookAngle().z * recoilStrength * 1
            ));
            player.hurtMarked = true;
            
            List<LivingEntity> entities = level.getNearbyEntities(
                    LivingEntity.class,
                    TargetingConditions.DEFAULT,
                    player,
                    player.getBoundingBox().inflate(Config.MAX_DAMAGE_RANGE.get())
            );

            Vec3 pos = player.getEyePosition();
            Vec3 look = player.getLookAngle();

            for (LivingEntity entity : entities) {
                Vec3 toTarget = entity.getEyePosition().subtract(pos).normalize();
                
                if (toTarget.dot(look) > Config.getShotgunSpread()) {

                    BlockHitResult result = level.clip(new ClipContext(
                            pos,
                            entity.getEyePosition(),
                            ClipContext.Block.OUTLINE,
                            ClipContext.Fluid.NONE,
                            player
                    ));

                    if (result.getType() == HitResult.Type.BLOCK)
                        continue;
                    
                    double distance = Math.min(pos.distanceTo(entity.position()),0.5);
                    double DamageReduction = Math.min((distance-0.5) / Config.DAMAGE_FALLOFF.get(),Config.SHOTGUN_BASE_DAMAGE.get());
                    entity.hurt(level.damageSources().playerAttack(player), (float)(Config.SHOTGUN_BASE_DAMAGE.get() - DamageReduction));
                    double knockbackStrenght = Config.KNOCKBACK_STRENGTH.get();
                    entity.push(
                            look.x * (knockbackStrenght / distance),
                            look.y * (knockbackStrenght / distance),
                            look.z * (knockbackStrenght / distance)
                    );
                }
            }

            if (!player.isCreative()) {
                stack.setDamageValue(stack.getDamageValue() + 1);
            }

            this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "shoot");

            level.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.SHOOT_SOUND.get(),
                    SoundSource.PLAYERS,
                    1f,
                    player.getRandom().nextFloat() * 0.5f + 0.75f
            );
        }
        
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "controller", state -> PlayState.STOP)
                        .triggerableAnim("shoot", RawAnimation.begin().thenPlay("animation.shotgun.shoot"))
                        .triggerableAnim("reload", RawAnimation.begin().thenPlay("animation.shotgun.reload"))
        );
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ShotgunRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new ShotgunRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public double getTick(Object itemStack){
        return RenderUtil.getCurrentTick();
    }
}
