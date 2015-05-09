package com.subsistence.common.block.prefab;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Thlayli
 */
public abstract class SubsistenceBasicFluid extends BlockFluidClassic {

    protected IIcon stillIcon, flowingIcon;
    protected boolean canDisplace = false;

    public SubsistenceBasicFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    public void setDisplace(boolean canDisplace) {
        this.canDisplace = canDisplace;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? stillIcon : flowingIcon;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return !(!canDisplace && world.getBlock(x, y, z).getMaterial().isLiquid()) && super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !(!canDisplace && world.getBlock(x, y, z).getMaterial().isLiquid()) && super.displaceIfPossible(world, x, y, z);
    }

    public abstract void registerBlockIcons(IIconRegister register);

    protected boolean checkSurroundingBlocksForSource(World world, int x, int y, int z, int range) {
        for (int x1 = -range + 1; x1 < range; x1++)
            for (int y1 = -range + 1; y1 < range; y1++)
                for (int z1 = -range + 1; z1 < range; z1++)
                    if (this.isSourceBlock(world, x + x1, y + y1, z + z1))
                        return true;

        return false;
    }

    public Vec3 getSourceBlock(World world, int x, int y, int z) {
        for (int x1 = -8; x1 < 8; x1++) {
            for (int y1 = 0; y1 < 8; y1++) {
                for (int z1 = -8; z1 < 8; z1++) {
                    Block b = world.getBlock(x, y, z);
                    if (b instanceof BlockFluidClassic) {
                        if (((BlockFluidClassic) b).isSourceBlock(world, x + x1, y + y1, z + z1)) {
                            return Vec3.createVectorHelper(x + x1, y + y1, z + z1);
                        }
                    }
                }
            }
        }
        return null;
    }
}
