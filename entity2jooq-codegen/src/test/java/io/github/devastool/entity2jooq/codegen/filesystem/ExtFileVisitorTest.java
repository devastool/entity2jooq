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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests of {@link ExtFileVisitor}.
 *
 * @author Andrey_Yurzanov
 */
class ExtFileVisitorTest {
  private static final List<String> JAVA_FILES = Arrays.asList("Test1.java", "Test2.java");
  private static final List<String> TXT_FILES = Arrays.asList("Test1.txt", "Test2.txt");

  @Test
  void getFilteredSuccessTest(@TempDir File root) throws IOException {
    for (String javaFile : JAVA_FILES) {
      new File(root, javaFile).createNewFile();
    }

    ExtFileVisitor visitor = new ExtFileVisitor(ExtFileVisitor.JAVA_FILE_EXT);
    Files.walkFileTree(root.toPath(), visitor);

    for (Path path : visitor.getFiltered()) {
      String fileName = path.getFileName().toString();
      Assertions.assertTrue(JAVA_FILES.contains(fileName));
    }
  }

  @Test
  void getFilteredWithMappingSuccessTest(@TempDir File root) throws IOException {
    for (String javaFile : JAVA_FILES) {
      new File(root, javaFile).createNewFile();
    }

    ExtFileVisitor visitor = new ExtFileVisitor(ExtFileVisitor.JAVA_FILE_EXT);
    Files.walkFileTree(root.toPath(), visitor);

    Collection<File> filtered = visitor.getFiltered(Path::toFile);
    for (File file : filtered) {
      String fileName = file.getName();
      Assertions.assertTrue(JAVA_FILES.contains(fileName));
    }
  }

  @Test
  void getFilteredNotFoundFilesTest(@TempDir File root) throws IOException {
    for (String txtFile : TXT_FILES) {
      new File(root, txtFile).createNewFile();
    }

    ExtFileVisitor visitor = new ExtFileVisitor(ExtFileVisitor.JAVA_FILE_EXT);
    Files.walkFileTree(root.toPath(), visitor);

    Assertions.assertTrue(visitor.getFiltered().isEmpty());
  }
}