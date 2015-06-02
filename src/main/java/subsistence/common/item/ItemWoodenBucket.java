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
        this.isFull = null;
    }

    @Override
    public String[] getNames() {
        return NAMES;
    }

    @Override
    public String getIconPrefix() {
        return "woodbucket";
    }

    @Override
    public void onUpdate (ItemStack item, World world, Entity holder, int whatDisDoo, boolean wutDisDo) {
        EntityPlayer player = (EntityPlayer) holder;
        int meta = item.getItemDamage();
        if (!world.isRemote) {
            if (this.isFull == null) {
                switch (meta) { //pull from creative
                    case 0:
                        this.isFull = Blocks.air;
                    case 1:
                        this.isFull = Blocks.water;
                    case 2:
                        this.isFull = SubsistenceBlocks.boilingWater;
                    case 3:
                        this.isFull = Blocks.lava;
                }
                System.out.println("updated bucket data, as it is missing required data");
            }
            if (meta == 3) { //lava dump
                int holderX = MathHelper.floor_double(player.posX);
                int holderY = MathHelper.floor_double(player.posY);
                int holderZ = MathHelper.floor_double(player.posZ);
                if (world.isAirBlock(holderX,holderY,holderZ)) {
                    world.setBlock(holderX, holderY, holderZ, Blocks.flowing_lava);
                }
                ItemStack[] inventory = player.inventory.mainInventory;
                ItemStack templateBucket = new ItemStack(SubsistenceItems.woodenBucket,1,3);
                for (int i = 0; i < inventory.length; i++) {
                    if (inventory[i] != null) {
                        if (inventory[i].isItemEqual(templateBucket) && inventory[i].getItemDamage() == templateBucket.getItemDamage()) {
                            inventory[i] = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    //DEFAULT BUCKET STUFF
    @Override
    public ItemStack onItemRightClick(ItemStack bucket, World world, EntityPlayer player)
    {
        boolean flag = this.isFull == Blocks.air;
        System.out.println("flag is "+flag);
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null) {
            System.out.println("moving object position is null, what?");
            return bucket;
        } else {
            FillBucketEvent event = new FillBucketEvent(player, bucket, world, movingobjectposition);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return bucket;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                if (player.capabilities.isCreativeMode) {
                    System.out.println("player in creative - event");
                    return bucket;
                }

                if (--bucket.stackSize <= 0) {
                    System.out.println("fill bucket - event");
                    return event.result;
                }

                if (!player.inventory.addItemStackToInventory(event.result)) {
                    System.out.println("drop bucket - event");
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
                    System.out.println("trying to fill bucket");
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, bucket)) {
                        System.out.println("player cant edit world here!");
                        return bucket;
                    }

                    Block worldLiquid = world.getBlock(i, j, k);
                    int l = world.getBlockMetadata(i, j, k);

                    if (worldLiquid == Blocks.water && l == 0) {
                        world.setBlockToAir(i, j, k);
                        return this.fillBucketFromWorld(bucket, player, new ItemStack(SubsistenceItems.woodenBucket, 1, 1)); //water
                    }

                    if (worldLiquid == Blocks.lava && l == 0) {
                        world.setBlockToAir(i, j, k);
                        return this.fillBucketFromWorld(bucket, player, new ItemStack(SubsistenceItems.woodenBucket, 1, 3)); //lava
                    }

                    if (worldLiquid == SubsistenceBlocks.boilingWater && l == 0) {
                        world.setBlockToAir(i,j,k);
                        return this.fillBucketFromWorld(bucket, player, new ItemStack(SubsistenceItems.woodenBucket, 1, 2)); //boiling water
                    }
                    System.out.println("no valid liquid? trying to fill with air?");
                } else {
                    System.out.println("trying to empty bucket");
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

    private ItemStack fillBucketFromWorld(ItemStack empty, EntityPlayer player, ItemStack full) {
        if (player.capabilities.isCreativeMode) {
            System.out.println("player in creative - fill from world");
            return empty;
        } else {
            System.out.println("filled bucket");
            return full;
        }
    }

    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        if (this.isFull == Blocks.air) {
            System.out.println("bucket is empty");
            return false;
        } else {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();
            if (!world.isAirBlock(x, y, z) && !flag) {
                System.out.println("trying to place in not air, or block is solid");
                return false;
            } else {
                //TODO: nether checking?
                world.setBlock(x, y, z, this.isFull, 0, 3);
                System.out.println("placed "+ this.isFull.getLocalizedName());
                return true;
            }
        }
    }
}
