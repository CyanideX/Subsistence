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

    private static String[] GET_FLOW_VECTOR = new String[] {"getFlowVector", "func_149800_f", "f"};

    private static Method getFlowVector;

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
}