package com.subsistence.common.block.prefab.item;

import com.subsistence.common.util.EntityHelper;
import com.subsistence.common.tile.core.TileCoreMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
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
