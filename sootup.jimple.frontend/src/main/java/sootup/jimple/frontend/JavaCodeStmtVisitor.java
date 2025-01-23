package sootup.jimple.frontend;

import sootup.core.jimple.basic.Local;
import sootup.core.jimple.common.ref.JThisRef;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.*;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.jimple.visitor.Visitor;

import java.util.List;

public class JavaCodeStmtVisitor implements StmtVisitor, Visitor {

    private final JavaCodeBuilder javaCodeBuilder = new JavaCodeBuilder();

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
        if (stmt.getRightOp() instanceof JThisRef) {
            javaCodeBuilder.addThisRef(stmt.getLeftOp(), stmt.getRightOp().getType());
        } else {
            javaCodeBuilder.addLocal(stmt.getLeftOp());
        }
        System.out.println("Identity");
    }

    @Override
    public void caseEnterMonitorStmt(JEnterMonitorStmt stmt) {
        System.out.println("JEnterMonitor");
    }

    @Override
    public void caseExitMonitorStmt(JExitMonitorStmt stmt) {
        System.out.println("JExitMonitor");
    }

    @Override
    public void caseGotoStmt(JGotoStmt stmt) {
        System.out.println("JGoto");
    }

    @Override
    public void caseIfStmt(JIfStmt stmt) {
        System.out.println("JIfStmt");
    }

    @Override
    public void caseNopStmt(JNopStmt stmt) {
        System.out.println("JNop");
    }

    @Override
    public void caseRetStmt(JRetStmt stmt) {
        System.out.println("JRetStmt");
    }

    @Override
    public void caseReturnStmt(JReturnStmt stmt) {
        System.out.println("JReturn");
    }

    @Override
    public void caseReturnVoidStmt(JReturnVoidStmt stmt) {
        System.out.println("JReturnVoid");
    }

    @Override
    public void caseSwitchStmt(JSwitchStmt stmt) {
        System.out.println("JSwitch");
    }

    @Override
    public void caseThrowStmt(JThrowStmt stmt) {
        System.out.println("JThrow");
    }

    @Override
    public void defaultCaseStmt(Stmt stmt) {
        System.out.println("Stmt");
    }
}
