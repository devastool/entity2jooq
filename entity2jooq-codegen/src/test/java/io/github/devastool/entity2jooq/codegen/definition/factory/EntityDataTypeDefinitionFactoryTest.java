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
import io.github.devastool.entity2jooq.codegen.definition.type.ConverterDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.model.TestEntity;
import io.github.devastool.entity2jooq.codegen.model.TestEntityConverter;
import io.github.devastool.entity2jooq.codegen.model.TestEntityDefaultConverter;
import io.github.devastool.entity2jooq.codegen.model.TestEntityPrimitiveTypes;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import io.github.devastool.entity2jooq.codegen.type.RouteTypeMapper;
import java.lang.reflect.Field;
import java.util.Collection;
import org.jooq.Converter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityDataTypeDefinitionFactory}.
 *
 * @author Andrey_Yurzanov
 */
class EntityDataTypeDefinitionFactoryTest extends CommonFactoryTest {
  @Test
  void buildTest() {
    EntityDataTypeDefinitionFactory factory = getTypeFactory();
    CodegenProperties properties = getProperties();
    for (Field field : TestEntity.class.getDeclaredFields()) {
      EntityDataTypeDefinition definition = Assertions.assertDoesNotThrow(
          () -> factory.build(field, properties)
      );

      Type annotation = field.getAnnotation(Type.class);
      if (annotation != null) {
        Assertions.assertEquals(annotation.value(), definition.getType());
      }
    }
  }
  @Test
  void buildWithoutTypeTest() {
    EntityDataTypeDefinitionFactory factory = getTypeFactory();
    CodegenProperties properties = getProperties();
    String dialect = properties.require(CodegenProperty.DIALECT);

    RouteTypeMapper typeMapper = new RouteTypeMapper();
    for (Field field : TestEntity.class.getDeclaredFields()) {
      EntityDataTypeDefinition definition = Assertions.assertDoesNotThrow(
          () -> factory.build(field, properties)
      );

      if (field.getAnnotation(Type.class) == null) {
        String sqlType = typeMapper.getSqlType(dialect, field.getType());
        Assertions.assertEquals(sqlType, definition.getType());
      }
    }
  }
  @Test
  void buildNotFoundTypeTest() {
    EntityDataTypeDefinitionFactory factory = getTypeFactory();
    CodegenProperties properties = getProperties();
    for (Field field : TestEntityPrimitiveTypes.class.getDeclaredFields()) {
      Assertions.assertThrows(
          NoSuchTypeException.class,
          () -> factory.build(field, properties)
      );
    }
  }
  @Test
  void buildConverterTypeTest() {
    EntityDataTypeDefinitionFactory factory = getTypeFactory();
    CodegenProperties properties = getProperties();
    for (Field field : TestEntityConverter.class.getDeclaredFields()) {
      EntityDataTypeDefinition definition = Assertions.assertDoesNotThrow(
          () -> factory.build(field, properties)
      );

      Type annotation = field.getAnnotation(Type.class);
      if (annotation != null) {
        ConverterDefinition converterDefinition = definition.getConverterDefinition();
        Assertions.assertNotNull(converterDefinition);
        Assertions.assertEquals(annotation.converter(), converterDefinition.getConverterType());

        Converter converter = converterDefinition.getConverter();
        Assertions.assertEquals(
            converter.fromType().getCanonicalName(),
            definition.getJavaType()
        );
      }
    }
  }
  @Test
  void buildDefaultConverterTypeTest() {
    EntityDataTypeDefinitionFactory factory = getTypeFactory();
    Collection<Class<? extends Converter>> defaultConverters = factory
        .getDefaultConverters()
        .values();

    CodegenProperties properties = getProperties();
    for (Field field : TestEntityDefaultConverter.class.getDeclaredFields()) {
      EntityDataTypeDefinition definition = Assertions.assertDoesNotThrow(
          () -> factory.build(field, properties)
      );

      ConverterDefinition converterDefinition = definition.getConverterDefinition();
      Assertions.assertNotNull(converterDefinition);

      Converter converter = converterDefinition.getConverter();
      Assertions.assertTrue(defaultConverters.contains(converter.getClass()));
      Assertions.assertEquals(
          converter.fromType().getCanonicalName(),
          definition.getJavaType()
      );
    }
  }
}