package oldStuff.client.render.tile;

import org.lwjgl.opengl.GL11;
import oldStuff.client.lib.Model;
import oldStuff.client.lib.Texture;
import oldStuff.client.render.SubsistenceTileRenderer;
import oldStuff.common.tile.machine.TileWaterMill;
import oldStuff.common.util.RenderHelper;

public class RenderTileWaterMill extends SubsistenceTileRenderer<TileWaterMill> {

    public void renderTileAt(TileWaterMill tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);

        GL11.glTranslated(0, 0.5, 0);
        GL11.glScaled(0.8, 0.8, 0.8);
        GL11.glRotated(tile.angle + (delta * tile.clientSpeed), 0, 0, 1);
        GL11.glTranslated(0, -0.5, 0);

        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.WATER_MILL.bindTexture();
        Model.WATER_MILL.renderAll();

        GL11.glPopMatrix();
    }
}
