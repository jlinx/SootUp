package sootup.jimple.frontend.buildjavacode;

import sootup.core.jimple.basic.LValue;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.ref.IdentityRef;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.*;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.jimple.visitor.Visitor;
import sootup.core.model.Body;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaCodeStmtVisitor implements StmtVisitor, Visitor {

  StmtValueVisitor stmtValueVisitor = new StmtValueVisitor();

  private final JavaCodeBuilder javaCodeBuilder;

  public JavaCodeStmtVisitor(Body body) {
    this.javaCodeBuilder = new JavaCodeBuilder(body);
  }

  public void createStmtGraph(Body body) {
    javaCodeBuilder.createStmtGraph(body);
  }

  public Set<String> getJavaCodeObjects() {
    return javaCodeBuilder.getJavaCodeObjects();
  }

  @Override
  public void caseBreakpointStmt(JBreakpointStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    System.out.println("JBreakPoint");
  }

  @Override
  public void caseInvokeStmt(JInvokeStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.containsInvokeExpr()){
      stmt.getInvokeExpr().get().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    System.out.println("Invoke");
  }

  @Override
  public void caseAssignStmt(JAssignStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.containsInvokeExpr()){
      stmt.getInvokeExpr().get().accept(stmtValueVisitor);
    }

    LValue leftOp = stmt.getLeftOp();
    Value rightOp = stmt.getRightOp();
    rightOp.accept(stmtValueVisitor);
    leftOp.accept(stmtValueVisitor);

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    List<Value> vals = Arrays.asList(leftOp, rightOp);
    List<String> valStrList = valueGenStr.keySet().stream()
            .filter(vals::contains)
            .map(valueGenStr::get) // Get the value for each key
            .collect(Collectors.toList());

    Map<Value, String> valueVarName = stmtValueVisitor.getValueVarName();
    javaCodeBuilder.addAssignment(stmt, valueVarName.get(leftOp), valueVarName.get(rightOp), valStrList);
    System.out.println("Assignment");
  }

  @Override
  public void caseIdentityStmt(JIdentityStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }

    Local leftOp = stmt.getLeftOp();
    IdentityRef rightOp = stmt.getRightOp();

    leftOp.accept(stmtValueVisitor);
    rightOp.accept(stmtValueVisitor);

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    Map<Value, String> valueVarName = stmtValueVisitor.getValueVarName();

    List<Value> vals = Arrays.asList(leftOp, rightOp);
    List<String> valStrList = valueGenStr.keySet().stream()
            .filter(vals::contains)
            .map(valueGenStr::get) // Get the value for each key
            .collect(Collectors.toList());

    javaCodeBuilder.addJIdentityStmt(stmt, valueVarName.get(leftOp), valueVarName.get(rightOp), valStrList);
    System.out.println("Identity");
  }

  @Override
  public void caseEnterMonitorStmt(JEnterMonitorStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getOp().accept(stmtValueVisitor);

    javaCodeBuilder.addJEnterMonitor(stmt);
    System.out.println("JEnterMonitor");
  }

  @Override
  public void caseExitMonitorStmt(JExitMonitorStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getOp().accept(stmtValueVisitor);

    javaCodeBuilder.addJExitMonitor(stmt);
    System.out.println("JExitMonitor");
  }

  @Override
  public void caseGotoStmt(JGotoStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    javaCodeBuilder.addGoto(stmt);
    System.out.println("JGoto");
  }

  @Override
  public void caseIfStmt(JIfStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getCondition().accept(stmtValueVisitor);

    javaCodeBuilder.addIf(stmt);
    System.out.println("JIfStmt");
  }

  @Override
  public void caseNopStmt(JNopStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    javaCodeBuilder.addNop(stmt);
    System.out.println("JNop");
  }

  @Override
  public void caseRetStmt(JRetStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getStmtAddress().accept(stmtValueVisitor);

    javaCodeBuilder.addJRet(stmt);
    System.out.println("JRet");
  }

  @Override
  public void caseReturnStmt(JReturnStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getOp().accept(stmtValueVisitor);

    javaCodeBuilder.addJReturn(stmt);
    System.out.println("JReturn");
  }

  @Override
  public void caseReturnVoidStmt(JReturnVoidStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    javaCodeBuilder.addJReturnVoid(stmt);
    System.out.println("JReturnVoid");
  }

  @Override
  public void caseSwitchStmt(JSwitchStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getKey().accept(stmtValueVisitor);
    stmt.getValues().forEach(val -> val.accept(stmtValueVisitor));

    javaCodeBuilder.addJSwitch(stmt);
    System.out.println("JSwitch");
  }

  @Override
  public void caseThrowStmt(JThrowStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }
    stmt.getOp().accept(stmtValueVisitor);

    javaCodeBuilder.addJThrow(stmt);
    System.out.println("JThrow");
  }

  @Override
  public void defaultCaseStmt(Stmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    System.out.println("Stmt");
  }
}
