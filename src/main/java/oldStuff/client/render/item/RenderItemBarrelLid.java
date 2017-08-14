package oldStuff.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import oldStuff.client.lib.Model;
import oldStuff.client.lib.Texture;

public class RenderItemBarrelLid implements IItemRenderer {

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

        GL11.glTranslated(0, -0.5, 0);

        if (type == ItemRenderType.ENTITY) {
            GL11.glTranslated(-0.5, 0, -0.5);
        }

        if (type == ItemRenderType.INVENTORY) {
            GL11.glTranslated(0.1, 0.0, 0.1);
            GL11.glRotated(180D, 0, 1, 0);
        }

        final Texture texture;
        switch (item.getItemDamage()) {
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
        switch (item.getItemDamage()) {
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
        model.renderOnly("lid", "lidHandle");

        GL11.glPopMatrix();
    }
}
