package br.com.leonardoferreira.primavera.metadata.asm;

import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
import java.util.stream.Stream;
import lombok.Getter;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Getter
public class MethodMetadata extends MethodVisitor {

    private final String name;

    private final String descriptor;

    private final NativeMethodMetadata nativeMethod;

    private final PrimaveraList<MethodParameterMetadata> parameters = new PrimaveraList<>();

    public MethodMetadata(final String name, final String descriptor, final NativeMethodMetadata nativeMethod) {
        super(Opcodes.ASM7);
        this.name = name;
        this.descriptor = descriptor;
        this.nativeMethod = nativeMethod;
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
        if (nativeMethod.isStatic()) {
            parameters.add(new MethodParameterMetadata(name, index));
        } else {
            parameters.add(new MethodParameterMetadata(name, index - 1));
        }
    }

    public Stream<MethodParameterMetadata> parameters() {
        return parameters.stream();
    }

}
