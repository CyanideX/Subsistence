package oldStuff;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import oldStuff.client.handler.ClientTimerHandler;
import oldStuff.client.model.FixedTechneModelLoader;
import oldStuff.client.render.FoliageHandler;
import oldStuff.client.render.item.*;
import oldStuff.client.render.tile.*;
import oldStuff.common.block.SubsistenceBlocks;
import oldStuff.common.item.SubsistenceItems;
import oldStuff.common.tile.machine.*;
import oldStuff.common.tile.misc.TileSpawnMarker;
import oldStuff.common.util.EventUtil;

import java.io.File;

public class ClientProxy extends CommonProxy {

    public static int renderPass;

    @Override
    public void preInit() {
        super.preInit();

        EventUtil.register(ClientTimerHandler.INSTANCE, EventUtil.Type.BOTH);

        File file = new File(SubsistenceOld.configPath + "foliage.json");
        if (file.exists()) {
            FoliageHandler.initialize(file);
        } else {
            FoliageHandler.initialize(null);
        }

        AdvancedModelLoader.registerModelHandler(new FixedTechneModelLoader());

        // TILE
        ClientRegistry.bindTileEntitySpecialRenderer(TileTable.class, new RenderTileTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSieveTable.class, new RenderTileSieveTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSinteringOven.class, new RenderTileSinteringOven());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHammerMill.class, new RenderTileHammerMill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHandCrank.class, new RenderTileCrank());
        ClientRegistry.bindTileEntitySpecialRenderer(TileKineticCrank.class, new RenderTileKineticCrank());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWaterMill.class, new RenderTileWaterMill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileKiln.class, new RenderTileKiln());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMetalPress.class, new RenderTileMetalPress());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSpawnMarker.class, new RenderTileSpawnMarker());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMetalShaft.class, new RenderTileMetalShaft());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCompost.class, new RenderTileCompost());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderTileBarrel());

        // ITEM
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.table), new RenderItemTable());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.tableSieve), new RenderItemSieveTable());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.sinteringOven), new RenderItemSinteringOven());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.hammerMill), new RenderItemHammerMill());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.kineticCrank), new RenderItemKineticCrank());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.kiln), new RenderItemKiln());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.metalPress), new RenderItemMetalPress());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.spawnMarker), new RenderItemSpawnMarker());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.compost), new RenderItemCompost());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(SubsistenceBlocks.barrel), new RenderItemBarrel());
        MinecraftForgeClient.registerItemRenderer(SubsistenceItems.barrelLid, new RenderItemBarrelLid());
    }
}
