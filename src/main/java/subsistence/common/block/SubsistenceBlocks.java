package subsistence.common.block;

import subsistence.common.block.core.BlockSpawnMarker;
import subsistence.common.block.item.ItemBlockCrank;
import subsistence.common.block.item.ItemBlockWaterMill;
import subsistence.common.block.machine.*;
import subsistence.common.block.prefab.item.SubsistenceItemBlock;
import subsistence.common.block.prefab.item.SubsistenceItemMultiBlock;
import subsistence.common.block.prefab.item.SubsistenceMachineBlock;
import subsistence.common.block.prefab.item.SubsistenceMultiMachineBlock;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.item.ItemBarrel;
import subsistence.common.block.fluid.BlockFluidBoiling;
import subsistence.common.block.item.ItemBlockMetalShaft;
import subsistence.common.tile.misc.TileSpawnMarker;
import cpw.mods.fml.common.registry.GameRegistry;
import subsistence.common.tile.machine.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * @author Royalixor.
 */
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

        table = new BlockTable().setBlockName("table");
        tableSieve = new BlockSieveTable().setBlockName("table_sieve");
        sinteringOven = new BlockSinteringOven().setBlockName("sintering_oven");
        hammerMill = new BlockHammerMill().setBlockName("hammer_mill");
        handCrank = new BlockCrank().setBlockName("crank");
        kineticCrank = new BlockKineticCrank().setBlockName("kinetic_crank");
        waterMill = new BlockWaterMill().setBlockName("water_mill");
        kiln = new BlockKiln().setBlockName("kiln");
        metalPress = new BlockMetalPress().setBlockName("metal_press");
        infernalFurnace = new BlockInfernalFurnace().setBlockName("hellfire_furnace");
        metalShaft = new BlockMetalShaft().setBlockName("metal_shaft");
        compost = new BlockCompost().setBlockName("compost");
        barrel = new BlockBarrel().setBlockName("barrel");

        componentGround = new BlockComponentGround().setBlockName("component_ground");
        componentWood = new BlockComponentWood().setBlockName("component_wood");
        storage = new BlockStorage().setBlockName("storage");
        limestone = new BlockLimestone().setBlockName("limestone");
        netherGrass = new BlockNetherGrass().setBlockName("nether_grass");
        spawnMarker = new BlockSpawnMarker().setBlockName("spawn_marker");
        infernalLog = new BlockInfernalLog(false).setBlockName("infernal_log");
        richInfernalLog = new BlockInfernalLog(true).setBlockName("infernal_log");
        infernalLeaves = new BlockInfernaLeaves().setBlockName("infernal_leaves");
        infernalSapling = new BlockInfernalSapling().setBlockName("infernal_sapling");

        boilingWater = new BlockFluidBoiling(SubsistenceFluids.boilingWaterFluid).setBlockName("boiling_water");

        wormwood = new BlockWormwood().setBlockName("wormwood");

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
        GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));
    }

    public static void registerBlock(Block block, String override) {
        GameRegistry.registerBlock(block, override);
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass) {
        GameRegistry.registerBlock(block, itemBlockClass, block.getUnlocalizedName().replace("tile.", ""));
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass, String override) {
        GameRegistry.registerBlock(block, itemBlockClass, override);
    }
}
