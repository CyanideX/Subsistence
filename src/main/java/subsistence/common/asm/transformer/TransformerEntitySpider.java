package subsistence.common.asm.transformer;

import com.google.common.collect.Maps;
import subsistence.common.asm.handler.EntitySpiderMethodHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Map;

/**
 * @author dmillerw
 */
public class TransformerEntitySpider implements IClassTransformer {

    public static final String TARGET_CLASS_NAME = "net.minecraft.entity.monster.EntitySpider";
    public static final String INVOKE_TARGET_CLASS_NAME = EntitySpiderMethodHandler.class.getName().replace(".", "/");

    public Map<String, String> mappings = Maps.newHashMap();

    public boolean obfuscated;

    public TransformerEntitySpider() {
        mappings.put("isAIEnabled", "func_70650_aV");
        mappings.put("findPlayerToAttack", "func_70782_k");
    }

    @Override
    public byte[] transform(String name, String deobfName, byte[] bytes) {
        if (deobfName.equals(TARGET_CLASS_NAME)) {
            this.obfuscated = !name.equals(deobfName);
            return transformEntitySpider(name, bytes);
        }
        return bytes;
    }

    private byte[] transformEntitySpider(String name, byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
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

    private String getMappedName(String name) {
        if (this.obfuscated && this.mappings.containsKey(name)) {
            return this.mappings.get(name);
        }
        return name;
    }
}
