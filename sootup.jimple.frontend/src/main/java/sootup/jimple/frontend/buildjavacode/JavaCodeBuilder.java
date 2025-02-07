package sootup.jimple.frontend.buildjavacode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.constant.IntConstant;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.JEnterMonitorStmt;
import sootup.core.jimple.javabytecode.stmt.JExitMonitorStmt;
import sootup.core.jimple.javabytecode.stmt.JRetStmt;
import sootup.core.jimple.javabytecode.stmt.JSwitchStmt;
import sootup.core.model.Body;
import sootup.core.types.Type;

public class JavaCodeBuilder {

  private final List<String> javaCodeObjects = new LinkedList<>();
  Map<String, String> varAndType = new HashMap<>();
  JavaCodeValueVisitor javaCodeValueVisitor = new JavaCodeValueVisitor();

  public JavaCodeBuilder(Body body) {
    this.initJavaCode();
    this.createStmtGraph(body);
    this.createMethod();
    this.createClass();
  }

  public List<String> getJavaCodeObjects() {
    return javaCodeObjects;
  }

  public void createClass() {
    JavaCodeClassSpec javaCodeClassSpec = new JavaCodeClassSpec();
    javaCodeClassSpec.addModifier("public");
    String classString = javaCodeClassSpec.build();
    javaCodeObjects.add(classString);
  }

  public void createMethod() {
    JavaCodeMethodSpec javaCodeMethodSpec = new JavaCodeMethodSpec();
    javaCodeMethodSpec.addMethodSignature("dummyMain", "main", "void", "Collections.empty()");
    javaCodeMethodSpec.addModifier("public").addModifier("static");
    String methodStr = javaCodeMethodSpec.build();
    javaCodeObjects.add(methodStr);
  }

  public void createStmtGraph(Body body) {
    javaCodeObjects.add("MutableStmtGraph stmtGraph = bodyBuilder.getStmtGraph();");
    javaCodeObjects.add(String.format("stmtGraph.setStartingStmt(%s);", body.getFirstNonIdentityStmt()));
    // TODO: loop over statements and put edges
    javaCodeObjects.add(String.format("stmtGraph.putEdge(%s, %s)", body.getFirstNonIdentityStmt(), body.getLastStmt()));
  }

  public void initJavaCode() {
    javaCodeObjects.add("Body.BodyBuilder bodyBuilder = Body.builder()");
    javaCodeObjects.add(
        String.format(
            "JavaIdentifierFactory %s = JavaIdentifierFactory.getInstance();", "factory"));
    javaCodeObjects.add(
        String.format(
            "StmtPositionInfo %s = StmtPositionInfo.getNoStmtPositionInfo();",
            "noStmtPositionInfo"));
  }

  public void addThisRef(Local leftOp, Type classType) {
    javaCodeObjects.add(
        String.format(
            "IdentityRef %s = JavaJimple.newThisRef(factory.getClassType(\"%s\"));",
            leftOp.getName(), classType));
  }

  public void addLocal(Local value) {
    javaCodeObjects.add(
        String.format(
            "Local %s = JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"));",
            value, value.getName(), value.getType()));
  }

  public void addAssignment(JAssignStmt stmt) {
    String typeString = "";
    if (stmt.getRightOp() instanceof IntConstant) {
      typeString =
          String.format(
              "IntConstant.getInstance(%s)", ((IntConstant) stmt.getRightOp()).getValue());
    } else if (stmt.getRightOp() instanceof Local) {
      typeString =
          String.format(
              "JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"))",
              ((Local) stmt.getRightOp()).getName(), stmt.getRightOp().getType());
    }
    javaCodeObjects.add(
        String.format(
            "Stmt %s = JavaJimple.newAssignment(JavaJimple.newLocal(\"%s\", factory.classType(\"%s\")), %s , noStmtPositionInfo)",
                stmt.getLeftOp(),
                stmt.getLeftOp(),
                stmt.getType(),
            typeString));
  }

  public void addNop(JNopStmt stmt) {
    javaCodeObjects.add(
        String.format("Stmt nop = new JNopStmt(StmtPositionInfo.getNoStmtPositionInfo())"));
  }

  public void addGoto(JGotoStmt stmt) {
    javaCodeObjects.add(
        String.format("Stmt stmt1 = new JGotoStmt(StmtPositionInfo.getNoStmtPositionInfo());"));
  }

  public void addJRet(JRetStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt stmt = new JRetStmt(%s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getStmtAddress()));
  }

  public void addJReturn(JReturnStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jreturn = new JReturnStmt(%s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getOp()));
  }

  public void addJThrow(JThrowStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jthrow = new JThrowStmt(%s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getOp()));
  }

  public void addJSwitch(JSwitchStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jswitch = new JSwitchStmt(%s, %s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getKey(), stmt.getValues()));
  }

  public void addJReturnVoid(JReturnVoidStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jreturnvoid = new JReturnVoidStmt(StmtPositionInfo.getNoStmtPositionInfo());"));
  }

  public void addIf(JIfStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jif = Jimple.newIfStmt(%s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getCondition()));
  }

  public void addJExitMonitor(JExitMonitorStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jexitmonitor = new JExitMonitorStmt(%s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getOp()));
  }

  public void addJEnterMonitor(JEnterMonitorStmt stmt) {
    javaCodeObjects.add(
        String.format(
            "Stmt jentermonitor = new JEnterMonitorStmt(%s, StmtPositionInfo.getNoStmtPositionInfo());",
            stmt.getOp()));
  }
}
