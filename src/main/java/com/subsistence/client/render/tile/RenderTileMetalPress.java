package com.subsistence.client.render.tile;

import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.common.tile.machine.TileMetalPress;
import com.subsistence.common.util.RenderHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderTileMetalPress extends SubsistenceTileRenderer<TileMetalPress> {

    public void renderTileAt(TileMetalPress tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.METAL_PRESS.bindTexture();
        Model.METAL_PRESS.renderAll();

        GL11.glPopMatrix();
    }
}
