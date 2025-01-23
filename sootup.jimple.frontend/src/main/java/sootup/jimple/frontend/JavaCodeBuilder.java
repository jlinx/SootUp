package sootup.jimple.frontend;

import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.constant.IntConstant;
import sootup.core.jimple.common.stmt.JAssignStmt;
import sootup.core.types.Type;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JavaCodeBuilder {

    private final List<String> javaCodeObjects = new LinkedList<>();
    Map<String, String> varAndType = new HashMap<>();

    public JavaCodeBuilder() {
        this.initJavaCode();
    }

    public List<String> getJavaCodeObjects() {
        return javaCodeObjects;
    }

    public void initJavaCode() {
        javaCodeObjects.add(String.format("JavaIdentifierFactory %s = JavaIdentifierFactory.getInstance();", "factory"));
        javaCodeObjects.add(String.format("StmtPositionInfo %s = StmtPositionInfo.getNoStmtPositionInfo();", "noStmtPositionInfo"));
    }

    public void addThisRef(Local leftOp, Type classType) {
        javaCodeObjects.add(String.format("IdentityRef %s = JavaJimple.newThisRef(factory.getClassType(\"%s\"));", leftOp.getName(), classType));
    }

    public void addLocal(Local value) {
        javaCodeObjects.add(String.format("Local %s = JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"));", value, value.getName(), value.getType()));
    }

    public void addAssignment(JAssignStmt stmt) {
        String typeString = "";
        if (stmt.getRightOp() instanceof IntConstant) {
            typeString = String.format("IntConstant.getInstance(%s)", ((IntConstant) stmt.getRightOp()).getValue());
        } else if (stmt.getRightOp() instanceof Local) {
            typeString = String.format("JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"))", ((Local) stmt.getRightOp()).getName(), stmt.getRightOp().getType());
        }
        javaCodeObjects.add(String.format("FallsThrough %s = JavaJimple.newAssignment(JavaJimple.newLocal(\"%s\", factory.classType(\"%s\")), %s , noStmtPositionInfo)", stmt.getLeftOp().toString(), stmt.getLeftOp().toString(), stmt.getType().toString() ,typeString));
    }
}
