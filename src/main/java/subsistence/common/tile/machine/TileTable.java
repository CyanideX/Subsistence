package subsistence.common.tile.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.lib.DurabilityMapping;
import subsistence.common.lib.tool.ToolDefinition;
import subsistence.common.network.packet.PacketFX;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.TableDryingRecipe;
import subsistence.common.recipe.wrapper.TableRecipe;
import subsistence.common.tile.core.TileCore;
import subsistence.common.util.InventoryHelper;
import subsistence.common.util.ItemHelper;

import java.util.Random;

/**
 * @author dmillerw
 */
public class TileTable extends TileCore {

    @NBTHandler.Sync(true)
    public ItemStack stack;

    @NBTHandler.Sync(true)
    public float durability;

    @NBTHandler.Sync(true)
    public float amountHammer;

    @NBTHandler.Sync(true)
    public float amountDrying;

    @NBTHandler.Sync(true)
    public int decayTimer = 0;

    @NBTHandler.Sync(true)
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
            amountDrying++;
            if (amountDrying >= recipe.getDuration()) {
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
                TableRecipe output = SubsistenceRecipes.TABLE.get(stack, tool, true);

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
}
