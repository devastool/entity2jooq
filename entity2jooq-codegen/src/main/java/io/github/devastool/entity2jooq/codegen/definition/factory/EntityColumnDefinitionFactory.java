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

import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.NAMING_STRATEGY;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.TABLE;

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.ColumnOverride;
import io.github.devastool.entity2jooq.annotation.ColumnOverride.ColumnOverrides;
import io.github.devastool.entity2jooq.annotation.Embedded;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.definition.FieldDetails;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.shared.utils.StringUtils;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityColumnDefinitionFactory extends
    DefinitionFactory<Field, List<EntityColumnDefinition>> {
  private final EntityDataTypeDefinitionFactory typeFactory;
  public static final String SEPARATOR = "_";
  public static final String DOT = ".";

  /**
   * Constructs new instance of {@link EntityColumnDefinitionFactory}.
   *
   * @param typeFactory instance of {@link EntityDataTypeDefinitionFactory}
   * @param context     instance of {@link FactoryContext}
   */
  public EntityColumnDefinitionFactory(
      EntityDataTypeDefinitionFactory typeFactory,
      FactoryContext context
  ) {
    super(context);
    this.typeFactory = typeFactory;
  }

  @Override
  public List<EntityColumnDefinition> build(Field field, CodegenProperties properties) {
    List<EntityColumnDefinition> columns = new ArrayList<>();
    Deque<FieldDetails> queue = new ArrayDeque<>();

    Map<String, Column> overrideColumns = getOverrideColumns(field.getDeclaredAnnotations(),
        field.getName());

    queue.push(new FieldDetails(field, null, new ArrayList<>()));

    while (!queue.isEmpty()) {
      FieldDetails fieldDetails = queue.pop();

      if (fieldDetails.getType().isAnnotationPresent(Embedded.class)) {
        for (Field declaredField : fieldDetails.getType().getDeclaredFields()) {
          List<String> fieldsName = new ArrayList<>(fieldDetails.getParentFieldsName());
          String parentFieldName = fieldDetails.getParentFieldName();

          if (parentFieldName != null) {
            fieldsName.add(fieldDetails.getParentFieldName());
          }

          queue.push(new FieldDetails(declaredField, fieldDetails.getField(), fieldsName));
        }
      } else {
        String name = fieldDetails.getName();
        Class<? extends NamingStrategy> naming = properties.require(NAMING_STRATEGY);

        var fieldNames = fieldDetails.getParentFieldsName();
        String parentName = fieldDetails.getParentFieldName();
        if (parentName != null) {
          fieldNames.add(parentName);
        }

        Column column = fieldDetails.getField().getAnnotation(Column.class);
        Column overrideColum = overrideColumns.get(String.join(DOT, fieldNames) + DOT + name);

        if (overrideColum != null) {
          naming = overrideColum.naming();
          name = replaceValueIfNotEmpty(overrideColum.value(), name);
        } else {
          if (column != null) {
            naming = column.naming();
            name = replaceValueIfNotEmpty(column.value(), name);
          }

          fieldNames.add(name);
          name = String.join(SEPARATOR, fieldNames);
        }

        FactoryContext context = getContext();
        NamingStrategy strategy = context.getInstance(naming);

        columns.add(new EntityColumnDefinition(
            properties.require(TABLE),
            strategy.resolve(name),
            typeFactory.build(fieldDetails.getField(), properties)
        ));
      }
    }

    return columns;
  }

  /**
   * Gets a map of overridden columns based on the annotations for the specified field.
   *
   * @param annotations an array of annotations applied to the field
   * @param fieldName   the name of the field for which overrides are being found
   * @return Map of overridden columns
   */
  private Map<String, Column> getOverrideColumns(Annotation[] annotations, String fieldName) {
    ColumnOverrides columnOverrides = findAnnotation(annotations, ColumnOverrides.class);
    ColumnOverride columnOverride = findAnnotation(annotations, ColumnOverride.class);
    Map<String, Column> overrideColumns = new HashMap<>();

    if (columnOverrides != null) {
      for (ColumnOverride override : columnOverrides.value()) {
        overrideColumns.put(fieldName + DOT + override.name(), override.column());
      }
    } else if (columnOverride != null) {
      overrideColumns.put(fieldName + DOT + columnOverride.name(), columnOverride.column());
    }

    return overrideColumns;
  }

  /**
   * Searches for an annotation of the specified type in the given array of annotations.
   *
   * @param <T>             The type of the annotation being searched for.
   * @param annotations     The array of annotations to search through.
   * @param annotationClass The class object representing the type of annotation to find.
   * @return The annotation of the specified type if found, otherwise null.
   */
  private <T extends Annotation> T findAnnotation(
      Annotation[] annotations,
      Class<T> annotationClass
  ) {
    if (annotations != null) {
      for (Annotation annotation : annotations) {
        if (annotationClass.isInstance(annotation)) {
          return annotationClass.cast(annotation);
        }
      }
    }
    return null;
  }

  /**
   * Replaces the original value with a new value if the new value is not empty.
   *
   * @param newValue     the new value
   * @param currentValue the original value
   * @return the new value if it is not empty, otherwise the original value
   */
  private String replaceValueIfNotEmpty(String newValue, String currentValue) {
    if (StringUtils.isNotEmpty(newValue)) {
      currentValue = newValue;
    }
    return currentValue;
  }
}
