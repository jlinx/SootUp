package sootup.java.bytecode.frontend.runstrcode;

import java.net.URI;
import java.util.Objects;
import javax.tools.SimpleJavaFileObject;

public class JavaSrcCodeFromString extends SimpleJavaFileObject {
  private String sourceCode;

  public JavaSrcCodeFromString(String name, String sourceCode) {
    super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
    this.sourceCode = Objects.requireNonNull(sourceCode, "sourceCode must not be null");
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    return sourceCode;
  }
}
