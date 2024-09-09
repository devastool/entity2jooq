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

import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.DATABASE;
import static io.github.devastool.entity2jooq.codegen.properties.CodegenProperty.NAMING_STRATEGY;

import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;

/**
 * The factory for {@link EntitySchemaDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntitySchemaDefinitionFactory
    extends DefinitionFactory<Class<?>, EntitySchemaDefinition> {
  /**
   * Constructs new instance of {@link EntitySchemaDefinitionFactory}.
   *
   * @param context instance of {@link FactoryContext}
   */
  public EntitySchemaDefinitionFactory(FactoryContext context) {
    super(context);
  }

  @Override
  public EntitySchemaDefinition build(Class<?> type, CodegenProperties properties) {
    String name = getLastPackageName(type.getPackage());
    Class<? extends NamingStrategy> naming = properties.require(NAMING_STRATEGY);

    Schema annotation = type.getAnnotation(Schema.class);
    if (annotation != null) {
      naming = annotation.naming();

      String definedName = annotation.value();
      if (!definedName.isEmpty()) {
        name = definedName;
      }
    }

    NamingStrategy strategy = getContext().getInstance(naming);
    return new EntitySchemaDefinition(
        properties.require(DATABASE),
        strategy.resolve(name)
    );
  }

  // Returns last name of class package
  private String getLastPackageName(Package packageDefinition) {
    String name = packageDefinition.getName();
    int index = name.lastIndexOf('.');
    if (index != -1) {
      return name.substring(index + 1);
    }
    return name;
  }
}
