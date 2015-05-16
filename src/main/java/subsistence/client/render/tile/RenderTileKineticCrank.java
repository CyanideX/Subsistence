package subsistence.client.render.tile;

import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.common.tile.machine.TileKineticCrank;
import subsistence.common.util.RenderHelper;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderTileKineticCrank extends SubsistenceTileRenderer<TileKineticCrank> {

    public static final String PART_WHEEL = "VIFS002";

    public void renderTileAt(TileKineticCrank tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.KINETIC_CRANK.bindTexture();

        Model.KINETIC_CRANK.renderAllExcept(PART_WHEEL);

        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glRotated(tile.angle, 0, 0, 1);
        GL11.glTranslated(-0.5, -0.5, -0.5);

        Model.KINETIC_CRANK.renderOnly(PART_WHEEL);

        GL11.glPopMatrix();
    }
}
