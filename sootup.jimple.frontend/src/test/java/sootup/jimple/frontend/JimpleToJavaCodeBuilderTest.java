package sootup.jimple.frontend;

import org.junit.jupiter.api.Test;

import java.util.List;

public class JimpleToJavaCodeBuilderTest {

    @Test
    public void testJimpleToJavaCode() {
        // Example Input
        String input = "r0 := @this: Test;\n" +
                "i1 = 5;\n" +
                "i2 = 0;";

        // Using the builder
        List<JimpleToJavaCodeBuilder.JavaCode> javaObjects = JimpleToJavaCodeBuilder.builder()
                .initObjects("Test")
                .addLocal("r0", "Test")
                .addIdentityStmt("r0", "Test")
                .addLocal("i1", "int")
                .addAssignStmt("i1", "IntConstant.getInstance(5)")
                .addLocal("i2", "int")
                .addAssignStmt("i2", "IntConstant.getInstance(0)")
                .build();

        // Generate the code
        for (JimpleToJavaCodeBuilder.JavaCode javaCode : javaObjects) {
            System.out.println(javaCode.generateCode());
        }
    }

    @Test
    public void testJimpleToJavaCode2() {
        String input = "\"r0 := @this: AbstractClass\",\n" +
                "            \"r1 = new AbstractClass\",\n" +
                "            \"specialinvoke r1.<AbstractClass: void <init>()>()\",\n" +
                "            \"virtualinvoke r1.<A: void a()>()\",\n" +
                "            \"return\"";


    }
}
