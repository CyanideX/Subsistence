package subsistence.blocks.prefab;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import subsistence.Subsistence;
import subsistence.util.Data;
import subsistence.util.IHasModel;

public class BlockBase extends Block implements IHasModel {
    public BlockBase(Material materialIn) {
        super(materialIn);

        super.setCreativeTab(Subsistence.creativeTab);

        Data.BLOCKS.add(this);
    }
}
