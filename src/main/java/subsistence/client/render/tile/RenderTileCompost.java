package subsistence.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
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
    public static final float DIMENSION_FILL = 0.5F;
    public static final float WATER_FILL = 0.075F;

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

        final float thickness = DIMENSION_FILL / (float) volume;

        Model.COMPOST.renderAllExcept(lid);

        GL11.glPushMatrix();

        swingLid(tile);

        Model.COMPOST.renderOnly(lid);

        GL11.glPopMatrix();

        if (tile.contents != null) {
            int lastSize = 0;

            for (int i = 0; i < tile.contents.length; i++) {
                if (tile.contents[i] != null) {
                    final ItemStack itemStack = tile.contents[i];
                    Item item = itemStack.getItem();
                    if (item instanceof ItemBlock) {
                        Block block = Block.getBlockFromItem(itemStack.getItem());
                        RenderHelper.renderColoredIcon(block.getIcon(1, 0), TextureMap.locationBlocksTexture, block.getBlockColor(), RENDER_START + (thickness * lastSize) + (thickness * itemStack.stackSize));
                    } else {
                        RenderHelper.renderColoredIcon(item.getIcon(itemStack, 0), TextureMap.locationBlocksTexture, 0xFFFFFF, RENDER_START + (thickness * lastSize) + (thickness * itemStack.stackSize));
                    }
                    lastSize = itemStack.stackSize;
                }
            }
        }

        if (tile.fluid != null) {
            renderLiquid(tile);
        }

        GL11.glPopMatrix();
    }

    private void renderLiquid(TileCompost tile) {
        GL11.glPushMatrix();

        final float progress = 0.625F * ((float) FluidContainerRegistry.BUCKET_VOLUME / tile.fluid.amount);
        float s = 1.0F / 256.0F * 14.0F;
        GL11.glTranslatef(-0.40F, -0.4F + progress, -0.40F);
        GL11.glScalef(s / 1.0F, s / 1.0F, s / 1.0F);

        RenderHelper.renderLiquid(tile.fluid);

        GL11.glPopMatrix();
    }

    private void swingLid(TileCompost tile) {
        GL11.glTranslated(0, 0.4, 0.35);

        final float progress = MathFX.sinerp(0F, 1F, (float)tile.animationTicks / TileCompost.ANIMATE_TICK_MAX);
        final float midAngle = TileCompost.ANGLE_MAX - TileCompost.ANGLE_MIN;

        GL11.glRotated(TileCompost.ANGLE_MIN + (midAngle * progress) , 1, 0, 0);
        GL11.glTranslated(0, -0.4, -0.35);
    }
}
