package subsistence.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.core.handler.TimerHandler;
import subsistence.common.item.prefab.SubsistenceItem;
import subsistence.common.network.packet.PacketFX;
import subsistence.common.util.InventoryHelper;

import java.util.Random;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

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
            final int meta = world.getBlockMetadata(x, y, z);

            boolean isLeaf = false;

            // If we don't simply sub-class a leaf block class
            if (!(block instanceof BlockLeaves)) {
                // Check to see if this block is specified in the ore dictionary as leaves
                final Item leaf = Item.getItemFromBlock(block);

                for (ItemStack itemStack : OreDictionary.getOres("treeLeaves")) {
                    if (itemStack.getItem() == leaf) {
                        if (itemStack.getItemDamage() == WILDCARD_VALUE) {
                            isLeaf = true;
                            break;
                        } else {
                            if (itemStack.getItemDamage() == meta) {
                                isLeaf = true;
                                break;
                            }
                        }
                    }
                }
            } else {
                // Otherwise, we simply are
                isLeaf = true;
            }

            if (!isLeaf)
                return true;

            TimerHandler.INSTANCE.startTimer(player, new TimerHandler.Timer("Searching...", 20, new TimerHandler.Callback(world, x, y, z) {
                @Override
                public void callback(EntityPlayer entityPlayer) {
                    World world = (World) args[0];
                    int x = (Integer) args[1];
                    int y = (Integer) args[2];
                    int z = (Integer) args[3];

                    final Item drop = block.getItemDropped(meta, new Random(), 0);
                    final int dropMeta = block.damageDropped(meta);
                    InventoryHelper.dropItem(world, x, y, z, ForgeDirection.UNKNOWN, new ItemStack(drop, 1, dropMeta), null);
                    PacketFX.breakFX(world.provider.dimensionId, x, y, z, new ItemStack(block));

                    world.setBlockToAir(x, y, z);
                }
            }));
        }
        return false;
    }
}
