package sootup.jimple.frontend;

import java.util.*;

public class JimpleToJavaCodeBuilder {

    private final List<JavaCode> jimpleToJavaCodeObjects = new ArrayList<>();

    public static JimpleToJavaCodeBuilder builder() {
        return new JimpleToJavaCodeBuilder();
    }

    public List<JavaCode> build() {
        return jimpleToJavaCodeObjects;
    }

    public JimpleToJavaCodeBuilder initObjects(String thisRefClassType) {
        InitObjects initObjects = new InitObjects(thisRefClassType);
        jimpleToJavaCodeObjects.add(initObjects);
        return this;
    }

    public JimpleToJavaCodeBuilder addLocal(String name, String type) {
        Local local = new Local(name, type);
        jimpleToJavaCodeObjects.add(local);
        return this;
    }

    public JimpleToJavaCodeBuilder addAssignStmt(String localName, String value) {
        AssignStmt assignStmt = new AssignStmt(localName, value);
        jimpleToJavaCodeObjects.add(assignStmt);
        return this;
    }

    public JimpleToJavaCodeBuilder addIdentityStmt(String localName, String type) {
        IdentityStmt identityStmt = new IdentityStmt(localName, type);
        jimpleToJavaCodeObjects.add(identityStmt);
        return this;
    }

    // Base JavaObject interface for all elements
    public interface JavaCode {
        String generateCode();
    }

    // Represents a local variable
    public static class Local implements JavaCode {
        private final String name;
        private final String type;

        public Local(String name, String type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String generateCode() {
            return "Local " + name + " = JavaJimple.newLocal(\"" + name + "\", factory.getClassType(\"" + type + "\"));";
        }
    }

    // Represents an identity statement
    public static class IdentityStmt implements JavaCode {
        private final String localName;
        private final String type;

        public IdentityStmt(String localName, String type) {
            this.localName = localName;
            this.type = type;
        }

        @Override
        public String generateCode() {
            return "FallsThroughStmt startingStmt = JavaJimple.newIdentityStmt(" +
                    localName + ", JavaJimple.newThisRef(factory.getClassType(\"" + type + "\")), noStmtPositionInfo);";
        }
    }

    // Represents an assignment statement
    public static class AssignStmt implements JavaCode {
        private final String localName;
        private final String value;

        public AssignStmt(String localName, String value) {
            this.localName = localName;
            this.value = value;
        }

        @Override
        public String generateCode() {
            return "FallsThroughStmt stmt = JavaJimple.newAssignStmt(" +
                    localName + ", " + value + ", noStmtPositionInfo);";
        }
    }

    private static class InitObjects implements JavaCode {

        private final String thisRefClassType;

        private InitObjects(String thisRefClassType) {
            this.thisRefClassType = thisRefClassType;
        }

        @Override
        public String generateCode() {
            return "JavaIdentifierFactory factory = JavaIdentifierFactory.getInstance();\n" +
                    "StmtPositionInfo noStmtPositionInfo = StmtPositionInfo.getNoStmtPositionInfo();\n" +
                    "IdentityRef identityRef = JavaJimple.newThisRef(factory.getClassType(\"" + thisRefClassType + "\");";
        }
    }
}
