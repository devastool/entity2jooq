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

package io.github.devastool.entity2jooq.codegen.type;

import io.github.devastool.entity2jooq.annotation.type.NoSuchTypeException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link RouteTypeMapper}.
 *
 * @author Andrey_Yurzanov
 */
class RouteTypeMapperTest {
  private final RouteTypeMapper mapper = new RouteTypeMapper();

  @Test
  void getTypeSuccessTest() {
    Class<?> result = Assertions.assertDoesNotThrow(() -> mapper.getType("", "bigint"));
    Assertions.assertEquals(Long.class, result);
  }

  @Test
  void getTypeFailureTest() {
    Assertions.assertThrows(NoSuchTypeException.class, () -> mapper.getType("wrong", "bigint"));
  }

  @Test
  void getSqlTypeSuccessTest() {
    String result = Assertions.assertDoesNotThrow(() -> mapper.getSqlType("", LocalDate.class));
    Assertions.assertEquals("date", result);
  }

  @Test
  void getSqlTypeFailureTest() {
    Assertions.assertThrows(
        NoSuchTypeException.class,
        () -> mapper.getSqlType("wrong", LocalDate.class)
    );
  }
}