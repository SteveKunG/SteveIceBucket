package stevekung.mods.steveicebucket.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.SteveIceBucketMod;
import stevekung.mods.steveicebucket.blocks.BlockIcyTNT;
import stevekung.mods.steveicebucket.client.renderer.entity.RenderIcyCreeper;
import stevekung.mods.steveicebucket.client.renderer.entity.RenderIcyTNT;
import stevekung.mods.steveicebucket.entity.EntityIcyCreeper;
import stevekung.mods.steveicebucket.entity.EntityIcyTNT;
import stevekung.mods.stevekunglib.utils.client.ClientRegistryUtils;

public class ClientProxy extends ServerProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ClientRegistryUtils.registerEntityRendering(EntityIcyCreeper.class, (manager) -> new RenderIcyCreeper(manager));
        ClientRegistryUtils.registerEntityRendering(EntityIcyTNT.class, (manager) -> new RenderIcyTNT(manager));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(SteveIceBucketMod.ICY_TNT), 0, new ModelResourceLocation(SteveIceBucketMod.MOD_ID + ":icy_tnt", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(SteveIceBucketMod.ICE_BUCKET, 0, new ModelResourceLocation(SteveIceBucketMod.MOD_ID + ":ice_bucket", "inventory"));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(SteveIceBucketMod.ICE_BUCKET_TNT, 0, new ModelResourceLocation(SteveIceBucketMod.MOD_ID + ":ice_bucket_tnt", "inventory"));
    }

    @Override
    public void spawnParticle(EnumParticleTypes type, World world, double x, double y, double z, double motionX, double motionY, double motionZ)
    {
        Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(type.getParticleID(), x, y, z, motionX, motionY, motionZ);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        ClientRegistryUtils.registerStateMapper(SteveIceBucketMod.ICY_FLUID_BLOCK, BlockFluidBase.LEVEL);
        ClientRegistryUtils.registerStateMapper(SteveIceBucketMod.ICY_TNT, BlockIcyTNT.EXPLODE);
    }
}