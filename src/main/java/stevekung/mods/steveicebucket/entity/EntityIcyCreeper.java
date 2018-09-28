package stevekung.mods.steveicebucket.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stevekung.mods.steveicebucket.SteveIceBucketMod;

public class EntityIcyCreeper extends EntityCreeper
{
    public EntityIcyCreeper(World world)
    {
        super(world);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return SteveIceBucketMod.ICY_CREEPER;
    }
}