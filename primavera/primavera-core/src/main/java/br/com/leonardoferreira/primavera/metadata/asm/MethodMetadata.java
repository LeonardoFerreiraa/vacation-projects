package br.com.leonardoferreira.primavera.metadata.asm;

import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Getter
@ToString
public class MethodMetadata extends MethodVisitor {

    private final String name;

    private final Type[] parameterTypes;

    private final PrimaveraList<MethodParameterMetadata> parameters = new PrimaveraList<>();

    public MethodMetadata(final String name, final String descriptor) {
        super(Opcodes.ASM7);

        this.name = name;
        this.parameterTypes = Type.getArgumentTypes(descriptor);
    }

    @Override
    public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
        parameters.add(new MethodParameterMetadata(name, index));
    }

    public Stream<MethodParameterMetadata> parameters() {
        return parameters.stream();
    }

}
