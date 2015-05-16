package subsistence.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * @author dmillerw
 */
public abstract class SubsistenceTileRenderer<T extends TileEntity> extends TileEntitySpecialRenderer {

    public abstract void renderTileAt(T tile, double x, double y, double z, float delta);

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float delta) {
        renderTileAt((T) tile, x, y, z, delta);
    }
}
