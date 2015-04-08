package com.subsistence.common.block.prefab;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
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
        if (!canDisplace && world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        if (!canDisplace && world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.displaceIfPossible(world, x, y, z);
    }

    public abstract void registerBlockIcons(IIconRegister register);

    protected boolean checkSurroundingBlocksForSource(World world, int x, int y, int z, int range){
        boolean found = false;
        for(int i = 1; i <= range;i++){
            if(this.isSourceBlock(world,x + i,y,z)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x - i,y,z)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x,y,z + i)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x,y,z - i)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x + i,y,z + i)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x + i,y,z - i)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x - i,y,z - i)){
                found = true;
                break;
            }
            if(this.isSourceBlock(world,x - i,y,z + i)){
                found = true;
                break;
            }
        }
        return found;
    }
}
