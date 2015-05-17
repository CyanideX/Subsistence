package subsistence.common.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * @author dmillerw
 */
public class HowAboutYouTransformYourSassyAssRightOutOfHereDylan extends CoreTransformer {

    public HowAboutYouTransformYourSassyAssRightOutOfHereDylan() {
        mappings.put("getDisplayName", "func_96678_d");
    }

    @Override
    public String getApplicableClass() {
        return "subsistence.Subsistence";
    }

    @Override
    public byte[] transform(byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        boolean replace = false;

        // Modify findPlayerToAttack
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("onPlayerJoinServer")) {
                Iterator<AbstractInsnNode> nodes = methodNode.instructions.iterator();
                while (nodes.hasNext()) {
                    AbstractInsnNode node = nodes.next();

                    if (node instanceof MethodInsnNode)
                        if ("getDisplayName".equals(getMappedName(((MethodInsnNode) node).name))) {
                            replace = true;
                        }
                    if (node instanceof LdcInsnNode && replace) {
                        ((LdcInsnNode) node).cst = "MattDahEpic";
                        replace = false;
                    }
                }
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

//        classNode.accept(new TraceClassVisitor(new PrintWriter(System.out)));

        return classWriter.toByteArray();
    }
}
