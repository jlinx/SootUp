package sootup.java.core.buildsrccode;

/*-
 * #%L
 * SootUp - a J*va Optimization Framework
 * %%
 * Copyright (C) 2025 Sahil Agichani
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.core.jimple.basic.LValue;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractConditionExpr;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.ref.IdentityRef;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.javabytecode.stmt.*;
import sootup.core.jimple.visitor.StmtVisitor;
import sootup.core.jimple.visitor.Visitor;
import sootup.core.model.Body;

public class JavaCodeStmtVisitor implements StmtVisitor, Visitor {

  public static final Logger logger = LoggerFactory.getLogger(JavaCodeStmtVisitor.class);

  StmtValueVisitor stmtValueVisitor = new StmtValueVisitor();

  private final JavaSrcCodeBuilder javaSrcCodeBuilder;

  public JavaCodeStmtVisitor(Body body) {
    this.javaSrcCodeBuilder = new JavaSrcCodeBuilder(body);
  }

  public void createStmtGraph(Body body) {
    javaSrcCodeBuilder.createStmtGraph(body);
  }

  public Set<String> getJavaCodeObjects() {
    return javaSrcCodeBuilder.getJavaCodeObjects();
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

    logger.debug("JBreakPoint");
  }

  @Override
  public void caseInvokeStmt(JInvokeStmt stmt) {
    List<Value> vals = new ArrayList<>();
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
      vals.add(stmt.getArrayRef());
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
      vals.add(stmt.getFieldRef());
    }
    if (stmt.containsInvokeExpr()) {
      stmt.getInvokeExpr().get().accept(stmtValueVisitor);
      AbstractInvokeExpr invokeExpr = stmt.getInvokeExpr().get();
      vals.add(invokeExpr);
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
      vals.add(stmt.getDef().get());
    }

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    List<String> valStrList =
        vals.stream()
            .map(valueGenStr::get) // Get the corresponding value from valueGenStr
            .filter(Objects::nonNull) // Ensure no null values are added
            .collect(Collectors.toList());
    Map<Value, String> valueVarName = stmtValueVisitor.getValueVarName();
    javaSrcCodeBuilder.addInvoke(stmt, valueVarName.get(stmt.getInvokeExpr().get()), valStrList);

    logger.debug("Invoke");
  }

  @Override
  public void caseAssignStmt(JAssignStmt stmt) {
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
    }
    if (stmt.containsInvokeExpr()) {
      stmt.getInvokeExpr().get().accept(stmtValueVisitor);
    }

    LValue leftOp = stmt.getLeftOp();
    Value rightOp = stmt.getRightOp();
    rightOp.accept(stmtValueVisitor);
    leftOp.accept(stmtValueVisitor);

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    List<Value> vals = Arrays.asList(leftOp, rightOp);
    List<String> valStrList =
        vals.stream()
            .map(valueGenStr::get) // Get the corresponding value from valueGenStr
            .filter(Objects::nonNull) // Ensure no null values are added
            .collect(Collectors.toList());

    Map<Value, String> valueVarName = stmtValueVisitor.getValueVarName();
    javaSrcCodeBuilder.addAssignment(
        stmt, valueVarName.get(leftOp), valueVarName.get(rightOp), valStrList);
    logger.debug("Assignment");
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
    List<String> valStrList =
        vals.stream()
            .map(valueGenStr::get) // Get the corresponding value from valueGenStr
            .filter(Objects::nonNull) // Ensure no null values are added
            .collect(Collectors.toList());

    javaSrcCodeBuilder.addJIdentityStmt(
        stmt, valueVarName.get(leftOp), valueVarName.get(rightOp), valStrList);
    logger.debug("Identity");
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

    javaSrcCodeBuilder.addJEnterMonitor(stmt);
    logger.debug("JEnterMonitor");
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

    javaSrcCodeBuilder.addJExitMonitor(stmt);
    logger.debug("JExitMonitor");
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

    javaSrcCodeBuilder.addGoto(stmt);
    logger.debug("JGoto");
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
    AbstractConditionExpr conditionExpr = stmt.getCondition();
    conditionExpr.accept(stmtValueVisitor);

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    Map<Value, String> valueVarName = stmtValueVisitor.getValueVarName();

    List<Value> vals = Arrays.asList(conditionExpr.getOp1(), conditionExpr.getOp2(), conditionExpr);
    List<String> valStrList =
        vals.stream()
            .map(valueGenStr::get) // Get the corresponding value from valueGenStr
            .filter(Objects::nonNull) // Ensure no null values are added
            .collect(Collectors.toList());

    javaSrcCodeBuilder.addIf(stmt, valueVarName.get(conditionExpr), valStrList);
    logger.debug("JIfStmt");
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

    javaSrcCodeBuilder.addNop(stmt);
    logger.debug("JNop");
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

    javaSrcCodeBuilder.addJRet(stmt);
    logger.debug("JRet");
  }

  @Override
  public void caseReturnStmt(JReturnStmt stmt) {
    List<Value> vals = new ArrayList<>();
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
      vals.add(stmt.getArrayRef());
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
      vals.add(stmt.getFieldRef());
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
      vals.add(stmt.getDef().get());
    }
    stmt.getOp().accept(stmtValueVisitor);
    vals.add(stmt.getOp());

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    List<String> valStrList =
        vals.stream()
            .map(valueGenStr::get) // Get the corresponding value from valueGenStr
            .filter(Objects::nonNull) // Ensure no null values are added
            .collect(Collectors.toList());
    Map<Value, String> valueVarName = stmtValueVisitor.getValueVarName();

    javaSrcCodeBuilder.addJReturn(stmt, valueVarName.get(stmt.getOp()), valStrList);
    logger.debug("JReturn");
  }

  @Override
  public void caseReturnVoidStmt(JReturnVoidStmt stmt) {
    List<Value> vals = new ArrayList<>();
    if (stmt.containsArrayRef()) {
      stmt.getArrayRef().accept(stmtValueVisitor);
      vals.add(stmt.getArrayRef());
    }
    if (stmt.containsFieldRef()) {
      stmt.getFieldRef().accept(stmtValueVisitor);
      vals.add(stmt.getFieldRef());
    }
    if (stmt.getDef().isPresent()) {
      stmt.getDef().get().accept(stmtValueVisitor);
      vals.add(stmt.getDef().get());
    }

    Map<Value, String> valueGenStr = stmtValueVisitor.getValueGenStr();
    List<String> valStrList =
        vals.stream()
            .map(valueGenStr::get) // Get the corresponding value from valueGenStr
            .filter(Objects::nonNull) // Ensure no null values are added
            .collect(Collectors.toList());

    javaSrcCodeBuilder.addJReturnVoid(stmt, valStrList);
    logger.debug("JReturnVoid");
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

    javaSrcCodeBuilder.addJSwitch(stmt);
    logger.debug("JSwitch");
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

    javaSrcCodeBuilder.addJThrow(stmt);
    logger.debug("JThrow");
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

    logger.debug("Stmt");
  }
}
