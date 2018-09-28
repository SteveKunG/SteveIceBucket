package stevekung.mods.steveicebucket.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.SteveIceBucketMod;
import stevekung.mods.steveicebucket.entity.EntityIcyTNT;

public class BlockIcyTNT extends BlockBreakable
{
    public static final PropertyBool EXPLODE = PropertyBool.create("explode");

    public BlockIcyTNT(String name)
    {
        super(Material.TNT, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, false));
        this.setSoundType(SoundType.GLASS);
        this.setDefaultSlipperiness(0.98F);
        this.setUnlocalizedName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return SteveIceBucketMod.ICE_BUCKET_TAB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        if (world.isBlockPowered(pos))
        {
            this.onBlockDestroyedByPlayer(world, pos, state.withProperty(EXPLODE, true));
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        if (world.isBlockPowered(pos))
        {
            this.onBlockDestroyedByPlayer(world, pos, state.withProperty(EXPLODE, true));
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion)
    {
        if (!world.isRemote)
        {
            EntityIcyTNT entitytntprimed = new EntityIcyTNT(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, explosion.getExplosivePlacedBy());
            entitytntprimed.setFuse((short)(world.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
            world.spawnEntity(entitytntprimed);
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
        this.explode(world, pos, state, null);
    }

    public void explode(World world, BlockPos pos, IBlockState state, EntityLivingBase igniter)
    {
        if (!world.isRemote)
        {
            if (state.getValue(EXPLODE).booleanValue())
            {
                EntityIcyTNT entitytntprimed = new EntityIcyTNT(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, igniter);
                world.spawnEntity(entitytntprimed);
                world.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!itemStack.isEmpty() && (itemStack.getItem() == Items.FLINT_AND_STEEL || itemStack.getItem() == Items.FIRE_CHARGE))
        {
            this.explode(world, pos, state.withProperty(EXPLODE, true), player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

            if (itemStack.getItem() == Items.FLINT_AND_STEEL)
            {
                itemStack.damageItem(1, player);
            }
            else if (!player.capabilities.isCreativeMode)
            {
                itemStack.shrink(1);
            }
            return true;
        }
        else
        {
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (!world.isRemote && entity instanceof EntityArrow)
        {
            EntityArrow arrow = (EntityArrow)entity;

            if (arrow.isBurning())
            {
                this.explode(world, pos, world.getBlockState(pos).withProperty(EXPLODE, true), arrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)arrow.shootingEntity : null);
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion)
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(EXPLODE, (meta & 1) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(EXPLODE).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, EXPLODE);
    }
}