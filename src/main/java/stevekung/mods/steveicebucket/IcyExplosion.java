package stevekung.mods.steveicebucket;

import java.util.*;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.entity.EntityIcyTNT;

public class IcyExplosion extends Explosion
{
    private boolean damagesTerrain;
    private World world;
    private double x;
    private double y;
    private double z;
    private Entity exploder;
    private float size;
    private List<BlockPos> affectedBlockPositions;
    private Map<EntityPlayer, Vec3d> playerKnockbackMap;
    private Vec3d position;

    @SideOnly(Side.CLIENT)
    public IcyExplosion(World world, Entity exploder, double x, double y, double z, float size, List<BlockPos> affectedPositions)
    {
        this(world, exploder, x, y, z, size, true, affectedPositions);
    }

    @SideOnly(Side.CLIENT)
    public IcyExplosion(World world, Entity exploder, double x, double y, double z, float size, boolean damagesTerrain, List<BlockPos> affectedPositions)
    {
        this(world, exploder, x, y, z, size, damagesTerrain);
        this.affectedBlockPositions.addAll(affectedPositions);
    }

    public IcyExplosion(World world, Entity exploder, double x, double y, double z, float size, boolean damagesTerrain)
    {
        super(world, exploder, x, y, z, size, false, damagesTerrain);
        this.affectedBlockPositions = new ArrayList<>();
        this.playerKnockbackMap = new HashMap<>();
        this.world = world;
        this.exploder = exploder;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.damagesTerrain = damagesTerrain;
        this.position = new Vec3d(this.x, this.y, this.z);
    }

    @Override
    public void doExplosionA()
    {
        Set<BlockPos> set = new HashSet<>();

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                for (int l = 0; l < 16; ++l)
                {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
                    {
                        double d0 = j / 15.0F * 2.0F - 1.0F;
                        double d1 = k / 15.0F * 2.0F - 1.0F;
                        double d2 = l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (; f > 0.0F; f -= 0.22500001F)
                        {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = this.world.getBlockState(blockpos);

                            if (iblockstate.getMaterial() != Material.AIR)
                            {
                                float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(this.world, blockpos, (Entity)null, this);
                                f -= (f2 + 0.3F) * 0.3F;
                            }
                            if (f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, f)))
                            {
                                set.add(blockpos);
                            }
                            d4 += d0 * 0.30000001192092896D;
                            d6 += d1 * 0.30000001192092896D;
                            d8 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
        float f3 = this.size * 2.0F;
        int k1 = MathHelper.floor(this.x - f3 - 1.0D);
        int l1 = MathHelper.floor(this.x + f3 + 1.0D);
        int i2 = MathHelper.floor(this.y - f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + f3 + 1.0D);
        int j2 = MathHelper.floor(this.z - f3 - 1.0D);
        int j1 = MathHelper.floor(this.z + f3 + 1.0D);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
        ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions())
            {
                double d12 = entity.getDistance(this.x, this.y, this.z) / f3;

                if (d12 <= 1.0D)
                {
                    double d5 = entity.posX - this.x;
                    double d7 = entity.posY + entity.getEyeHeight() - this.y;
                    double d9 = entity.posZ - this.z;
                    double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D)
                    {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (int)((d10 * d10 + d10) / 2.0D * 7.0D * f3 + 1.0D));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase)
                        {
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer)
                        {
                            EntityPlayer entityplayer = (EntityPlayer)entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying))
                            {
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doExplosionB(boolean spawnParticles)
    {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.size >= 2.0F && this.damagesTerrain)
        {
            SteveIceBucketMod.PROXY.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.world, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }
        else
        {
            SteveIceBucketMod.PROXY.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.world, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }

        if (this.damagesTerrain)
        {
            for (BlockPos blockpos : this.affectedBlockPositions)
            {
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (spawnParticles)
                {
                    double d0 = blockpos.getX() + this.world.rand.nextFloat();
                    double d1 = blockpos.getY() + this.world.rand.nextFloat();
                    double d2 = blockpos.getZ() + this.world.rand.nextFloat();
                    double d3 = d0 - this.x;
                    double d4 = d1 - this.y;
                    double d5 = d2 - this.z;
                    double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / this.size + 0.1D);
                    d7 = d7 * (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    SteveIceBucketMod.PROXY.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.world, (d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5);
                    SteveIceBucketMod.PROXY.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.world, d0, d1, d2, d3, d4, d5);
                }

                if (iblockstate.getMaterial() != Material.AIR)
                {
                    this.world.setBlockState(blockpos, Blocks.ICE.getDefaultState());
                    block.onBlockDestroyedByExplosion(this.world, blockpos, this);
                }
                if (iblockstate.getMaterial() == Material.LAVA)
                {
                    this.world.setBlockState(blockpos, Blocks.OBSIDIAN.getDefaultState());
                }
            }
        }
    }

    @Override
    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap()
    {
        return super.getPlayerKnockbackMap();
    }

    @Override
    @Nullable
    public EntityLivingBase getExplosivePlacedBy()
    {
        if (this.exploder == null)
        {
            return null;
        }
        else if (this.exploder instanceof EntityIcyTNT)
        {
            return ((EntityIcyTNT)this.exploder).getTntPlacedBy();
        }
        else
        {
            return this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null;
        }
    }

    @Override
    public void clearAffectedBlockPositions()
    {
        this.affectedBlockPositions.clear();
    }

    @Override
    public List<BlockPos> getAffectedBlockPositions()
    {
        return this.affectedBlockPositions;
    }

    @Override
    public Vec3d getPosition()
    {
        return this.position;
    }
}