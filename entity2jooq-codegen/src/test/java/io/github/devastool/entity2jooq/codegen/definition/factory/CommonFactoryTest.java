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

import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import io.github.devastool.entity2jooq.annotation.type.converter.EnumConverter;
import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;
import io.github.devastool.entity2jooq.codegen.properties.CodegenProperty;
import java.util.Map;
import org.jooq.SQLDialect;

/**
 * Common class for tests of factories.
 *
 * @author Andrey_Yurzanov
 */
public class CommonFactoryTest {
  /**
   * Returns properties for tests.
   *
   * @return properties for tests
   */
  public CodegenProperties getProperties() {
    Entity2JooqDatabase database = new Entity2JooqDatabase();
    EntitySchemaDefinition schema = new EntitySchemaDefinition(database, "test_schema");
    EntityTableDefinition table = new EntityTableDefinition(schema, "test_table");

    return new CodegenProperties(
        Map.of(
            CodegenProperty.NAMING_STRATEGY, SnakeCaseStrategy.class,
            CodegenProperty.TABLE, table,
            CodegenProperty.DIALECT, SQLDialect.POSTGRES.getName(),
            CodegenProperty.SCHEMA, schema,
            CodegenProperty.DATABASE, new Entity2JooqDatabase()
        )
    );
  }

  /**
   * Returns instance of {@link EntityDataTypeDefinitionFactory}.
   *
   * @return instance of {@link EntityDataTypeDefinitionFactory}.
   */
  public EntityDataTypeDefinitionFactory getTypeFactory() {
    return new EntityDataTypeDefinitionFactory(
        new FactoryContext(),
        Map.of(Enum.class, EnumConverter.class)
    );
  }

  /**
   * Returns instance of {@link EntityColumnDefinitionFactory}.
   *
   * @return instance of {@link EntityColumnDefinitionFactory}.
   */
  public EntityColumnDefinitionFactory getColumnFactory() {
    FactoryContext context = new FactoryContext();
    return new EntityColumnDefinitionFactory(
        new EntityDataTypeDefinitionFactory(context, Map.of(Enum.class, EnumConverter.class)),
        context
    );
  }

  /**
   * Returns instance of {@link EntityTableDefinitionFactory}.
   *
   * @return instance of {@link EntityTableDefinitionFactory}.
   */
  public EntityTableDefinitionFactory getTableFactory() {
    FactoryContext context = new FactoryContext();
    return new EntityTableDefinitionFactory(
        new EntitySchemaDefinitionFactory(context),
        new EntityColumnDefinitionFactory(
            new EntityDataTypeDefinitionFactory(context, Map.of(Enum.class, EnumConverter.class)),
            context
        ),
        context
    );
  }

  /**
   * Returns instance of {@link EntitySchemaDefinitionFactory}.
   *
   * @return instance of {@link EntitySchemaDefinitionFactory}.
   */
  public EntitySchemaDefinitionFactory getSchemaFactory() {
    return new EntitySchemaDefinitionFactory(new FactoryContext());
  }
}
