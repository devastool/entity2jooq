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

import static io.github.devastool.entity2jooq.codegen.filesystem.ExtFileVisitor.CLASS_FILE_EXT;

import io.github.devastool.entity2jooq.codegen.filesystem.ExtFileVisitor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class loader context, it holds information about classpath, files of classes, etc.
 *
 * @since 1.0.0
 * @author Andrey_Yurzanov
 */
public class ClassLoaderContext implements Iterable<ClassFile> {
  private final List<URL> classpath;
  private final Map<String, ClassFile> classFiles;

  private static final String CLASSPATH_SEPARATOR = File.pathSeparator;

  /**
   * Constructs new instance of {@link ClassLoaderContext}.
   */
  public ClassLoaderContext() {
    classpath = new ArrayList<>();
    classFiles = new HashMap<>();
  }

  /**
   * Adds information about the class path, the class path contains path elements separated by the
   * symbol ':' or ';'
   *
   * @param value classpath information
   */
  public void addClasspath(String value) {
    try {
      String[] paths = value.split(CLASSPATH_SEPARATOR);
      for (String path : paths) {
        classpath.add(Paths.get(path).toUri().toURL());
      }
    } catch (Exception exception) {
      throw new RuntimeException(
          String.join("", "Incorrect classpath: [", value, "]"),
          exception
      );
    }
  }

  /**
   * Adds root directory, root directory is directory that contains class files.
   *
   * @param root root directory
   */
  public void addRoot(Path root) {
    try {
      ExtFileVisitor visitor = new ExtFileVisitor(CLASS_FILE_EXT);
      Files.walkFileTree(root, visitor);

      visitor
          .getFiltered(path -> new ClassFile(root, path))
          .forEach(file -> classFiles.put(file.getCanonicalClassName(), file));
    } catch (IOException exception) {
      throw new RuntimeException(
          String.join("Loading error of files by path: [", root.toString(), "]"),
          exception
      );
    }
  }

  /**
   * Returns element of path information by name of the class.
   *
   * @param name name of the class
   * @return element of path information or empty container
   */
  public Optional<ClassFile> getClassFile(String name) {
    return Optional.ofNullable(classFiles.get(name));
  }

  /**
   * Returns classpath URLs.
   *
   * @return classpath URLs
   */
  public URL[] getClasspath() {
    return classpath.toArray(new URL[]{});
  }

  @Override
  public Iterator<ClassFile> iterator() {
    return classFiles
        .values()
        .iterator();
  }
}
