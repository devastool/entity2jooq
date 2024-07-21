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

import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import java.util.Optional;
import org.jooq.meta.Database;

/**
 * The factory for {@link EntitySchemaDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class EntitySchemaDefinitionFactory {
  /**
   * Builds new instance of {@link EntitySchemaDefinition}.
   *
   * @param type     entity class, annotation {@link Schema} is optional
   * @param database meta-information provider
   */
  public Optional<EntitySchemaDefinition> build(Class<?> type, Database database) {
    String schemaName;
    Schema schemaAnnotation = type.getAnnotation(Schema.class);
    Class<? extends NamingStrategy> strategyClass = null;
    if (schemaAnnotation != null && !schemaAnnotation.value().isEmpty()) {
      schemaName = schemaAnnotation.value();
      strategyClass = schemaAnnotation.naming();
    } else {
      schemaName = getLastPackageName(type.getPackage());
    }

    NamingStrategy strategy = NamingStrategy.getInstance(strategyClass);
    return Optional.of(new EntitySchemaDefinition(database, strategy.resolve(schemaName)));
  }

  private String getLastPackageName(Package packageDefinition) {
    String name = packageDefinition.getName();
    int index = name.lastIndexOf('.');
    if (index != -1) {
      return name.substring(index + 1);
    }
    return name;
  }
}
