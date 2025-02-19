package sootup.jimple.frontend.buildjavacode;

import sootup.core.jimple.basic.Local;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.constant.*;
import sootup.core.jimple.common.expr.*;
import sootup.core.jimple.common.ref.*;
import sootup.core.jimple.visitor.ValueVisitor;
import sootup.core.jimple.visitor.Visitor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class StmtValueVisitor implements ValueVisitor, Visitor {

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
        System.out.println("defaultCaseValue");
    }

    @Override
    public void caseLocal(@Nonnull Local local) {
        if (!valueGenStr.containsKey(local)) {
            String localVarName = "local" + valueCounter++;
            valueVarName.put(local, localVarName);
            String localStr = String.format(
                    "Local %s = JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"));",
                    localVarName, local.getName(), local.getType());
            valueGenStr.put(local, localStr);
        }
        System.out.println("caseLocal");
    }

    @Override
    public void caseBooleanConstant(@Nonnull BooleanConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String boolVarName = "bool" + valueCounter++;
            valueVarName.put(constant, boolVarName);
            String boolConstStr = String.format("BooleanConstant %s = new BooleanConstant(%s);", boolVarName, constant);
            valueGenStr.put(constant, boolConstStr);
        }
        System.out.println("caseBooleanConstant");
    }

    @Override
    public void caseDoubleConstant(@Nonnull DoubleConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String doubleVarName = "double" + valueCounter++;
            valueVarName.put(constant, doubleVarName);
            String doubleConstStr = String.format("DoubleConstant %s = DoubleConstant.getInstance(%s);", doubleVarName, constant.getValue());
            valueGenStr.put(constant, doubleConstStr);
        }
        System.out.println("caseDoubleConstant");
    }

    @Override
    public void caseFloatConstant(@Nonnull FloatConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String floatVarName = "float" + valueCounter++;
            valueVarName.put(constant, floatVarName);
            String floatConstStr = String.format("FloatConstant %s = FloatConstant.getInstance(%s);", floatVarName, constant.getValue());
            valueGenStr.put(constant, floatConstStr);
        }
        System.out.println("caseFloatConstant");
    }

    @Override
    public void caseIntConstant(@Nonnull IntConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String intVarName = "int" + valueCounter++;
            valueVarName.put(constant, intVarName);
            String intConstStr = String.format("IntConstant %s = IntConstant.getInstance(%s);", intVarName, constant.getValue());
            valueGenStr.put(constant, intConstStr);
        }
        System.out.println("caseIntConstant");
    }

    @Override
    public void caseLongConstant(@Nonnull LongConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String longVarName = "long" + valueCounter++;
            valueVarName.put(constant, longVarName);
            String longConstStr = String.format("LongConstant %s = LongConstant.getInstance(%s);", longVarName, constant.getValue());
            valueGenStr.put(constant, longConstStr);
        }
        System.out.println("caseLongConstant");
    }

    @Override
    public void caseNullConstant(@Nonnull NullConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String nullVarName = "null" + valueCounter++;
            valueVarName.put(constant, nullVarName);
            String nullConstStr = String.format("NullConstant %s = NullConstant.getInstance();", nullVarName);
            valueGenStr.put(constant, nullConstStr);
        }
        System.out.println("caseNullConstant");
    }

    @Override
    public void caseStringConstant(@Nonnull StringConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String strVarName = "str" + valueCounter++;
            valueVarName.put(constant, strVarName);
            String strConstStr = String.format("StringConstant %s = JavaJimple.getInstance().newStringConstant(\"%s\");", strVarName, constant.getValue());
            valueGenStr.put(constant, strConstStr);
        }
        System.out.println("caseStringConstant");
    }

    @Override
    public void caseEnumConstant(@Nonnull EnumConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String enumVarName = "enum" + valueCounter++;
            valueVarName.put(constant, enumVarName);
            String enumConstStr = String.format("EnumConstant %s = JavaJimple.getInstance().newEnumConstant(\"%s\");", enumVarName, constant.getValue());
            valueGenStr.put(constant, enumConstStr);
        }
        System.out.println("caseEnumConstant");
    }

    @Override
    public void caseClassConstant(@Nonnull ClassConstant constant) {
        if (!valueGenStr.containsKey(constant)) {
            String clsVarName = "cls" + valueCounter++;
            valueVarName.put(constant, clsVarName);
            String classConstStr = String.format("ClassConstant %s = JavaJimple.getInstance().newClassConstant(\"%s\");", clsVarName, constant.getValue());
            valueGenStr.put(constant, classConstStr);
        }
        System.out.println("caseClassConstant");
    }

    @Override
    public void caseMethodHandle(@Nonnull MethodHandle handle) {
        if (!valueGenStr.containsKey(handle)) {
            String methodHandleVarName = "methodHandle" + valueCounter++;
            valueVarName.put(handle, methodHandleVarName);
            String methodHandleStr = String.format("MethodHandle %s = JavaJimple.getInstance().newMethodHandle(%s, %s);", methodHandleVarName, handle.getReferenceSignature(), handle.getKind().toString());
            valueGenStr.put(handle, methodHandleStr);
        }
        System.out.println("caseMethodHandle");
    }

    @Override
    public void caseMethodType(@Nonnull MethodType methodType) {
        if (!valueGenStr.containsKey(methodType)) {
            String methodTypeVarName = "methodType" + valueCounter++;
            valueVarName.put(methodType, methodTypeVarName);
            String methodTypeStr = String.format("MethodType %s = new MethodType(%s, %s);", methodTypeVarName, methodType.getReturnType(), methodType.hashCode());
            valueGenStr.put(methodType, methodTypeStr);
        }
        System.out.println("caseMethodType");
    }

    @Override
    public void defaultCaseConstant(@Nonnull Constant constant) {
        System.out.println("defaultCaseConstant");
    }

    @Override
    public void caseAddExpr(JAddExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseAddExpr");
    }

    @Override
    public void caseAndExpr(JAndExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseAndExpr");
    }

    @Override
    public void caseCmpExpr(JCmpExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseCmpExpr");
    }

    @Override
    public void caseCmpgExpr(JCmpgExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseCmpgExpr");
    }

    @Override
    public void caseCmplExpr(JCmplExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseCmplExpr");
    }

    @Override
    public void caseDivExpr(JDivExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseDivExpr");
    }

    @Override
    public void caseEqExpr(JEqExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseEqExpr");
    }

    @Override
    public void caseNeExpr(JNeExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseNeExpr");
    }

    @Override
    public void caseGeExpr(JGeExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseGeExpr");
    }

    @Override
    public void caseGtExpr(JGtExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseGtExpr");
    }

    @Override
    public void caseLeExpr(JLeExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseLeExpr");
    }

    @Override
    public void caseLtExpr(JLtExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseLtExpr");
    }

    @Override
    public void caseMulExpr(JMulExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseMulExpr");
    }

    @Override
    public void caseOrExpr(JOrExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseOrExpr");
    }

    @Override
    public void caseRemExpr(JRemExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseRemExpr");
    }

    @Override
    public void caseShlExpr(JShlExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseShlExpr");
    }

    @Override
    public void caseShrExpr(JShrExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseShrExpr");
    }

    @Override
    public void caseUshrExpr(JUshrExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseUshrExpr");
    }

    @Override
    public void caseSubExpr(JSubExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseSubExpr");
    }

    @Override
    public void caseXorExpr(JXorExpr expr) {
        expr.getOp1().accept(this);
        expr.getOp2().accept(this);
        System.out.println("caseXorExpr");
    }

    @Override
    public void caseSpecialInvokeExpr(JSpecialInvokeExpr expr) {
        expr.getBase().accept(this);
        expr.getArgs().forEach(args -> args.accept(this));
        System.out.println("caseSpecialInvokeExpr");
    }

    @Override
    public void caseVirtualInvokeExpr(JVirtualInvokeExpr expr) {
        expr.getBase().accept(this);
        expr.getArgs().forEach(args -> args.accept(this));
        System.out.println("caseVirtualInvokeExpr");
    }

    @Override
    public void caseInterfaceInvokeExpr(JInterfaceInvokeExpr expr) {
        expr.getBase().accept(this);
        expr.getArgs().forEach(args -> args.accept(this));
        System.out.println("caseInterfaceInvokeExpr");
    }

    @Override
    public void caseStaticInvokeExpr(JStaticInvokeExpr expr) {
        expr.getArgs().forEach(args -> args.accept(this));
        System.out.println("caseStaticInvokeExpr");
    }

    @Override
    public void caseDynamicInvokeExpr(JDynamicInvokeExpr expr) {
        expr.getBootstrapArgs().forEach(bootargs -> bootargs.accept(this));
        expr.getArgs().forEach(args -> args.accept(this));
        System.out.println("caseDynamicInvokeExpr");
    }

    @Override
    public void caseCastExpr(JCastExpr expr) {
        expr.getOp().accept(this);
        System.out.println("caseCastExpr");
    }

    @Override
    public void caseInstanceOfExpr(JInstanceOfExpr expr) {
        expr.getOp().accept(this);
        System.out.println("caseInstanceOfExpr");
    }

    @Override
    public void caseNewArrayExpr(JNewArrayExpr expr) {
        expr.getSize().accept(this);
        System.out.println("caseNewArrayExpr");
    }

    @Override
    public void caseNewMultiArrayExpr(JNewMultiArrayExpr expr) {
        expr.getSizes().forEach(size-> size.accept(this));
        System.out.println("caseNewMultiArrayExpr");
    }

    @Override
    public void caseNewExpr(JNewExpr expr) {
        System.out.println("caseNewExpr");
    }

    @Override
    public void caseLengthExpr(JLengthExpr expr) {
        expr.getOp().accept(this);
        System.out.println("caseLengthExpr");
    }

    @Override
    public void caseNegExpr(JNegExpr expr) {
        expr.getOp().accept(this);
        System.out.println("caseNegExpr");
    }

    @Override
    public void casePhiExpr(JPhiExpr v) {
        v.getArgs().forEach(arg -> arg.accept(this));
        System.out.println("casePhiExpr");
    }

    @Override
    public void defaultCaseExpr(Expr expr) {
        System.out.println("defaultCaseExpr");
    }

    @Override
    public void caseStaticFieldRef(JStaticFieldRef ref) {
        if (!valueGenStr.containsKey(ref)) {
            int i = valueCounter++;
            String staticFieldRefVarName = "staticFieldRef" + i;
            valueVarName.put(ref, staticFieldRefVarName);
            String fieldSigVarName = "fieldSig" + i;
            String fieldSigStr = String.format("FieldSignature %s = view.getIdentifierFactory().getFieldSignature(\"%s\", JavaIdentifierFactory.getInstance().getClassType(\"%s\"), \"%s\");",
                    fieldSigVarName, ref.getFieldSignature().getName(), ref.getFieldSignature().getDeclClassType(), ref.getType());
            String staticFieldRefStr = String.format("JStaticFieldRef %s = Jimple.newStaticFieldRef(%s);", staticFieldRefVarName, fieldSigVarName);
            valueGenStr.put(ref, String.join("\n", fieldSigStr, staticFieldRefStr));
        }
        System.out.println("caseStaticFieldRef");
    }

    @Override
    public void caseInstanceFieldRef(JInstanceFieldRef ref) {
        ref.getBase().accept(this);
        System.out.println("caseInstanceFieldRef");
    }

    @Override
    public void caseArrayRef(JArrayRef ref) {
        ref.getBase().accept(this);
        ref.getIndex().accept(this);
        System.out.println("caseArrayRef");
    }

    @Override
    public void caseParameterRef(JParameterRef ref) {
        if (!valueGenStr.containsKey(ref)) {
            String parameterRefVarName = "parameterRef" + valueCounter++;
            valueVarName.put(ref, parameterRefVarName);
            String parameterRefStr = String.format("JParameterRef %s = new JParameterRef(JavaIdentifierFactory.getInstance().getType(\"%s\"), %s);", parameterRefVarName, ref.getType(), ref.getIndex());
            valueGenStr.put(ref, parameterRefStr);
        }
        System.out.println("caseParameterRef");
    }

    @Override
    public void caseCaughtExceptionRef(JCaughtExceptionRef ref) {
        if (!valueGenStr.containsKey(ref)) {
            String caughtExceptionRefVarName = "caughtException" + valueCounter++;
            valueVarName.put(ref, caughtExceptionRefVarName);
            String caughtExceptionRefStr = String.format("JCaughtExceptionRef %s = new JCaughtExceptionRef(JavaIdentifierFactory.getInstance().getType(\"java.lang.Throwable\"));", caughtExceptionRefVarName);
            valueGenStr.put(ref, caughtExceptionRefStr);
        }
        System.out.println("caseCaughtExceptionRef");
    }

    @Override
    public void caseThisRef(JThisRef ref) {
        if (!valueGenStr.containsKey(ref)) {
            String thisRefVarName = "thisRef" + valueCounter++;
            valueVarName.put(ref, thisRefVarName);
            String thisRefStr = String.format("JThisRef %s = new JThisRef(JavaIdentifierFactory.getInstance().getType(\"%s\"));", thisRefVarName, ref.getType());
            valueGenStr.put(ref, thisRefStr);
        }
        System.out.println("caseThisRef");
    }

    @Override
    public void defaultCaseRef(Ref ref) {
        System.out.println("defaultCaseRef");
    }
}
