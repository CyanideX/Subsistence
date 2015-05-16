package subsistence.client.render.tile;

import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.util.RenderHelper;
import subsistence.common.tile.machine.TileKiln;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
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
