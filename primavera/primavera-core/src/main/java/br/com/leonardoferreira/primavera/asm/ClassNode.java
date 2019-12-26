package br.com.leonardoferreira.primavera.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Getter
public class ClassNode extends ClassVisitor {

    private String name;

    private List<MethodNode> methods = new ArrayList<>();

    public ClassNode() {
        super(Opcodes.ASM7);
    }

    public static ClassNode fromClass(final Class<?> clazz) {
        try {
            final ClassNode classNode = new ClassNode();

            final ClassReader reader = new ClassReader(clazz.getName());
            reader.accept(classNode, ClassReader.SKIP_FRAMES);

            return classNode;
        } catch (final Exception e) {
            throw new RuntimeException("Unable to convert to CtClass", e);
        }
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        if (name != null) {
            this.name = name.replaceAll("/", ".");
        }
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        final MethodNode methodNode = new MethodNode(name, descriptor);
        methods.add(methodNode);

        return methodNode;
    }

    public Stream<MethodNode> methods() {
        return methods.stream();
    }
}
