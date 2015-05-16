package subsistence.client.render.tile;

import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.misc.TileSpawnMarker;
import subsistence.common.util.RenderHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderTileSpawnMarker extends SubsistenceTileRenderer<TileSpawnMarker> {

    public void renderTileAt(TileSpawnMarker tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        GL11.glRotated(RenderHelper.getRotationAngle(ForgeDirection.getOrientation(tile.getBlockMetadata())), 0, 1, 0);

        Texture.SPAWN_MARKER.bindTexture();
        Model.SPAWN_MARKER.renderAll();

        GL11.glPopMatrix();
    }
}
