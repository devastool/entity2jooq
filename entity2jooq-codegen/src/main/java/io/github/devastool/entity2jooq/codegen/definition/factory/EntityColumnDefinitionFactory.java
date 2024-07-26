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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jooq.meta.TableDefinition;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityColumnDefinitionFactory {
  private final EntityDataTypeDefinitionFactory typeFactory;
  public static final String SEPARATOR = "_";

  /**
   * Constructs new instance of {@link EntityColumnDefinitionFactory}.
   *
   * @param typeFactory instance of {@link EntityDataTypeDefinitionFactory}
   */
  public EntityColumnDefinitionFactory(EntityDataTypeDefinitionFactory typeFactory) {
    this.typeFactory = typeFactory;
  }

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
    Column columnAnnotation = field.getAnnotation(Column.class);
    var embedded = (Embedded) findAnnotation(annotations, Embedded.class);
    var overrideColumn = getOverrideColumn(annotations, name);

    if (overrideColumn != null) {
      name = replaceNameIfNotEmpty(overrideColumn.value(), name);
    }
    else if (columnAnnotation != null) {
      name = replaceNameIfNotEmpty(columnAnnotation.value(), name);
    }

    if (embedded != null) {
      var prefix = embedded.prefix();
      if (StringUtils.isNotEmpty(prefix)) {
        name = String.join(SEPARATOR, prefix, name);
      }
    }

    NamingStrategy strategy = new SnakeCaseStrategy(); // TODO. Use columnAnnotation.naming()
    return Optional.of(
        new EntityColumnDefinition(
            table,
            strategy.resolve(name),
            typeFactory.build(table.getSchema(), field)
        )
    );
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

    var columnOverrides = (AttributeOverrides) findAnnotation(annotations, AttributeOverrides.class);
    if (columnOverrides != null) {
      for (AttributeOverride override : columnOverrides.value()) {
        if (Objects.equals(override.name(), name)) {
          return override.column();
        }
      }
    }
    return null;
  }

  private String replaceNameIfNotEmpty(String value, String name) {
    if (StringUtils.isNotEmpty(value)) {
      name = value;
    }
    return name;
  }
}