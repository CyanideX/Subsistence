package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.tile.machine.TileBarrel;
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
        if (tile.getBlockMetadata() == 0)
            Texture.BARREL_WOOD.bindTexture();
        else
            Texture.BARREL_STONE.bindTexture();

        if (tile.lidOff()) {
            if (tile.getBlockMetadata() == 0)
                Model.BARREL_WOOD.renderAllExcept("lid", "lidHandle");
            else
                Model.BARREL_STONE.renderAllExcept("lid", "lidHandle");

            if (tile.getFluid() != null) {
                this.renderLiquid(tile);
            }
        } else {
            if (tile.getBlockMetadata() == 0)
                Model.BARREL_WOOD.renderAll();
            else
                Model.BARREL_STONE.renderAll();
        }

        GL11.glPopMatrix();
    }

    private void renderLiquid(TileBarrel tile) {
        GL11.glPushMatrix();
        IIcon icon = FluidRegistry.getFluid(tile.getFluid().fluidID).getIcon();

        float s = 1.0F / 256.0F * 14.0F;
        float level = (float) tile.getFluidAmount() / (float) tile.getCapacity();

        GL11.glTranslatef(0.0F, 0.9F, 0.0F);
        GL11.glTranslatef(-0.43F, -1.0F - (0.43F - level), -0.43F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(s / 1.0F, s / 1.0F, s / 1.0F);

        this.bindTexture(TextureMap.locationBlocksTexture);
        this.renderIcon(0, 0, icon, 15, 15, 240);

        GL11.glPopMatrix();
    }

    private void renderIcon(int x, int y, IIcon icon, int width, int height, int brightness) {
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setBrightness(brightness);
        tess.addVertexWithUV(x, y + width, 0, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(x + width, y + height, 0, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(x + width, y, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(x, y, 0, icon.getMinU(), icon.getMinV());
        tess.draw();
    }
}