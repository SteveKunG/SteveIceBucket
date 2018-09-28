package stevekung.mods.steveicebucket.proxy;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy
{
    public void preInit(FMLPreInitializationEvent event) {}
    public void postInit(FMLPostInitializationEvent event) {}
    public void spawnParticle(EnumParticleTypes type, World world, double x, double y, double z, double motionX, double motionY, double motionZ) {}
}