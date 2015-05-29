package subsistence.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemBlock;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.block.prefab.SubsistenceTileBlock;
import subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import subsistence.common.tile.machine.TileTable;
import subsistence.common.util.RenderHelper;

public class RenderTileTable extends SubsistenceTileRenderer<TileTable> {

    public static final float WOOD_RENDER_MAX = 0.5625F;
    public static final float STONE_RENDER_MAX = 0.875F;

    public void renderTileAt(TileTable tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        switch (tile.getBlockMetadata()) {
            case 2:
                Texture.TABLE_NETHER.bindTexture();
                Model.TABLE_STONE.renderAll();
                break;
            case 1:
                Texture.TABLE_COBBLESTONE.bindTexture();
                Model.TABLE_STONE.renderAll();
                break;
            case 0:
            default:
                Texture.TABLE_WOOD.bindTexture();
                Model.TABLE_WOOD.renderAll();
                break;
        }

        GL11.glTranslated(0.5, 0, 0.5);

        if (tile.stack != null) {
            float renderMax = tile.getBlockMetadata() == 0 ? WOOD_RENDER_MAX : STONE_RENDER_MAX;
            if (tile.stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(tile.stack.getItem()).getRenderType())) {
                Block block = Block.getBlockFromItem(tile.stack.getItem());
                boolean fixOffset = false;

                // EDX specific rendering, since we offset our models
                if (block instanceof SubsistenceTileBlock) {
                    fixOffset = ((SubsistenceTileBlock) block).useCustomRender();
                } else if (block instanceof SubsistenceTileMultiBlock) {
                    fixOffset = ((SubsistenceTileMultiBlock) block).useCustomRender();
                }

                if (fixOffset) {
                    // If it has a model render, move it back down a half, as we offset our entity render by a half
                    GL11.glTranslated(0, -0.25, 0);
                }

                GL11.glTranslated(0, renderMax + 0.0625F, 0);
                GL11.glScaled(2, 2, 2);
            } else {
                GL11.glTranslated(0, renderMax + (0.0625F / 2), -(0.0625F * 3.65));
                GL11.glRotated(90, 1, 0, 0);
            }

            RenderHelper.renderItemStack(tile.stack, true);
        }

        GL11.glPopMatrix();
    }
}
