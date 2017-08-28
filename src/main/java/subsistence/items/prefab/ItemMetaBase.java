package subsistence.items.prefab;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemMetaBase extends ItemBase {
    public static final String INVALID_META = "invalid_meta";

    protected List<String> subItemNames = new ArrayList<>();

    private Object2IntOpenHashMap<String> itemNameMap = new Object2IntOpenHashMap<>();

    public ItemMetaBase(String registryName, CreativeTabs creativeTab) {
        super(registryName, creativeTab);

        // for optimized meta getting
        initItemNames(subItemNames);
        for (int i = 0; i < subItemNames.size(); i++) {
            itemNameMap.put(subItemNames.get(i), i);
        }

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Add all names of the subitems to this list
     */
    protected abstract void initItemNames(List<String> subItemNamesList);

    public ItemStack createItemStack(String name, int count) {
         return new ItemStack(this, count, getMetaFromName(name));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + getNameFromMeta(stack.getMetadata());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < subItemNames.size(); i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent e) {
        for (int i = 0; i < subItemNames.size(); i++) {
            String variant = "type=" + subItemNames.get(i);
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName(), variant));
        }
    }

    public String getNameFromMeta(int meta){
        if (meta >= 0 && meta < subItemNames.size()){
            String name = subItemNames.get(meta);
            if (name != null){
                return name;
            }
        }

        return ItemMetaBase.INVALID_META;
    }

    public int getMetaFromName(String name){
        return itemNameMap.getOrDefault(name, Short.MAX_VALUE - 1);
    }
}
