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

    Map<String, String> valueGenStr = new HashMap<>();

    public Map<String, String> getValueGenStr() {
        return valueGenStr;
    }

    @Override
    public void defaultCaseValue(@Nonnull Value v) {
        System.out.println("defaultCaseValue");
    }

    @Override
    public void caseLocal(@Nonnull Local local) {
        String localStr = String.format(
                "Local %s = JavaJimple.newLocal(\"%s\", factory.getClassType(\"%s\"));",
                local.getName(), local.getName(), local.getType());
        valueGenStr.put(local.getName(), localStr);
        System.out.println("caseLocal");
    }

    @Override
    public void caseBooleanConstant(@Nonnull BooleanConstant constant) {
        String boolConstStr = String.format("BooleanConstant %s = new BooleanConstant(%s);", constant, constant);
        valueGenStr.put(constant.toString(), boolConstStr);
        System.out.println("caseBooleanConstant");
    }

    @Override
    public void caseDoubleConstant(@Nonnull DoubleConstant constant) {
        String doubleConstStr = String.format("DoubleConstant %s = DoubleConstant.getInstance(%s);", constant, constant.getValue());
        valueGenStr.put(constant.toString(), doubleConstStr);
        System.out.println("caseDoubleConstant");
    }

    @Override
    public void caseFloatConstant(@Nonnull FloatConstant constant) {
        String floatConstStr = String.format("FloatConstant %s = FloatConstant.getInstance(%s);", constant, constant.getValue());
        valueGenStr.put(constant.toString(), floatConstStr);
        System.out.println("caseFloatConstant");
    }

    @Override
    public void caseIntConstant(@Nonnull IntConstant constant) {
        String intConstStr = String.format("IntConstant %s = IntConstant.getInstance(%s);", constant, constant.getValue());
        valueGenStr.put(constant.toString(), intConstStr);
        System.out.println("caseIntConstant");
    }

    @Override
    public void caseLongConstant(@Nonnull LongConstant constant) {
        String longConstStr = String.format("LongConstant %s = LongConstant.getInstance(%s);", constant, constant.getValue());
        valueGenStr.put(constant.toString(), longConstStr);
        System.out.println("caseLongConstant");
    }

    @Override
    public void caseNullConstant(@Nonnull NullConstant constant) {
        String nullConstStr = String.format("NullConstant %s = NullConstant.getInstance();", constant);
        valueGenStr.put(constant.toString(), nullConstStr);
        System.out.println("caseNullConstant");
    }

    @Override
    public void caseStringConstant(@Nonnull StringConstant constant) {
        String strConstStr = String.format("StringConstant %s = JavaJimple.getInstance().newStringConstant(\"%s\");", constant, constant.getValue());
        valueGenStr.put(constant.toString(), strConstStr);
        System.out.println("caseStringConstant");
    }

    @Override
    public void caseEnumConstant(@Nonnull EnumConstant constant) {
        String enumConstStr = String.format("EnumConstant %s = JavaJimple.getInstance().newEnumConstant(\"%s\");", constant, constant.getValue());
        valueGenStr.put(constant.toString(), enumConstStr);
        System.out.println("caseEnumConstant");
    }

    @Override
    public void caseClassConstant(@Nonnull ClassConstant constant) {
        String classConstStr = String.format("ClassConstant %s = JavaJimple.getInstance().newClassConstant(\"%s\");", constant, constant.getValue());
        valueGenStr.put(constant.toString(), classConstStr);
        System.out.println("caseClassConstant");
    }

    @Override
    public void caseMethodHandle(@Nonnull MethodHandle handle) {
        String methodHandleStr = String.format("MethodHandle %s = JavaJimple.getInstance().newMethodHandle(%s, %s);", handle, handle.getReferenceSignature(), handle.getKind().toString());
        valueGenStr.put(handle.toString(), methodHandleStr);
        System.out.println("caseMethodHandle");
    }

    @Override
    public void caseMethodType(@Nonnull MethodType methodType) {
        String methodTypeStr = String.format("MethodType %s = new MethodType(%s, %s);", methodType, methodType.getReturnType(), methodType.hashCode());
        valueGenStr.put(methodType.toString(), methodTypeStr);
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
        System.out.println("caseParameterRef");
    }

    @Override
    public void caseCaughtExceptionRef(JCaughtExceptionRef ref) {
        String caughtExceptionRefStr = String.format("JCaughtExceptionRef %s = new JCaughtExceptionRef(JavaIdentifierFactory.getInstance().getType(\"java.lang.Throwable\"));", ref.toString());
        valueGenStr.put(ref.toString(), caughtExceptionRefStr);
        System.out.println("caseCaughtExceptionRef");
    }

    @Override
    public void caseThisRef(JThisRef ref) {
        System.out.println("caseThisRef");
    }

    @Override
    public void defaultCaseRef(Ref ref) {
        System.out.println("defaultCaseRef");
    }
}
