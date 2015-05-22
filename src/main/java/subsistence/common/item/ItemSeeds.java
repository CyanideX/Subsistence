package subsistence.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import subsistence.common.block.BlockWormwood;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.prefab.SubsistenceMultiItem;

public class ItemSeeds extends SubsistenceMultiItem {

    public static final String[] NAMES = new String[]{"grass", "nether_grass", "wormwoodSeeds"};

    public ItemSeeds() {
        super(SubsistenceCreativeTab.ITEMS);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z);

            if (block == Blocks.dirt && stack.getItemDamage() == 0) { //grass seeds
                world.setBlock(x, y, z, Blocks.grass, 0, 3);
                if (player.capabilities.isCreativeMode) {
                    return true;
                }
                stack.stackSize--;
                return true;
            } else if (block == Blocks.soul_sand && stack.getItemDamage() == 1) { //nether seeds
                world.setBlock(x, y, z, SubsistenceBlocks.netherGrass, 0, 3);
                if (player.capabilities.isCreativeMode) {
                    return true;
                }
                stack.stackSize--;
                return true;
            } else if (((BlockWormwood) SubsistenceBlocks.wormwood).canPlaceBlockOn(block) && stack.getItemDamage() == 2) { //wormwood seeds
                world.setBlock(x, y + 1, z, SubsistenceBlocks.wormwood, 0, 3);
                if (player.capabilities.isCreativeMode) {
                    return true;
                }
                stack.stackSize--;
                return true;
            }
        }

        return false;
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "world/seed";
    }
}
