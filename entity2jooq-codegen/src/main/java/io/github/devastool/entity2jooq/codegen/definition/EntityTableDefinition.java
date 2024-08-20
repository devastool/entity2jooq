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
import java.util.Collection;
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
  private boolean mapping;
  private Class<?> entityType;
  private List<ColumnDefinition> columns;

  /**
   * Constructs new instance of {@link EntityTableDefinition}.
   *
   * @param schema meta-information about schema
   * @param name   name of the table
   */
  public EntityTableDefinition(SchemaDefinition schema, String name) {
    super(schema, name, "");
  }

  @Override
  protected List<ColumnDefinition> getElements0() throws SQLException {
    return columns;
  }

  /**
   * Sets columns of the table.
   *
   * @param columns columns of the table
   */
  public void setColumns(Collection<? extends ColumnDefinition> columns) {
    this.columns = List.copyOf(columns);
  }

  /**
   * Returns enable/disable flag of mapping functionality.
   *
   * @return enable/disable flag of mapping functionality
   */
  public boolean isMapping() {
    return mapping;
  }

  /**
   * Sets enable/disable flag of mapping functionality.
   *
   * @param mapping enable/disable flag of mapping functionality
   */
  public void setMapping(boolean mapping) {
    this.mapping = mapping;
  }

  /**
   * Returns type of entity.
   *
   * @return type of entity
   */
  public Class<?> getEntityType() {
    return entityType;
  }

  /**
   * Sets type of entity.
   *
   * @param entityType type of entity
   */
  public void setEntityType(Class<?> entityType) {
    this.entityType = entityType;
  }
}
