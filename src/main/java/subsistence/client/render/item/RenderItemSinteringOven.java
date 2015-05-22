package subsistence.client.render.item;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;

public class RenderItemSinteringOven implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        // Required for any models with partially transparent textures
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        if (type == ItemRenderType.ENTITY) {
            GL11.glTranslated(-0.5, 0, -0.5);
        }

        if (type == ItemRenderType.INVENTORY) {
            GL11.glTranslated(0.1, 0, 0.1);
        }

        Texture.SINTERING_OVEN.bindTexture();
        Model.SINTERING_OVEN.renderAll();

        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }
}
