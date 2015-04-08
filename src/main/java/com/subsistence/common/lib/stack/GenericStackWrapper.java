package com.subsistence.common.lib.stack;

/**
 * @author dmillerw
 */
public abstract class GenericStackWrapper<T> {

    public final T contents;

    public GenericStackWrapper(T contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GenericStackWrapper)) {
            return false;
        }

        if (!(contents.getClass().isInstance(((GenericStackWrapper) obj).contents))) {
            return false;
        }

        return ((GenericStackWrapper<T>) obj).equals(this);
    }

    public abstract GenericStackWrapper<T> copy();

    public abstract boolean equals(GenericStackWrapper<T> wrapper);

    public abstract int hashCode();
}
