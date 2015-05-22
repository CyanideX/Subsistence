package subsistence.client.render.tile;

import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileHammerMill;
import subsistence.common.util.RenderHelper;

public class RenderTileHammerMill extends SubsistenceTileRenderer<TileHammerMill> {

    public static final String PART_GRINDER = "VIFS001";
    public static final String PART_SWITCH = "VIFS003";

    public void renderTileAt(TileHammerMill tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);

        Texture.HAMMER_MILL.bindTexture();
        Model.HAMMER_MILL.renderAllExcept(PART_GRINDER, PART_SWITCH);

        GL11.glPushMatrix();

        // First we move the pivot point
        GL11.glTranslated(0, 0.55, 0.55);
        GL11.glRotated(-tile.angle, 1, 0, 0);
        GL11.glTranslated(0, -0.55, -0.55);

        // Then we move the object in relation to that pivot point
        GL11.glTranslated(0, 0.02, 0);
        Model.HAMMER_MILL.renderOnly(PART_GRINDER);

        GL11.glPopMatrix();

        if (TileHammerMill.MAX_STAGE % 2 == 0) {
            int min = (int) Math.floor(((float) TileHammerMill.MAX_STAGE / (float) 2) - 0.5F);
            int max = (int) Math.ceil(((float) TileHammerMill.MAX_STAGE / (float) 2) - 0.5F);

            if (tile.grindingStage <= min) {
                float offset = -0.0625F;
                offset += -0.125F * (min - tile.grindingStage);
                GL11.glTranslated(offset, 0, 0);
            } else if (tile.grindingStage >= max) {
                float offset = 0.0625F;
                offset += 0.125F * (tile.grindingStage - max);
                GL11.glTranslated(offset, 0, 0);
            }
        } else {
            int middle = TileHammerMill.MAX_STAGE / 2;

            if (tile.grindingStage <= middle) {
                float offset = -0.0625F * (middle - tile.grindingStage);
                GL11.glTranslated(offset, 0, 0);
            } else if (tile.grindingStage >= middle) {
                float offset = 0.0625F * (tile.grindingStage - middle);
                GL11.glTranslated(offset, 0, 0);
            }
        }

        Model.HAMMER_MILL.renderOnly(PART_SWITCH);

        GL11.glPopMatrix();
    }
}
