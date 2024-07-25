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

import io.github.devastool.entity2jooq.annotation.AttributeOverride;
import io.github.devastool.entity2jooq.annotation.AttributeOverrides;
import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.Embedded;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.TableDefinition;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class EntityColumnDefinitionFactory {
  private final Map<Class<?>, String> SQL_TYPES = initTypes();
  public static final String SEPARATOR = "_";

  /**
   * Builds new instance of {@link EntityColumnDefinition}.
   *
   * @param field entity field, annotation {@link Column} is optional
   * @param table meta-information about table
   */
  public Optional<EntityColumnDefinition> build(
      Field field,
      Annotation[] annotations,
      TableDefinition table
  ) {
    String name = field.getName();
    Class<?> classType = field.getType();
    String sqlType = SQL_TYPES.get(classType);

    Column columnAnnotation = field.getAnnotation(Column.class);
    var embedded = (Embedded) findAnnotation(annotations, Embedded.class);
    var overrideColumn = getOverrideColumn(annotations, name);

    if (columnAnnotation != null) {
      name = replaceIfNotEmpty(columnAnnotation.value(), name);
      sqlType = replaceIfNotEmpty(columnAnnotation.type(), sqlType);
    }

    if (overrideColumn != null) {
      name = replaceIfNotEmpty(overrideColumn.value(), name);
      sqlType = replaceIfNotEmpty(overrideColumn.type(), sqlType);
    }

    if (embedded != null) {
      var prefix = embedded.prefix();
      if (StringUtils.isNotEmpty(prefix)) {
        name = String.join(SEPARATOR, prefix, name);
      }
    }

    SchemaDefinition schema = table.getSchema();
    NamingStrategy strategy = new SnakeCaseStrategy(); // TODO. Use columnAnnotation.naming()
    EntityDataTypeDefinition type = new EntityDataTypeDefinition(schema, classType, sqlType);
    return Optional.of(
        new EntityColumnDefinition(table, strategy.resolve(name), type)
    );
  }

  private static Map<Class<?>, String> initTypes() { // TODO. Use columnAnnotation.typeMapper()
    Map<Class<?>, String> types = new HashMap<>();
    types.put(SQLDataType.BIGINT.getType(), SQLDataType.BIGINT.getTypeName());
    types.put(SQLDataType.VARCHAR.getType(), SQLDataType.VARCHAR.getTypeName());
    types.put(SQLDataType.BOOLEAN.getType(), SQLDataType.BOOLEAN.getTypeName());
    types.put(SQLDataType.TINYINT.getType(), SQLDataType.TINYINT.getTypeName());
    types.put(SQLDataType.SMALLINT.getType(), SQLDataType.SMALLINT.getTypeName());
    types.put(SQLDataType.INTEGER.getType(), SQLDataType.INTEGER.getTypeName());
    types.put(SQLDataType.BIGINT.getType(), SQLDataType.BIGINT.getTypeName());
    types.put(SQLDataType.DATE.getType(), SQLDataType.DATE.getTypeName());
    types.put(SQLDataType.TIMESTAMP.getType(), SQLDataType.TIMESTAMP.getTypeName());
    types.put(SQLDataType.TIME.getType(), SQLDataType.TIME.getTypeName());
    types.put(SQLDataType.LOCALDATE.getType(), SQLDataType.LOCALDATE.getTypeName());
    types.put(SQLDataType.LOCALTIME.getType(), SQLDataType.LOCALTIME.getTypeName());
    types.put(SQLDataType.LOCALDATETIME.getType(), SQLDataType.LOCALDATETIME.getTypeName());

    return Collections.unmodifiableMap(types);
  }

  private <T extends Annotation> Annotation findAnnotation(
      Annotation[] annotations,
      Class<T> annotationClass
  ) {
    for (Annotation annotation : annotations) {
      if (annotationClass.isInstance(annotation)) {
        return annotation;
      }
    }
    return null;
  }

  private Column getOverrideColumn(Annotation[] annotations, String name) {
    var columnOverride = (AttributeOverride) findAnnotation(annotations, AttributeOverride.class);
    if (columnOverride != null) {
      if (Objects.equals(columnOverride.name(), name)) {
        return columnOverride.column();
      }
    }

    var columnOverrides = (AttributeOverrides) findAnnotation(annotations,
        AttributeOverrides.class);
    if (columnOverrides != null) {
      for (AttributeOverride override : columnOverrides.value()) {
        if (Objects.equals(override.name(), name)) {
          return override.column();
        }
      }
    }
    return null;
  }

  private String replaceIfNotEmpty(String newValue, String currentValue) {
    if (StringUtils.isNoneEmpty(newValue)) {
      currentValue = newValue;
    }
    return currentValue;
  }
}