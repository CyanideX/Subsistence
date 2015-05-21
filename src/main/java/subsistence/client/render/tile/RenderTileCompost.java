package subsistence.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.lib.MathFX;
import subsistence.common.tile.machine.TileCompost;
import subsistence.common.util.RenderHelper;

/**
 * Created by Thlayli
 */
public class RenderTileCompost extends SubsistenceTileRenderer<TileCompost> {

    public static final float RENDER_START = 0.15F;
    public static final float DIMENSION_FILL = 0.575F;

    public final String lid = "lid";

    @Override
    public void renderTileAt(TileCompost tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation.getOpposite()), 0, 1, 0);

        int volume;

        switch (tile.getBlockMetadata()) {
            case 1:
                volume = TileCompost.VOLUME_STONE;
                Texture.COMPOST_STONE.bindTexture();
                break;
            case 0:
            default:
                volume = TileCompost.VOLUME_WOOD;
                Texture.COMPOST_WOOD.bindTexture();
                break;
        }

        final float thickness = DIMENSION_FILL / (float)volume;

        Model.COMPOST.renderAllExcept(lid);

        GL11.glPushMatrix();

        swingLid(tile);

        Model.COMPOST.renderOnly(lid);

        GL11.glPopMatrix();

        if (tile.contents != null) {
            int lastSize = 0;

            if (tile.isOutput) {
                final ItemStack itemStack = tile.contents[0];
                Block block = Block.getBlockFromItem(itemStack.getItem());
                renderContents(block.getIcon(1, 0), block.getBlockColor(), RENDER_START + DIMENSION_FILL);
            } else {
                for (int i = 0; i < tile.contents.length; i++) {
                    if (tile.contents[i] != null) {
                        final ItemStack itemStack = tile.contents[i];
                        Block block = Block.getBlockFromItem(itemStack.getItem());
                        renderContents(block.getIcon(1, 0), block.getBlockColor(), RENDER_START + (thickness * lastSize) + (thickness * itemStack.stackSize));
                        lastSize = itemStack.stackSize;
                    }
                }
            }
        } else if (tile.fluid != null) {

        }

        GL11.glPopMatrix();
    }

    private void swingLid(TileCompost tile) {
        float percentage;
        float sinerp;

        GL11.glTranslated(0, 0.37, 0.29);
        percentage = tile.currentAngle / TileCompost.ANGLE_MAX;
        sinerp = MathFX.sinerp(0, 1, percentage);
        GL11.glRotated((-sinerp) * TileCompost.ANGLE_MAX, 1, 0, 0);
        GL11.glTranslated(0, -0.37, -0.29);
    }

    private void renderContents(IIcon icon, int color, float level) {
        float min = -0.5F + 0.1F;
        float max = 0.5F - 0.1F;

        bindTexture(TextureMap.locationBlocksTexture);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorOpaque_I(color);

        tessellator.setNormal(0, 1, 0);

        tessellator.addVertexWithUV(min, level - 0.5F, min, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(min, level - 0.5F, max, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(max, level - 0.5F, max, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(max, level - 0.5F, min, icon.getMaxU(), icon.getMinV());

        tessellator.draw();
    }
}
