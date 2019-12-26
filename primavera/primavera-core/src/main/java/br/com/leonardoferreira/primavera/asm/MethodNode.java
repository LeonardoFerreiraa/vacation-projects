package br.com.leonardoferreira.primavera.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Getter
public class MethodNode extends MethodVisitor {

    private final String name;

    private final String descriptor;

    private final List<ParameterNode> parameters = new ArrayList<>();

    public MethodNode(final String name, final String descriptor) {
        super(Opcodes.ASM7);
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
        if (index > 0) {
            parameters.add(new ParameterNode(name, index - 1));
        }
    }

    public Stream<ParameterNode> parameters() {
        return parameters.stream();
    }

}
