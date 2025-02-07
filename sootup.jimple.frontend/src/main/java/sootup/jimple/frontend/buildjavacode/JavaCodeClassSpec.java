package sootup.jimple.frontend.buildjavacode;

import java.util.ArrayList;
import java.util.List;

public class JavaCodeClassSpec {
    private final List<String> modifiers = new ArrayList<>();
    private final List<JavaCodeMethodSpec> methods = new ArrayList<>();

    public JavaCodeClassSpec addModifier(String modifier) {
        if (modifier != null && !modifier.isEmpty()) {
            modifiers.add("MethodModifier." + modifier.toUpperCase());
        }
        return this;
    }

    public JavaCodeClassSpec addMethod(JavaCodeMethodSpec javaCodeMethodSpec) {
        methods.add(javaCodeMethodSpec);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        String modifiersString = String.join(", ", modifiers);
        builder.append("JavaSootClass mainClass = new JavaSootClass(new OverridingJavaClassSource(")
                .append("new EagerInputLocation(),")
                .append("null,")
                .append("view.getIdentifierFactory().getClassType(\"dummyMain\"),")
                .append("null,")
                .append("Collections.emptySet(),")
                .append("null,")
                .append("Collections.emptySet(),")
                .append("Collections.singleton(dummyMainMethod),")
                .append("NoPositionInformation.getInstance(),")
                .append("EnumSet.of(").append(modifiersString).append("),")
                .append("Collections.emptyList(),")
                .append("Collections.emptyList(),")
                .append("Collections.emptyList()),")
                .append("SourceType.Application);");

        return builder.toString();
    }
}

