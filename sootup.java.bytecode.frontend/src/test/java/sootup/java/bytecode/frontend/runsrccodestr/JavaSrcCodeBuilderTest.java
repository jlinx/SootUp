package sootup.java.bytecode.frontend.runsrccodestr;

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

import categories.TestCategories;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sootup.core.graph.BasicBlock;
import sootup.core.graph.MutableStmtGraph;
import sootup.core.graph.StmtGraph;
import sootup.core.inputlocation.EagerInputLocation;
import sootup.core.jimple.Jimple;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.basic.StmtPositionInfo;
import sootup.core.jimple.common.constant.IntConstant;
import sootup.core.jimple.common.expr.JGeExpr;
import sootup.core.jimple.common.ref.JParameterRef;
import sootup.core.jimple.common.ref.JThisRef;
import sootup.core.jimple.common.stmt.*;
import sootup.core.model.Body;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.interceptors.TypeAssigner;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaSootMethod;
import sootup.java.core.buildsrccode.JavaCodeStmtVisitor;
import sootup.java.core.language.JavaJimple;
import sootup.java.core.runsrccodestr.InMemoryClass;
import sootup.java.core.runsrccodestr.InMemoryFileManager;
import sootup.java.core.runsrccodestr.JavaSrcCodeFromString;
import sootup.java.core.views.JavaView;

@Tag(TestCategories.JAVA_8_CATEGORY)
public class JavaSrcCodeBuilderTest {

