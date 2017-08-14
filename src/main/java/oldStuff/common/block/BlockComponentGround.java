package oldStuff.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import oldStuff.SubsistenceOld;
import oldStuff.common.block.prefab.SubsistenceMultiBlock;
import oldStuff.common.util.ArrayHelper;

import java.util.List;
import java.util.Random;

//TODO Eventually migrate nether_rind to BlockComponentStone permanently
public class BlockComponentGround extends SubsistenceMultiBlock {

    public static final int FINE_SAND = 0;
    public static final int NETHER_GRIT = 1;
    public static final int NETHER_RIND = 2;
    public static final int SOUL_DUST = 3;

    private static final String[] NAMES = new String[]{"fine_sand", "nether_grit", "nether_rind", "soul_dust"};
    private IIcon[] icons;

    public BlockComponentGround() {
        super(Material.sand, 0.5F, 0F);
        setStepSound(soundTypeGravel);

        setHarvestLevel("shovel", 0, 0); //fine sand
        setHarvestLevel("shovel", 0, 1); //nether grit
        setHarvestLevel("pickaxe", 0, 2); //nether rind
        setHarvestLevel("shovel", 0, 3); //soul dust
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
    public Item getItemDropped(int meta, Random random, int fortune) {
        // Drop new block if its nether rind
        if (meta == NETHER_RIND) {
            return Item.getItemFromBlock(SubsistenceBlocks.componentStone);
        } else {
            return super.getItemDropped(meta, random, fortune);
        }
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
            icons[i] = register.registerIcon(SubsistenceOld.RESOURCE_PREFIX + "component/" + NAMES[i]);
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < NAMES.length; ++meta) {
            // Prevent old block from appearing
            if (meta == NETHER_RIND)
                continue;

            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        //thing is that we have a dirt-like and a stone-like substance in the same id, so a general hardness wont work
        int meta = world.getBlockMetadata(x,y,z);
        switch (meta) {
            case 0: //fine sand
                return 1.0F;
            case 1: //nether grit
                return 1.0F;
            case 2: //nether rind
                return 2.0F;
            case 3: //soul dust
                return 1.0F;
        }
        return 1.0F;
    }
}
