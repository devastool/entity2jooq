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

import io.github.devastool.entity2jooq.codegen.definition.factory.CommonFactoryTest;
import io.github.devastool.entity2jooq.codegen.definition.factory.EntityColumnDefinitionFactory;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntitySmallFieldName;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityColumnDefinition}.
 *
 * @author Andrey_Yurzanov
 */
class EntityColumnDefinitionTest extends CommonFactoryTest {
  @Test
  void getGetterNameTest() {
    EntityColumnDefinitionFactory factory = getColumnFactory();
    CodegenProperties properties = getProperties();

    Field[] fields = TestEntity.class.getDeclaredFields();
    for (Field field : fields) {
      EntityColumnDefinition definition = factory.build(field, properties).get(0);
      String name = field.getName();
      Assertions.assertEquals(
          "get" + name.substring(0, 1).toUpperCase() + name.substring(1),
          definition.getGetterName()
      );
    }
  }

  @Test
  void getSetterNameTest() {
    EntityColumnDefinitionFactory factory = getColumnFactory();
    CodegenProperties properties = getProperties();

    Field[] fields = TestEntity.class.getDeclaredFields();
    for (Field field : fields) {
      EntityColumnDefinition definition = factory.build(field, properties).get(0);
      String name = field.getName();
      Assertions.assertEquals(
          "set" + name.substring(0, 1).toUpperCase() + name.substring(1),
          definition.getSetterName()
      );
    }
  }

  @Test
  void getGetterNameWithSmallNameTest() {
    EntityColumnDefinitionFactory factory = getColumnFactory();
    CodegenProperties properties = getProperties();

    Field[] fields = TestEntitySmallFieldName.class.getDeclaredFields();
    for (Field field : fields) {
      EntityColumnDefinition definition = factory.build(field, properties).get(0);
      String name = field.getName();
      Assertions.assertEquals(
          "get" + name.toUpperCase(),
          definition.getGetterName()
      );
    }
  }

  @Test
  void getSetterNameWithSmallNameTest() {
    EntityColumnDefinitionFactory factory = getColumnFactory();
    CodegenProperties properties = getProperties();

    Field[] fields = TestEntitySmallFieldName.class.getDeclaredFields();
    for (Field field : fields) {
      EntityColumnDefinition definition = factory.build(field, properties).get(0);
      String name = field.getName();
      Assertions.assertEquals(
          "set" + name.toUpperCase(),
          definition.getSetterName()
      );
    }
  }
}