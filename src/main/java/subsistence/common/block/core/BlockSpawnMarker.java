package subsistence.common.block.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.core.data.WorldDataSpawnPosition;
import subsistence.common.tile.misc.TileSpawnMarker;
import subsistence.common.util.EntityHelper;

import java.util.Random;

/**
 * @author dmillerw
 */
public class BlockSpawnMarker extends BlockContainer {

    public static ChunkCoordinates getSpawnPosition(World world) {
        if (world.perWorldStorage.loadData(WorldDataSpawnPosition.class, "subsistence:spawn_position") == null) {
            world.perWorldStorage.setData("subsistence:spawn_position", new WorldDataSpawnPosition());
        }
        return ((WorldDataSpawnPosition) world.perWorldStorage.loadData(WorldDataSpawnPosition.class, "subsistence:spawn_position")).spawn;
    }

    public static void setSpawnPosition(World world, ChunkCoordinates chunkCoordinates) {
        WorldDataSpawnPosition spawnPosition = new WorldDataSpawnPosition();
        spawnPosition.spawn = chunkCoordinates;

        world.perWorldStorage.setData("subsistence:spawn_position", spawnPosition);
    }

    public BlockSpawnMarker() {
        super(Material.rock);

        setHardness(2F);
        setResistance(100F);
        setLightLevel(0.5F);
        setBlockBounds(0.25F, 0, 0.25F, 0.75F, 0.5F, 0.75F);
        setCreativeTab(SubsistenceCreativeTab.BLOCKS.get());
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote && world.provider.dimensionId == -1) {
            setSpawnPosition(world, BlockBed.func_149977_a(world, x, y, z, 0));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        world.setBlockMetadataWithNotify(x, y, z, EntityHelper.get2DRotation(entity).ordinal(), 3);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.quartz_block.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        double d0 = (double) ((float) x + 0.5F);
        double d1 = (double) ((float) y + 0.3F);
        double d2 = (double) ((float) z + 0.5F);

        world.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileSpawnMarker();
    }
}
