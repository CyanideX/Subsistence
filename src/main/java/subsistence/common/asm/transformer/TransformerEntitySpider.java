package subsistence.common.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import subsistence.common.asm.handler.StaticMethods;

public class TransformerEntitySpider extends CoreTransformer {

    public static final String TARGET_CLASS_NAME = "net.minecraft.entity.monster.EntitySpider";
    public static final String INVOKE_TARGET_CLASS_NAME = StaticMethods.class.getName().replace(".", "/");

    public TransformerEntitySpider() {
        mappings.put("findPlayerToAttack", "bR");
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
                String desc = obfuscated ? "(Lyn;)Lyz;" : "(Lnet/minecraft/entity/monster/EntitySpider;)Lnet/minecraft/entity/player/EntityPlayer;";
                methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, INVOKE_TARGET_CLASS_NAME, "findPlayerToAttack", desc, false));
                methodNode.instructions.add(new InsnNode(Opcodes.ARETURN));
            }
        }

        //COMPUTE_FRAMES is not added due to an issue with getCommonSuperClass. Refer to this for more info: http://www.minecraftforge.net/forum/index.php?topic=20911.0
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
