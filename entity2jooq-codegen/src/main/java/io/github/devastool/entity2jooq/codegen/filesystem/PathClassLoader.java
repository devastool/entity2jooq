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

package io.github.devastool.entity2jooq.codegen.filesystem;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classes loader by paths.
 *
 * @since 0.0.1
 * @author Andrey_Yurzanov
 */
public class PathClassLoader extends ClassLoader {
  private static final String EMPTY = "";
  private static final String PACKAGE_SEPARATOR = ".";
  private static final String FILE_EXTENSION = ".class";
  private static final String PATH_SEPARATOR = getPathSeparator();

  /**
   * Constructs new instance of class loader.
   */
  public PathClassLoader() {
    super(Thread.currentThread().getContextClassLoader());
  }

  /**
   * Loads class by path.
   *
   * @param root      path without name of the file and package
   * @param classFile path of the class's file
   * @return loaded class
   * @throws ClassNotFoundException when class not found
   */
  public Class<?> loadClass(Path root, Path classFile) throws ClassNotFoundException {
    try {
      String className = root
        .relativize(classFile)
        .toString()
        .replace(PATH_SEPARATOR, PACKAGE_SEPARATOR)
        .replace(FILE_EXTENSION, EMPTY);

      byte[] classData = Files.readAllBytes(classFile);
      Class<?> defined = defineClass(className, classData, 0, classData.length);
      resolveClass(defined);

      return defined;
    } catch (Exception exception) {
      throw new ClassNotFoundException(
        String.join("", "Loading error of class [", classFile.toString(), "]"),
        exception
      );
    }
  }

  // Returns path's separator of current os
  private static String getPathSeparator() {
    FileSystem fileSystem = FileSystems.getDefault();
    return fileSystem.getSeparator();
  }
}
