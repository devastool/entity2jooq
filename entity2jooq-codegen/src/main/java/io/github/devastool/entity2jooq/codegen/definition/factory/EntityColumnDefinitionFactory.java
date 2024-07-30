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

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityColumnDefinitionFactory extends ContextableFactory {
  private final EntityDataTypeDefinitionFactory typeFactory;

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

  /**
   * Builds new instance of {@link EntityColumnDefinition}.
   *
   * @param field entity field, annotation {@link Column} is optional
   * @param table meta-information about table
   */
  public Optional<EntityColumnDefinition> build(Field field, EntityTableDefinition table) {
    String name = field.getName();
    Column columnAnnotation = field.getAnnotation(Column.class);
    if (columnAnnotation != null) {
      String definedName = columnAnnotation.value();
      if (definedName != null && !definedName.isEmpty()) {
        name = definedName;
      }
    }

    var strategy = getNamingStrategy(table, columnAnnotation);
    return Optional.of(
        new EntityColumnDefinition(
            table,
            strategy.resolve(name),
            typeFactory.build(table.getSchema(), field)
        )
    );
  }

  // if columnAnnotation present return naming strategy from it naming parameter, else
  // return naming strategy of table annotation.
  private NamingStrategy getNamingStrategy(EntityTableDefinition table, Column columnAnnotation) {
    Class<? extends NamingStrategy> strategy;
    if (columnAnnotation != null) {
      strategy = columnAnnotation.naming();
    } else {
      strategy = table.getStrategy();
    }
    return getContext().getInstance(strategy);
  }
}
