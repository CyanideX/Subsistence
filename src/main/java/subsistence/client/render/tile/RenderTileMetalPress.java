package subsistence.client.render.tile;

import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileMetalPress;
import subsistence.common.util.RenderHelper;

public class RenderTileMetalPress extends SubsistenceTileRenderer<TileMetalPress> {

    public void renderTileAt(TileMetalPress tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.METAL_PRESS.bindTexture();
        Model.METAL_PRESS.renderAllExcept("MetalPress___Press_1", "MetalPress___Handel_2");

        renderPress(tile);
        renderIngot(tile);

        GL11.glPopMatrix();
    }

    private void renderPress(TileMetalPress tile) {
        final float progress = (tile.animationTicker / (float) TileMetalPress.ANIMATE_TICK_MAX);

        GL11.glPushMatrix();
        GL11.glTranslated(0, (-0.375 * progress), 0);
        Model.METAL_PRESS.renderOnly("MetalPress___Press_1");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(0, 0.25, 0.25);
        GL11.glRotated((float) 45 * progress, 1, 0, 0);
        GL11.glTranslated(0, -0.25, -0.25);

        Model.METAL_PRESS.renderOnly("MetalPress___Handel_2");
        GL11.glPopMatrix();
    }

    private void renderIngot(TileMetalPress tile) {
        GL11.glPushMatrix();
        if (tile.itemStack != null) {
            GL11.glTranslated(0.5, TileMetalPress.RENDER_MIN - 0.2, 0.3);
            GL11.glRotated(90, 1, 0, 0);

            RenderHelper.renderItemStack(tile.itemStack, true);
        }
        GL11.glPopMatrix();
    }
}
