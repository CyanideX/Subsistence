package com.subsistence.common.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class RenderHelper {

    public static boolean graphicsCache;

    public static int lightingCache;

    public static void forceGraphics(boolean fancy) {
        graphicsCache = Minecraft.getMinecraft().gameSettings.fancyGraphics;
        Minecraft.getMinecraft().gameSettings.fancyGraphics = fancy;
    }

    public static void resetGraphics() {
        Minecraft.getMinecraft().gameSettings.fancyGraphics = graphicsCache;
    }

    public static void forceLighting(int lighting) {
        lightingCache = Minecraft.getMinecraft().gameSettings.ambientOcclusion;
        Minecraft.getMinecraft().gameSettings.ambientOcclusion = lighting;
    }

    public static void resetLighting() {
        Minecraft.getMinecraft().gameSettings.ambientOcclusion = lightingCache;
    }

    public static void renderItemStack(ItemStack stack, boolean force3D) {
        if (force3D) {
            forceGraphics(true);
        }

        EntityItem item = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, stack);
        item.getEntityItem().stackSize = 1;
        item.hoverStart = 0;

        RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0, 0);

        if (force3D) {
            resetGraphics();
        }
    }

    public static void setGLColor(int color) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, 1.0F);
    }

    public static double getRotationAngle(ForgeDirection direction) {
        switch (direction) {
            case NORTH:
                return 180D;
            case SOUTH:
                return 0D;
            case EAST:
                return 90D;
            case WEST:
                return 270D;
        }
        return 0D;
    }

    public static void renderAllSides(RenderBlocks renderer, Block block) {
        renderAllSides(renderer, block, renderer.overrideBlockTexture);
    }

    public static void renderAllSides(RenderBlocks renderblocks, Block block, IIcon icon) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        if (icon != null) {
            renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        if (icon != null) {
            renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        if (icon != null) {
            renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        if (icon != null) {
            renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        if (icon != null) {
            renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        if (icon != null) {
            renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        }
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void renderAllSides(int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, x, y, z, tex);
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, x, y, z, tex);
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, x, y, z, tex);
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, x, y, z, tex);
        tessellator.setNormal(0, -1, 0);
        renderer.renderFaceYNeg(block, x, y, z, tex);
        tessellator.setNormal(0, 1, 0);
        renderer.renderFaceYPos(block, x, y, z, tex);
    }

    public static void renderAllSidesInverted(int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex) {
        renderer.renderFaceXNeg(block, x + 1, y, z, tex);
        renderer.renderFaceXPos(block, x - 1, y, z, tex);
        renderer.renderFaceZNeg(block, x, y, z + 1, tex);
        renderer.renderFaceZPos(block, x, y, z - 1, tex);
        renderer.renderFaceYNeg(block, x, y + 1, z, tex);
        renderer.renderFaceYPos(block, x, y - 1, z, tex);
    }

    public static void setBrightness(IBlockAccess blockAccess, int i, int j, int k, Block block) {
        Tessellator tessellator = Tessellator.instance;
        int mb = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
        tessellator.setBrightness(mb);

        float f = 1.0F;

        int l = block.colorMultiplier(blockAccess, i, j, k);
        float f1 = (l >> 16 & 0xFF) / 255.0F;
        float f2 = (l >> 8 & 0xFF) / 255.0F;
        float f3 = (l & 0xFF) / 255.0F;
        if (EntityRenderer.anaglyphEnable) {
            float f6 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f7 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f6;
            f2 = f4;
            f3 = f7;
        }
        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
    }
}
