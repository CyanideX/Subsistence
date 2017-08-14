package oldStuff.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import oldStuff.client.lib.Model;
import oldStuff.client.lib.Texture;

public class RenderItemTable implements IItemRenderer {

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

        switch (item.getItemDamage()) {
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

        GL11.glPopMatrix();
    }
}
