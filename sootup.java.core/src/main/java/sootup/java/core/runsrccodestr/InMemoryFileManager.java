package sootup.java.core.runsrccodestr;

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

import java.util.Hashtable;
import java.util.Map;
import javax.tools.*;

public class InMemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
  private Map<String, JavaClassAsBytes> compiledClasses;
  private ClassLoader loader;

  public InMemoryFileManager(StandardJavaFileManager standardManager) {
    super(standardManager);
    this.compiledClasses = new Hashtable<>();
    this.loader = new InMemoryClassLoader(this.getClass().getClassLoader(), this);
  }

  @Override
  public JavaFileObject getJavaFileForOutput(
      Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {

    JavaClassAsBytes classAsBytes = new JavaClassAsBytes(className, kind);
    compiledClasses.put(className, classAsBytes);

    return classAsBytes;
  }

  public Map<String, JavaClassAsBytes> getBytesMap() {
    return compiledClasses;
  }

  @Override
  public ClassLoader getClassLoader(Location location) {
    return loader;
  }
}
