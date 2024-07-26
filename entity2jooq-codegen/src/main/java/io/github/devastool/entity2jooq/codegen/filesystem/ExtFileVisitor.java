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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Files visitor with filtering by file extension.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class ExtFileVisitor implements FileVisitor<Path> {
  private final String ext;
  private final Collection<Path> filtered = new TreeSet<>();

  public static final String JAVA_FILE_EXT = ".java";
  public static final String CLASS_FILE_EXT = ".class";

  /**
   * Constructs new instance of {@link ExtFileVisitor}.
   *
   * @param ext file extension
   */
  public ExtFileVisitor(String ext) {
    this.ext = ext;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (hasExt(file)) {
      filtered.add(file);
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  /**
   * Returns filtered files.
   *
   * @return filtered files
   */
  public Collection<Path> getFiltered() {
    return filtered;
  }

  /**
   * Returns filtered files with additional mapping.
   *
   * @param mapper instance for mapping
   * @return filtered files
   */
  public <T> Collection<T> getFiltered(Function<Path, T> mapper) {
    return this
        .getFiltered()
        .stream()
        .map(mapper)
        .collect(Collectors.toList());
  }

  private boolean hasExt(Path path) {
    return path.toString().endsWith(ext);
  }
}
