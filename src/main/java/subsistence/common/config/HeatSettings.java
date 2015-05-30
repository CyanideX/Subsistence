package subsistence.common.config;

import com.google.common.collect.Sets;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static subsistence.common.util.ItemHelper.ItemAndIntTuple;

public class HeatSettings {

    private static Set<ItemAndIntTuple> torch = Sets.newHashSet();
    private static Set<ItemAndIntTuple> fire = Sets.newHashSet();
    private static Set<ItemAndIntTuple> lava = Sets.newHashSet();

    public static class Heat {

        public ItemAndIntTuple[] torch = new ItemAndIntTuple[0];
        public ItemAndIntTuple[] fire = new ItemAndIntTuple[0];
        public ItemAndIntTuple[] lava = new ItemAndIntTuple[0];
    }

    public static void initialize(File file) {
        if (!file.exists())
            return;

        try {
            SubsistenceLogger.info("Parsing " + file.getName());
            Heat heat = JsonUtil.gson().fromJson(new FileReader(file), Heat.class);
            torch.addAll(Arrays.asList(heat.torch));
            fire.addAll(Arrays.asList(heat.fire));
            lava.addAll(Arrays.asList(heat.lava));
        } catch (IOException e) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            e.printStackTrace();
        }
    }

    public static boolean isHeatSource(World world, int x, int y, int z) {
        return isTorch(world, x, y, z) || isFire(world, x, y, z) || isLava(world, x, y, z);
    }

    public static boolean isTorch(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z))
            return false;

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        // 'cause it ain't workin' when added to heat.json
        String ident = GameData.getBlockRegistry().getNameForObject(block);
        if (ident.equals("CarpentersBlocks:blockCarpentersTorch"))
            return true;

        return block == Blocks.torch || torch.contains(new ItemAndIntTuple(Item.getItemFromBlock(block), meta));
    }

    public static boolean isFire(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z))
            return false;

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        return block == Blocks.fire || fire.contains(new ItemAndIntTuple(Item.getItemFromBlock(block), meta));
    }

    public static boolean isLava(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z))
            return false;

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        return block == Blocks.lava || block == Blocks.flowing_lava || lava.contains(new ItemAndIntTuple(Item.getItemFromBlock(block), meta));
    }
}
