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
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntitySchemaDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntitySchemaDefinitionFactoryTest {
  private final EntitySchemaDefinitionFactory factory = new EntitySchemaDefinitionFactory(new StrategyContext());
  private static final String TEST_SCHEMA = "test_schema";
  private static final String PACKAGE_NAME = "factory";

  @Test
  void buildSuccessTest() {
    Optional<EntitySchemaDefinition> built = factory.build(TestEntity.class, null);
    Assertions.assertTrue(built.isPresent());

    EntitySchemaDefinition definition = built.orElseThrow();
    Assertions.assertEquals(TEST_SCHEMA, definition.getName());
  }

  @Test
  void buildWithoutAnnotationSuccessTest() {
    Optional<EntitySchemaDefinition> built = factory.build(TestEntityWithoutAnnotation.class, null);
    Assertions.assertTrue(built.isPresent());

    EntitySchemaDefinition definition = built.orElseThrow();
    Assertions.assertEquals(PACKAGE_NAME, definition.getName());
  }

  @Schema(value = TEST_SCHEMA)
  static final class TestEntity {}
  static final class TestEntityWithoutAnnotation {}
}