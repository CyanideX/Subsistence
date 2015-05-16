package subsistence.common.particle.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import subsistence.Subsistence;

/**
 * Created by Thlayli
 */
public abstract class ParticleCore extends EntityFX {

    private static final ResourceLocation texture = new ResourceLocation(Subsistence.RESOURCE_PREFIX + "textures/particles/particles.png");

    protected ParticleCore(World par1World, double par2, double par4, double par6, int indexX, int indexY) {
        super(par1World, par2, par4, par6);
        particleTextureIndexX = indexX;
        particleTextureIndexY = indexY;
    }

    @Override
    public void renderParticle(Tessellator tess, float par2, float par3, float par4, float par5, float par6, float par7) {
        tess.draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        tess.startDrawingQuads();
        tess.setBrightness(200);
        float minU = (float) this.particleTextureIndexX * 0.125f;
        float minV = (float) this.particleTextureIndexY * 0.125f;
        float maxU = minU + 0.125F;
        float maxV = minV + 0.125F;
        float scale = 0.1f * this.particleScale;
        float x = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) par2 - interpPosX);
        float y = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) par2 - interpPosY);
        float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) par2 - interpPosZ);
        tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tess.addVertexWithUV((double) (x - par3 * scale - par6 * scale), (double) (y - par4 * scale), (double) (z - par5 * scale - par7 * scale), (double) maxU, (double) maxV);
        tess.addVertexWithUV((double) (x - par3 * scale + par6 * scale), (double) (y + par4 * scale), (double) (z - par5 * scale + par7 * scale), (double) maxU, (double) minV);
        tess.addVertexWithUV((double) (x + par3 * scale + par6 * scale), (double) (y + par4 * scale), (double) (z + par5 * scale + par7 * scale), (double) minU, (double) minV);
        tess.addVertexWithUV((double) (x + par3 * scale - par6 * scale), (double) (y - par4 * scale), (double) (z + par5 * scale - par7 * scale), (double) minU, (double) maxV);
        tess.draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));
        tess.startDrawingQuads();
    }
}
