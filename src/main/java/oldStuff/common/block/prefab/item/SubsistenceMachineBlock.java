package oldStuff.common.block.prefab.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import oldStuff.common.tile.core.TileCoreMachine;
import oldStuff.common.util.EntityHelper;

public class SubsistenceMachineBlock extends SubsistenceItemBlock {

    public SubsistenceMachineBlock(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean result = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

        if (result) {
            TileCoreMachine tile = (TileCoreMachine) world.getTileEntity(x, y, z);

            tile.orientation = EntityHelper.get2DRotation(player).getOpposite();
        }

        return result;
    }
}
