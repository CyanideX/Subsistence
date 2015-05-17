package subsistence.common.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import subsistence.common.asm.handler.EntitySpiderMethodHandler;

/**
 * @author dmillerw
 */
public class TransformerEntitySpider extends CoreTransformer {

    public static final String TARGET_CLASS_NAME = "net.minecraft.entity.monster.EntitySpider";
    public static final String INVOKE_TARGET_CLASS_NAME = EntitySpiderMethodHandler.class.getName().replace(".", "/");

    public TransformerEntitySpider() {
        mappings.put("isAIEnabled", "func_70650_aV");
        mappings.put("findPlayerToAttack", "func_70782_k");
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

        // Modify findPlayerToAttack
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(getMappedName("findPlayerToAttack"))) {
                methodNode.instructions.clear();
                methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, INVOKE_TARGET_CLASS_NAME, "findPlayerToAttack", "(Lnet/minecraft/entity/monster/EntitySpider;)Lnet/minecraft/entity/player/EntityPlayer;", false));
                methodNode.instructions.add(new InsnNode(Opcodes.ARETURN));
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

//        classNode.accept(new TraceClassVisitor(new PrintWriter(System.out)));

        return classWriter.toByteArray();
    }
}
