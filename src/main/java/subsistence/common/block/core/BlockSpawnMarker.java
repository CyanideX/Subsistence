package subsistence.common.block.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import subsistence.common.core.SubsistenceCreativeTab;
import subsistence.common.core.data.WorldDataSpawnPosition;
import subsistence.common.tile.misc.TileSpawnMarker;
import subsistence.common.util.EntityHelper;

import java.util.Random;

public class BlockSpawnMarker extends BlockContainer {

    public BlockSpawnMarker() {
        super(Material.rock);

        setBlockUnbreakable();
        setResistance(6000000.0F);
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            player.setSpawnChunk(BlockBed.func_149977_a(world, x, y, z, 0), true, player.dimension);
            player.addChatComponentMessage(new ChatComponentText("Spawn set!"));
        }
        return true;
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
