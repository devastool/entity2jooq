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
import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import io.github.devastool.entity2jooq.codegen.type.RouteTypeMapper;
import java.lang.reflect.Field;
import org.jooq.meta.SchemaDefinition;

/**
 * The factory for {@link EntityDataTypeDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class EntityDataTypeDefinitionFactory {
  private static final RouteTypeMapper route = new RouteTypeMapper();

  /**
   * Builds new instance of {@link EntityDataTypeDefinition}.
   *
   * @param schema meta-information about schema
   * @param field entity field, annotation {@link Type} is optional
   */
  public EntityDataTypeDefinition build(
      SchemaDefinition schema,
      Field field
  ) throws NoSuchTypeException {
    String sqlType = "";
    String dialect = ""; // TODO. Extract from configuration
    Class<?> classType = field.getType();

    Type annotation = field.getAnnotation(Type.class);
    if (annotation != null) {
      sqlType = annotation.value();
    }

    if (sqlType.isEmpty()) {
      sqlType = route.getSqlType(dialect, classType);
    }
    return new EntityDataTypeDefinition(schema, classType, sqlType);
  }
}
