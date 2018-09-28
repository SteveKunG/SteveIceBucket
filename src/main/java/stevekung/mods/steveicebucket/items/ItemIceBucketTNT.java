package stevekung.mods.steveicebucket.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.SteveIceBucketMod;
import stevekung.mods.steveicebucket.entity.EntityIcyTNT;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class ItemIceBucketTNT extends Item
{
    public ItemIceBucketTNT(String name)
    {
        super();
        this.setUnlocalizedName(name);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "item.ice_bucket";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(TextFormatting.AQUA + LangUtils.translate("desc.icy_tnt.name"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (world.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!player.canPlayerEdit(pos.offset(facing), facing, itemStack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(world, blockpos);
            Entity entity = this.spawn(world, blockpos.getX() + 0.5D, blockpos.getY() + d0, blockpos.getZ() + 0.5D);

            if (entity != null)
            {
                if (!player.capabilities.isCreativeMode)
                {
                    itemStack.shrink(1);
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
        return SteveIceBucketMod.ICE_BUCKET_TAB;
    }

    private Entity spawn(World world, double x, double y, double z)
    {
        EntityIcyTNT tnt = new EntityIcyTNT(world);

        for (int i = 0; i < 1; ++i)
        {
            tnt.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            world.playSound(null, tnt.posX, tnt.posY, tnt.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.spawnEntity(tnt);
        }
        return tnt;
    }

    private double getYOffset(World world, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = world.getCollisionBoxes(null, axisalignedbb);

        if (list.isEmpty())
        {
            return 0.0D;
        }
        else
        {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }
            return d0 - pos.getY();
        }
    }
}