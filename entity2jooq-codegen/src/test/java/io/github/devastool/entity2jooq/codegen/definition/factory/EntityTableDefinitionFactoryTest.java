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

import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityTableDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityTableDefinitionFactoryTest {
  private final EntityTableDefinitionFactory factory = new EntityTableDefinitionFactory(
      new EntitySchemaDefinitionFactory(),
      new EntityColumnDefinitionFactory(new EntityDataTypeDefinitionFactory())
  );
  private static final String TEST_TABLE = "test_table";
  private static final String CLASS_NAME = "test_entity_without_table_name";

  @Test
  void buildSuccessTest() {
    Optional<EntityTableDefinition> built = factory.build(TestEntity.class, null);
    Assertions.assertTrue(built.isPresent());

    EntityTableDefinition definition = built.orElseThrow();
    Assertions.assertEquals(TEST_TABLE, definition.getName());
  }

  @Test
  void buildWithoutTableNameSuccessTest() {
    Optional<EntityTableDefinition> built = factory.build(TestEntityWithoutTableName.class, null);
    Assertions.assertTrue(built.isPresent());

    EntityTableDefinition definition = built.orElseThrow();
    Assertions.assertEquals(CLASS_NAME, definition.getName());
  }

  @Test
  void buildWithoutAnnotationSuccessTest() {
    Optional<EntityTableDefinition> built = factory.build(TestEntityWithoutAnnotation.class, null);
    Assertions.assertFalse(built.isPresent());
  }

  @Table(TEST_TABLE)
  static class TestEntity {}
  @Table
  static class TestEntityWithoutTableName {}
  static class TestEntityWithoutAnnotation {}
}