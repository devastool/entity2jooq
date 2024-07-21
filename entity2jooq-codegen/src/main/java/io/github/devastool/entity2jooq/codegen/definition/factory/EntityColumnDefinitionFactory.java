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
import io.github.devastool.entity2jooq.codegen.definition.EntityDataTypeDefinition;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.TableDefinition;

/**
 * The factory for {@link EntityColumnDefinition} building.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class EntityColumnDefinitionFactory extends ContextableFactory{
  private final Map<Class<?>, String> SQL_TYPES = initTypes();

  public EntityColumnDefinitionFactory(FactoryContext<?> context) {
    super(context);
  }

  /**
   * Builds new instance of {@link EntityColumnDefinition}.
   *
   * @param field entity field, annotation {@link Column} is optional
   * @param table meta-information about table
   */
  public Optional<EntityColumnDefinition> build(Field field, TableDefinition table) {
    String name = field.getName();
    Class<?> classType = field.getType();
    String sqlType = SQL_TYPES.get(classType);

    Column columnAnnotation = field.getAnnotation(Column.class);
    Class<? extends NamingStrategy> strategyClass = null;
    if (columnAnnotation != null) {
      String definedName = columnAnnotation.value();
      if (definedName != null && !definedName.isEmpty()) {
        name = definedName;
      }

      String definedType = columnAnnotation.type();
      if (definedName != null && !definedType.isEmpty()) {
        sqlType = definedType;
      }

      strategyClass = columnAnnotation.naming();
    }

    SchemaDefinition schema = table.getSchema();
    var strategy = (NamingStrategy) getContext().getInstance(strategyClass);
    EntityDataTypeDefinition type = new EntityDataTypeDefinition(schema, classType, sqlType);
    return Optional.of(
        new EntityColumnDefinition(table, strategy.resolve(name), type)
    );
  }

  private static Map<Class<?>, String> initTypes() { // TODO. Use columnAnnotation.typeMapper()
    Map<Class<?>, String> types = new HashMap<>();
    types.put(SQLDataType.BIGINT.getType(), SQLDataType.BIGINT.getTypeName());
    types.put(SQLDataType.VARCHAR.getType(), SQLDataType.VARCHAR.getTypeName());
    types.put(SQLDataType.BOOLEAN.getType(), SQLDataType.BOOLEAN.getTypeName());
    types.put(SQLDataType.TINYINT.getType(), SQLDataType.TINYINT.getTypeName());
    types.put(SQLDataType.SMALLINT.getType(), SQLDataType.SMALLINT.getTypeName());
    types.put(SQLDataType.INTEGER.getType(), SQLDataType.INTEGER.getTypeName());
    types.put(SQLDataType.BIGINT.getType(), SQLDataType.BIGINT.getTypeName());
    types.put(SQLDataType.DATE.getType(), SQLDataType.DATE.getTypeName());
    types.put(SQLDataType.TIMESTAMP.getType(), SQLDataType.TIMESTAMP.getTypeName());
    types.put(SQLDataType.TIME.getType(), SQLDataType.TIME.getTypeName());
    types.put(SQLDataType.LOCALDATE.getType(), SQLDataType.LOCALDATE.getTypeName());
    types.put(SQLDataType.LOCALTIME.getType(), SQLDataType.LOCALTIME.getTypeName());
    types.put(SQLDataType.LOCALDATETIME.getType(), SQLDataType.LOCALDATETIME.getTypeName());

    return Collections.unmodifiableMap(types);
  }
}
