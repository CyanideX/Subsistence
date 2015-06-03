package subsistence.common.item;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.item.prefab.SubsistenceItem;

public class ItemWoodenBucket extends SubsistenceItem {

    private static final int DURABILITY = 12;

    private Fluid fluid;
    private String texture;

    public ItemWoodenBucket(Fluid fluid, String texture) {
        super(SubsistenceCreativeTab.TOOLS);

        this.fluid = fluid;
        this.texture = texture;

        setMaxDamage(DURABILITY);
        setMaxStackSize(1);
    }

    @Override
    public String getIcon() {
        return "woodbucket/" + texture;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        boolean pickup = fluid == null;

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, pickup);
        if (movingobjectposition == null) {
            return itemStack;
        } else {
            FillBucketEvent event = new FillBucketEvent(entityPlayer, itemStack, world, movingobjectposition);

            if (MinecraftForge.EVENT_BUS.post(event)) {
                return itemStack;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                if (entityPlayer.capabilities.isCreativeMode) {
                    return itemStack;
                }

                if (--itemStack.stackSize <= 0) {
                    return event.result;
                }

                if (!entityPlayer.inventory.addItemStackToInventory(event.result)) {
                    entityPlayer.dropPlayerItemWithRandomChoice(event.result, false);
                }

                return itemStack;
            }

            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                if (!world.canMineBlock(entityPlayer, x, y, z)) {
                    return itemStack;
                }

                if (pickup) {
                    if (!entityPlayer.canPlayerEdit(x, y, z, movingobjectposition.sideHit, itemStack)) {
                        return itemStack;
                    }

                    Block block = world.getBlock(x, y, z);
                    Material material = block.getMaterial();
                    int metadata = world.getBlockMetadata(x, y, z);

                    if (block instanceof IFluidBlock) {
                        FluidStack fluid = ((IFluidBlock) block).drain(world, x, y, z, false);
                        if (fluid != null) {
                            return fillContainer(itemStack, entityPlayer, ((IFluidBlock) block).drain(world, x, y, z, true));
                        }
                    } else {
                        if (block == SubsistenceBlocks.boilingWater) {
                            world.setBlockToAir(x, y, z);
                            return fillContainer(itemStack, entityPlayer, new FluidStack(SubsistenceFluids.boilingWaterFluid, 1000));
                        } else if (material == Material.water && metadata == 0) {
                            world.setBlockToAir(x, y, z);
                            return fillContainer(itemStack, entityPlayer, new FluidStack(FluidRegistry.WATER, 1000));
                        } else if (material == Material.lava && metadata == 0) {
                            world.setBlockToAir(x, y, z);
                            // BUUUUUUUURN
                            entityPlayer.setFire(100);

                            itemStack.stackSize--;

                            return itemStack;
                        }
                    }

                } else {
                    if (movingobjectposition.sideHit == 0) {
                        --y;
                    }

                    if (movingobjectposition.sideHit == 1) {
                        ++y;
                    }

                    if (movingobjectposition.sideHit == 2) {
                        --z;
                    }

                    if (movingobjectposition.sideHit == 3) {
                        ++z;
                    }

                    if (movingobjectposition.sideHit == 4) {
                        --x;
                    }

                    if (movingobjectposition.sideHit == 5) {
                        ++x;
                    }

                    if (!entityPlayer.canPlayerEdit(x, y, z, movingobjectposition.sideHit, itemStack)) {
                        return itemStack;
                    }

                    if (this.placeFluid(world, x, y, z) && !entityPlayer.capabilities.isCreativeMode) {
                        return new ItemStack(SubsistenceItems.woodenBucket);
                    }
                }
            }

            return itemStack;
        }
    }

    private ItemStack fillContainer(ItemStack input, EntityPlayer entityPlayer, FluidStack fluidStack) {
        if (entityPlayer.capabilities.isCreativeMode) {
            return input;
        } else {
            return FluidContainerRegistry.fillFluidContainer(fluidStack, input);
        }
    }

    private boolean placeFluid(World world, int x, int y, int z) {
        if (fluid == null) {
            return false;
        } else {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !flag) {
                return false;
            } else {
                Fluid place = fluid;

                if (world.provider.isHellWorld && this.fluid == FluidRegistry.WATER) {
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                    for (int l = 0; l < 8; ++l) {
                        world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                    place = SubsistenceFluids.boilingWaterFluid;
                }

                if (!world.isRemote && flag && !material.isLiquid()) {
                    world.func_147480_a(x, y, z, true);
                }

                world.setBlock(x, y, z, place.getBlock(), 0, 3);

                return true;
            }
        }
    }
}
