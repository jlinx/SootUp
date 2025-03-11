package sootup.jimple.frontend.buildjavacode;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.constant.*;
import sootup.core.jimple.common.expr.*;
import sootup.core.jimple.common.ref.*;
import sootup.core.jimple.visitor.ValueVisitor;
import sootup.core.jimple.visitor.Visitor;

public class StmtValueVisitor implements ValueVisitor, Visitor {

  public static final Logger logger = LoggerFactory.getLogger(StmtValueVisitor.class);
  private final Map<Value, String> valueGenStr = new HashMap<>();
  private final Map<Value, String> valueVarName = new HashMap<>();
  static int valueCounter = 1;

  public Map<Value, String> getValueGenStr() {
    return valueGenStr;
  }

  public Map<Value, String> getValueVarName() {
    return valueVarName;
  }

  @Override
  public void defaultCaseValue(@Nonnull Value v) {
    logger.debug("defaultCaseValue");
  }

  @Override
  public void caseLocal(@Nonnull Local local) {
    if (!valueGenStr.containsKey(local)) {
      String localVarName = "local" + valueCounter++;
      valueVarName.put(local, localVarName);
      String localStr =
          String.format(
              "Local %s = JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"));",
              localVarName, local.getName(), local.getType());
      valueGenStr.put(local, localStr);
    }
    logger.debug("caseLocal");
  }

  @Override
  public void caseBooleanConstant(@Nonnull BooleanConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String boolVarName = "bool" + valueCounter++;
      valueVarName.put(constant, boolVarName);
      String boolConstStr =
          String.format("BooleanConstant %s = new BooleanConstant(%s);", boolVarName, constant);
      valueGenStr.put(constant, boolConstStr);
    }
    logger.debug("caseBooleanConstant");
  }

  @Override
  public void caseDoubleConstant(@Nonnull DoubleConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String doubleVarName = "double" + valueCounter++;
      valueVarName.put(constant, doubleVarName);
      String doubleConstStr =
          String.format(
              "DoubleConstant %s = DoubleConstant.getInstance(%s);",
              doubleVarName, constant.getValue());
      valueGenStr.put(constant, doubleConstStr);
    }
    logger.debug("caseDoubleConstant");
  }

  @Override
  public void caseFloatConstant(@Nonnull FloatConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String floatVarName = "float" + valueCounter++;
      valueVarName.put(constant, floatVarName);
      String floatConstStr =
          String.format(
              "FloatConstant %s = FloatConstant.getInstance(%s);",
              floatVarName, constant.getValue());
      valueGenStr.put(constant, floatConstStr);
    }
    logger.debug("caseFloatConstant");
  }

  @Override
  public void caseIntConstant(@Nonnull IntConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String intVarName = "int" + valueCounter++;
      valueVarName.put(constant, intVarName);
      String intConstStr =
          String.format(
              "IntConstant %s = IntConstant.getInstance(%s);", intVarName, constant.getValue());
      valueGenStr.put(constant, intConstStr);
    }
    logger.debug("caseIntConstant");
  }

  @Override
  public void caseLongConstant(@Nonnull LongConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String longVarName = "long" + valueCounter++;
      valueVarName.put(constant, longVarName);
      String longConstStr =
          String.format(
              "LongConstant %s = LongConstant.getInstance(%s);", longVarName, constant.getValue());
      valueGenStr.put(constant, longConstStr);
    }
    logger.debug("caseLongConstant");
  }

  @Override
  public void caseNullConstant(@Nonnull NullConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String nullVarName = "null" + valueCounter++;
      valueVarName.put(constant, nullVarName);
      String nullConstStr =
          String.format("NullConstant %s = NullConstant.getInstance();", nullVarName);
      valueGenStr.put(constant, nullConstStr);
    }
    logger.debug("caseNullConstant");
  }

  @Override
  public void caseStringConstant(@Nonnull StringConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String strVarName = "str" + valueCounter++;
      valueVarName.put(constant, strVarName);
      String strConstStr =
          String.format(
              "StringConstant %s = JavaJimple.getInstance().newStringConstant(\"%s\");",
              strVarName, constant.getValue());
      valueGenStr.put(constant, strConstStr);
    }
    logger.debug("caseStringConstant");
  }

  @Override
  public void caseEnumConstant(@Nonnull EnumConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String enumVarName = "enum" + valueCounter++;
      valueVarName.put(constant, enumVarName);
      String enumConstStr =
          String.format(
              "EnumConstant %s = JavaJimple.getInstance().newEnumConstant(\"%s\");",
              enumVarName, constant.getValue());
      valueGenStr.put(constant, enumConstStr);
    }
    logger.debug("caseEnumConstant");
  }

  @Override
  public void caseClassConstant(@Nonnull ClassConstant constant) {
    if (!valueGenStr.containsKey(constant)) {
      String clsVarName = "cls" + valueCounter++;
      valueVarName.put(constant, clsVarName);
      String classConstStr =
          String.format(
              "ClassConstant %s = JavaJimple.getInstance().newClassConstant(\"%s\");",
              clsVarName, constant.getValue());
      valueGenStr.put(constant, classConstStr);
    }
    logger.debug("caseClassConstant");
  }

  @Override
  public void caseMethodHandle(@Nonnull MethodHandle handle) {
    if (!valueGenStr.containsKey(handle)) {
      String methodHandleVarName = "methodHandle" + valueCounter++;
      valueVarName.put(handle, methodHandleVarName);
      String methodHandleStr =
          String.format(
              "MethodHandle %s = JavaJimple.getInstance().newMethodHandle(%s, %s);",
              methodHandleVarName, handle.getReferenceSignature(), handle.getKind().toString());
      valueGenStr.put(handle, methodHandleStr);
    }
    logger.debug("caseMethodHandle");
  }

  @Override
  public void caseMethodType(@Nonnull MethodType methodType) {
    if (!valueGenStr.containsKey(methodType)) {
      String methodTypeVarName = "methodType" + valueCounter++;
      valueVarName.put(methodType, methodTypeVarName);
      String methodTypeStr =
          String.format(
              "MethodType %s = new MethodType(%s, %s);",
              methodTypeVarName, methodType.getReturnType(), methodType.hashCode());
      valueGenStr.put(methodType, methodTypeStr);
    }
    logger.debug("caseMethodType");
  }

  @Override
  public void defaultCaseConstant(@Nonnull Constant constant) {
    logger.debug("defaultCaseConstant");
  }

  @Override
  public void caseAddExpr(JAddExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseAddExpr");
  }

  @Override
  public void caseAndExpr(JAndExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseAndExpr");
  }

  @Override
  public void caseCmpExpr(JCmpExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseCmpExpr");
  }

  @Override
  public void caseCmpgExpr(JCmpgExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseCmpgExpr");
  }

  @Override
  public void caseCmplExpr(JCmplExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseCmplExpr");
  }

  @Override
  public void caseDivExpr(JDivExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseDivExpr");
  }

  @Override
  public void caseEqExpr(JEqExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseEqExpr");
  }

  @Override
  public void caseNeExpr(JNeExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseNeExpr");
  }

  @Override
  public void caseGeExpr(JGeExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseGeExpr");
  }

  @Override
  public void caseGtExpr(JGtExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseGtExpr");
  }

  @Override
  public void caseLeExpr(JLeExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseLeExpr");
  }

  @Override
  public void caseLtExpr(JLtExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseLtExpr");
  }

  @Override
  public void caseMulExpr(JMulExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseMulExpr");
  }

  @Override
  public void caseOrExpr(JOrExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseOrExpr");
  }

  @Override
  public void caseRemExpr(JRemExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseRemExpr");
  }

  @Override
  public void caseShlExpr(JShlExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseShlExpr");
  }

  @Override
  public void caseShrExpr(JShrExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseShrExpr");
  }

  @Override
  public void caseUshrExpr(JUshrExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseUshrExpr");
  }

  @Override
  public void caseSubExpr(JSubExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseSubExpr");
  }

  @Override
  public void caseXorExpr(JXorExpr expr) {
    expr.getOp1().accept(this);
    expr.getOp2().accept(this);
    logger.debug("caseXorExpr");
  }

  @Override
  public void caseSpecialInvokeExpr(JSpecialInvokeExpr expr) {
    expr.getBase().accept(this);
    expr.getArgs().forEach(args -> args.accept(this));
    logger.debug("caseSpecialInvokeExpr");
  }

  @Override
  public void caseVirtualInvokeExpr(JVirtualInvokeExpr expr) {
    expr.getBase().accept(this);
    expr.getArgs().forEach(args -> args.accept(this));
    if (!valueGenStr.containsKey(expr)) {
      int i = valueCounter++;
      String virtualInvokeExprVarName = "virtualInvokeExpr" + i;
      valueVarName.put(expr, virtualInvokeExprVarName);
      String methodSigVarName = "methodSig" + i;
      String methodSigStr =
          String.format(
              "MethodSignature %s = new MethodSignature(%s, %s);",
              methodSigVarName,
              expr.getMethodSignature().getDeclClassType(),
              expr.getMethodSignature().getSubSignature());
      String virtualInvokeExprStr =
          String.format(
              "JVirtualInvokeExpr %s = new JVirtualInvokeExpr(%s, %s, %s);",
              virtualInvokeExprVarName,
              valueVarName.get(expr.getBase()),
              methodSigVarName,
              expr.getArgs());
      valueGenStr.put(expr, String.join("\n", methodSigStr, virtualInvokeExprStr));
    }
    logger.debug("caseVirtualInvokeExpr");
  }

  @Override
  public void caseInterfaceInvokeExpr(JInterfaceInvokeExpr expr) {
    expr.getBase().accept(this);
    expr.getArgs().forEach(args -> args.accept(this));
    logger.debug("caseInterfaceInvokeExpr");
  }

  @Override
  public void caseStaticInvokeExpr(JStaticInvokeExpr expr) {
    expr.getArgs().forEach(args -> args.accept(this));
    logger.debug("caseStaticInvokeExpr");
  }

  @Override
  public void caseDynamicInvokeExpr(JDynamicInvokeExpr expr) {
    expr.getBootstrapArgs().forEach(bootargs -> bootargs.accept(this));
    expr.getArgs().forEach(args -> args.accept(this));
    logger.debug("caseDynamicInvokeExpr");
  }

  @Override
  public void caseCastExpr(JCastExpr expr) {
    expr.getOp().accept(this);
    logger.debug("caseCastExpr");
  }

  @Override
  public void caseInstanceOfExpr(JInstanceOfExpr expr) {
    expr.getOp().accept(this);
    logger.debug("caseInstanceOfExpr");
  }

  @Override
  public void caseNewArrayExpr(JNewArrayExpr expr) {
    expr.getSize().accept(this);
    logger.debug("caseNewArrayExpr");
  }

  @Override
  public void caseNewMultiArrayExpr(JNewMultiArrayExpr expr) {
    expr.getSizes().forEach(size -> size.accept(this));
    logger.debug("caseNewMultiArrayExpr");
  }

  @Override
  public void caseNewExpr(JNewExpr expr) {
    logger.debug("caseNewExpr");
  }

  @Override
  public void caseLengthExpr(JLengthExpr expr) {
    expr.getOp().accept(this);
    logger.debug("caseLengthExpr");
  }

  @Override
  public void caseNegExpr(JNegExpr expr) {
    expr.getOp().accept(this);
    logger.debug("caseNegExpr");
  }

  @Override
  public void casePhiExpr(JPhiExpr v) {
    v.getArgs().forEach(arg -> arg.accept(this));
    logger.debug("casePhiExpr");
  }

  @Override
  public void defaultCaseExpr(Expr expr) {
    logger.debug("defaultCaseExpr");
  }

  @Override
  public void caseStaticFieldRef(JStaticFieldRef ref) {
    if (!valueGenStr.containsKey(ref)) {
      int i = valueCounter++;
      String staticFieldRefVarName = "staticFieldRef" + i;
      valueVarName.put(ref, staticFieldRefVarName);
      String fieldSigVarName = "fieldSig" + i;
      String fieldSigStr =
          String.format(
              "FieldSignature %s = view.getIdentifierFactory().getFieldSignature(\"%s\", JavaIdentifierFactory.getInstance().getClassType(\"%s\"), \"%s\");",
              fieldSigVarName,
              ref.getFieldSignature().getName(),
              ref.getFieldSignature().getDeclClassType(),
              ref.getType());
      String staticFieldRefStr =
          String.format(
              "JStaticFieldRef %s = Jimple.newStaticFieldRef(%s);",
              staticFieldRefVarName, fieldSigVarName);
      valueGenStr.put(ref, String.join("\n", fieldSigStr, staticFieldRefStr));
    }
    logger.debug("caseStaticFieldRef");
  }

  @Override
  public void caseInstanceFieldRef(JInstanceFieldRef ref) {
    ref.getBase().accept(this);
    logger.debug("caseInstanceFieldRef");
  }

  @Override
  public void caseArrayRef(JArrayRef ref) {
    ref.getBase().accept(this);
    ref.getIndex().accept(this);
    logger.debug("caseArrayRef");
  }

  @Override
  public void caseParameterRef(JParameterRef ref) {
    if (!valueGenStr.containsKey(ref)) {
      String parameterRefVarName = "parameterRef" + valueCounter++;
      valueVarName.put(ref, parameterRefVarName);
      String parameterRefStr =
          String.format(
              "JParameterRef %s = new JParameterRef(JavaIdentifierFactory.getInstance().getType(\"%s\"), %s);",
              parameterRefVarName, ref.getType(), ref.getIndex());
      valueGenStr.put(ref, parameterRefStr);
    }
    logger.debug("caseParameterRef");
  }

  @Override
  public void caseCaughtExceptionRef(JCaughtExceptionRef ref) {
    if (!valueGenStr.containsKey(ref)) {
      String caughtExceptionRefVarName = "caughtException" + valueCounter++;
      valueVarName.put(ref, caughtExceptionRefVarName);
      String caughtExceptionRefStr =
          String.format(
              "JCaughtExceptionRef %s = new JCaughtExceptionRef(JavaIdentifierFactory.getInstance().getType(\"java.lang.Throwable\"));",
              caughtExceptionRefVarName);
      valueGenStr.put(ref, caughtExceptionRefStr);
    }
    logger.debug("caseCaughtExceptionRef");
  }

  @Override
  public void caseThisRef(JThisRef ref) {
    if (!valueGenStr.containsKey(ref)) {
      String thisRefVarName = "thisRef" + valueCounter++;
      valueVarName.put(ref, thisRefVarName);
      String thisRefStr =
          String.format(
              "JThisRef %s = new JThisRef(JavaIdentifierFactory.getInstance().getClassType(\"%s\"));",
              thisRefVarName, ref.getType());
      valueGenStr.put(ref, thisRefStr);
    }
    logger.debug("caseThisRef");
  }

  @Override
  public void defaultCaseRef(Ref ref) {
    logger.debug("defaultCaseRef");
  }
}
