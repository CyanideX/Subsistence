package oldStuff.common.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import oldStuff.common.block.SubsistenceBlocks;
import oldStuff.common.item.resource.ItemResource;
import cpw.mods.fml.common.registry.GameRegistry;

public class SubsistenceItems {

    // HAMMERS
    public static ItemHammer hammerWood;
    public static ItemHammer hammerStone;
    public static ItemHammer hammerIron;
    public static ItemHammer hammerSteel;
    public static ItemHammer hammerDiamond;

    // TOOLS
    public static Item net;
    public static Item handSieve;
    public static Item component;
    public static Item tray;
    public static Item hatchet;

    // RESOURCES
    public static Item resourceChunk;
    public static Item resourceClump;
    public static Item resourceDust;
    public static Item resourceFlake;
    public static Item resourceIngot;
    public static Item resourceNugget;
    public static Item resourcePebble;

    // OTHER
    public static Item seeds;
    public static Item cosmetic;
    public static Item boilingBucket;
    public static Item barrelLid;
    public static Item woodenBucket;

    public static void initialize() {
        hammerWood = new ItemHammer(64, "wood", "plankWood");
        hammerStone = new ItemHammer(128, "stone", "cobblestone");
        hammerIron = new ItemHammer(160, "iron", "ingotIron");
        hammerSteel = new ItemHammer(320, "steel", "ingotSteel");
        hammerDiamond = new ItemHammer(640, "diamond", Items.diamond);
        registerItem(hammerWood);
        registerItem(hammerStone);
        registerItem(hammerIron);
        registerItem(hammerSteel);
        registerItem(hammerDiamond);

        net = new ItemNet();
        register(net, "net");
        handSieve = new ItemHandSieve();
        register(handSieve, "hand_sieve");
        component = new ItemComponent();
        register(component, "component_item");
        tray = new ItemTray();
        register(tray, "tray");
        hatchet = new ItemHatchet();
        register(hatchet, "hatchet");

        resourceChunk = new ItemResource().setType("Chunk");
        resourceClump = new ItemResource().setType("Clump");
        resourceDust = new ItemResource().setType("Dust");
        resourceFlake = new ItemResource().setType("Flakes");
        resourceIngot = new ItemResource().setType("Ingot");
        resourceNugget = new ItemResource().setType("Nugget");
        resourcePebble = new ItemResource().setType("Pebbles");
        registerItem(resourceChunk);
        registerItem(resourceClump);
        registerItem(resourceDust);
        registerItem(resourceFlake);
        registerItem(resourceIngot);
        registerItem(resourceNugget);
        registerItem(resourcePebble);

        seeds = new ItemSeeds();
        cosmetic = new ItemCosmetic();
        boilingBucket = new ItemBoilingBucket(SubsistenceBlocks.boilingWater).setContainerItem(Items.bucket);
        barrelLid = new ItemBarrelLid();
        woodenBucket = new ItemWoodenBucket(null, "empty");
        register(seeds, "seeds");
        register(cosmetic, "cosmetic");
        register(boilingBucket, "boiling_bucket");
        register(barrelLid, "barrel_lid");
        register(woodenBucket, "wooden_bucket");
    }

    public static void register(Item item, String reg) {
        registerItem(item, reg);
        item.setUnlocalizedName(reg);
    }
    
    public static void registerItem(Item item, String reg) {
        GameRegistry.registerItem(item, reg);
    }
    
    public static void registerItem(Item item) {
        registerItem(item, item.getUnlocalizedName().replace("item.subsistence.", ""));
    }
}
