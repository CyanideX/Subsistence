package oldStuff.common.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import oldStuff.SubsistenceOld;
import oldStuff.common.core.SubsistenceCreativeTab;

import java.util.Random;

public class BlockInfernaLeaves extends BlockLeaves {

    private IIcon[] icons;

    public BlockInfernaLeaves() {
        super();

        setCreativeTab(SubsistenceCreativeTab.BLOCKS.get());
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icons[field_150127_b];
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[2];
        icons[0] = register.registerIcon(SubsistenceOld.RESOURCE_PREFIX + "world/infernalLeaves_fancy");
        icons[1] = register.registerIcon(SubsistenceOld.RESOURCE_PREFIX + "world/infernalLeaves_fast");
    }

    @Override
    public String[] func_150125_e() {
        return new String[]{getUnlocalizedName()};
    }

    @Override
    public Item getItemDropped(int i1, Random random, int i2) {
        return Item.getItemFromBlock(SubsistenceBlocks.infernalSapling);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 0;
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return true;
    }

    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
        return true;
    }
}
