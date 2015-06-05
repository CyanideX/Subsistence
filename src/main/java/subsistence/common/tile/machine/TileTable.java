package subsistence.common.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.block.machine.BarrelType;
import subsistence.common.block.machine.TableType;
import subsistence.common.config.ToolSettings;
import subsistence.common.item.ItemHammer;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.network.packet.PacketFX;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.TableAxeRecipe;
import subsistence.common.recipe.wrapper.TableDryingRecipe;
import subsistence.common.recipe.wrapper.TableSmashingRecipe;
import subsistence.common.tile.core.TileCore;
import subsistence.common.util.ArrayHelper;
import subsistence.common.util.InventoryHelper;
import subsistence.common.util.ItemHelper;

import java.util.Random;

public class TileTable extends TileCore {

    @NBTHandler.Sync(true)
    public ItemStack stack;

    @NBTHandler.Sync(true)
    public float durability;

    @NBTHandler.Sync(true)
    public float damage ;

    @NBTHandler.Sync(true)
    public float amountDrying;

    @NBTHandler.Sync(true)
    public int decayTimer = 0;

    @NBTHandler.Sync(true)
    public boolean attractedFlies = false;

    public void setStack(ItemStack stack) {
        this.stack = stack;

        decayTimer = 0;
        attractedFlies = false;
        if (this.stack != null && SubsistenceRecipes.PERISHABLE.containsKey(this.stack.getItem())) {
            decayTimer = SubsistenceRecipes.PERISHABLE.get(this.stack.getItem()) * 20;
        }

        damage = 0;
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
        TableDryingRecipe recipe = SubsistenceRecipes.TABLE.getDryingRecipe(stack);
        if (recipe != null) {
            amountDrying++;
            if (amountDrying >= recipe.duration) {
                stack = recipe.output.copy();
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
    
    public TableType getType() {
        return ArrayHelper.safeGetArrayIndex(TableType.values(), getBlockMetadata());
    }
    
    public boolean smash(EntityPlayer player) {
        ItemStack tool = player.getCurrentEquippedItem();
        Random random = new Random();

        if (tool == null)
            return false;

        if (getType().isWood()) {
            boolean converted = false;
            if (stack != null) {
                if (ItemHelper.isBlock(stack, Blocks.cobblestone) && tool.getItem() instanceof ItemHammer) {
                    worldObj.setBlock(xCoord, yCoord, zCoord, SubsistenceBlocks.table, 1, 3);
                    converted = true;
                } else if (ItemHelper.isBlock(stack, SubsistenceBlocks.componentGround) && stack.getItemDamage() == 2 && tool.getItem() instanceof ItemHammer) {
                    worldObj.setBlock(xCoord, yCoord, zCoord, SubsistenceBlocks.table, 2, 3);
                    converted = true;
                }
            }

            if (converted) {
                PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, new ItemStack(Blocks.stone_slab));
                getWorldObj().playSoundEffect(xCoord, yCoord, zCoord, "subsistence:oreCrumble", 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                stack = null;

                return true;
            } else {
                return false;
            }
        } else {
            if (stack != null) {
                if (ToolSettings.isHammer(tool)) {
                    TableSmashingRecipe recipe = SubsistenceRecipes.TABLE.getSmashingRecipe(stack);
                    if (recipe != null) {
                        damage += ToolSettings.getHammerStrength(tool);
                        if (damage >= recipe.durability) {
                            if (stack.getItem() instanceof ItemBlock) {
                                PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, stack);
                            }
                            setStack(recipe.output.copy());
                        }
                    }
                } else if (ToolSettings.isAxe(tool)) {
                    TableAxeRecipe recipe = SubsistenceRecipes.TABLE.getAxeRecipe(stack);
                    if (recipe != null) {
                        damage += ToolSettings.getAxeStrength(tool);
                        if (damage >= recipe.durability) {
                            if (stack.getItem() instanceof ItemBlock) {
                                PacketFX.breakFX(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, stack);
                            }
                            setStack(recipe.output.copy());
                        }
                    }
                }
            }
        }

        return false;
    }
}
