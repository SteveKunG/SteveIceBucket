package stevekung.mods.steveicebucket;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.blocks.BlockFluidIcy;
import stevekung.mods.steveicebucket.blocks.BlockIcyTNT;
import stevekung.mods.steveicebucket.entity.EntityIcyCreeper;
import stevekung.mods.steveicebucket.entity.EntityIcyTNT;
import stevekung.mods.steveicebucket.items.ItemIceBucket;
import stevekung.mods.steveicebucket.items.ItemIceBucketTNT;
import stevekung.mods.steveicebucket.proxy.ClientProxy;
import stevekung.mods.steveicebucket.proxy.ServerProxy;
import stevekung.mods.stevekunglib.utils.BlockUtils;
import stevekung.mods.stevekunglib.utils.CommonRegistryUtils;
import stevekung.mods.stevekunglib.utils.CommonUtils;

@Mod(name = SteveIceBucketMod.NAME, version = SteveIceBucketMod.VERSION, modid = SteveIceBucketMod.MOD_ID)
public class SteveIceBucketMod
{
    public static final String NAME = "Steve's Ice Bucket";
    public static final String MOD_ID = "steve's_ice_bucket";
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 0;
    private static final int BUILD_VERSION = 0;
    public static final String VERSION = SteveIceBucketMod.MAJOR_VERSION + "." + SteveIceBucketMod.MINOR_VERSION + "." + SteveIceBucketMod.BUILD_VERSION;

    @SidedProxy(clientSide = "stevekung.mods.steveicebucket.proxy.ClientProxy", serverSide = "stevekung.mods.steveicebucket.proxy.ServerProxy")
    public static ServerProxy PROXY;

    public static final CreativeTabs ICE_BUCKET_TAB = new CreativeTabs(CreativeTabs.getNextID(), "steve's_ice_bucket")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return new ItemStack(SteveIceBucketMod.ICE_BUCKET);
        }
    };

    public static final CommonRegistryUtils COMMON_REGISTRY = new CommonRegistryUtils(MOD_ID);
    public static ResourceLocation ICY_CREEPER;

    public static Block ICY_TNT;
    public static Block ICY_FLUID_BLOCK;
    public static Fluid ICY_FLUID;

    public static Item ICE_BUCKET;
    public static Item ICE_BUCKET_TNT;

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CommonUtils.registerEventHandler(ClientProxy.class);
        SteveIceBucketMod.initModInfo(event.getModMetadata());
        SteveIceBucketMod.PROXY.preInit(event);

        SteveIceBucketMod.ICY_TNT = new BlockIcyTNT("icy_tnt");
        SteveIceBucketMod.ICY_FLUID = new Fluid("icy", new ResourceLocation("blocks/ice"), new ResourceLocation(SteveIceBucketMod.MOD_ID + ":blocks/icy")).setBlock(SteveIceBucketMod.ICY_FLUID_BLOCK).setViscosity(2000);
        SteveIceBucketMod.COMMON_REGISTRY.registerFluid(SteveIceBucketMod.ICY_FLUID);
        SteveIceBucketMod.ICY_FLUID_BLOCK = new BlockFluidIcy("icy");

        SteveIceBucketMod.COMMON_REGISTRY.registerBlock(SteveIceBucketMod.ICY_TNT);
        SteveIceBucketMod.COMMON_REGISTRY.registerBlock(SteveIceBucketMod.ICY_FLUID_BLOCK);
        BlockUtils.setFireBurn(SteveIceBucketMod.ICY_TNT, 5, 20);

        SteveIceBucketMod.ICE_BUCKET = new ItemIceBucket("ice_bucket");
        SteveIceBucketMod.ICE_BUCKET_TNT = new ItemIceBucketTNT("ice_bucket_tnt");

        SteveIceBucketMod.COMMON_REGISTRY.registerItem(SteveIceBucketMod.ICE_BUCKET);
        SteveIceBucketMod.COMMON_REGISTRY.registerItem(SteveIceBucketMod.ICE_BUCKET_TNT);

        SteveIceBucketMod.COMMON_REGISTRY.registerEntity(EntityIcyCreeper.class, "icy_creeper", -8355712, -6896388);
        SteveIceBucketMod.COMMON_REGISTRY.registerNonMobEntity(EntityIcyTNT.class, "icy_tnt", 160, 10, true);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        SteveIceBucketMod.ICY_CREEPER = SteveIceBucketMod.COMMON_REGISTRY.registerEntityLoot("icy_creeper");
        SteveIceBucketMod.COMMON_REGISTRY.registerForgeBucket(SteveIceBucketMod.ICY_FLUID);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        SteveIceBucketMod.PROXY.postInit(event);
        GameRegistry.addShapedRecipe(new ResourceLocation(SteveIceBucketMod.MOD_ID, "icy_tnt"), null, new ItemStack(SteveIceBucketMod.ICY_TNT), new Object[] { "GIG", "IGI", "GIG", 'G', Items.GUNPOWDER, 'I', Blocks.ICE });
        GameRegistry.addShapedRecipe(new ResourceLocation(SteveIceBucketMod.MOD_ID, "ice_bucket"), new ResourceLocation(SteveIceBucketMod.MOD_ID, "ice_bucket"), new ItemStack(SteveIceBucketMod.ICE_BUCKET), new Object[] { "P", "I", "W", 'W', Items.WATER_BUCKET, 'I', Blocks.ICE, 'P', Blocks.PACKED_ICE });
        GameRegistry.addShapedRecipe(new ResourceLocation(SteveIceBucketMod.MOD_ID, "ice_bucket_tnt"), new ResourceLocation(SteveIceBucketMod.MOD_ID, "ice_bucket"), new ItemStack(SteveIceBucketMod.ICE_BUCKET_TNT), new Object[] { "I", "B", 'B', SteveIceBucketMod.ICE_BUCKET, 'I', SteveIceBucketMod.ICY_TNT });
    }

    private static void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = SteveIceBucketMod.MOD_ID;
        info.name = SteveIceBucketMod.NAME;
        info.version = SteveIceBucketMod.VERSION;
        info.description = "Ice Bucket Challenge in Minecraft!";
        info.authorList = Arrays.asList("SteveKunG");
    }
}