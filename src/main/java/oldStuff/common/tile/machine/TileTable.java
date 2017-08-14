package oldStuff.common.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import oldStuff.common.block.SubsistenceBlocks;
import oldStuff.common.config.ToolSettings;
import oldStuff.common.item.ItemHammer;
import oldStuff.common.network.nbt.NBTHandler;
import oldStuff.common.network.packet.PacketFX;
import oldStuff.common.recipe.SubsistenceRecipes;
import oldStuff.common.recipe.wrapper.TableAxeRecipe;
import oldStuff.common.recipe.wrapper.TableDryingRecipe;
import oldStuff.common.recipe.wrapper.TableSmashingRecipe;
import oldStuff.common.tile.core.TileCore;
import oldStuff.common.util.InventoryHelper;
import oldStuff.common.util.ItemHelper;

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
    
    public String getType() {
        return getBlockMetadata() == 0 ? "wood" : "stone";
    }

    public boolean smash(EntityPlayer player) {
        ItemStack tool = player.getCurrentEquippedItem();
        Random random = new Random();

        if (tool == null)
            return false;

        if (getType().equalsIgnoreCase("wood")) {
            boolean converted = false;
            if (stack != null) {
                if (ItemHelper.isBlock(stack, Blocks.cobblestone) && tool.getItem() instanceof ItemHammer) {
                    setStack(null); // to prevent the cobble dropping
                    worldObj.setBlock(xCoord, yCoord, zCoord, SubsistenceBlocks.table, 1, 3);
                    converted = true;
                } else if (ItemHelper.isBlock(stack, SubsistenceBlocks.componentGround) && stack.getItemDamage() == 2 && tool.getItem() instanceof ItemHammer) {
                    setStack(null); // to prevent the cobble dropping
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
