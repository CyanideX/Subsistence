package subsistence.client.render.tile;

import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileKiln;
import subsistence.common.util.RenderHelper;

public class RenderTileKiln extends SubsistenceTileRenderer<TileKiln> {

    public void renderTileAt(TileKiln tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.KILN.bindTexture();
        Model.KILN.renderAll();

        GL11.glPopMatrix();
    }
}
