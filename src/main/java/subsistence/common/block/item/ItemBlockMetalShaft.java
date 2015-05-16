package subsistence.common.block.item;

import subsistence.common.block.prefab.item.SubsistenceItemBlock;
import subsistence.common.tile.machine.TileHammerMill;
import subsistence.common.lib.SubsistenceProps;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.tile.machine.TileKineticCrank;
import subsistence.common.tile.machine.TileMetalShaft;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author dmillerw
 */
public class ItemBlockMetalShaft extends SubsistenceItemBlock {

    private IIcon icon;

    public ItemBlockMetalShaft(Block block) {
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
        icon = register.registerIcon(SubsistenceProps.RESOURCE_PREFIX + "tools/metalBar");
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

        TileEntity tile = world.getTileEntity(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ);

        if (!(tile instanceof TileHammerMill) && !(tile instanceof TileKineticCrank) && !(tile instanceof TileMetalShaft)) {
            return false;
        }

        TileCoreMachine machine = (TileCoreMachine) tile;

        ForgeDirection orientation = ForgeDirection.getOrientation(side);
        if (machine instanceof TileHammerMill || machine instanceof TileMetalShaft) {
            if (orientation != machine.orientation.getRotation(ForgeDirection.UP) && orientation != machine.orientation.getRotation(ForgeDirection.UP).getOpposite()) {
                return false;
            }
        } else if (machine instanceof TileKineticCrank) {
            if (orientation != machine.orientation && orientation != machine.orientation.getOpposite()) {
                return false;
            }
        }

        boolean result = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        if (result) {
            TileMetalShaft shaft = (TileMetalShaft) world.getTileEntity(x, y, z);
            shaft.orientation = ForgeDirection.getOrientation(side).getOpposite().getRotation(ForgeDirection.UP);
        }

        return result;
    }
}
