package oldStuff.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import oldStuff.client.lib.Model;
import oldStuff.client.lib.Texture;

public class RenderItemKiln implements IItemRenderer {

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

        if (type == ItemRenderType.ENTITY) {
            GL11.glTranslated(-0.5, 0, -0.5);
        }

        if (type == ItemRenderType.INVENTORY) {
            GL11.glTranslated(0.1, 0, 0.1);
        }

        Texture.KILN.bindTexture();
        Model.KILN.renderAll();

        GL11.glPopMatrix();
    }
}
