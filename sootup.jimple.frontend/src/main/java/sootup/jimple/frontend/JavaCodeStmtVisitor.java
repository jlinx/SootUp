package sootup.jimple.frontend;

import java.util.List;
import sootup.core.jimple.common.ref.JThisRef;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.*;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.jimple.visitor.Visitor;
import sootup.core.model.Body;

public class JavaCodeStmtVisitor implements StmtVisitor, Visitor {

  private JavaCodeBuilder javaCodeBuilder = null;

  public JavaCodeStmtVisitor(Body body) {
    javaCodeBuilder = new JavaCodeBuilder(body);
  }

  public List<String> getJavaCodeObjects() {
    return javaCodeBuilder.getJavaCodeObjects();
  }

  @Override
  public void caseBreakpointStmt(JBreakpointStmt stmt) {
    System.out.println("JBreakPoint");
  }

  @Override
  public void caseInvokeStmt(JInvokeStmt stmt) {
    System.out.println("Invoke");
  }

  @Override
  public void caseAssignStmt(JAssignStmt stmt) {
    //        if (stmt.getLeftOp() instanceof Local) {
    //            javaCodeBuilder.addLocal((Local) stmt.getLeftOp());
    //        }
    //        if (stmt.getRightOp() instanceof Local) {
    //            javaCodeBuilder.addLocal((Local) stmt.getRightOp());
    //        }
    javaCodeBuilder.addAssignment(stmt);
    System.out.println("Assignment");
  }

  @Override
  public void caseIdentityStmt(JIdentityStmt stmt) {
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
    javaCodeBuilder.addJEnterMonitor(stmt);
    System.out.println("JEnterMonitor");
  }

  @Override
  public void caseExitMonitorStmt(JExitMonitorStmt stmt) {
    javaCodeBuilder.addJExitMonitor(stmt);
    System.out.println("JExitMonitor");
  }

  @Override
  public void caseGotoStmt(JGotoStmt stmt) {
    javaCodeBuilder.addGoto(stmt);
    System.out.println("JGoto");
  }

  @Override
  public void caseIfStmt(JIfStmt stmt) {
    javaCodeBuilder.addIf(stmt);
    System.out.println("JIfStmt");
  }

  @Override
  public void caseNopStmt(JNopStmt stmt) {
    javaCodeBuilder.addNop(stmt);
    System.out.println("JNop");
  }

  @Override
  public void caseRetStmt(JRetStmt stmt) {
    javaCodeBuilder.addJRet(stmt);
    System.out.println("JRet");
  }

  @Override
  public void caseReturnStmt(JReturnStmt stmt) {
    javaCodeBuilder.addJReturn(stmt);
    System.out.println("JReturn");
  }

  @Override
  public void caseReturnVoidStmt(JReturnVoidStmt stmt) {
    javaCodeBuilder.addJReturnVoid(stmt);
    System.out.println("JReturnVoid");
  }

  @Override
  public void caseSwitchStmt(JSwitchStmt stmt) {
    javaCodeBuilder.addJSwitch(stmt);
    System.out.println("JSwitch");
  }

  @Override
  public void caseThrowStmt(JThrowStmt stmt) {
    javaCodeBuilder.addJThrow(stmt);
    System.out.println("JThrow");
  }

  @Override
  public void defaultCaseStmt(Stmt stmt) {
    System.out.println("Stmt");
  }
}
