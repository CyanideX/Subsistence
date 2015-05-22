package subsistence.common.block.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.Subsistence;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.block.prefab.item.SubsistenceItemBlock;
import subsistence.common.tile.machine.TileHammerMill;
import subsistence.common.tile.machine.TileHandCrank;

/**
 * @author dmillerw
 */
public class ItemBlockCrank extends SubsistenceItemBlock {

    private IIcon icon;

    public ItemBlockCrank(Block block) {
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
        icon = register.registerIcon(Subsistence.RESOURCE_PREFIX + "tools/handCrank");
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

        if (world.getBlock(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ) != SubsistenceBlocks.hammerMill) {
            return false;
        }

        TileEntity tile = world.getTileEntity(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ);

        if (!(tile instanceof TileHammerMill)) {
            return false;
        }

        TileHammerMill hammerMill = (TileHammerMill) tile;

        ForgeDirection orientation = ForgeDirection.getOrientation(side);
        if (orientation != hammerMill.orientation.getRotation(ForgeDirection.UP) && orientation != hammerMill.orientation.getRotation(ForgeDirection.UP).getOpposite()) {
            return false;
        }

        boolean result = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

        if (result) {
            TileHandCrank crank = (TileHandCrank) world.getTileEntity(x, y, z);
            crank.orientation = ForgeDirection.getOrientation(side).getOpposite();

            if (hammerMill.orientation == ForgeDirection.WEST && crank.orientation == ForgeDirection.SOUTH) {
                crank.reverse = true;
            } else if (hammerMill.orientation == ForgeDirection.SOUTH && crank.orientation == ForgeDirection.EAST) {
                crank.reverse = true;
            } else if (hammerMill.orientation == ForgeDirection.EAST && crank.orientation == ForgeDirection.NORTH) {
                crank.reverse = true;
            } else if (hammerMill.orientation == ForgeDirection.NORTH && crank.orientation == ForgeDirection.WEST) {
                crank.reverse = true;
            }
        }

        return result;
    }
}
