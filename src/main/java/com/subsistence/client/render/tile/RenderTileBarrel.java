package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.tile.machine.TileBarrel;
import com.subsistence.common.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

public class RenderTileBarrel extends SubsistenceTileRenderer<TileBarrel> {

    @Override
    public void renderTileAt(TileBarrel tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation.getOpposite()), 0, 1, 0);

        switch (tile.getBlockMetadata()) {
            case 1:
                Texture.BARREL_STONE.bindTexture();
                break;
            case 0:
            default:
                Texture.BARREL_WOOD.bindTexture();
                break;
        }

        if (tile.getBlockMetadata() == 0)
            Model.BARREL_WOOD.renderAllExcept("lid", "lidHandle");
        else
            Model.BARREL_STONE.renderAllExcept("lid", "lidHandle");

        GL11.glPushMatrix();
        renderLid(tile);
        GL11.glPopMatrix();
        if (tile.fluid != null) {
            renderLiquid(tile);
        } else if (tile.contents != null) {
            for (int i = 0; i < tile.contents.length; i++) {
                if (tile.contents[i] != null) {
                    Block block = Block.getBlockFromItem(tile.contents[i].getItem());
                    renderContents(block.getIcon(1, 0), block.getBlockColor(), 0.35F + ((float) i * 0.35F));
                }
            }
        }

        GL11.glPopMatrix();
    }

    private void renderLiquid(TileBarrel tile) {
        GL11.glPushMatrix();
        IIcon icon = FluidRegistry.getFluid(tile.fluid.fluidID).getIcon();

        float s = 1.0F / 256.0F * 14.0F;
        float level = (float) tile.fluid.amount / (float) tile.getCapacity();

        GL11.glTranslatef(-0.40F, (TileBarrel.DIMENSION_FILL * level) - TileBarrel.DIMENSION_FILL / 2, -0.40F);
        GL11.glScalef(s / 1.0F, s / 1.0F, s / 1.0F);

        if (tile.fluid.getFluid() == FluidRegistry.WATER) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 0.75F);
        }

        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        this.bindTexture(TextureMap.locationBlocksTexture);
        this.renderIcon(0, 0, icon, 15, 15);

        if (tile.fluid.getFluid() == FluidRegistry.WATER) {
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }
    private void renderIcon(int x, int y, IIcon icon, int width, int height) {
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(x, 0, y + width, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(x + width, 0, y + height, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(x + width, 0, y, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(x, 0, y, icon.getMinU(), icon.getMinV());
        tess.draw();
    }
    private void renderLid(TileBarrel tile) {
        if (tile.hasLid()) {
            if (tile.getBlockMetadata() == 0)
                Model.BARREL_WOOD.renderOnly("lid", "lidHandle");
            else
                Model.BARREL_STONE.renderOnly("lid", "lidHandle");
        }
    }

    private void renderContents(IIcon icon, int color, float level) {
        float min = -0.5F + 0.125F;
        float max = 0.5F - 0.125F;

        bindTexture(TextureMap.locationBlocksTexture);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorOpaque_I(color);

        tessellator.setNormal(0, 1, 0);

        tessellator.addVertexWithUV(min, level - 0.5F, min, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(min, level - 0.5F, max, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(max, level - 0.5F, max, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(max, level - 0.5F, min, icon.getMaxU(), icon.getMinV());

        tessellator.draw();
    }
}