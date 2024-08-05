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
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityTableDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityTableDefinitionFactoryTest {
  private final FactoryContext context = new FactoryContext();
  private final EntityTableDefinitionFactory factory = new EntityTableDefinitionFactory(
      new EntitySchemaDefinitionFactory(context),
      new EntityColumnDefinitionFactory(new EntityDataTypeDefinitionFactory(context), context),
      context
  );
  private static final String TEST_TABLE = "test_table";
  private static final String CLASS_NAME = "test_entity_without_table_name";
  private static final CodegenProperties PROPERTIES = new CodegenProperties(
      Map.of(
          CodegenProperty.DIALECT, "",
          CodegenProperty.DATABASE, new Entity2JooqDatabase()
      )
  );

  @Test
  void buildSuccessTest() {
    EntityTableDefinition built =
        Assertions.assertDoesNotThrow(() -> factory.build(TestEntity.class, PROPERTIES));

    Assertions.assertNotNull(built);
    Assertions.assertEquals(TEST_TABLE, built.getName());
  }

  @Test
  void buildWithoutTableNameSuccessTest() {
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityWithoutTableName.class, PROPERTIES)
    );

    Assertions.assertNotNull(built);
    Assertions.assertEquals(CLASS_NAME, built.getName());
  }

  @Test
  void buildWithoutAnnotationSuccessTest() {
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityWithoutAnnotation.class, PROPERTIES)
    );
    Assertions.assertNull(built);
  }

  @Table(TEST_TABLE)
  static class TestEntity {}
  @Table
  static class TestEntityWithoutTableName {}
  static class TestEntityWithoutAnnotation {}
}