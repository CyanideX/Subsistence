package subsistence.common.core.handler;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;
import subsistence.common.config.CoreSettings;
import subsistence.common.network.packet.PacketFX;
import subsistence.common.util.InventoryHelper;

import java.util.Map;
import java.util.Random;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * @author dmillerw
 */
public class LeafHandler {

    private static final Random RANDOM = new Random();
    private static final int COOLDOWN = 2000;

    private static Map<String, Long> cooldownMap = Maps.newHashMap();

    private static long get(String player) {
        return cooldownMap.containsKey(player) ? cooldownMap.get(player) : 0;
    }

    @SubscribeEvent
    public void onLeafInteract(PlayerInteractEvent event) {
        if (event.world.isRemote)
            return;

        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.entityPlayer.getHeldItem() != null)
            return;

        // Can only be done once every two seconds
        long currentTime = System.currentTimeMillis();
        long get = get(event.entityPlayer.getCommandSenderName());

        if (get > 10 && currentTime - get <= COOLDOWN) {
            return;
        }

        Block block = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);

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
            return;

        cooldownMap.put(event.entityPlayer.getCommandSenderName(), currentTime);

        if (RANDOM.nextInt(100) <= CoreSettings.STATIC.leafSaplingChance) {
            final Item drop = block.getItemDropped(meta, RANDOM, 0);
            final int dropMeta = block.damageDropped(meta);
            InventoryHelper.dropItem(event.world, event.x, event.y, event.z, ForgeDirection.UNKNOWN, new ItemStack(drop, 1, dropMeta), RANDOM);

            if (RANDOM.nextInt(100) <= CoreSettings.STATIC.leafSaplingDoubleChance) {
                InventoryHelper.dropItem(event.world, event.x, event.y, event.z, ForgeDirection.UNKNOWN, new ItemStack(drop, 1, dropMeta), RANDOM);
            }
        }

        event.world.setBlockToAir(event.x, event.y, event.z);
        PacketFX.breakFX(event.world.provider.dimensionId, event.x, event.y, event.z, new ItemStack(block, 1, meta));
    }
}
