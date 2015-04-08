package com.subsistence.common.item;

import com.subsistence.Subsistence;
import com.subsistence.common.item.prefab.SubsistenceItem;
import com.subsistence.common.core.SubsistenceCreativeTab;
import com.subsistence.common.core.handler.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Royalixor
 */
public class ItemTray extends SubsistenceItem {

    public ItemTray() {
        super(SubsistenceCreativeTab.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                player.openGui(Subsistence.instance, GuiHandler.GUI_TRAY, world, 0, 0, 0);
                return stack;
            }
        }
        return stack;
    }

    @Override
    public String getIcon() {
        return "tools/ovenTray";
    }
}
