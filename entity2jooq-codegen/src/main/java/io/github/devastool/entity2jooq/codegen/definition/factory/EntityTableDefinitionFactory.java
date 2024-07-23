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

import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.Database;

/**
 * The factory for {@link EntityTableDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityTableDefinitionFactory {
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
      EntityColumnDefinitionFactory columnFactory
  ) {
    this.schemaFactory = schemaFactory;
    this.columnFactory = columnFactory;
  }

  /**
   * Builds new instance of {@link EntityTableDefinition}.
   *
   * @param type     entity class which has {@link Table} annotation
   * @param database meta-information provider
   */
  public Optional<EntityTableDefinition> build(Class<?> type, Database database) {
    Table tableAnnotation = type.getAnnotation(Table.class);
    if (tableAnnotation != null) {
      Optional<EntitySchemaDefinition> schema = schemaFactory.build(type, database);
      if (schema.isPresent()) {
        String name = tableAnnotation.value();
        NamingStrategy strategy = new SnakeCaseStrategy(); // TODO. Use tableAnnotation.naming()
        if (name.isEmpty()) {
          name = strategy.resolve(type.getSimpleName());
        }

        ArrayList<ColumnDefinition> columns = new ArrayList<>();
        EntityTableDefinition table = new EntityTableDefinition(schema.get(), name, columns);
        for (Field field : type.getDeclaredFields()) {
          columnFactory
              .build(field, table)
              .ifPresent(columns::add);
        }
        return Optional.of(table);
      }
    }
    return Optional.empty();
  }
}
