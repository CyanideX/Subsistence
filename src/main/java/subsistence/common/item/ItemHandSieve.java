package subsistence.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import subsistence.Subsistence;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.core.handler.GuiHandler;
import subsistence.common.inventory.InventoryItem;
import subsistence.common.item.prefab.SubsistenceItem;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.SieveRecipe;

/**
 * @author dmillerw
 */
public class ItemHandSieve extends SubsistenceItem {

    public static void process(ItemStack stack, EntityPlayer player) {
        InventoryItem inventoryItem = new InventoryItem(stack, 1);
        ItemStack stack1 = inventoryItem.getStackInSlot(0);

        if (stack1 != null) {
            SieveRecipe recipe = SubsistenceRecipes.SIEVE.get(stack1);

            for (ItemStack stack2 : recipe.get(null, false)) {
                player.dropPlayerItemWithRandomChoice(stack2, false);
            }

            stack1.stackSize--;
            if (stack1.stackSize <= 0) {
                inventoryItem.setInventorySlotContents(0, null);
            }
            inventoryItem.markDirty();
        }
    }

    public static void recalculate(ItemStack stack, EntityPlayer player) {
        ItemStack stack1 = new InventoryItem(stack, 1).getStackInSlot(0);

        if (stack1 != null) {
            SieveRecipe recipe = SubsistenceRecipes.SIEVE.get(stack1);

            ItemHandSieve.setCurrentDuration(stack, 0);
            ItemHandSieve.setMaxDuration(stack, recipe.getDurationHand());
        } else {
            ItemHandSieve.setCurrentDuration(stack, 0);
            ItemHandSieve.setMaxDuration(stack, 0);
        }

        // Sync
        player.setCurrentItemOrArmor(0, stack);
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).updateHeldItem();
        }
    }

    public static int getCurrentDuration(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        }

        NBTTagCompound nbt = stack.getTagCompound();

        if (!nbt.hasKey("current")) {
            return 0;
        }

        return nbt.getInteger("current");
    }

    public static int getMaxDuration(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return 0;
        }

        NBTTagCompound nbt = stack.getTagCompound();

        if (!nbt.hasKey("max")) {
            return 0;
        }

        return nbt.getInteger("max");
    }

    public static void setCurrentDuration(ItemStack stack, int duration) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = stack.getTagCompound();

        nbt.setInteger("current", duration);

        stack.setTagCompound(nbt);
    }

    public static void setMaxDuration(ItemStack stack, int duration) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = stack.getTagCompound();

        nbt.setInteger("max", duration);

        stack.setTagCompound(nbt);
    }

    public ItemHandSieve() {
        super(SubsistenceCreativeTab.TOOLS);

        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                player.openGui(Subsistence.instance, GuiHandler.GUI_HAND_SIEVE, world, 0, 0, 0);
                return stack;
            }
        }

        player.setItemInUse(stack, getMaxItemUseDuration(stack));

        return stack;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        int current = getCurrentDuration(stack);
        int max = getMaxDuration(stack);

        player.swingItem();

        if (max != 0) {
            if (current < max) {
                setCurrentDuration(stack, current + 1);
            } else {
                process(stack, player);
                recalculate(stack, player);
            }
        } else {
            recalculate(stack, player);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return getMaxDuration(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.none;
    }

    @Override
    public String getIcon() {
        return "tools/handSieve";
    }
}
