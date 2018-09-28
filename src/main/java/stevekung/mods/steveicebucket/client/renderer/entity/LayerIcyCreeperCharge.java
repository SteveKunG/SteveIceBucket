package stevekung.mods.steveicebucket.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.steveicebucket.client.model.ModelIcyCreeper;
import stevekung.mods.steveicebucket.entity.EntityIcyCreeper;

@SideOnly(Side.CLIENT)
public class LayerIcyCreeperCharge implements LayerRenderer<EntityIcyCreeper>
{
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderIcyCreeper creeperRenderer;
    private final ModelIcyCreeper creeperModel = new ModelIcyCreeper(2.0F);

    public LayerIcyCreeperCharge(RenderIcyCreeper render)
    {
        this.creeperRenderer = render;
    }

    @Override
    public void doRenderLayer(EntityIcyCreeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entity.getPowered())
        {
            boolean flag = entity.isInvisible();
            GlStateManager.depthMask(!flag);
            this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = entity.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.creeperModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}