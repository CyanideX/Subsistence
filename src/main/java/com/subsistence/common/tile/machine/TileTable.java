package com.subsistence.common.tile.machine;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.lib.DurabilityMapping;
import com.subsistence.common.lib.tool.ToolDefinition;
import com.subsistence.common.network.PacketFX;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.wrapper.TableDryingRecipe;
import com.subsistence.common.recipe.wrapper.TableRecipe;
import com.subsistence.common.tile.core.TileCore;
import com.subsistence.common.util.ItemHelper;
import com.subsistence.common.network.nbt.NBTHandler;
import com.subsistence.common.util.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * @author dmillerw
 */
public class TileTable extends TileCore implements ISidedInventory {

	@NBTHandler.NBTData
	@NBTHandler.DescriptionData
	public ItemStack stack;

	@NBTHandler.NBTData
	@NBTHandler.DescriptionData
	public float durability;

	@NBTHandler.NBTData
	@NBTHandler.DescriptionData
	public float amountHammer;

	@NBTHandler.NBTData
	@NBTHandler.DescriptionData
	public float amountDrying;

	@NBTHandler.NBTData
	@NBTHandler.DescriptionData
	public int decayTimer = 0;

	@NBTHandler.NBTData
	@NBTHandler.DescriptionData
	public boolean attractedFlies = false;

	public void setStack(ItemStack stack) {
		this.stack = stack;

		if (stack != null) {
			durability = DurabilityMapping.INSTANCE.getDurability(stack);
		}

		decayTimer = 0;
		attractedFlies = false;
		if (this.stack != null && SubsistenceRecipes.PERISHABLE.containsKey(this.stack.getItem())) {
			decayTimer = SubsistenceRecipes.PERISHABLE.get(this.stack.getItem()) * 20;
		}
		amountHammer = 0;
		amountDrying = 0;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote)
			if (stack != null) {
				dry();

				if (SubsistenceRecipes.PERISHABLE.containsKey(stack.getItem())) {
					if (decayTimer > 0) {
						decayTimer--;
					} else {
						stack = new ItemStack(Items.rotten_flesh);
						attractedFlies = true;
						markForUpdate();
					}
				}

			}
	}

	public void dry() {
		TableDryingRecipe recipe = SubsistenceRecipes.TABLE.getDrying(stack);
		if (recipe != null) {
			// 1 seconds/20 ticks per second
			amountDrying += 0.05f;
			if (amountDrying >= recipe.getSpeed()) {
				stack = recipe.getOutput();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				amountDrying = 0;
			}
		}
	}

	@Override
	public void onBlockBroken() {
		if (stack != null) {
			InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, stack, new Random());
		}
	}

	public boolean smash(EntityPlayer player) {
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ItemStack tool = player.getCurrentEquippedItem();
		Random random = new Random();

		if (meta == 0) {
			if (stack != null && ItemHelper.isBlock(stack, Blocks.stone_slab) && stack.getItemDamage() == 0 && ToolDefinition.isType(tool, ToolDefinition.HAMMER)) {
				PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, new ItemStack(Blocks.stone_slab));
				getWorldObj().playSoundEffect(xCoord, yCoord, zCoord, "subsistence:oreCrumble", 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

				stack = null;
				worldObj.setBlock(xCoord, yCoord, zCoord, SubsistenceBlocks.table, 1, 3);

				return true;
			}
		} else if (meta == 1) {
			if (stack != null) {
				TableRecipe output = SubsistenceRecipes.TABLE.get(stack, tool);

				if (output != null) {
					durability -= ToolDefinition.getStrength(tool);

					if (durability <= 0F) {
						if (stack.getItem() instanceof ItemBlock) {
							PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, stack);
						}

						if (output.damageTool) {
							tool.damageItem(1, player);
						}
						amountHammer++;
						if (amountHammer >= output.getSpeed())
							setStack(output.getOutput(false));

						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int i, int count) {
		ItemStack itemstack = getStackInSlot(0);

		if (itemstack != null) {
			if (itemstack.stackSize <= count) {
				setInventorySlotContents(0, null);
			} else {
				itemstack = itemstack.splitStack(count);

			}
		}

		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.stack = stack;

		if (stack != null) {
			durability = DurabilityMapping.INSTANCE.getDurability(stack);
		}

		decayTimer = 0;
		attractedFlies = false;
		if (this.stack != null && SubsistenceRecipes.PERISHABLE.containsKey(this.stack.getItem())) {
			decayTimer = SubsistenceRecipes.PERISHABLE.get(this.stack.getItem()) * 20;
		}
		amountHammer = 0;
		amountDrying = 0;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f) <= 64;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0, 1, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return true;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return !SubsistenceRecipes.PERISHABLE.containsKey(p_102008_2_.getItem());
	}

	@Override
	public String getInventoryName() {
		return "";
	}
}
