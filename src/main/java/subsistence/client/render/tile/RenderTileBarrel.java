package subsistence.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.FoliageHandler;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileBarrel;
import subsistence.common.util.RenderHelper;

public class RenderTileBarrel extends SubsistenceTileRenderer<TileBarrel> {

    @Override
    public void renderTileAt(TileBarrel tile, double x, double y, double z, float delta) {
        if (tile == null)
            return;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation.getOpposite()), 0, 1, 0);

        final Texture texture;
        switch (tile.getBlockMetadata()) {
            case 2:
                texture = Texture.BARREL_NETHER;
                break;
            case 1:
                texture = Texture.BARREL_STONE;
                break;
            case 0:
            default:
                texture = Texture.BARREL_WOOD;
                break;
        }

        final Model model;
        switch (tile.getBlockMetadata()) {
            case 2:
            case 1:
                model = Model.BARREL_STONE;
                break;
            case 0:
            default:
                model = Model.BARREL_WOOD;
                break;
        }

        texture.bindTexture();
        model.renderAllExcept("lid", "lidHandle");

        GL11.glPushMatrix();
        if (tile.hasLid)
            model.renderOnly("lid", "lidHandle");
        GL11.glPopMatrix();

        if (tile.itemContents != null) {
            for (int i = 0; i < tile.itemContents.length; i++) {
                if (tile.itemContents[i] != null) {
                    final ItemStack itemStack = tile.itemContents[i];

                    if (FoliageHandler.shouldRender(itemStack)) {
                        RenderHelper.renderColoredIcon(Blocks.dirt.getIcon(1, 0), TextureMap.locationBlocksTexture, Blocks.leaves.getBlockColor(), 0.35F + ((float) i * 0.35F));
                    } else if (itemStack.getItem() instanceof ItemBlock) {
                        Block block = Block.getBlockFromItem(tile.itemContents[i].getItem());
                        RenderHelper.renderColoredIcon(block.getIcon(1, 0), TextureMap.locationBlocksTexture, block.getBlockColor(), 0.35F + ((float) i * 0.35F));
                    } else {
                        RenderHelper.renderColoredIcon(itemStack.getItem().getIcon(itemStack, 0), TextureMap.locationBlocksTexture, 0xFFFFFF, 0.35F + ((float) i * 0.35F));
                    }
                }
            }
        }

        if (tile.fluidContents != null) {
            renderLiquid(tile);
        }

        GL11.glPopMatrix();
    }

    private void renderLiquid(TileBarrel tile) {
        GL11.glPushMatrix();

        final float volume = tile.getType().fluidCapacity;
        float s = 1.0F / 256.0F * 14.0F;
        float level = (float) tile.fluidContents.amount / (float) volume;

        GL11.glTranslatef(-0.40F, (TileBarrel.DIMENSION_FILL * level) - TileBarrel.DIMENSION_FILL / 2, -0.40F);
        GL11.glScalef(s / 1.0F, s / 1.0F, s / 1.0F);

        RenderHelper.renderLiquid(tile.fluidContents);

        GL11.glPopMatrix();
    }
}