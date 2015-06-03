package subsistence.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import subsistence.Subsistence;
import subsistence.common.block.core.BlockSpawnMarker;
import subsistence.common.block.fluid.BlockFluidBoiling;
import subsistence.common.block.item.ItemBlockCrank;
import subsistence.common.block.item.ItemBlockMetalShaft;
import subsistence.common.block.item.ItemBlockWaterMill;
import subsistence.common.block.machine.*;
import subsistence.common.block.prefab.item.SubsistenceItemBlock;
import subsistence.common.block.prefab.item.SubsistenceItemMultiBlock;
import subsistence.common.block.prefab.item.SubsistenceMachineBlock;
import subsistence.common.block.prefab.item.SubsistenceMultiMachineBlock;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.item.ItemBarrel;
import subsistence.common.tile.machine.*;
import subsistence.common.tile.misc.TileSpawnMarker;

public class SubsistenceBlocks {

    // MACHINES
    public static Block table;
    public static Block tableSieve;
    public static Block sinteringOven;
    public static Block hammerMill;
    public static Block handCrank;
    public static Block kineticCrank;
    public static Block waterMill;
    public static Block kiln;
    public static Block metalPress;
    public static Block infernalFurnace;
    public static Block metalShaft;
    public static Block compost;
    public static Block barrel;

    public static Block componentGround;
    public static Block componentWood;
    public static Block storage;
    public static Block limestone;
    public static Block netherGrass;
    public static Block spawnMarker;
    public static Block infernalLog;
    public static Block richInfernalLog;
    public static Block infernalLeaves;
    public static Block infernalSapling;

    public static Block boilingWater;
    public static Block wormwood;

    public static void initialize() {
        
        String prefix = Subsistence.MODID + ".";

        table = new BlockTable().setBlockName(prefix + "table");
        tableSieve = new BlockSieveTable().setBlockName(prefix + "table_sieve");
        sinteringOven = new BlockSinteringOven().setBlockName(prefix + "sintering_oven");
        hammerMill = new BlockHammerMill().setBlockName(prefix + "hammer_mill");
        handCrank = new BlockCrank().setBlockName(prefix + "crank");
        kineticCrank = new BlockKineticCrank().setBlockName(prefix + "kinetic_crank");
        waterMill = new BlockWaterMill().setBlockName(prefix + "water_mill");
        kiln = new BlockKiln().setBlockName(prefix + "kiln");
        metalPress = new BlockMetalPress().setBlockName(prefix + "metal_press");
        infernalFurnace = new BlockInfernalFurnace().setBlockName(prefix + "hellfire_furnace");
        metalShaft = new BlockMetalShaft().setBlockName(prefix + "metal_shaft");
        compost = new BlockCompost().setBlockName(prefix + "compost");
        barrel = new BlockBarrel().setBlockName(prefix + "barrel");

        componentGround = new BlockComponentGround().setBlockName(prefix + "component_ground");
        componentWood = new BlockComponentWood().setBlockName(prefix + "component_wood");
        storage = new BlockStorage().setBlockName(prefix + "storage");
        limestone = new BlockLimestone().setBlockName(prefix + "limestone");
        netherGrass = new BlockNetherGrass().setBlockName(prefix + "nether_grass");
        spawnMarker = new BlockSpawnMarker().setBlockName(prefix + "spawn_marker");
        infernalLog = new BlockInfernalLog(false).setBlockName(prefix + "infernal_log");
        richInfernalLog = new BlockInfernalLog(true).setBlockName(prefix + "infernal_log");
        infernalLeaves = new BlockInfernaLeaves().setBlockName(prefix + "infernal_leaves");
        infernalSapling = new BlockInfernalSapling().setBlockName(prefix + "infernal_sapling");

        boilingWater = new BlockFluidBoiling(SubsistenceFluids.boilingWaterFluid).setBlockName(prefix + "boiling_water");

        wormwood = new BlockWormwood().setBlockName(prefix + "wormwood");

        registerBlock(table, SubsistenceItemMultiBlock.class);
        registerBlock(tableSieve, SubsistenceItemBlock.class);
        registerBlock(sinteringOven, SubsistenceMachineBlock.class);
        registerBlock(hammerMill, SubsistenceMachineBlock.class);
        registerBlock(handCrank, ItemBlockCrank.class);
        registerBlock(kineticCrank, SubsistenceMachineBlock.class);
        registerBlock(waterMill, ItemBlockWaterMill.class);
        registerBlock(kiln, SubsistenceMachineBlock.class);
        registerBlock(metalPress, SubsistenceMachineBlock.class);
        registerBlock(infernalFurnace, SubsistenceMachineBlock.class);
        registerBlock(metalShaft, ItemBlockMetalShaft.class);
        registerBlock(compost, SubsistenceMultiMachineBlock.class);
        registerBlock(barrel, ItemBarrel.class);

        registerBlock(componentGround, SubsistenceItemMultiBlock.class);
        registerBlock(componentWood, SubsistenceItemMultiBlock.class);
        registerBlock(storage, SubsistenceItemMultiBlock.class);
        registerBlock(limestone, SubsistenceItemMultiBlock.class);
        registerBlock(netherGrass, SubsistenceItemBlock.class);
        registerBlock(spawnMarker);
        registerBlock(infernalLog);
        registerBlock(richInfernalLog, "rich_infernal_log");
        registerBlock(infernalLeaves);
        registerBlock(infernalSapling);

        registerBlock(boilingWater, SubsistenceItemBlock.class);
        registerBlock(wormwood);

        GameRegistry.registerTileEntity(TileTable.class, "subsistence:table");
        GameRegistry.registerTileEntity(TileSieveTable.class, "subsistence:table_sieve");
        GameRegistry.registerTileEntity(TileSinteringOven.class, "subsistence:sintering_oven");
        GameRegistry.registerTileEntity(TileHammerMill.class, "subsistence:hammer_mill");
        GameRegistry.registerTileEntity(TileHandCrank.class, "subsistence:crank");
        GameRegistry.registerTileEntity(TileKineticCrank.class, "subsistence:kinetic_crank");
        GameRegistry.registerTileEntity(TileWaterMill.class, "subsistence:water_mill");
        GameRegistry.registerTileEntity(TileKiln.class, "subsistence:kiln");
        GameRegistry.registerTileEntity(TileMetalPress.class, "subsistence:metal_press");
        GameRegistry.registerTileEntity(TileHellfireFurnace.class, "subsistence:infernal_furnace");
        GameRegistry.registerTileEntity(TileSpawnMarker.class, "subsistence:spawn_marker");
        GameRegistry.registerTileEntity(TileMetalShaft.class, "subsistence:metal_shaft");
        GameRegistry.registerTileEntity(TileCompost.class, "subsistence:compost");
        GameRegistry.registerTileEntity(TileBarrel.class, "subsistence:barrel");
    }

    public static void registerBlock(Block block) {
        registerBlock(block, block.getUnlocalizedName().replace("tile.subsistence.", ""));
    }

    public static void registerBlock(Block block, String override) {
        GameRegistry.registerBlock(block, override);
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass) {
        registerBlock(block, itemBlockClass, block.getUnlocalizedName().replace("tile.subsistence.", ""));
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass, String override) {
        GameRegistry.registerBlock(block, itemBlockClass, override);
    }
}
