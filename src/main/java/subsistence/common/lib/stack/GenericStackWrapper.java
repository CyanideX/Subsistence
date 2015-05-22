package subsistence.common.lib.stack;

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

        return contents.getClass().isInstance(((GenericStackWrapper) obj).contents) && ((GenericStackWrapper<T>) obj).equals(this);

    }

    public abstract GenericStackWrapper<T> copy();

    public abstract boolean equals(GenericStackWrapper<T> wrapper);

    public abstract int hashCode();
}
