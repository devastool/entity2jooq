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

package io.github.devastool.entity2jooq.codegen.definition.factory;

import io.github.devastool.entity2jooq.annotation.type.NoSuchTypeException;
import io.github.devastool.entity2jooq.annotation.type.Type;
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityDataTypeDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityDataTypeDefinitionFactoryTest {
  private final EntityDataTypeDefinitionFactory factory = new EntityDataTypeDefinitionFactory();
  private static final EntitySchemaDefinition SCHEMA_DEFINITION =
      new EntitySchemaDefinition(new Entity2JooqDatabase(), "test_schema");

  @Test
  void buildSuccessTest() {
    Map<Class<?>, String> types = Map.of(
        Integer.class, "integer",
        BigDecimal.class, "decimal",
        LocalDateTime.class, "timestamp"
    );

    for (Field field : TestEntity.class.getDeclaredFields()) {
      EntityDataTypeDefinition definition =
          Assertions.assertDoesNotThrow(() -> factory.build(SCHEMA_DEFINITION, field));

      Assertions.assertEquals(types.get(field.getType()), definition.getType());
    }
  }

  @Test
  void buildWithoutTypeSuccessTest() {
    Map<Class<?>, String> types = Map.of(
        Integer.class, "integer",
        BigDecimal.class, "decimal",
        LocalDateTime.class, "timestamp"
    );

    for (Field field : TestEntityWithoutType.class.getDeclaredFields()) {
      EntityDataTypeDefinition definition =
          Assertions.assertDoesNotThrow(() -> factory.build(SCHEMA_DEFINITION, field));

      Assertions.assertEquals(types.get(field.getType()), definition.getType());
    }
  }

  @Test
  void buildNotFoundTypeFailureTest() {
    for (Field field : TestEntityNotFoundType.class.getDeclaredFields()) {
      Assertions.assertThrows(
          NoSuchTypeException.class,
          () -> factory.build(SCHEMA_DEFINITION, field)
      );
    }
  }

  static class TestEntity {
    @Type("integer")
    private Integer id;
    @Type("decimal")
    private BigDecimal amount;
    @Type("timestamp")
    private LocalDateTime insertType;
  }

  static class TestEntityWithoutType {
    private Integer id;
    private BigDecimal amount;
    private LocalDateTime insertType;
  }

  static class TestEntityNotFoundType {
    private Object field;
  }
}