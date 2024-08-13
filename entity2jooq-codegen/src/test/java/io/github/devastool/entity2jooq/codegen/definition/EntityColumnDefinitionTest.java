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

package io.github.devastool.entity2jooq.codegen.definition;

import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityColumnDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityDataTypeDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.definition.factory.FactoryContext;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityColumnDefinition}.
 *
 * @author Andrey_Yurzanov
 */
class EntityColumnDefinitionTest {
  private final FactoryContext context = new FactoryContext();
  private final EntityColumnDefinitionFactory factory =
      new EntityColumnDefinitionFactory(new EntityDataTypeDefinitionFactory(context), context);
  private static final EntitySchemaDefinition SCHEMA_DEFINITION =
      new EntitySchemaDefinition(new Entity2JooqDatabase(), "test_schema");
  private static final EntityTableDefinition TABLE_DEFINITION = new EntityTableDefinition(
      new EntitySchemaDefinition(new Entity2JooqDatabase(), "test_schema"),
      "test_table",
      Object.class,
      new ArrayList<>()
  );
  private static final CodegenProperties PROPERTIES = new CodegenProperties(
      Map.of(
          CodegenProperty.NAMING_STRATEGY, SnakeCaseStrategy.class,
          CodegenProperty.TABLE, TABLE_DEFINITION,
          CodegenProperty.DIALECT, "",
          CodegenProperty.SCHEMA, SCHEMA_DEFINITION
      )
  );

  @Test
  void getGetterNameTest() {
    Field[] fields = TestEntity.class.getDeclaredFields();
    EntityColumnDefinition built = factory.build(fields[0], PROPERTIES);
    Assertions.assertEquals("getId", built.getGetterName());
  }

  @Test
  void getSetterNameTest() {
    Field[] fields = TestEntity.class.getDeclaredFields();
    EntityColumnDefinition built = factory.build(fields[0], PROPERTIES);
    Assertions.assertEquals("setId", built.getSetterName());
  }

  @Test
  void getGetterNameWithSmallNameTest() {
    Field[] fields = TestEntityWithSmallName.class.getDeclaredFields();
    EntityColumnDefinition built = factory.build(fields[0], PROPERTIES);
    Assertions.assertEquals("getI", built.getGetterName());
  }

  @Test
  void getSetterNameWithSmallNameTest() {
    Field[] fields = TestEntityWithSmallName.class.getDeclaredFields();
    EntityColumnDefinition built = factory.build(fields[0], PROPERTIES);
    Assertions.assertEquals("setI", built.getSetterName());
  }

  static class TestEntity {
    private Integer id;
  }

  static class TestEntityWithSmallName {
    private Integer i;
  }
}