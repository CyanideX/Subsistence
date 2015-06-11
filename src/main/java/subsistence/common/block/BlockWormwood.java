package subsistence.common.block;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import subsistence.common.config.HeatSettings;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.util.ItemHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWormwood extends BlockBush implements IGrowable {

    private IIcon[] textures;

    protected BlockWormwood() {
        this.setTickRandomly(true);
        this.setBlockBounds(0, 0.0F, 0, 1, 0.25F, 1);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
    }

    @Override
    public boolean canPlaceBlockOn(Block block) {
        return block == Blocks.grass || block == Blocks.dirt || block == SubsistenceBlocks.netherGrass;
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int light = world.getBlockLightValue(x, y + 1, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (light >= 9) {
            if (meta < 7) { //if not grown
                float modifier = getGrowthModifier(world, x, y, z); //get blocks around, to increase speed?
                if (rand.nextInt((int) (25.0F / modifier) + 1) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
                }
            } else if (meta >= 7 && meta < 9) {
                float modifier = getDryingModifier(world, x, y, z); //get blocks around, to increase speed?
                if (rand.nextInt((int) (25.0F / modifier) + 1) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
                }
            }
        }
    }

    private float getGrowthModifier(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean blocksX = block2 == this || block3 == this; //if block +x or -x is wormwood
        boolean blocksZ = block == this || block1 == this; //if block +z or -z is wormwood
        boolean blocksCorners = block4 == this || block5 == this || block6 == this || block7 == this; //if block on any corner is wormwood

        for (int currX = x - 1; currX <= x + 1; ++currX) {
            for (int currZ = z - 1; currZ <= z + 1; ++currZ) {
                float f1 = 0.0F;

                if (canPlaceBlockOn(world.getBlock(currX, y - 1, currZ))) { //if ground blocks around wormwood can be placed on
                    f1 = 1.0F;
                }

                if (currX != x || currZ != z) { //if you're not checking next to you? | checking corners
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (blocksCorners || blocksX && blocksZ) { //if wormwood on corner or wormwood adjacent
            f /= 2.0F;
        }

        return f;
    }

    private float getDryingModifier(World world, int x, int y, int z) {
        float f = 1.0F;

        boolean heat1 = HeatSettings.isHeatSource(world, x,     y, z - 1);
        boolean heat2 = HeatSettings.isHeatSource(world, x,     y, z + 1);
        boolean heat3 = HeatSettings.isHeatSource(world, x - 1, y, z);
        boolean heat4 = HeatSettings.isHeatSource(world, x + 1, y, z);
        boolean heat5 = HeatSettings.isHeatSource(world, x - 1, y, z - 1);
        boolean heat6 = HeatSettings.isHeatSource(world, x + 1, y, z + 1);
        boolean heat7 = HeatSettings.isHeatSource(world, x - 1, y, z + 1);
        boolean heat8 = HeatSettings.isHeatSource(world, x + 1, y, z - 1);

        boolean blocksX = heat3 || heat4; //if block +x or -x is wormwood
        boolean blocksZ = heat1 || heat2; //if block +z or -z is wormwood
        boolean blocksCorners = heat5 || heat6 || heat7 || heat8; //if block on any corner is wormwood

        for (int currX = x - 1; currX <= x + 1; ++currX) {
            for (int currZ = z - 1; currZ <= z + 1; ++currZ) {
                float f1 = 0.0F;

                if (canPlaceBlockOn(world.getBlock(currX, y - 1, currZ))) { //if ground blocks around wormwood can be placed on
                    f1 = 1.0F;
                }

                if (currX != x || currZ != z) { //if you're not checking next to you? | checking corners
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (blocksCorners || blocksX && blocksZ) { //if wormwood on corner or wormwood adjacent
            f /= 2.0F;
        }

        return f;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 9) {
            meta = 9;
        }

        return this.textures[meta];
    }

    @Override
    public int getRenderType() {
        return 6;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
        super.dropBlockAsItemWithChance(world, x, y, z, meta, chance, 0);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) { //canFertilize
        return world.getBlockMetadata(x, y, z) <= 7;
    }

    @Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z) { //shouldFertilize
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textures = new IIcon[10];

        for (int i = 0; i < this.textures.length; i++) {

            if (i == 8)
                this.textures[i] = iconRegister.registerIcon("subsistence:plants/crops_7_dry_1");
            else if (i == 9)
                this.textures[i] = iconRegister.registerIcon("subsistence:plants/crops_7_dry_2");
            else
                this.textures[i] = iconRegister.registerIcon("subsistence:plants/crops_" + i);
        }
    }

    @Override
    public void func_149853_b(World world, Random random, int x, int y, int z) { //fertilize
        int l = world.getBlockMetadata(x, y, z) + MathHelper.getRandomIntegerInRange(random, 2, 4);

        if (l > 7) {
            l = 7;
        }

        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> ret = Lists.newArrayList();
        Random rand = new Random();

        if (meta >= 9) { //dry
            if (this.lessThanOneDropChance(rand)) { //0-1 twine
                ret.add(new ItemStack(SubsistenceItems.component, 1, 0)); //twine
            }
            if (this.lessThanOneDropChance(rand)) { //0-1 sap
                ret.add(new ItemStack(SubsistenceItems.component, 1, 3)); //sap
            }
            if (this.lessThanOneDropChance(rand)) { //0-1 leaves
                ret.add(new ItemStack(SubsistenceItems.component,1,2)); //leaves
            }
            ret.add(new ItemStack(SubsistenceItems.seeds, 1, 2)); //always 1 wormwood seed
            if (rand.nextFloat() <= 0.1F) {
                ret.add(new ItemStack(SubsistenceItems.seeds, 1, 2)); //maybe an extra wormwood seed
            }
        } else if (meta >= 7 && meta < 9) { //fully grown
            if (this.lessThanOneDropChance(rand)) { //0-1 twine
                ret.add(new ItemStack(SubsistenceItems.component, 1, 0)); //twine
            }
            ret.add(new ItemStack(SubsistenceItems.seeds, 1, 2)); //always 1 wormwood seed
            if (rand.nextFloat() <= 0.1F) {
                ret.add(new ItemStack(SubsistenceItems.seeds, 1, 2)); //maybe an extra wormwood seed
            }
        } else { //immature
            ret.add(new ItemStack(SubsistenceItems.seeds, 1, 2)); //always 1 wormwood seed
        }

        // sanitize drops, just in case
        List<ItemStack> drops = Lists.newArrayList();
        for (ItemStack stack : ret) {
            drops.add(ItemHelper.sanitizeStack(stack));
        }
        return (ArrayList<ItemStack>) drops;
    }

    private boolean lessThanOneDropChance(Random rand) {
        return rand.nextFloat() <= 0.5F;
    }
}
