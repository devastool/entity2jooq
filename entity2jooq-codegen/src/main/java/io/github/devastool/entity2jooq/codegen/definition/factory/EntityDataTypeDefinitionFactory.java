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
import io.github.devastool.entity2jooq.annotation.type.converter.ParameterizableConverter;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.ConverterDefinition;
import io.github.devastool.entity2jooq.codegen.definition.type.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import io.github.devastool.entity2jooq.codegen.type.RouteTypeMapper;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import org.jooq.Converter;

/**
 * The factory for {@link EntityDataTypeDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityDataTypeDefinitionFactory
    extends DefinitionFactory<Field, EntityDataTypeDefinition> {
  private final Map<Class<?>, Class<? extends Converter>> defaultConverters;
  private final RouteTypeMapper route = new RouteTypeMapper();

  /**
   * Constructs new instance of {@link EntityDataTypeDefinitionFactory}.
   *
   * @param context instance of {@link FactoryContext}
   */
  public EntityDataTypeDefinitionFactory(
      FactoryContext context,
      Map<Class<?>, Class<? extends Converter>> defaultConverters
  ) {
    super(context);
    this.defaultConverters = defaultConverters;
  }

  @Override
  public EntityDataTypeDefinition build(Field field, CodegenProperties properties)
      throws NoSuchTypeException, IllegalArgumentException {
    String sqlType = "";
    Class<?> classType = field.getType();

    // Extracts SQL type
    Type annotation = field.getAnnotation(Type.class);
    if (annotation != null) {
      sqlType = annotation.value();
    }

    // Extracts converter
    Optional<Converter> extracted = getConverter(annotation, classType);
    if (extracted.isPresent()) {
      Converter converter = extracted.get();
      classType = converter.fromType();
    }

    // Extracts SQL type, if type is not specified
    String dialect = properties.require(CodegenProperty.DIALECT);
    if (sqlType.isEmpty()) {
      sqlType = route.getSqlType(dialect, classType);
    }

    EntitySchemaDefinition schema = properties.require(CodegenProperty.SCHEMA);
    EntityDataTypeDefinition type = new EntityDataTypeDefinition(schema, classType, sqlType);
    type.setDialect(dialect);

    if (extracted.isPresent()) {
      type.setConverterDefinition(new ConverterDefinition(extracted.get()));
    }
    return type;
  }

  /**
   * Returns default converters.
   *
   * @return default converters
   */
  public Map<Class<?>, Class<? extends Converter>> getDefaultConverters() {
    return Map.copyOf(defaultConverters);
  }

  // Returns converter or empty container
  private Optional<Converter> getConverter(Type annotation, Class<?> fieldType) {
    FactoryContext context = getContext();
    Class<? extends Converter> type = null;

    // Extracts converter from annotation
    if (annotation != null) {
      type = annotation.converter();
    }

    // Extracts default converter
    if (type == null) {
      for (var entry : defaultConverters.entrySet()) {
        if (entry.getKey().isAssignableFrom(fieldType)) {
          type = entry.getValue();
          break;
        }
      }
    }

    if (type != null && ParameterizableConverter.class.isAssignableFrom(type)) {
      return Optional.ofNullable(context.getInstance(type, fieldType));
    } else if (type != null && !Converter.class.equals(type)) {
      return Optional.ofNullable(context.getInstance(type));
    }
    return Optional.empty();
  }
}