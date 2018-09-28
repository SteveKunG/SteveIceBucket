package stevekung.mods.steveicebucket.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import stevekung.mods.steveicebucket.IcyExplosion;

public class EntityIcyTNT extends Entity
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityIcyTNT.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;

    public EntityIcyTNT(World world)
    {
        super(world);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityIcyTNT(World world, double x, double y, double z, EntityLivingBase igniter)
    {
        this(world);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = -((float)Math.sin(f)) * 0.02F;
        this.motionY = 0.20000000298023224D;
        this.motionZ = -((float)Math.cos(f)) * 0.02F;
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(FUSE, 80);
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;

        if (this.fuse <= 0)
        {
            this.setDead();

            if (!this.world.isRemote)
            {
                this.explode();
            }
        }
        else
        {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Nullable
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }

    @Override
    public float getEyeHeight()
    {
        return 0.0F;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
    }

    public void setFuse(int fuse)
    {
        this.dataManager.set(FUSE, Integer.valueOf(fuse));
        this.fuse = fuse;
    }

    public int getFuseDataManager()
    {
        return this.dataManager.get(FUSE).intValue();
    }

    public int getFuse()
    {
        return this.fuse;
    }

    private void explode()
    {
        IcyExplosion explosion = new IcyExplosion(this.world, this, this.posX, this.posY + this.height / 16.0F, this.posZ, 4.0F, true);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
    }
}