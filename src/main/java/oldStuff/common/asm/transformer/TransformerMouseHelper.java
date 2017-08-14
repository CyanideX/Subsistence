package oldStuff.common.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import oldStuff.common.asm.handler.StaticMethods;

/**
 * @author dmillerw
 */
public class TransformerMouseHelper extends CoreTransformer {

    public static final String INVOKE_TARGET_CLASS_NAME = StaticMethods.class.getName().replace(".", "/");

    public TransformerMouseHelper() {
        mappings.put("mouseXYChange", "func_74374_c");
    }

    @Override
    public String getApplicableClass() {
        return "net.minecraft.util.MouseHelper";
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.contains(getMappedName("mouseXYChange"))) {
                methodNode.instructions.clear();
                methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                final String desc = obfuscated ? "(Lbbg;)V" : "(Lnet/minecraft/util/MouseHelper;)V";
                methodNode.instructions.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        INVOKE_TARGET_CLASS_NAME,
                        "updateMouse",
                        "(Lnet/minecraft/util/MouseHelper;)V",
                        false
                ));
                methodNode.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