  @Test
  public void testStmtGraphToJavaSrcCodeObjectsPrinter()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String classPath = "../shared-test-resources/miniTestSuite/java6/binary";
    JavaClassPathAnalysisInputLocation inputLocation =
        new JavaClassPathAnalysisInputLocation(
            classPath, SourceType.Application, Collections.singletonList(new TypeAssigner()));
    JavaView view = new JavaView(inputLocation);
    List<JavaSootClass> javaSootClassList =
        view.getClasses()
            .filter(cls -> cls.getName().equals("StringConcatenation"))
            .collect(Collectors.toList());
    // System.out.println(javaSootClassList);
    List<JavaSootMethod> javaSootMethods =
        javaSootClassList.get(0).getMethods().stream()
            .filter(sm -> sm.getName().equals("stringConcatenation"))
            .collect(Collectors.toList());
    for (SootMethod sm : javaSootMethods) {
      JavaCodeStmtVisitor javaCodeStmtVisitor = new JavaCodeStmtVisitor(sm.getBody());
      StmtGraph<?> bodyStmtGraph = sm.getBody().getStmtGraph();
      Iterator<BasicBlock<?>> bodyStmtGraphBlkIt = bodyStmtGraph.getBlockIterator();
      while (bodyStmtGraphBlkIt.hasNext()) {
        BasicBlock<?> block = bodyStmtGraphBlkIt.next();
        List<Stmt> blockStmts = block.getStmts();
        for (Stmt blockStmt : blockStmts) {
          blockStmt.accept(javaCodeStmtVisitor);
        }
      }
      // has to be called at last when all stmts are visited
      javaCodeStmtVisitor.createStmtGraph(sm.getBody());
      // javaCodeStmtVisitor.getJavaCodeObjects().forEach(System.out::println);
      Assertions.assertEquals(
          bodyStmtGraph.toString(),
          whenStrIsCompiled_ThenCodeShouldExecute(javaCodeStmtVisitor.getJavaCodeObjects()));
    }
  }

  @Test
  public void testStmtGraphToJavaSrcCodeObjects_ifElse()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String classPath = "../shared-test-resources/miniTestSuite/java6/binary";
    JavaClassPathAnalysisInputLocation inputLocation =
        new JavaClassPathAnalysisInputLocation(
            classPath, SourceType.Application, Collections.singletonList(new TypeAssigner()));
    JavaView view = new JavaView(inputLocation);
    List<JavaSootClass> javaSootClassList =
        view.getClasses()
            .filter(cls -> cls.getName().equals("IfElseStatement"))
            .collect(Collectors.toList());
    // System.out.println(javaSootClassList);
    List<JavaSootMethod> javaSootMethods =
        javaSootClassList.get(0).getMethods().stream()
            .filter(sm -> sm.getName().equals("ifElseCascadingStatement"))
            .collect(Collectors.toList());
    for (SootMethod sm : javaSootMethods) {
      JavaCodeStmtVisitor javaCodeStmtVisitor = new JavaCodeStmtVisitor(sm.getBody());
      StmtGraph<?> bodyStmtGraph = sm.getBody().getStmtGraph();
      Iterator<BasicBlock<?>> bodyStmtGraphBlkIt = bodyStmtGraph.getBlockIterator();
      while (bodyStmtGraphBlkIt.hasNext()) {
        BasicBlock<?> block = bodyStmtGraphBlkIt.next();
        List<Stmt> blockStmts = block.getStmts();
        for (Stmt blockStmt : blockStmts) {
          blockStmt.accept(javaCodeStmtVisitor);
        }
      }

      // has to be called at last when all stmts are visited
      javaCodeStmtVisitor.createStmtGraph(sm.getBody());
      javaCodeStmtVisitor.getJavaCodeObjects().forEach(System.out::println);
      Assertions.assertEquals(
          bodyStmtGraph.toString(),
          whenStrIsCompiled_ThenCodeShouldExecute(javaCodeStmtVisitor.getJavaCodeObjects()));
    }
  }

  @Test
  public void testcode() {
    JavaView view = new JavaView(Collections.singletonList(new EagerInputLocation()));
    Body.BodyBuilder bodyBuilder = Body.builder();
    JavaIdentifierFactory factory = JavaIdentifierFactory.getInstance();
    StmtPositionInfo noStmtPositionInfo = StmtPositionInfo.getNoStmtPositionInfo();
    Local local1 = JavaJimple.newLocal("this", factory.getClassType("IfElseStatement"));
    JThisRef thisRef2 = new JThisRef(factory.getClassType("IfElseStatement"));
    JIdentityStmt identity1 = new JIdentityStmt(local1, thisRef2, noStmtPositionInfo);
    Local local3 = JavaJimple.newLocal("l1", factory.getClassType("int"));
    JParameterRef parameterRef4 = new JParameterRef(factory.getType("int"), 0);
    JIdentityStmt identity2 = new JIdentityStmt(local3, parameterRef4, noStmtPositionInfo);
    Local local6 = JavaJimple.newLocal("l2", factory.getClassType("byte"));
    IntConstant int5 = IntConstant.getInstance(0);
    JAssignStmt assignment3 = JavaJimple.newAssignStmt(local6, int5, noStmtPositionInfo);
    IntConstant int7 = IntConstant.getInstance(42);
    JGeExpr geExpr8 = Jimple.newGeExpr(local3, int7);
    JIfStmt if4 = Jimple.newIfStmt(geExpr8, noStmtPositionInfo);
    JGeExpr geExpr9 = Jimple.newGeExpr(local3, int7);
    JIfStmt if5 = Jimple.newIfStmt(geExpr9, noStmtPositionInfo);
    IntConstant int10 = IntConstant.getInstance(11);
    JAssignStmt assignment6 = JavaJimple.newAssignStmt(local6, int10, noStmtPositionInfo);
    JGotoStmt goto7 = new JGotoStmt(noStmtPositionInfo);
    IntConstant int11 = IntConstant.getInstance(12);
    JAssignStmt assignment8 = JavaJimple.newAssignStmt(local6, int11, noStmtPositionInfo);
    JGotoStmt goto9 = new JGotoStmt(noStmtPositionInfo);
    IntConstant int12 = IntConstant.getInstance(3);
    JAssignStmt assignment10 = JavaJimple.newAssignStmt(local6, int12, noStmtPositionInfo);
    JReturnStmt return11 = new JReturnStmt(local6, noStmtPositionInfo);
    MutableStmtGraph stmtGraph = bodyBuilder.getStmtGraph();
    stmtGraph.setStartingStmt(identity1);
    stmtGraph.addNode(identity1);
    stmtGraph.addNode(identity2);
    stmtGraph.addNode(assignment3);
    stmtGraph.addNode(if4);
    stmtGraph.addNode(if5);
    stmtGraph.addNode(assignment6);
    stmtGraph.addNode(goto7);
    stmtGraph.addNode(assignment8);
    stmtGraph.addNode(goto9);
    stmtGraph.addNode(assignment10);
    stmtGraph.addNode(return11);
    System.out.println(stmtGraph);
  }

  public String whenStrIsCompiled_ThenCodeShouldExecute(Set<String> javaCodeObjects)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String dynamicStr = String.join("\n", javaCodeObjects);
    String sourceCode =
        // This changes according to the packageName of the class where test is written
        "package sootup.java.bytecode.frontend.runsrccodestr;\n"
            + "import java.util.*;\n"
            + "import sootup.core.graph.*;\n"
            + "import sootup.core.inputlocation.*;\n"
            + "import sootup.core.jimple.basic.*;\n"
            + "import sootup.core.jimple.common.ref.*;\n"
            + "import sootup.core.jimple.common.constant.*;\n"
            + "import sootup.core.jimple.common.stmt.*;\n"
            + "import sootup.core.model.*;\n"
            + "import sootup.java.core.JavaIdentifierFactory;\n"
            + "import sootup.java.core.language.JavaJimple;\n"
            + "import sootup.java.core.views.JavaView;\n"
            + "import sootup.java.core.runsrccodestr.InMemoryClass;\n"
            + "public class TestClass implements InMemoryClass {\n"
            + "@Override\n"
            + "    public String runCode() {\n"
            + "        "
            + dynamicStr
            + "\n"
            + "    }\n"
            + "}\n";
    // This changes according to the fullyQualifiedName of the class where test is written
    String qualifiedClassName = "sootup.java.bytecode.frontend.runsrccodestr.TestClass";

    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    InMemoryFileManager manager =
        new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));

    List<JavaFileObject> sourceFiles =
        Collections.singletonList(new JavaSrcCodeFromString(qualifiedClassName, sourceCode));
    JavaCompiler.CompilationTask task =
        compiler.getTask(null, manager, diagnostics, null, null, sourceFiles);
    boolean result = task.call();

    if (!result) {
      diagnostics.getDiagnostics().forEach(d -> logger.error(String.valueOf(d)));
    } else {
      ClassLoader classLoader = manager.getClassLoader(null);
      Class<?> clazz = classLoader.loadClass(qualifiedClassName);
      InMemoryClass instanceOfClass = (InMemoryClass) clazz.newInstance();
      Assertions.assertInstanceOf(InMemoryClass.class, instanceOfClass);
      return instanceOfClass.runCode();
    }
    return "ClassNotFoundException, InstantiationException, IllegalAccessException";
  }
}
