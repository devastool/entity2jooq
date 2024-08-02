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

package io.github.devastool.entity2jooq.codegen.properties;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link CodegenProperties}.
 *
 * @author Andrey_Yurzanov
 */
class CodegenPropertiesTest {
  private static final String DEFAULT_DIALECT = "DEFAULT";

  @Test
  void getSuccessTest() {
    CodegenProperties properties = new CodegenProperties(Map.of(
        CodegenProperty.DIALECT, ""
    ));

    Optional<String> found = Assertions.assertDoesNotThrow(
        () -> properties.get(CodegenProperty.DIALECT)
    );
    Assertions.assertTrue(found.isPresent());
    Assertions.assertEquals("", found.get());
  }

  @Test
  void getNotFoundTest() {
    CodegenProperties properties = new CodegenProperties();

    Optional<String> found = Assertions.assertDoesNotThrow(
        () -> properties.get(CodegenProperty.DIALECT)
    );
    Assertions.assertFalse(found.isPresent());
  }

  @Test
  void getWithDefaultSuccessTest() {
    CodegenProperties properties = new CodegenProperties(Map.of(
        CodegenProperty.DIALECT, ""
    ));

    String found = Assertions.assertDoesNotThrow(
        () -> properties.get(CodegenProperty.DIALECT, DEFAULT_DIALECT)
    );
    Assertions.assertEquals("", found);
  }

  @Test
  void getWithDefaultNotFoundTest() {
    CodegenProperties properties = new CodegenProperties();

    String found = Assertions.assertDoesNotThrow(
        () -> properties.get(CodegenProperty.DIALECT, DEFAULT_DIALECT)
    );
    Assertions.assertEquals(DEFAULT_DIALECT, found);
  }

  @Test
  void requireSuccessTest() {
    CodegenProperties properties = new CodegenProperties(Map.of(
        CodegenProperty.DIALECT, ""
    ));

    String found = Assertions.assertDoesNotThrow(
        () -> properties.require(CodegenProperty.DIALECT)
    );
    Assertions.assertEquals("", found);
  }

  @Test
  void requireNotFoundTest() {
    CodegenProperties properties = new CodegenProperties();
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> properties.require(CodegenProperty.DIALECT)
    );
  }
}