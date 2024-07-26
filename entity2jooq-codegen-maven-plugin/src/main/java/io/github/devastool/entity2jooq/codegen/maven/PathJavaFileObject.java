/*
 *    Copyright 2024 All entity2jooq contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.devastool.entity2jooq.codegen.maven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

/**
 * Simple realization of {@link JavaFileObject} for source code loading by path.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class PathJavaFileObject extends SimpleJavaFileObject {
  private final Path path;

  /**
   * Constructs new instance of {@link PathJavaFileObject}.
   *
   * @param path source code path
   */
  public PathJavaFileObject(Path path) {
    super(path.toUri(), Kind.SOURCE);
    this.path = path;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return new String(Files.readAllBytes(path));
  }
}
