package subsistence.common.util;

import java.lang.reflect.Array;

/**
 * @author dmillerw
 */
public class ArrayHelper {

    /**
     * Takes an array and returns an int array containing all the indexes contained in that array
     */
    public static int[] getArrayIndexes(Object[] array) {
        int[] returnArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            returnArray[i] = i;
        }
        return returnArray;
    }

    public static <T> T safeGetArrayIndex(T[] array, int index) {
        return array[index % array.length];
    }

    public static <T> T[] handleGenericArray(Object object, Class<T> type) {
        if (object == null)
            return null;

        T[] array = (T[]) Array.newInstance(type, ((Object[]) object).length);
        for (int i = 0; i < ((Object[]) object).length; i++) {
            array[i] = (T) ((Object[]) object)[i];
        }
        return array;
    }
}
