package com.subsistence.common.block.machine;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.block.prefab.SubsistenceTileBlock;
import com.subsistence.common.tile.core.TileCoreMachine;
import com.subsistence.common.tile.machine.TileHandCrank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class BlockCrank extends SubsistenceTileBlock {

	public BlockCrank() {
		super(Material.iron);

		setHardness(2F);
		setResistance(2F);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
		if (player.isSneaking()) {
			ItemStack held = player.getHeldItem();

			if (held == null || getBlockFromItem(held.getItem()) == this) {
				if (!world.isRemote) {
					dropBlockAsItem(world, x, y, z, 0, 0);
					world.setBlockToAir(x, y, z);
				}
				return true;
			}
		} else {
			if (!world.isRemote) {
				TileHandCrank tile = (TileHandCrank) world.getTileEntity(x, y, z);
				tile.crank();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			TileHandCrank tile = (TileHandCrank) world.getTileEntity(x, y, z);

			if (world.getBlock(x + tile.orientation.offsetX, y + tile.orientation.offsetY, z + tile.orientation.offsetZ) != SubsistenceBlocks.hammerMill) {
				dropBlockAsItem(world, x, y, z, 0, 0);
				world.setBlockToAir(x, y, z);
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
		TileCoreMachine tile = (TileCoreMachine) world.getTileEntity(x, y, z);

		switch (tile.orientation) {
		case SOUTH:
			setBlockBounds(0.28125F, 0.28125F, 0.625F, 0.71875F, 0.71875F, 1);
			break;
		case NORTH:
			setBlockBounds(0.28125F, 0.28125F, 0, 0.71875F, 0.71875F, 0.375F);
			break;
		case WEST:
			setBlockBounds(0, 0.28125F, 0.28125F, 0.375F, 0.71875F, 0.71875F);
			break;
		case EAST:
			setBlockBounds(0.625F, 0.28125F, 0.28125F, 1, 0.71875F, 0.71875F);
			break;
		default:
			setBlockBounds(0, 0, 0, 1, 1, 1);
			break;
		}

		return super.collisionRayTrace(world, x, y, z, start, end);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileHandCrank();
	}

	@Override
	public boolean useCustomRender() {
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		
	}

	@Override
	public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
		return false;
	}
}
