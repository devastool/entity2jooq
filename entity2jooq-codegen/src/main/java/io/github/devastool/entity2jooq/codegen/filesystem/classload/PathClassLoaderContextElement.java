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

package io.github.devastool.entity2jooq.codegen.filesystem.classload;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Information about path of class file.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class PathClassLoaderContextElement {
  private final Path root;
  private final Path classFile;

  private static final String EMPTY = "";
  private static final String PACKAGE_SEPARATOR = ".";
  private static final String FILE_EXTENSION = ".class";
  private static final String PATH_SEPARATOR = getPathSeparator();

  /**
   * Constructs new instance of {@link PathClassLoaderContextElement}.
   *
   * @param root      directory that containing this element
   * @param classFile path to class file
   */
  public PathClassLoaderContextElement(Path root, Path classFile) {
    this.root = root;
    this.classFile = classFile;
  }

  /**
   * Returns full name of the class, example: 'my.package.MyClass'.
   *
   * @return full name of the class
   */
  public String getCanonicalClassName() {
    return root
        .relativize(classFile)
        .toString()
        .replace(PATH_SEPARATOR, PACKAGE_SEPARATOR)
        .replace(FILE_EXTENSION, EMPTY);
  }

  /**
   * Returns simple name of the class, example: 'MyClass'.
   *
   * @return simple name of the class
   */
  public String getSimpleClassName() {
    return classFile
        .toString()
        .replace(FILE_EXTENSION, EMPTY);
  }

  /**
   * Returns data of the class.
   *
   * @return data of the class
   * @throws RuntimeException when loading of data fails
   */
  public byte[] getClassData() throws RuntimeException {
    try {
      return Files.readAllBytes(classFile);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  // Returns path's separator of current os
  private static String getPathSeparator() {
    FileSystem fileSystem = FileSystems.getDefault();
    return fileSystem.getSeparator();
  }
}
