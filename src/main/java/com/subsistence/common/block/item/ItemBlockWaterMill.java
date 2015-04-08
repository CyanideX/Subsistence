package com.subsistence.common.block.item;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.block.prefab.item.SubsistenceItemBlock;
import com.subsistence.common.lib.SubsistenceProps;
import com.subsistence.common.tile.machine.TileKineticCrank;
import com.subsistence.common.tile.machine.TileWaterMill;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author dmillerw
 */
public class ItemBlockWaterMill extends SubsistenceItemBlock {

    private IIcon icon;

    public ItemBlockWaterMill(Block block) {
        super(block);
    }

    @Override
    public int getSpriteNumber() {
        return 1;
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        icon = register.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "tools/waterMill");
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (side == 0 || side == 1) {
            return false;
        }

        ForgeDirection opposite = ForgeDirection.getOrientation(side).getOpposite();

        if (world.getBlock(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ) != SubsistenceBlocks.kineticCrank) {
            return false;
        }

        TileKineticCrank tile = (TileKineticCrank) world.getTileEntity(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ);

        // Prevents the mill from being placed on the front/back of the block
        if (!(opposite == tile.orientation || opposite == tile.orientation.getOpposite())) {
            return false;
        }

        TileEntity otherTile = world.getTileEntity(tile.xCoord + opposite.offsetX, tile.yCoord + opposite.offsetY, tile.zCoord + opposite.offsetZ);

        if (otherTile != null && otherTile instanceof TileWaterMill) {
            return false;
        }

        boolean xAxis = ForgeDirection.getOrientation(side).getRotation(ForgeDirection.UP).getOpposite().offsetX != 0;
        for (int ix = -1; ix < 2; ix++) {
            for (int iy = -1; iy < 2; iy++) {
                for (int iz = -1; iz < 2; iz++) {
                    int sx = xAxis ? x + ix : x;
                    int sy = y + iy;
                    int sz = xAxis ? z : z + iz;

                    if (sy != 0 && (xAxis ? (iz == -1) : (ix == -1))) {
                        Block block = world.getBlock(sx, sy, sz);
                        if (block != Blocks.water && block != Blocks.flowing_water && !world.isAirBlock(sx, sy, sz)) {
                            return false;
                        }
                    }
                }
            }
        }

        boolean result = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

        if (result) {
            TileWaterMill mill = (TileWaterMill) world.getTileEntity(x, y, z);
            mill.orientation = ForgeDirection.getOrientation(side).getOpposite();
        }

        return result;
    }
}
