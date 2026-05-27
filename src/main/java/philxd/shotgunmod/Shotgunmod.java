package philxd.shotgunmod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import philxd.shotgunmod.items.Shotgun;
import philxd.shotgunmod.items.client.ShotgunRenderer;
import philxd.shotgunmod.sound.ModSounds;
import software.bernie.geckolib.renderer.GeoItemRenderer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Shotgunmod.MODID)
public class Shotgunmod {

    public static final String MODID = "shotgunmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<Item> SHOTGUN_ITEM = ITEMS.register("shotgun", () -> new Shotgun(new Item.Properties().durability(5).setNoRepair()));

  
    public Shotgunmod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        ITEMS.register(modEventBus);

        ModSounds.register(modEventBus);
        
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addToCreativeTab);
        
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        
    }
    
    public void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(SHOTGUN_ITEM);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        boolean holdingShotgun =
                player.getMainHandItem().getItem() instanceof Shotgun ||
                        player.getOffhandItem().getItem() instanceof Shotgun;

        if (!holdingShotgun) return;
        
        // Vanilla subtracts 3 naturally (safe fall height), this adds on top of that
        event.setDistance(event.getDistance() * (float)Config.getFallDamageReduction());
    }
    
    @SubscribeEvent
    public void onEntitySpawn(EntityJoinLevelEvent event){
        if(!Config.ENDER_PEARL_THROW_SHOOT.get()) return;
        if(!(event.getEntity() instanceof ThrownEnderpearl pearl)) return;
            
        Entity Owner = pearl.getOwner();
        
        if(!(Owner instanceof Player player)) return;
        if (!player.getMainHandItem().is(SHOTGUN_ITEM.get())) return;

        double knockbackStrenght = Config.ENDER_PEARL_BOOST_AMOUNT.get();
        Vec3 look = player.getLookAngle();
        pearl.push(
                look.x * (knockbackStrenght),
                look.y * (knockbackStrenght),
                look.z * (knockbackStrenght)
        );
    }
    
}
