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


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link ClassFile}.
 *
 * @author Evgeniy_Gerasimov
 */
class ClassFileTest {
  private final Path classFilePath = Path.of(File.createTempFile("prfx", "sfx").toURI());
  private final ClassFile classFile = new ClassFile(
      Path.of("C:\\"),
      classFilePath
  );

  ClassFileTest() throws IOException {
  }

  @Test
  void getCanonicalClassNameTest() {
    var split = classFile.getCanonicalClassName().split("\\.");
    Assertions.assertEquals(classFilePath.getFileName().toString(), split[split.length - 1]);
  }

  @Test
  void getClassDataTest() {
    Assertions.assertDoesNotThrow(() -> classFile.getClassData());
  }

  @Test
  void equalsAndHashCodeTest() throws IOException {
    Assertions.assertTrue(classFile.equals(classFile));

    var other = new ClassFile(Path.of("C:\\"), classFilePath);
    Assertions.assertTrue(classFile.equals(other));
    Assertions.assertEquals(classFile.hashCode(), other.hashCode());

    other = new ClassFile(Path.of("C://"), Path.of(File.createTempFile("ttt", "sss").toURI()));

    Assertions.assertFalse(classFile.equals(other));
    Assertions.assertFalse(classFile.equals(null));
  }
}