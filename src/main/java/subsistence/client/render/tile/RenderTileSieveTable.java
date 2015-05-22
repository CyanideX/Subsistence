package subsistence.client.render.tile;

import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileSieveTable;

public class RenderTileSieveTable extends SubsistenceTileRenderer<TileSieveTable> {

    public void renderTileAt(TileSieveTable tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        Texture.TABLE_SIEVE.bindTexture();
        Model.TABLE_SIEVE.renderAll();

        GL11.glPopMatrix();
    }
}
