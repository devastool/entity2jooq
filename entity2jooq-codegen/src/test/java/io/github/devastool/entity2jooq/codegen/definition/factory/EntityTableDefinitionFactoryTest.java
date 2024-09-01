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
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntityDisabledInheritance;
import io.github.devastool.entity2jooq.codegen.model.TestEntityEmpty;
import io.github.devastool.entity2jooq.codegen.model.TestEntityEnabledInheritance;
import io.github.devastool.entity2jooq.codegen.model.TestEntityTableName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityTableDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityTableDefinitionFactoryTest extends CommonFactoryTest {
  @Test
  void buildSuccessTest() {
    EntityTableDefinitionFactory factory = getTableFactory();
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityTableName.class, getProperties())
    );
    Assertions.assertNotNull(built);

    Table annotation = TestEntityTableName.class.getAnnotation(Table.class);
    Assertions.assertEquals(annotation.value(), built.getName());
  }

  @Test
  void buildWithoutTableNameTest() {
    EntityTableDefinitionFactory factory = getTableFactory();
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntity.class, getProperties())
    );

    Assertions.assertNotNull(built);
    Assertions.assertEquals(
        new SnakeCaseStrategy().resolve(TestEntity.class.getSimpleName()),
        built.getName()
    );
  }

  @Test
  void buildWithoutAnnotationTest() {
    EntityTableDefinitionFactory factory = getTableFactory();
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityEmpty.class, getProperties())
    );
    Assertions.assertNull(built);
  }

  @Test
  void buildInheritanceEnabledTest() {
    EntityTableDefinitionFactory factory = getTableFactory();
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityEnabledInheritance.class, getProperties())
    );
    Assertions.assertNotNull(built);

    Assertions.assertEquals(
        TestEntity.class.getDeclaredFields().length,
        built.getColumns().size()
    );
  }

  @Test
  void buildInheritanceDisabledTest() {
    EntityTableDefinitionFactory factory = getTableFactory();
    EntityTableDefinition built = Assertions.assertDoesNotThrow(
        () -> factory.build(TestEntityDisabledInheritance.class, getProperties())
    );
    Assertions.assertNotNull(built);
    Assertions.assertEquals(0, built.getColumns().size());
  }
}