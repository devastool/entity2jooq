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
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.SCHEMA;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.TABLE;

import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.meta.ColumnDefinition;

/**
 * The factory for {@link EntityTableDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityTableDefinitionFactory
    extends DefinitionFactory<Class<?>, EntityTableDefinition> {
  private final EntitySchemaDefinitionFactory schemaFactory;
  private final EntityColumnDefinitionFactory columnFactory;

  /**
   * Constructs new instance of {@link EntityTableDefinitionFactory}.
   *
   * @param schemaFactory instance of {@link EntitySchemaDefinitionFactory}
   * @param columnFactory instance of {@link EntityColumnDefinitionFactory}
   */
  public EntityTableDefinitionFactory(
      EntitySchemaDefinitionFactory schemaFactory,
      EntityColumnDefinitionFactory columnFactory,
      FactoryContext context
  ) {
    super(context);
    this.schemaFactory = schemaFactory;
    this.columnFactory = columnFactory;
  }

  @Override
  public EntityTableDefinition build(Class<?> type, CodegenProperties properties) {
    Table annotation = type.getAnnotation(Table.class);
    if (annotation != null) {
      String name = annotation.value();

      Class<? extends NamingStrategy> naming = annotation.naming();
      NamingStrategy strategy = getContext().getInstance(naming);
      if (name.isEmpty()) {
        name = strategy.resolve(type.getSimpleName());
      }

      EntitySchemaDefinition schema = schemaFactory.build(
          type,
          new CodegenProperties(properties, Map.of(NAMING_STRATEGY, naming))
      );
      ArrayList<ColumnDefinition> columns = new ArrayList<>();
      EntityTableDefinition table = new EntityTableDefinition(
          schema,
          name,
          columns
      );

      CodegenProperties columnProperties = new CodegenProperties(
          properties,
          Map.of(TABLE, table, SCHEMA, schema, NAMING_STRATEGY, naming)
      );
      for (Field field : type.getDeclaredFields()) {
        columns.addAll(columnFactory.build(field, columnProperties));
      }

      Set<ColumnDefinition> uniqueColumns = new HashSet<>();
      var existsColumns = columns
          .stream()
          .filter(column -> !uniqueColumns.add(column))
          .map(ColumnDefinition::getName)
          .collect(Collectors.joining(","));

      if (!existsColumns.isEmpty()) {
        throw new IllegalArgumentException(
            String.format(
                "Column %s already exists. Use @ColumnOverride",
                existsColumns
            )
        );
      }

      return table;
    }
    return null;
  }

  @Override
  public boolean canBuild(Class<?> type) {
    return Objects.nonNull(type.getAnnotation(Table.class));
  }
}