package subsistence.common.asm.transformer;

import com.google.common.collect.Sets;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Set;

public class TransformerDelegate implements IClassTransformer {

    private static Set<CoreTransformer> transformers = Sets.newHashSet();

    static {
        transformers.add(new TransformerEntitySpider());
        transformers.add(new TransformBlockBOPSapling());
        transformers.add(new TransformerMouseHelper());
    }

    public static byte[] handle(String obfName, String deobfName, byte[] data) {
        for (CoreTransformer transformer : transformers) {
            if (transformer.canHandle(obfName, deobfName)) {
                return transformer.transform(obfName, deobfName, data);
            }
        }
        return data;
    }

    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        return handle(s, s1, bytes);
    }
}