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

public class JavaCodeClassSpec {
  private final List<String> modifiers = new ArrayList<>();
  private final List<JavaCodeMethodSpec> methods = new ArrayList<>();

  public JavaCodeClassSpec addModifier(String modifier) {
    if (modifier != null && !modifier.isEmpty()) {
      modifiers.add("MethodModifier." + modifier.toUpperCase());
    }
    return this;
  }

  public JavaCodeClassSpec addMethod(JavaCodeMethodSpec javaCodeMethodSpec) {
    methods.add(javaCodeMethodSpec);
    return this;
  }

  public String build() {
    StringBuilder builder = new StringBuilder();
    String modifiersString = String.join(", ", modifiers);
    builder
        .append("JavaSootClass mainClass = new JavaSootClass(new OverridingJavaClassSource(")
        .append("new EagerInputLocation(),")
        .append("null,")
        .append("view.getIdentifierFactory().getClassType(\"dummyMain\"),")
        .append("null,")
        .append("Collections.emptySet(),")
        .append("null,")
        .append("Collections.emptySet(),")
        .append("Collections.singleton(dummyMainMethod),")
        .append("NoPositionInformation.getInstance(),")
        .append("EnumSet.of(")
        .append(modifiersString)
        .append("),")
        .append("Collections.emptyList(),")
        .append("Collections.emptyList(),")
        .append("Collections.emptyList()),")
        .append("SourceType.Application);");

    return builder.toString();
  }
}
