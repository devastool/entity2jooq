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
  private final Class<?> entityType;
  private final List<ColumnDefinition> columns;

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
      Class<?> entityType,
      List<ColumnDefinition> columns
  ) {
    super(schema, name, "");
    this.entityType = entityType;
    this.columns = columns;
  }

  @Override
  protected List<ColumnDefinition> getElements0() throws SQLException {
    return columns;
  }

  /**
   * Returns type of entity.
   *
   * @return type of entity
   */
  public Class<?> getEntityType() {
    return entityType;
  }
}
