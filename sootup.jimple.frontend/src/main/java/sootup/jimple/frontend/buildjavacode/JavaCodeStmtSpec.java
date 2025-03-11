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

public class JavaCodeStmtSpec {
  private final String stmt;

  private JavaCodeStmtSpec(String stmt) {
    this.stmt = stmt;
  }

  public static JavaCodeStmtSpec identity(String variable, String type) {
    return new JavaCodeStmtSpec(variable + " := @" + type);
  }

  public static JavaCodeStmtSpec assign(String variable, String value) {
    return new JavaCodeStmtSpec(variable + " = " + value + ";");
  }

  public static JavaCodeStmtSpec ifStmt(String condition) {
    return new JavaCodeStmtSpec("if (" + condition + ") {");
  }

  public static JavaCodeStmtSpec endBlock() {
    return new JavaCodeStmtSpec("}");
  }

  public String build() {
    return stmt;
  }
}
