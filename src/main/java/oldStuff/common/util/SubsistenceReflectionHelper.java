package oldStuff.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class SubsistenceReflectionHelper {

    private static String[] GET_FLOW_VECTOR = new String[]{"getFlowVector", "func_149800_f", "f"};
    private static String[] GET_EFFECTIVE_FLOW_DECAY = new String[]{"getEffectiveFlowDecay", "func_149798_e", "e"};
    private static Method getFlowVector;
    private static Method getEffectiveFlowDecay;
    private static Field quantaPerBlock;

    public static Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
        try {
            if (getFlowVector == null) {
                getFlowVector = ReflectionHelper.findMethod(BlockLiquid.class, Blocks.flowing_water, GET_FLOW_VECTOR, IBlockAccess.class, int.class, int.class, int.class);
            }
            return (Vec3) getFlowVector.invoke(Blocks.flowing_water, world, x, y, z);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Vec3.createVectorHelper(0, 0, 0);
    }

    public static Integer getEffectiveFlowDecay(IBlockAccess world, int x, int y, int z) {
        try {
            if (getEffectiveFlowDecay == null) {
                getEffectiveFlowDecay = ReflectionHelper.findMethod(BlockLiquid.class, Blocks.flowing_water, GET_EFFECTIVE_FLOW_DECAY, IBlockAccess.class, int.class, int.class, int.class);
            }
            return (Integer) getEffectiveFlowDecay.invoke(Blocks.flowing_water, world, x, y, z);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getQuantaPerBlock(BlockFluidBase fluid) {
        if (quantaPerBlock == null) {
            quantaPerBlock = ReflectionHelper.findField(BlockFluidBase.class, "quantaPerBlock");
        }
        try {
            return quantaPerBlock.getInt(fluid);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 8;
    }
}