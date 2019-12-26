package br.com.leonardoferreira.primavera.metadata.asm;

import br.com.leonardoferreira.primavera.collection.PrimaveraCollectionCollector;
import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
import br.com.leonardoferreira.primavera.util.Try;
import java.io.InputStream;
import java.util.stream.Stream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassMetadata extends ClassVisitor {

    private Class<?> targetClass;

    private PrimaveraList<MethodMetadata> methods = new PrimaveraList<>();

    private PrimaveraList<NativeMethodMetadata> nativeMethods;

    private ClassMetadata() {
        super(Opcodes.ASM7);
    }

    public static ClassMetadata fromClass(final Class<?> clazz) {
        try {
            final ClassMetadata classMetadata = new ClassMetadata();

            final ClassReader reader = new ClassReader(clazz.getName());
            reader.accept(classMetadata, ClassReader.SKIP_FRAMES);

            return classMetadata;
        } catch (final Exception e) {
            throw new RuntimeException("Unable to convert to ClassMetadata", e);
        }
    }

    public static ClassMetadata fromInputStream(final InputStream inputStream) {
        try {
            final ClassMetadata classMetadata = new ClassMetadata();

            final ClassReader reader = new ClassReader(inputStream);
            reader.accept(classMetadata, ClassReader.SKIP_FRAMES);

            return classMetadata;
        } catch (final Exception e) {
            throw new RuntimeException("Unable to convert to ClassMetadata", e);
        }
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        this.targetClass = Try.rethrowAsRuntime(() -> Class.forName(name.replaceAll("/", ".")));
        this.nativeMethods = Stream.concat(Stream.of(targetClass.getMethods()), Stream.of(targetClass.getConstructors()))
                .map(NativeMethodMetadata::fromExecutable)
                .collect(PrimaveraCollectionCollector.toList());
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        return methods.add(new MethodMetadata(
                name,
                descriptor,
                nativeMethods.find(s -> s.matches(name, Type.getArgumentTypes(descriptor))).orElseThrow()
        ));
    }

    public Stream<MethodMetadata> methods() {
        return methods.stream();
    }

    public Class<?> toClass() {
        return targetClass;
    }

}
