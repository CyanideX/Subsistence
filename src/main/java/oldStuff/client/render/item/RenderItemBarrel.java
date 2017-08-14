package oldStuff.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import oldStuff.client.lib.Model;
import oldStuff.client.lib.Texture;
import oldStuff.common.item.ItemBarrel;

public class RenderItemBarrel implements IItemRenderer {

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
                Texture.BARREL_NETHER.bindTexture();
                Model.BARREL_STONE.renderAllExcept("lid", "lidHandle");
                if (((ItemBarrel) item.getItem()).hasLid(item))
                    Model.BARREL_STONE.renderOnly("lid", "lidHandle");
                break;

            case 1:
                Texture.BARREL_STONE.bindTexture();
                Model.BARREL_STONE.renderAllExcept("lid", "lidHandle");
                if (((ItemBarrel) item.getItem()).hasLid(item))
                    Model.BARREL_STONE.renderOnly("lid", "lidHandle");
                break;

            case 0:
            default:
                Texture.BARREL_WOOD.bindTexture();
                Model.BARREL_WOOD.renderAllExcept("lid", "lidHandle");
                if (((ItemBarrel) item.getItem()).hasLid(item))
                    Model.BARREL_WOOD.renderOnly("lid", "lidHandle");
                break;
        }

        GL11.glPopMatrix();
    }
}
