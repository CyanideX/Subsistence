package subsistence.common.block.prefab;

import subsistence.common.core.SubsistenceCreativeTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

/**
 * @author Royalixor
 */
public abstract class SubsistenceBasicBlock extends Block {

    public SubsistenceBasicBlock(Material material, float hardness, float resistance) {
        super(material);
        this.setCreativeTab(SubsistenceCreativeTab.BLOCKS.get());
        this.setHardness(hardness);
        this.setResistance(resistance);
    }

    public SubsistenceBasicBlock(Material material) {
        this(material, 2F, 2F);
    }

    @SideOnly(Side.CLIENT)
    public abstract boolean useCustomRender();

    @SideOnly(Side.CLIENT)
    public abstract void registerBlockIcons(IIconRegister iconRegister);

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isOpaqueCube() {
        return !this.useCustomRender();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderAsNormalBlock() {
        return !this.useCustomRender();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return this.useCustomRender() ? -1 : 0;
    }
}
