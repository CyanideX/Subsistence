package subsistence;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import subsistence.items.ItemMaterial;
import subsistence.util.Data;
import subsistence.util.IHasModel;
import subsistence.util.IHasSpecialRegistry;

public class ModItems {

    public static final ItemMaterial itemMaterial;

    static {
        itemMaterial = new ItemMaterial();
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        for (Item item : Data.ITEMS) {
            if (!(item instanceof IHasSpecialRegistry)) {
                registry.register(item);
            }
        }

        for (Block block : Data.BLOCKS)
            if (!(block instanceof IHasSpecialRegistry)) {
                registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }

        // OreDictionary.registerOre("clayPorcelain", ItemResource.getResourceStack(ItemResource.PORCELAIN_CLAY));
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(ModelRegistryEvent e) {
        for (Item item : Data.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).initModel(e);
            }
        }
    }

}
