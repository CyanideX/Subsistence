package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.tile.machine.TileWaterMill;
import com.subsistence.common.util.RenderHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderTileWaterMill extends SubsistenceTileRenderer<TileWaterMill> {

    public void renderTileAt(TileWaterMill tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);

        GL11.glTranslated(0, 0.5, 0);
        GL11.glScaled(0.8, 0.8, 0.8);
        GL11.glRotated(tile.angle, 0, 0, 1);
        GL11.glTranslated(0, -0.5, 0);

        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.WATER_MILL.bindTexture();
        Model.WATER_MILL.renderAll();

        GL11.glPopMatrix();
    }
}
