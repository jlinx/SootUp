package sootup.jimple.frontend.buildjavacode;

import sootup.core.jimple.common.ref.JThisRef;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.*;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.jimple.visitor.Visitor;
import sootup.core.model.Body;

import java.util.List;
import java.util.Map;

public class JavaCodeStmtVisitor implements StmtVisitor, Visitor {

  StmtValueVisitor stmtValueVisitor = new StmtValueVisitor();

  private final JavaCodeBuilder javaCodeBuilder;

  public JavaCodeStmtVisitor(Body body) {
    this.javaCodeBuilder = new JavaCodeBuilder(body);
  }

  public List<String> getJavaCodeObjects() {
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
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
    }

    stmt.getRightOp().accept(stmtValueVisitor);
    stmt.getLeftOp().accept(stmtValueVisitor);

    javaCodeBuilder.addAssignment(stmt);
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

    stmt.getLeftOp().accept(stmtValueVisitor);
    stmt.getRightOp().accept(stmtValueVisitor);

    Map<String, String> valueGenStr = stmtValueVisitor.getValueGenStr();

    // javaCodeBuilder.addIdentity(stmt);
    if (stmt.getRightOp() instanceof JThisRef) {
      javaCodeBuilder.addThisRef(stmt.getLeftOp(), stmt.getRightOp().getType());
    } else {
      javaCodeBuilder.addLocal(stmt.getLeftOp());
    }

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
