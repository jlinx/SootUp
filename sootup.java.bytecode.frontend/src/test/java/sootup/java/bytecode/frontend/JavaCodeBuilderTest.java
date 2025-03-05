package sootup.java.bytecode.frontend;

import categories.TestCategories;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
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
import sootup.java.core.views.JavaView;
import sootup.jimple.frontend.buildjavacode.JavaCodeStmtVisitor;

@Tag(TestCategories.JAVA_8_CATEGORY)
public class JavaCodeBuilderTest {

  @Test
  public void testSootClassToJavaObjectPrinter() {
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
        javaCodeStmtVisitor.getJavaCodeObjects().forEach(System.out::println);
      }
    }
  }
}
