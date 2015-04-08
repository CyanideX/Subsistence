package com.subsistence.client.render.tile;

import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.common.tile.machine.TileHandCrank;
import com.subsistence.common.util.RenderHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderTileCrank extends SubsistenceTileRenderer<TileHandCrank> {

    @Override
    public void renderTileAt(TileHandCrank tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);

        ForgeDirection orientation = tile.orientation.getOpposite();

        GL11.glTranslated(x, y, z);

        GL11.glTranslated(0.5 - 0.03125F, 0, 0.5 - 0.03125F);
        GL11.glTranslated(0.03125F, 0, 0.03125F);
        GL11.glRotated(RenderHelper.getRotationAngle(orientation) + 90, 0, 1, 0);
        GL11.glTranslated(-0.03125F, 0, -0.03125F);

        GL11.glRotated(90, 0, 0, 1);
        GL11.glTranslated(0.5 - 0.0625F / 2, -0.5 - 0.03125F, 0);

        GL11.glTranslated(0.03125, 0, 0.03125);
        GL11.glRotated(tile.reverse ? tile.spin : tile.spin, 0, 1, 0);
        GL11.glTranslated(-0.03125, 0, -0.03125F);

        GL11.glPushMatrix();

        Texture.HAND_CRANK.bindTexture();
        Model.HAND_CRANK.renderAll();

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
}
