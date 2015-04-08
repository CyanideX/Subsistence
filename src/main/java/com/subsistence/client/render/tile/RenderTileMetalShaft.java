package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.tile.machine.TileMetalShaft;
import com.subsistence.common.util.RenderHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderTileMetalShaft extends SubsistenceTileRenderer<TileMetalShaft> {

    @Override
    public void renderTileAt(TileMetalShaft tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);
        GL11.glRotated(tile.angle, 1, 0, 0);

        Texture.METAL_SHAFT.bindTexture();
        Model.METAL_SHAFT.renderAll();

        GL11.glPopMatrix();
    }
}
