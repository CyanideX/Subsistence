package subsistence.common.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import subsistence.common.asm.handler.StaticMethods;

public class TransformBlockBOPSapling extends CoreTransformer
{
    public static final String TARGET_CLASS_NAME        = "biomesoplenty.common.blocks.BlockBOPSapling";
    public static final String INVOKE_TARGET_CLASS_NAME = StaticMethods.class.getName().replace(".", "/");

    public TransformBlockBOPSapling(){
        mappings.put("updateTick", "func_149674_a");
    }

    @Override
    public String getApplicableClass() {
        return TARGET_CLASS_NAME;
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        // Modify updateTick
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(getMappedName("updateTick")) && methodNode.desc.equals("(Lnet/minecraft/world/World;IIILjava/util/Random;)V")) {
                AbstractInsnNode firstNode = methodNode.instructions.getFirst();
                for (AbstractInsnNode insn = firstNode; insn != null; insn = insn.getNext()) {
                    if (insn.getOpcode() == Opcodes.BIPUSH) {
                        if (((IntInsnNode)insn).operand == 9) { // Finds the BIPUSH node which pushes the value 9
                            methodNode.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, INVOKE_TARGET_CLASS_NAME, "getMinimumLightLevel", "()I", false));
                            methodNode.instructions.remove(insn);
                            break;
                        }
                    }
                }
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
