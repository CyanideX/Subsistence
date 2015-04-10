package com.subsistence.common.util;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author dmillerw
 */
public class SubsistenceReflectionHelper {

    private static String[] GET_FLOW_VECTOR = new String[]{"getFlowVector", "func_149800_f", "f"};
    private static String[] GET_EFFECTIVE_FLOW_DECAY = new String[]{"getEffectiveFlowDecay", "func_149800_f", "f"};
    private static Method getFlowVector;
    private static Method getEffectiveFlowDecay;

    public static Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
        try {
            if (getFlowVector == null) {
                getFlowVector = ReflectionHelper.findMethod(BlockLiquid.class, Blocks.flowing_water, GET_FLOW_VECTOR, IBlockAccess.class, int.class, int.class, int.class);
            }
            return (Vec3) getFlowVector.invoke(world.getBlock(x, y, z), world, x, y, z);
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
            return (Integer) getEffectiveFlowDecay.invoke(world.getBlock(x, y, z), world, x, y, z);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }
}