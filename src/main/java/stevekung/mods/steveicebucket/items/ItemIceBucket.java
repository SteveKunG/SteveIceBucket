package stevekung.mods.steveicebucket.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.SteveIceBucketMod;

public class ItemIceBucket extends Item
{
    public ItemIceBucket(String name)
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(1000);
        this.setUnlocalizedName(name);
    }

    @Override
    public void onUsingTick(ItemStack itemStack, EntityLivingBase player, int count)
    {
        World world = player.world;

        for (int i = 0; i < 10; i++)
        {
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, player.posX, player.posY + 2.0D, player.posZ, 0.0D, 0.0D, 0.0D, new int[] { Block.getIdFromBlock(Blocks.ICE) });
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, player.posX, player.posY + 2.0D, player.posZ, 0.0D, 0.0D, 0.0D, new int[] { Block.getIdFromBlock(Blocks.WATER) });
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, player.posX, player.posY + 2.0D, player.posZ, 0.0D, 0.0D, 0.0D, new int[] { Block.getIdFromBlock(Blocks.PACKED_ICE) });
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        itemStack.damageItem(1, player);
        return new ActionResult(EnumActionResult.PASS, itemStack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack)
    {
        return 20;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab()
    {
        return SteveIceBucketMod.ICE_BUCKET_TAB;
    }
}