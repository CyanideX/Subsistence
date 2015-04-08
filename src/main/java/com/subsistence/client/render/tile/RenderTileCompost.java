package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.lib.MathFX;
import com.subsistence.common.tile.machine.TileCompost;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import com.subsistence.common.util.RenderHelper;
import org.lwjgl.opengl.GL11;

/**
 * Created by Thlayli
 */
public class RenderTileCompost extends SubsistenceTileRenderer<TileCompost> {

    public final String lid = "lid";

    @Override
    public void renderTileAt(TileCompost tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation.getOpposite()), 0, 1, 0);

        switch (tile.getBlockMetadata()) {
            case 1:
                Texture.COMPOST_STONE.bindTexture();
                break;
            case 0:
            default:
                Texture.COMPOST_WOOD.bindTexture();
                break;
        }

        Model.COMPOST.renderAllExcept(lid);
        GL11.glPushMatrix();
        swingLid(tile);
        Model.COMPOST.renderOnly(lid);
        GL11.glPopMatrix();
        if(tile.fluid != null){
            Fluid fluid = tile.fluid.getFluid();
            renderContents(fluid.getIcon(),fluid.getColor(),0.8F);
        }
        else if(tile.binContents != null) {
            for(int i = 0; i < tile.binContents.length;i++){
                Block block = Block.getBlockFromItem(tile.binContents[i].getItem());
                renderContents(block.getIcon(1,0),block.getBlockColor(),0.35F +((float)i * 0.35F));
            }
        }

        GL11.glPopMatrix();
    }

    private void swingLid(TileCompost tile) {
        float percentage;
        float sinerp;

        GL11.glTranslated(0, 0.37, 0.29);
        percentage = tile.currentAngle / tile.maxAngle;
        sinerp = MathFX.sinerp(0, 1, percentage);
        GL11.glRotated((-sinerp) * tile.maxAngle, 1, 0, 0);
        GL11.glTranslated(0, -0.37, -0.29);
    }

    private void renderContents(IIcon icon,  int color, float level){
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
