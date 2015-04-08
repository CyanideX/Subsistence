package com.subsistence.common.block;

import com.subsistence.common.block.prefab.SubsistenceMultiBlock;
import com.subsistence.common.lib.SubsistenceProps;
import com.subsistence.common.util.ArrayHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * @author dmillerw
 */
public class BlockComponentGround extends SubsistenceMultiBlock {

    public static final int FINE_SAND = 0;
    public static final int NETHER_GRIT = 1;
    public static final int NETHER_RIND = 2;
    public static final int SOUL_DUST = 3;

    public static final String[] NAMES = new String[]{"fine_sand", "nether_grit", "nether_rind", "soul_dust"};
    public IIcon[] icons;

    public BlockComponentGround() {
        super(Material.sand, 0.5F, 0F);

        setStepSound(soundTypeGravel);
        setHarvestLevel("shovel", 0);
    }

    @Override
    public int[] getSubtypes() {
        return ArrayHelper.getArrayIndexes(NAMES); // Forces all aspects of this block to base themselves off the NAMES array
    }

    @Override
    public String getNameForType(int type) {
        return NAMES[type];
    }

    @Override
    public boolean useCustomRender() {
        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta >= NAMES.length) {
            meta = 0;
        }

        return icons[meta];
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[NAMES.length];
        for (int i = 0; i < NAMES.length; i++) {
            icons[i] = register.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "component/" + NAMES[i]);
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < NAMES.length; ++meta) {
            list.add(new ItemStack(item, 1, meta));
        }
    }
}
