package subsistence.common.lib.stack;

import net.minecraftforge.oredict.OreDictionary;

public class OreStackWrapper extends GenericStackWrapper<String> {

    public OreStackWrapper(String contents) {
        super(contents);
    }

    @Override
    public GenericStackWrapper<String> copy() {
        return new OreStackWrapper(contents);
    }

    @Override
    public boolean equals(GenericStackWrapper<String> wrapper) {
        return !(wrapper.contents.isEmpty() || OreDictionary.getOres(wrapper.contents).isEmpty()) && wrapper.contents.equals(contents);

    }

    @Override
    public int hashCode() {
        return contents.hashCode();
    }
}
