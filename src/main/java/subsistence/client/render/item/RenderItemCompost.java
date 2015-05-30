package subsistence.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;


public class RenderItemCompost implements IItemRenderer {

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
            GL11.glTranslated(0.1, 0.0, 0.1);
            GL11.glRotated(180D, 0, 1, 0);
        }

        switch (item.getItemDamage()) {
            case 2:
                Texture.COMPOST_NETHER.bindTexture();
                break;

            case 1:
                Texture.COMPOST_STONE.bindTexture();
                break;

            case 0:
            default:
                Texture.COMPOST_WOOD.bindTexture();
                break;
        }

        Model.COMPOST.renderAll();

        GL11.glPopMatrix();
    }
}
