package stevekung.mods.steveicebucket.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.client.model.ModelIcyCreeper;
import stevekung.mods.steveicebucket.entity.EntityIcyCreeper;

@SideOnly(Side.CLIENT)
public class RenderIcyCreeper extends RenderLiving<EntityIcyCreeper>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("steve's_ice_bucket:textures/entity/icy_creeper.png");

    public RenderIcyCreeper(RenderManager manager)
    {
        super(manager, new ModelIcyCreeper(), 0.5F);
        this.addLayer(new LayerIcyCreeperCharge(this));
    }

    @Override
    protected void preRenderCallback(EntityIcyCreeper entity, float partialTickTime)
    {
        float f = entity.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    @Override
    protected int getColorMultiplier(EntityIcyCreeper entity, float lightBrightness, float partialTickTime)
    {
        float f = entity.getCreeperFlashIntensity(partialTickTime);

        if ((int)(f * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int i = (int)(f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityIcyCreeper entity)
    {
        return RenderIcyCreeper.TEXTURE;
    }
}