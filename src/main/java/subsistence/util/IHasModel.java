package subsistence.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IHasModel {

    @SideOnly(Side.CLIENT)
    default void initModel(ModelRegistryEvent e) {
        if (this instanceof Item)
            ModelLoader.setCustomModelResourceLocation((Item) this, 0,
                    new ModelResourceLocation(((IForgeRegistryEntry<?>) this).getRegistryName(), "inventory"));
        else if (this instanceof Block)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block) this), 0,
                    new ModelResourceLocation(((IForgeRegistryEntry<?>) this).getRegistryName(), "inventory"));
        else
            throw new IllegalArgumentException("Unable to register model");
    }

}
