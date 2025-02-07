package sootup.jimple.frontend.buildjavacode;

import java.util.ArrayList;
import java.util.List;

public class JavaCodeMethodSpec {
    private static StringBuilder javaCodeMethodSignature = new StringBuilder();
    private final List<JavaCodeStmtSpec> statements = new ArrayList<>();
    private final List<String> modifiers = new ArrayList<>();

    public JavaCodeMethodSpec addMethodSignature(String clsSig, String methodName, String returnType, String parameters) {
        javaCodeMethodSignature.append("MethodSignature methodSignature = view.getIdentifierFactory().getMethodSignature(")
                .append("\"").append(clsSig).append("\"")
                .append("\"").append(methodName).append("\"")
                .append("\"").append(returnType).append("\"")
                .append("\"").append(parameters).append("\"")
                .append(");");
        return this;
    }

    public JavaCodeMethodSpec addModifier(String modifier) {
        if (modifier != null && !modifier.isEmpty()) {
            modifiers.add("MethodModifier." + modifier.toUpperCase());
        }
        return this;
    }

    public JavaCodeMethodSpec addStatement(JavaCodeStmtSpec statement) {
        statements.add(statement);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        String modifiersString = String.join(", ", modifiers);

        builder.append("JavaSootMethod dummyMainMethod = new JavaSootMethod(")
                .append("new OverridingBodySource(methodSignature, body),")
                .append("methodSignature,")
                .append("EnumSet.of(").append(modifiersString).append("),")
                .append("Collections.emptyList(),")
                .append("Collections.emptyList(),")
                .append("NoPositionInformation.getInstance());");

        return builder.toString();
    }
}
