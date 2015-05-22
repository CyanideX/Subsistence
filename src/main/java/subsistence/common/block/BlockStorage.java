package subsistence.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import subsistence.Subsistence;
import subsistence.common.block.prefab.SubsistenceMultiBlock;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.resource.ItemResource;
import subsistence.common.util.ArrayHelper;

public class BlockStorage extends SubsistenceMultiBlock {

    public static ItemStack getStorageFromResource(ItemStack resource) {
        String name = ItemResource.NAMES[resource.getItemDamage()];

        if (name.equalsIgnoreCase("gold")) {
            return new ItemStack(Blocks.gold_block);
        } else if (name.equalsIgnoreCase("iron")) {
            return new ItemStack(Blocks.iron_block);
        }

        for (int i = 0; i < NAMES.length; i++) {
            if (name.equalsIgnoreCase(NAMES[i])) {
                return new ItemStack(SubsistenceBlocks.storage, 1, i);
            }
        }
        return null;
    }

    public static final String[] NAMES = new String[]{"copper", "lead", "nickel", "silver", "steel", "tin"};
    private IIcon[] textures;

    public BlockStorage() {
        super(Material.iron);
        setCreativeTab(SubsistenceCreativeTab.BLOCKS.get());
        setStepSound(Block.soundTypeMetal);
        setHardness(4F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean useCustomRender() {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        textures = new IIcon[NAMES.length];
        for (int i = 0; i < NAMES.length; i++) {
            textures[i] = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "ore/storage/" + NAMES[i]);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta >= textures.length) {
            meta = 0;
        }
        return textures[meta];
    }

    @Override
    public int[] getSubtypes() {
        return ArrayHelper.getArrayIndexes(NAMES);
    }

    @Override
    public String getNameForType(int type) {
        return NAMES[type];
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }
}
