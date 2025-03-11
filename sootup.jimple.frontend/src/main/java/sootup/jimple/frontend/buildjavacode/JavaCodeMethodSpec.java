package sootup.jimple.frontend.buildjavacode;

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

import java.util.ArrayList;
import java.util.List;

public class JavaCodeMethodSpec {
  private static StringBuilder javaCodeMethodSignature = new StringBuilder();
  private final List<JavaCodeStmtSpec> statements = new ArrayList<>();
  private final List<String> modifiers = new ArrayList<>();

  public JavaCodeMethodSpec addMethodSignature(
      String clsSig, String methodName, String returnType, String parameters) {
    javaCodeMethodSignature
        .append("MethodSignature methodSignature = view.getIdentifierFactory().getMethodSignature(")
        .append("\"")
        .append(clsSig)
        .append("\"")
        .append("\"")
        .append(methodName)
        .append("\"")
        .append("\"")
        .append(returnType)
        .append("\"")
        .append("\"")
        .append(parameters)
        .append("\"")
        .append(");");
    return this;
  }

  public JavaCodeMethodSpec addModifier(String modifier) {
    if (modifier != null && !modifier.isEmpty()) {
      modifiers.add("MethodModifier." + modifier.toUpperCase());
    }
    return this;
  }

  public JavaCodeMethodSpec addStatement(JavaCodeStmtSpec statement) {
    statements.add(statement);
    return this;
  }

  public String build() {
    StringBuilder builder = new StringBuilder();
    String modifiersString = String.join(", ", modifiers);

    builder
        .append("JavaSootMethod dummyMainMethod = new JavaSootMethod(")
        .append("new OverridingBodySource(methodSignature, body),")
        .append("methodSignature,")
        .append("EnumSet.of(")
        .append(modifiersString)
        .append("),")
        .append("Collections.emptyList(),")
        .append("Collections.emptyList(),")
        .append("NoPositionInformation.getInstance());");

    return builder.toString();
  }
}
