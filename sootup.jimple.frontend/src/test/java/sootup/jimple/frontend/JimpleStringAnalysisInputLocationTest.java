package sootup.jimple.frontend;

/*-
 * #%L
 * Soot
 * %%
 * Copyright (C) 2018-2024 Markus Schmidt
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sootup.core.graph.BasicBlock;
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.ClassType;
import sootup.core.types.VoidType;
import sootup.core.views.View;
import sootup.interceptors.DeadAssignmentEliminator;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.buildsrccode.JavaCodeStmtVisitor;
import sootup.java.core.runsrccodestr.InMemoryClass;
import sootup.java.core.runsrccodestr.InMemoryFileManager;
import sootup.java.core.runsrccodestr.JavaSrcCodeFromString;

@Tag("Java8")
public class JimpleStringAnalysisInputLocationTest {

  @Test
  public void testInvalidInput() {
    String methodStr = "This is not Jimple its just a Sentence.";
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          JimpleStringAnalysisInputLocation analysisInputLocation =
              new JimpleStringAnalysisInputLocation(methodStr);
          JimpleView view = new JimpleView(analysisInputLocation);
          analysisInputLocation.getClassSources(view);
        });
  }

  @Test
  public void test() {

    String methodStr =
        "class DummyClass extends java.lang.Object {\n\t"
            + "void banana(){\n\t\t"
            + "params = new java.security.AlgorithmParameters;\n\t\t"
            + "return;\n\t"
            + "}\n"
            + "}";

    JimpleStringAnalysisInputLocation analysisInputLocation =
        new JimpleStringAnalysisInputLocation(
            methodStr,
            SourceType.Application,
            Collections.singletonList(new DeadAssignmentEliminator()));

    View view = new JimpleView(Collections.singletonList(analysisInputLocation));
    assertNotNull(view.getIdentifierFactory().getClassType("DummyClass"));

    MethodSignature methodSig =
        view.getIdentifierFactory()
            .getMethodSignature(
                view.getIdentifierFactory().getClassType("DummyClass"),
                "banana",
                VoidType.getInstance(),
                Collections.emptyList());
    assertTrue(view.getMethod(methodSig).isPresent());
  }

  @Test
  public void testJimpleJavaObjectPrinter()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String jimpleString =
        "public class JimpleJavaObjectPrinter extends java.lang.Object\n"
            + "{\n"
            + "    int tc1()\n"
            + "    {\n"
            + "        byte b0, b1;\n"
            + "        java.io.PrintStream r0;\n"
            + "        JB_CP r1;\n"
            + "\n"
            + "        r1 := @this: JB_CP;\n"
            + "        b0 = 5;\n"
            + "        b1 = b0;\n"
            + "        r0 = <java.lang.System: java.io.PrintStream out>;\n"
            + "        virtualinvoke r0.<java.io.PrintStream: void println(int)>(b1);\n"
            + "\n"
            + "        return b1;\n"
            + "    }\n"
            + "}";

    JimpleStringAnalysisInputLocation analysisInputLocation =
        new JimpleStringAnalysisInputLocation(
            jimpleString,
            SourceType.Application,
            Collections.singletonList(new DeadAssignmentEliminator()));

    View view = new JimpleView(Collections.singletonList(analysisInputLocation));
    ClassType jimpleJavaObjectPrinter =
        view.getIdentifierFactory().getClassType("JimpleJavaObjectPrinter");
    assertNotNull(jimpleJavaObjectPrinter);
    if (view.getClass(jimpleJavaObjectPrinter).isPresent()) {
      SootClass sc = view.getClass(jimpleJavaObjectPrinter).get();
      MethodSignature methodSignature =
          JavaIdentifierFactory.getInstance()
              .getMethodSignature(jimpleJavaObjectPrinter, "tc1", "int", Collections.emptyList());
      Optional<? extends SootMethod> method = sc.getMethod(methodSignature.getSubSignature());
      if (method.isPresent()) {
        Body body = method.get().getBody();
        JavaCodeStmtVisitor javaCodeStmtVisitor = new JavaCodeStmtVisitor(body);
        StmtGraph<?> bodyStmtGraph = body.getStmtGraph();
        Iterator<BasicBlock<?>> bodyStmtGraphBlkIt = bodyStmtGraph.getBlockIterator();
        while (bodyStmtGraphBlkIt.hasNext()) {
          BasicBlock<?> block = bodyStmtGraphBlkIt.next();
          List<Stmt> blockStmts = block.getStmts();
          for (Stmt blockStmt : blockStmts) {
            blockStmt.accept(javaCodeStmtVisitor);
          }
        }
        // has to be called at last when all stmts are visited
        javaCodeStmtVisitor.createStmtGraph(body);
        // javaCodeStmtVisitor.getJavaCodeObjects().forEach(System.out::println);
        Assertions.assertEquals(
            bodyStmtGraph.toString(),
            whenStrIsCompiled_ThenCodeShouldExecute(javaCodeStmtVisitor.getJavaCodeObjects()));
      }
    }
  }

  public String whenStrIsCompiled_ThenCodeShouldExecute(Set<String> javaCodeObjects)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    String dynamicStr = String.join("\n", javaCodeObjects);
    String sourceCode =
        // This changes according to the packageName of the class where test is written
        "package sootup.jimple.frontend;\n"
            + "import java.util.*;\n"
            + "import sootup.core.graph.*;\n"
            + "import sootup.core.signatures.*;\n"
            + "import sootup.core.jimple.*;\n"
            + "import sootup.core.inputlocation.*;\n"
            + "import sootup.core.jimple.basic.*;\n"
            + "import sootup.core.jimple.common.ref.*;\n"
            + "import sootup.core.jimple.common.constant.*;\n"
            + "import sootup.core.jimple.common.stmt.*;\n"
            + "import sootup.core.jimple.common.expr.*;\n"
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
    String qualifiedClassName = "sootup.jimple.frontend.TestClass";

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
