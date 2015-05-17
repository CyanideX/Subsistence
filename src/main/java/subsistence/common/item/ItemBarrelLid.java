package subsistence.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.prefab.SubsistenceMultiItem;
import subsistence.common.tile.machine.TileWoodBarrel;

/**
 * @author dmillerw
 */
public class ItemBarrelLid extends SubsistenceMultiItem {

    private static final String[] NAMES = new String[]{"wood", "stone"};

    public ItemBarrelLid() {
        super(SubsistenceCreativeTab.ITEMS);

        setMaxStackSize(1);
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "";
    }

    @Override
    public void registerIcons(IIconRegister register) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        // if (!world.isRemote) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileWoodBarrel && stack.getItemDamage() == tile.getBlockMetadata()) {
            if (!((TileWoodBarrel) tile).hasLid()) {
                ((TileWoodBarrel) tile).toggleLid();
                stack.stackSize = 0;
            }
        }
        // }
        return false;
    }
}
