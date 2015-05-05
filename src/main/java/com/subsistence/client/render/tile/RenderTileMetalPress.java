package com.subsistence.client.render.tile;

import com.subsistence.client.render.SubsistenceTileRenderer;
import com.subsistence.client.lib.Model;
import com.subsistence.client.lib.Texture;
import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.block.prefab.SubsistenceTileMultiBlock;
import com.subsistence.common.lib.MathFX;
import com.subsistence.common.tile.machine.TileMetalPress;
import com.subsistence.common.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.item.ItemBlock;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * @author dmillerw
 */
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
        GL11.glPushMatrix();
        GL11.glTranslated(0, (tile.currentAngle) - 1, 0);
        Model.METAL_PRESS.renderOnly("MetalPress___Press_1");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(0, 0.25, 0.25);
        double xz = (-tile.currentAngle / (tile.max - tile.min)) * 100 / (100 / 30) + 83;
        GL11.glRotated(xz, 1, 0, 0);
        GL11.glTranslated(0, -0.25, -0.25);

        Model.METAL_PRESS.renderOnly("MetalPress___Handel_2");
        GL11.glPopMatrix();
    }

    private void renderIngot(TileMetalPress tile) {
        GL11.glPushMatrix();
        if (tile.itemStack != null) {
            GL11.glTranslated(0.5, tile.min - 0.2, 0.3);
            GL11.glRotated(90, 1, 0, 0);

            RenderHelper.renderItemStack(tile.itemStack, true);
        }
        GL11.glPopMatrix();
    }
}
