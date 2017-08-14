package oldStuff.client.render.tile;

import org.lwjgl.opengl.GL11;
import oldStuff.client.lib.Model;
import oldStuff.client.lib.Texture;
import oldStuff.client.render.SubsistenceTileRenderer;
import oldStuff.common.tile.machine.TileMetalShaft;
import oldStuff.common.util.RenderHelper;

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
