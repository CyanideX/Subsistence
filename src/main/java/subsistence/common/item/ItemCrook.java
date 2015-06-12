package subsistence.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.core.handler.TimerHandler;
import subsistence.common.item.prefab.SubsistenceItem;
import subsistence.common.network.packet.PacketFX;
import subsistence.common.util.InventoryHelper;

/**
 * @author dmillerw
 */
public class ItemCrook extends SubsistenceItem {

    public ItemCrook() {
        super(SubsistenceCreativeTab.TOOLS);

        setMaxDamage(56);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            final Block block = world.getBlock(x, y, z);

            if (block instanceof BlockLeaves) {
                TimerHandler.INSTANCE.startTimer(player, new TimerHandler.Timer("Searching...", 20, new TimerHandler.Callback(world, x, y, z) {
                    @Override
                    public void callback(EntityPlayer entityPlayer) {
                        World world = (World) args[0];
                        int x = (Integer) args[1];
                        int y = (Integer) args[2];
                        int z = (Integer) args[3];

                        InventoryHelper.dropItem(world, x, y, z, ForgeDirection.UNKNOWN, new ItemStack(Blocks.sapling), null);
                        PacketFX.breakFX(world.provider.dimensionId, x, y, z, new ItemStack(block));

                        world.setBlockToAir(x, y, z);
                    }
                }));
            }
        }
        return false;
    }
}
