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

import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntitySchemaDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntitySchemaDefinitionFactoryTest {
  private final EntitySchemaDefinitionFactory factory =
      new EntitySchemaDefinitionFactory(new FactoryContext());
  private static final String TEST_SCHEMA = "test_schema";
  private static final String PACKAGE_NAME = "factory";
  private static final CodegenProperties PROPERTIES = new CodegenProperties(
      Map.of(
          CodegenProperty.NAMING_STRATEGY, SnakeCaseStrategy.class,
          CodegenProperty.DIALECT, "",
          CodegenProperty.DATABASE, new Entity2JooqDatabase()
      )
  );

  @Test
  void buildSuccessTest() {
    EntitySchemaDefinition built =
        Assertions.assertDoesNotThrow(() -> factory.build(TestEntity.class, PROPERTIES));

    Assertions.assertNotNull(built);
    Assertions.assertEquals(TEST_SCHEMA, built.getName());
  }

  @Test
  void buildWithTableAnnotationSuccessTest() {
    EntitySchemaDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityWithTableAnnotation.class, PROPERTIES)
    );

    Assertions.assertNotNull(built);
    Assertions.assertEquals(PACKAGE_NAME, built.getName());
  }

  @Schema(value = TEST_SCHEMA)
  static final class TestEntity {}
  @Table
  static final class TestEntityWithTableAnnotation {}
}