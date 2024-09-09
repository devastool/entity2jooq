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
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntityPrimitiveTypes;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityColumnDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityColumnDefinitionFactoryTest extends CommonFactoryTest {
  @Test
  void buildTest() {
    SnakeCaseStrategy naming = new SnakeCaseStrategy();
    CodegenProperties properties = getProperties();
    EntityColumnDefinitionFactory factory = getColumnFactory();
    for (Field field : TestEntity.class.getDeclaredFields()) {
      EntityColumnDefinition definition = factory.build(field, properties).get(0);
      Assertions.assertNotNull(definition);

      Column annotation = field.getAnnotation(Column.class);
      if (annotation != null && !annotation.value().isEmpty()) {
        Assertions.assertEquals(annotation.value(), definition.getName());
      } else {
        Assertions.assertEquals(naming.resolve(field.getName()), definition.getName());
      }
    }
  }

//  TODO. After issue: #50
//  @Test
//  void buildEmbeddedTest() {
//    int size = TestEmbeddable.class.getDeclaredFields().length
//        * TestEntityEmbedded.class.getDeclaredFields().length;
//
//    SnakeCaseStrategy naming = new SnakeCaseStrategy();
//    CodegenProperties properties = getProperties();
//    EntityColumnDefinitionFactory factory = getColumnFactory();
//    for (Field field : TestEntityEmbedded.class.getDeclaredFields()) {
//      List<EntityColumnDefinition> definitions = factory.build(field, properties);
//      Assertions.assertEquals(size, definitions.size());
//
//      Class<?> embeddableType = field.getType();
//      for (Field embeddableField : embeddableType.getDeclaredFields()) {
//        for (EntityColumnDefinition definition : definitions) {
//          if (Objects.equals(definition.getField(), embeddableField)) {
//            Assertions.assertEquals(
//                naming.resolve(embeddableField.getName()),
//                definition.getName()
//            );
//          }
//        }
//      }
//    }
//  }
//
//  @Test
//  void buildEmbeddedOverrideTest() {
//    int size = TestEmbeddable.class.getDeclaredFields().length
//        * TestEntityOverrideEmbedded.class.getDeclaredFields().length;
//
//    CodegenProperties properties = getProperties();
//    EntityColumnDefinitionFactory factory = getColumnFactory();
//    for (Field field : TestEntityEmbedded.class.getDeclaredFields()) {
//      List<EntityColumnDefinition> definitions = factory.build(field, properties);
//      Assertions.assertEquals(size, definitions.size());
//
//      Class<?> embeddableType = field.getType();
//      for (Field embeddableField : embeddableType.getDeclaredFields()) {
//        for (EntityColumnDefinition definition : definitions) {
//          if (Objects.equals(definition.getField(), embeddableField)) {
//            ColumnOverride[] overrides = field.getAnnotationsByType(ColumnOverride.class);
//            for (ColumnOverride override : overrides) {
//              if (Objects.equals(override.name(), embeddableField.getName())) {
//                Assertions.assertEquals(
//                    override.column().value(),
//                    definition.getName()
//                );
//              }
//            }
//          }
//        }
//      }
//    }
//  }

  @Test
  void canBuildSuccessTest() {
    EntityColumnDefinitionFactory factory = getColumnFactory();
    for (Field field : TestEntity.class.getDeclaredFields()) {
      Assertions.assertTrue(factory.canBuild(field));
    }
  }

  @Test
  void canBuildFailureTest() {
    EntityColumnDefinitionFactory factory = getColumnFactory();
    for (Field field : TestEntityPrimitiveTypes.class.getDeclaredFields()) {
      Assertions.assertFalse(factory.canBuild(field));
    }
  }
}