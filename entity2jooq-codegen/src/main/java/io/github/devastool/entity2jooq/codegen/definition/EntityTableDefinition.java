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

package io.github.devastool.entity2jooq.codegen.definition;

import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import java.sql.SQLException;
import java.util.List;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * Meta-information about table by annotation
 * {@link io.github.devastool.entity2jooq.annotation.Table}.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityTableDefinition extends AbstractTableDefinition {
  private final List<ColumnDefinition> columns;
  private final Class<? extends NamingStrategy> strategy;

  /**
   * Constructs new instance of {@link EntityTableDefinition}.
   *
   * @param schema  meta-information about schema
   * @param name    name of the table
   * @param columns list of the table columns
   */
  public EntityTableDefinition(
      SchemaDefinition schema,
      String name,
      List<ColumnDefinition> columns,
      Class<? extends NamingStrategy> strategy
  ) {
    super(schema, name, "");
    this.columns = columns;
    this.strategy = strategy;
  }

  public Class<? extends NamingStrategy> getStrategy() {
    return strategy;
  }

  @Override
  protected List<ColumnDefinition> getElements0() throws SQLException {
    return columns;
  }
}
