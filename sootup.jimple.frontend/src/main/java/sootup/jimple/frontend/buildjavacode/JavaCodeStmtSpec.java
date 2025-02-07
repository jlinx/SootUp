package sootup.jimple.frontend.buildjavacode;

public class JavaCodeStmtSpec {
    private final String stmt;

    private JavaCodeStmtSpec(String stmt) {
        this.stmt = stmt;
    }

    public static JavaCodeStmtSpec identity(String variable, String type) {
        return new JavaCodeStmtSpec(variable + " := @" + type);
    }

    public static JavaCodeStmtSpec assign(String variable, String value) {
        return new JavaCodeStmtSpec(variable + " = " + value + ";");
    }

    public static JavaCodeStmtSpec ifStmt(String condition) {
        return new JavaCodeStmtSpec("if (" + condition + ") {");
    }

    public static JavaCodeStmtSpec endBlock() {
        return new JavaCodeStmtSpec("}");
    }

    public String build() {
        return stmt;
    }
}
