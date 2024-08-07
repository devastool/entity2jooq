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
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import java.lang.reflect.Field;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityColumnDefinitionFactory extends
    DefinitionFactory<Field, EntityColumnDefinition> {
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

  @Override
  public EntityColumnDefinition build(Field field, CodegenProperties properties) {
    String name = field.getName();
    Class<? extends NamingStrategy> naming = properties.require(NAMING_STRATEGY);

    Column annotation = field.getAnnotation(Column.class);
    if (annotation != null) {
      naming = annotation.naming();

      String definedName = annotation.value();
      if (definedName != null && !definedName.isEmpty()) {
        name = definedName;
      }
    }

    FactoryContext context = getContext();
    NamingStrategy strategy = context.getInstance(naming);
    return new EntityColumnDefinition(
        properties.require(TABLE),
        strategy.resolve(name),
        typeFactory.build(field, properties)
    );
  }

  @Override
  public boolean canBuild(Field field) {
    Class<?> type = field.getType();
    return !type.isPrimitive() && typeFactory.canBuild(field);
  }
}
