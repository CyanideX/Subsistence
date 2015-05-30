package subsistence.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import subsistence.client.lib.Model;
import subsistence.client.lib.Texture;
import subsistence.client.render.SubsistenceTileRenderer;
import subsistence.common.tile.machine.TileBarrel;
import subsistence.common.util.RenderHelper;

import java.util.ArrayList;
import java.util.List;

public class RenderTileBarrel extends SubsistenceTileRenderer<TileBarrel> {

    private List<ItemStack> foliage = new ArrayList<ItemStack>();

    public RenderTileBarrel(){
        foliage.add(new ItemStack(Blocks.leaves, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.sapling, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.vine, 1));
        foliage.add(new ItemStack(Blocks.waterlily, 1));
    }

    @Override
    public void renderTileAt(TileBarrel tile, double x, double y, double z, float delta) {
        if (tile == null)
            return;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotated(RenderHelper.getRotationAngle(tile.orientation.getOpposite()), 0, 1, 0);

        switch (tile.getBlockMetadata()) {
            case 2:
                Texture.BARREL_NETHER.bindTexture();
                break;

            case 1:
                Texture.BARREL_STONE.bindTexture();
                break;

            case 0:
            default:
                Texture.BARREL_WOOD.bindTexture();
                break;
        }

        if (tile.getBlockMetadata() == 0)
            Model.BARREL_WOOD.renderAllExcept("lid", "lidHandle");
        else
            Model.BARREL_STONE.renderAllExcept("lid", "lidHandle");

        GL11.glPushMatrix();
        renderLid(tile);
        GL11.glPopMatrix();
        boolean hasRendered = false;

        if (tile.itemContents != null) {
            for (int i = 0; i < tile.itemContents.length; i++) {
                if (tile.itemContents[i] != null) {
                    final ItemStack itemStack = tile.itemContents[i];
                    for (int c = 0; c < foliage.size(); c++) {
                        if (itemStack.getItem() == foliage.get(c).getItem()) {
                            RenderHelper.renderColoredIcon(Blocks.dirt.getIcon(1, 0), TextureMap.locationBlocksTexture, Blocks.leaves.getBlockColor(), 0.35F + ((float) i * 0.35F));
                            hasRendered = true;
                        }
                    }
                    if (!hasRendered && itemStack.getItem() instanceof ItemBlock) {
                        Block block = Block.getBlockFromItem(tile.itemContents[i].getItem());
                        RenderHelper.renderColoredIcon(block.getIcon(1, 0), TextureMap.locationBlocksTexture, block.getBlockColor(), 0.35F + ((float) i * 0.35F));
                        hasRendered = true;
                    }
                    //If we still havent rendered anything it must be an item
                    if(!hasRendered){
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

        final float volume = tile.blockMetadata == 1 ? TileBarrel.VOLUME_FLUID_STONE : TileBarrel.VOLUME_FLUID_WOOD;
        float s = 1.0F / 256.0F * 14.0F;
        float level = (float) tile.fluidContents.amount / (float) volume;

        GL11.glTranslatef(-0.40F, (TileBarrel.DIMENSION_FILL * level) - TileBarrel.DIMENSION_FILL / 2, -0.40F);
        GL11.glScalef(s / 1.0F, s / 1.0F, s / 1.0F);

        RenderHelper.renderLiquid(tile.fluidContents);

        GL11.glPopMatrix();
    }

    private void renderLid(TileBarrel tile) {
        if (tile.hasLid) {
            if (tile.getBlockMetadata() == 0)
                Model.BARREL_WOOD.renderOnly("lid", "lidHandle");
            else
                Model.BARREL_STONE.renderOnly("lid", "lidHandle");
        }
    }
}