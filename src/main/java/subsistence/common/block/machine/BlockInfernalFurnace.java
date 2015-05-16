package subsistence.common.block.machine;

import subsistence.Subsistence;
import subsistence.common.block.prefab.SubsistenceTileBlock;
import subsistence.common.core.handler.GuiHandler;
import subsistence.common.tile.machine.TileHellfireFurnace;
import subsistence.common.util.InventoryHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockInfernalFurnace extends SubsistenceTileBlock {

    private IIcon[] icons;

    public BlockInfernalFurnace() {
        super(Material.rock);
        setTickRandomly(true);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        if (!world.isRemote && !player.isSneaking()) {

            if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.flint_and_steel) {
                TileHellfireFurnace tile = (TileHellfireFurnace) world.getTileEntity(x, y, z);

                if (tile != null && !tile.lit) {
                    tile.lit = true;

                    world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                    player.getHeldItem().damageItem(1, player);

                    tile.markForUpdate();
                }
            } else {
                player.openGui(Subsistence.instance, GuiHandler.GUI_INFERNAL_FURNACE, world, x, y, z);
            }
        }
        return !player.isSneaking();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileHellfireFurnace tile = (TileHellfireFurnace) world.getTileEntity(x, y, z);

        if (tile != null) {
            for (int i = 0; i < tile.getSizeInventory(); i++) {
                ItemStack stack = tile.getStackInSlot(i);

                if (stack != null)
                    InventoryHelper.dropItem(world, x, y, z, ForgeDirection.UNKNOWN, stack, world.rand);

            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == 1 ? (int) (0.875F * 16) : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileHellfireFurnace();
    }

    @Override
    public boolean useCustomRender() {
        return false;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileHellfireFurnace tile = (TileHellfireFurnace) world.getTileEntity(x, y, z);

        if (tile != null)
            if (tile.orientation.ordinal() == side)
                return icons[world.getBlockMetadata(x, y, z) == 1 ? 2 : 1];


        if (side == 1)
            return icons[3];
        else
            return icons[0];

    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == ForgeDirection.SOUTH.ordinal()) {
            return icons[1];
        } else if (side == 1) {
            return icons[3];
        } else {
            return icons[0];
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[4];

        icons[0] = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "machine/hellfireFurnace_side");
        icons[1] = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "machine/hellfireFurnace_front");
        icons[2] = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "machine/hellfireFurnace_front_ON");
        icons[3] = iconRegister.registerIcon(Subsistence.RESOURCE_PREFIX + "machine/hellfireFurnace_top");
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(this, 1, ForgeDirection.SOUTH.ordinal()));
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileHellfireFurnace tile = (TileHellfireFurnace) world.getTileEntity(x, y, z);

        if (tile != null && world.getBlockMetadata(x,y,z) == 1) {
            int l = tile.orientation.ordinal();
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}