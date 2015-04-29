package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.lib.MathFX;
import com.subsistence.common.tile.machine.TileBarrel;
import com.subsistence.common.tile.machine.TileCompost;
import com.subsistence.common.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
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
            Fluid fluid = tile.fluid.getFluid();
            renderContents(fluid.getIcon(), fluid.getColor(), 0.8F);
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