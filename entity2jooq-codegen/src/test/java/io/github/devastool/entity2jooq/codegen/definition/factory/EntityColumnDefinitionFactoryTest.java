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

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityColumnDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityColumnDefinitionFactoryTest {
  private final EntityColumnDefinitionFactory factory = new EntityColumnDefinitionFactory();
  private static final String ENTITY_ID = "entity_id";
  private static final EntityTableDefinition TABLE_DEFINITION = new EntityTableDefinition(
      new EntitySchemaDefinition(new Entity2JooqDatabase(), "test_schema"),
      "test_table",
      new ArrayList<>()
  );

  @Test
  void buildSuccessTest() {
    for (Field field : TestEntity.class.getDeclaredFields()) {
      Optional<EntityColumnDefinition> built = factory.build(field, new Annotation[0], TABLE_DEFINITION);
      Assertions.assertTrue(built.isPresent());

      EntityColumnDefinition definition = built.orElseThrow();
      Assertions.assertEquals(ENTITY_ID, definition.getName());
    }
  }

  @Test
  void buildWithoutColumnNameSuccessTest() {
    for (Field field : TestEntityWithoutColumnName.class.getDeclaredFields()) {
      Optional<EntityColumnDefinition> built = factory.build(field, new Annotation[0], TABLE_DEFINITION);
      Assertions.assertTrue(built.isPresent());

      EntityColumnDefinition definition = built.orElseThrow();
      Assertions.assertEquals(field.getName(), definition.getName());
    }
  }

  @Test
  void buildWithoutAnnotationSuccessTest() {
    for (Field field : TestEntityWithoutAnnotation.class.getDeclaredFields()) {
      Optional<EntityColumnDefinition> built = factory.build(field, new Annotation[0], TABLE_DEFINITION);
      Assertions.assertTrue(built.isPresent());

      EntityColumnDefinition definition = built.orElseThrow();
      Assertions.assertEquals(field.getName(), definition.getName());
    }
  }

  static class TestEntity {
    @Column(ENTITY_ID)
    private Integer id;
  }
  static class TestEntityWithoutColumnName {
    @Column
    private Integer id;
  }
  static class TestEntityWithoutAnnotation {
    private Integer id;
  }
}