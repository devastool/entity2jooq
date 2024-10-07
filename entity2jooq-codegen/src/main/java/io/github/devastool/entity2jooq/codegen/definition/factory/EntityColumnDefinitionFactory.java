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
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov, Sergey_Konovalov
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

    Map<String, Column> overrideColumns = getOverrideColumns(field);
    queue.push(new FieldDetails(field, new ArrayList<>(), new ArrayList<>()));

    while (!queue.isEmpty()) {
      FieldDetails fieldDetails = queue.pop();
      Class<?> processedType = fieldDetails.getProcessedType();

      if (processedType.isAnnotationPresent(Embedded.class)) {
        for (Field declaredField : processedType.getDeclaredFields()) {
          List<String> nameSegments = new ArrayList<>(fieldDetails.getNameSegments());
          List<Field> parentFields = new ArrayList<>(fieldDetails.getParentFields());
          parentFields.add(fieldDetails.getProcessedField());
          nameSegments.add(fieldDetails.getProcessedField().getName());

          queue.push(new FieldDetails(declaredField, parentFields, nameSegments));
        }
      } else {
        String name = fieldDetails.getName();
        Class<? extends NamingStrategy> naming = properties.require(NAMING_STRATEGY);
        var nameSegments = fieldDetails.getNameSegments();

        Field processedField = fieldDetails.getProcessedField();
        Column column = processedField.getAnnotation(Column.class);
        Column overrideColum = overrideColumns.get(String.join(DOT, nameSegments) + DOT + name);

        if (overrideColum != null) {
          naming = overrideColum.naming();
          name = replaceValueIfNotEmpty(overrideColum.value(), name);
        } else {
          if (column != null) {
            naming = column.naming();
            name = replaceValueIfNotEmpty(column.value(), name);
          }

          nameSegments.add(name);
          name = String.join(SEPARATOR, nameSegments);
        }

        FactoryContext context = getContext();
        NamingStrategy strategy = context.getInstance(naming);

        columns.add(new EntityColumnDefinition(
            properties.require(TABLE),
            fieldDetails,
            strategy.resolve(name),
            typeFactory.build(processedField, properties)
        ));
      }
    }

    return columns;
  }

  @Override
  public boolean canBuild(Field field) {
    Class<?> type = field.getType();
    return !type.isPrimitive();
  }

  // Gets a map of overridden columns based on the annotations for the specified field.
  private Map<String, Column> getOverrideColumns(Field field) {
    ColumnOverrides columnOverrides = field.getAnnotation(ColumnOverrides.class);
    ColumnOverride columnOverride = field.getAnnotation(ColumnOverride.class);

    String fieldName = field.getName();
    Map<String, Column> overrideColumns = new HashMap<>();
    if (columnOverrides != null) {
      for (ColumnOverride override : columnOverrides.value()) {
        overrideColumns.put(
            String.join(DOT, fieldName, override.name()),
            override.column()
        );
      }
    } else if (columnOverride != null) {
      overrideColumns.put(
          String.join(DOT, fieldName, columnOverride.name()),
          columnOverride.column()
      );
    }
    return overrideColumns;
  }

  // Replaces the original value with a new value if the new value is not empty.
  private String replaceValueIfNotEmpty(String newValue, String currentValue) {
    if (!newValue.isEmpty()) {
      currentValue = newValue;
    }
    return currentValue;
  }
}
