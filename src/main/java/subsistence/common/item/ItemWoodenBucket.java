package subsistence.common.item;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.item.prefab.SubsistenceMultiItem;

public class ItemWoodenBucket extends SubsistenceMultiItem {
    public static final String[] NAMES = new String[]{"empty","water","boiling_water","lava"};
    private Block isFull;
    public ItemWoodenBucket () {
        super(SubsistenceCreativeTab.TOOLS);
        this.setMaxStackSize(1);
        this.isFull = Blocks.air;
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "wooden_bucket";
    }

    @Override
    public void onUpdate (ItemStack item, World world, Entity holder, int whatDisDoo, boolean wutDisDo) {
        if (!world.isRemote) {
            if (item.getItemDamage() == 3) { //lava
                int holderX = MathHelper.floor_double(holder.posX);
                int holderY = MathHelper.floor_double(holder.posY);
                int holderZ = MathHelper.floor_double(holder.posZ);
                world.setBlock(holderX,holderY,holderZ,Blocks.flowing_lava);
                holder.setCurrentItemOrArmor(0,new ItemStack(SubsistenceItems.woodenBucket,1,0));
            }
        }
    }

    //DEFAULT BUCKET STUFF
    @Override
    public ItemStack onItemRightClick(ItemStack bucket, World world, EntityPlayer player)
    {
        boolean flag = this.isFull == Blocks.air;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null) {
            return bucket;
        } else {
            FillBucketEvent event = new FillBucketEvent(player, bucket, world, movingobjectposition);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return bucket;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                if (player.capabilities.isCreativeMode) {
                    return bucket;
                }

                if (--bucket.stackSize <= 0) {
                    return event.result;
                }

                if (!player.inventory.addItemStackToInventory(event.result)) {
                    player.dropPlayerItemWithRandomChoice(event.result, false);
                }

                return bucket;
            }
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k)) {
                    return bucket;
                }

                if (flag) {
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, bucket)) {
                        return bucket;
                    }

                    Block worldLiquid = world.getBlock(i, j, k);
                    int l = world.getBlockMetadata(i, j, k);

                    if (worldLiquid == Blocks.water && l == 0) {
                        world.setBlockToAir(i, j, k);
                        return this.fillBucket(bucket, player, new ItemStack(SubsistenceItems.woodenBucket,1,1)); //water
                    }

                    if (worldLiquid == Blocks.lava && l == 0) {
                        world.setBlockToAir(i, j, k);
                        return this.fillBucket(bucket, player, new ItemStack(SubsistenceItems.woodenBucket,1,3)); //lava
                    }

                    if (worldLiquid == SubsistenceBlocks.boilingWater && l == 0) {
                        world.setBlockToAir(i,j,k);
                        return this.fillBucket(bucket,player,new ItemStack(SubsistenceItems.woodenBucket,1,2)); //boiling water
                    }
                }
                else
                {
                    if (this.isFull == Blocks.air) {
                        System.out.println("bucket full of air");
                        return new ItemStack(SubsistenceItems.woodenBucket,1,0);
                    }

                    if (movingobjectposition.sideHit == 0) {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1) {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2) {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3) {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4) {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5) {
                        ++i;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, bucket)) {
                        System.out.println("player cant edit block");
                        return bucket;
                    }

                    if (this.tryPlaceContainedLiquid(world, i, j, k) && !player.capabilities.isCreativeMode) {
                        System.out.println("placed, returning bucket");
                        return new ItemStack(SubsistenceItems.woodenBucket,1,0);
                    }
                }
            }

            return bucket;
        }
    }

    private ItemStack fillBucket(ItemStack empty, EntityPlayer player, ItemStack full) {
        if (player.capabilities.isCreativeMode) {
            return empty;
        } else if (--empty.stackSize <= 0) {
            return full;
        } else {
            if (!player.inventory.addItemStackToInventory(full)) {
                player.dropPlayerItemWithRandomChoice(full, false);
            }
            return empty;
        }
    }

    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        if (this.isFull == Blocks.air) {
            return false;
        } else {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();
            if (!world.isAirBlock(x, y, z) && !flag) {
                return false;
            } else {
                //TODO: nether checking?
                world.setBlock(x, y, z, this.isFull, 0, 3);
                return true;
            }
        }
    }
}
