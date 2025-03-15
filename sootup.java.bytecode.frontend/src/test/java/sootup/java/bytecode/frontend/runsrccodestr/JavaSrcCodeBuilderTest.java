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
import sootup.core.graph.StmtGraph;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;
import sootup.core.model.SourceType;
import sootup.interceptors.TypeAssigner;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaSootClass;
import sootup.java.core.JavaSootMethod;
import sootup.java.core.buildsrccode.JavaCodeStmtVisitor;
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
        // has to be called at last when all stmts are visited
        javaCodeStmtVisitor.createStmtGraph(sm.getBody());
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
    ;
    String sourceCode =
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
