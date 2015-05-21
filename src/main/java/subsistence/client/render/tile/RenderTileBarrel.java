package subsistence.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileBarrel;
import subsistence.common.util.RenderHelper;

public class RenderTileBarrel extends SubsistenceTileRenderer<TileBarrel> {

    @Override
    public void renderTileAt(TileBarrel tile, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation.getOpposite()), 0, 1, 0);

        switch (tile.getBlockMetadata()) {
            case 1:
                Texture.BARREL_STONE.bindTexture();
                break;
            case 0:
            default:
                Texture.BARREL_WOOD.bindTexture();
                break;
        }

        if (tile.getBlockMetadata() == 0)
            Model.BARREL_WOOD.renderAllExcept("lid", "lidHandle");
        else
            Model.BARREL_STONE.renderAllExcept("lid", "lidHandle");

        GL11.glPushMatrix();
        renderLid(tile);
        GL11.glPopMatrix();
        if (tile.fluid != null) {
            renderLiquid(tile);
        } else if (tile.contents != null) {
            for (int i = 0; i < tile.contents.length; i++) {
                if (tile.contents[i] != null) {
                    Block block = Block.getBlockFromItem(tile.contents[i].getItem());
                    RenderHelper.renderColoredIcon(block.getIcon(1, 0), TextureMap.locationBlocksTexture, block.getBlockColor(), 0.35F + ((float) i * 0.35F));
                }
            }
        }

        GL11.glPopMatrix();
    }

    private void renderLiquid(TileBarrel tile) {
        GL11.glPushMatrix();

        float s = 1.0F / 256.0F * 14.0F;
        float level = (float) tile.fluid.amount / (float) tile.getCapacity();

        GL11.glTranslatef(-0.40F, (TileBarrel.DIMENSION_FILL * level) - TileBarrel.DIMENSION_FILL / 2, -0.40F);
        GL11.glScalef(s / 1.0F, s / 1.0F, s / 1.0F);

        RenderHelper.renderLiquid(tile.fluid);

        GL11.glPopMatrix();
    }

    private void renderLid(TileBarrel tile) {
        if (tile.hasLid()) {
            if (tile.getBlockMetadata() == 0)
                Model.BARREL_WOOD.renderOnly("lid", "lidHandle");
            else
                Model.BARREL_STONE.renderOnly("lid", "lidHandle");
        }
    }
}