package sootup.jimple.frontend.buildjavacode;

import java.util.*;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.JEnterMonitorStmt;
import sootup.core.jimple.javabytecode.stmt.JExitMonitorStmt;
import sootup.core.jimple.javabytecode.stmt.JRetStmt;
import sootup.core.jimple.javabytecode.stmt.JSwitchStmt;
import sootup.core.model.Body;

public class JavaCodeBuilder {

  private final Set<String> javaCodeObjects = new LinkedHashSet<>();
  Map<Stmt, String> stmtGenStr = new HashMap<>();
  Map<Stmt, String> stmtVarName = new HashMap<>();
  static int stmtCounter = 1;

  public Map<Stmt, String> getStmtGenStr() {
    return stmtGenStr;
  }

  public Map<Stmt, String> getStmtVarName() {
    return stmtVarName;
  }

  public JavaCodeBuilder(Body body) {
    this.initJavaCode();
    // this.createMethod();
    // this.createClass();
  }

  public Set<String> getJavaCodeObjects() {
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
    javaCodeObjects.add(
        String.format("stmtGraph.setStartingStmt(%s);", stmtVarName.get(body.getThisStmt())));
    // loop over statements and put edges
    List<Stmt> bodyStmts = body.getStmts();
    if (!bodyStmts.isEmpty()) {
      Iterator<Stmt> stmtIterator = bodyStmts.iterator();
      Stmt previous = stmtIterator.next();
      while (stmtIterator.hasNext()) {
        Stmt current = stmtIterator.next();
        String outNode = stmtVarName.get(previous);
        String inNode = stmtVarName.get(current);
        javaCodeObjects.add(String.format("stmtGraph.putEdge(%s, %s);", outNode, inNode));
        previous = current;
      }
    }
  }

  public void initJavaCode() {
    javaCodeObjects.add(
        "JavaView view = new JavaView(Collections.singletonList(new EagerInputLocation()));");
    javaCodeObjects.add("Body.BodyBuilder bodyBuilder = Body.builder()");
    javaCodeObjects.add(
        String.format(
            "JavaIdentifierFactory %s = JavaIdentifierFactory.getInstance();", "factory"));
    javaCodeObjects.add(
        String.format(
            "StmtPositionInfo %s = StmtPositionInfo.getNoStmtPositionInfo();",
            "noStmtPositionInfo"));
  }

  public void addJIdentityStmt(
      JIdentityStmt stmt, String localVarName, String rightOpName, List<String> getValueStrs) {
    if (!stmtGenStr.containsKey(stmt)) {
      String identityVarName = "identity" + stmtCounter++;
      stmtVarName.put(stmt, identityVarName);
      javaCodeObjects.addAll(getValueStrs);
      String identityStmtStr =
          String.format(
              "JIdentityStmt %s = new JIdentityStmt(%s, %s, noStmtPositionInfo);",
              identityVarName, localVarName, rightOpName);
      stmtGenStr.put(stmt, identityStmtStr);
      javaCodeObjects.add(identityStmtStr);
    }
  }

  public void addAssignment(
      JAssignStmt stmt, String leftOpVarName, String rightOpVarName, List<String> getValueStrs) {
    if (!stmtGenStr.containsKey(stmt)) {

      String assignmentVarName = "assignment" + stmtCounter++;
      stmtVarName.put(stmt, assignmentVarName);
      javaCodeObjects.addAll(getValueStrs);
      String assignmentStmtStr =
          String.format(
              "JAssignStmt %s = JavaJimple.newAssignment(%s, %s , noStmtPositionInfo)",
              assignmentVarName, leftOpVarName, rightOpVarName);
      stmtGenStr.put(stmt, assignmentStmtStr);
      javaCodeObjects.add(assignmentStmtStr);
    }
  }

  public void addInvoke(JInvokeStmt stmt, String exprVarName, List<String> getValueStrs) {
    if (!stmtGenStr.containsKey(stmt)) {
      String invokeVarName = "invoke" + stmtCounter++;
      stmtVarName.put(stmt, invokeVarName);
      javaCodeObjects.addAll(getValueStrs);
      String invokeStmtStr =
          String.format(
              "JInvokeStmt %s = new JInvokeStmt(%s, noStmtPositionInfo);",
              invokeVarName, exprVarName);
      stmtGenStr.put(stmt, invokeStmtStr);
      javaCodeObjects.add(invokeStmtStr);
    }
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

  public void addJReturn(JReturnStmt stmt, String returnOpVarName, List<String> getValueStrs) {
    if (!stmtGenStr.containsKey(stmt)) {
      String returnVarName = "return" + stmtCounter++;
      stmtVarName.put(stmt, returnVarName);
      javaCodeObjects.addAll(getValueStrs);

      String returnStmtStr =
          String.format(
              "JReturn %s = new JReturn(%s, noStmtPositionInfo);", returnVarName, returnOpVarName);
      stmtGenStr.put(stmt, returnStmtStr);
      javaCodeObjects.add(returnStmtStr);
    }
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

  public void addJReturnVoid(JReturnVoidStmt stmt, List<String> getValueStrs) {
    if (!stmtGenStr.containsKey(stmt)) {
      String returnVoidVarName = "returnVoid" + stmtCounter++;
      stmtVarName.put(stmt, returnVoidVarName);
      javaCodeObjects.addAll(getValueStrs);
      String returnVoidStmtStr =
          String.format(
              "JReturnVoidStmt %s = new JReturnVoidStmt(StmtPositionInfo.getNoStmtPositionInfo());",
              returnVoidVarName);
      stmtGenStr.put(stmt, returnVoidStmtStr);
      javaCodeObjects.add(returnVoidStmtStr);
    }
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
