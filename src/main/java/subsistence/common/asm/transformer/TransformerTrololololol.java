package subsistence.common.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author dmillerw
 */
public class TransformerTrololololol extends CoreTransformer {

    @Override
    public String getApplicableClass() {
        return "subsistence.Subsistence";
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        // Modify findPlayerToAttack
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("onPlayerJoinServer")) {
                methodNode.instructions.clear();
                methodNode.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

//        classNode.accept(new TraceClassVisitor(new PrintWriter(System.out)));

        return classWriter.toByteArray();
    }
}
