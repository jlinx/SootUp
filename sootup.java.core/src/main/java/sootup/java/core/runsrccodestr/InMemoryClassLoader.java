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

import java.util.Map;
import java.util.Objects;

public class InMemoryClassLoader extends ClassLoader {

  private InMemoryFileManager manager;

  public InMemoryClassLoader(ClassLoader parent, InMemoryFileManager manager) {
    super(parent);
    this.manager = Objects.requireNonNull(manager, "manager must not be null");
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    Map<String, JavaClassAsBytes> compiledClasses = manager.getBytesMap();
    if (compiledClasses.containsKey(name)) {
      byte[] bytes = compiledClasses.get(name).getBytes();
      return defineClass(name, bytes, 0, bytes.length);
    } else {
      throw new ClassNotFoundException();
    }
  }
}
