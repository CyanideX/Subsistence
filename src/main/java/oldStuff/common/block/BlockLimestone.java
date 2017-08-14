package oldStuff.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import oldStuff.SubsistenceOld;
import oldStuff.common.block.prefab.SubsistenceMultiBlock;
import oldStuff.common.core.SubsistenceCreativeTab;
import oldStuff.common.util.ArrayHelper;

public class BlockLimestone extends SubsistenceMultiBlock {

    private static final String[] limestoneTypes = new String[]{"limestone", "limestoneCobblestone", "limestoneBricks", "limestoneScorched", "limestoneScorchedBricks", "limestoneTiled"};
    private IIcon[] textures;

    public BlockLimestone() {
        super(Material.rock);
        setStepSound(Block.soundTypeStone);
        setCreativeTab(SubsistenceCreativeTab.BLOCKS.get());
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean useCustomRender() {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        textures = new IIcon[limestoneTypes.length];
        for (int i = 0; i < limestoneTypes.length; i++) {
            textures[i] = iconRegister.registerIcon(SubsistenceOld.RESOURCE_PREFIX + "world/" + limestoneTypes[i]);
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
        return ArrayHelper.getArrayIndexes(limestoneTypes);
    }

    @Override
    public String getNameForType(int type) {
        return limestoneTypes[type];
    }
}
