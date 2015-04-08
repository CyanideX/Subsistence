package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.tile.machine.TileSieveTable;
import org.lwjgl.opengl.GL11;

/**
 * @author Royalixor
 */
public class RenderTileSieveTable extends SubsistenceTileRenderer<TileSieveTable> {

    public void renderTileAt(TileSieveTable tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        Texture.TABLE_SIEVE.bindTexture();
        Model.TABLE_SIEVE.renderAll();

        GL11.glPopMatrix();
    }
}
