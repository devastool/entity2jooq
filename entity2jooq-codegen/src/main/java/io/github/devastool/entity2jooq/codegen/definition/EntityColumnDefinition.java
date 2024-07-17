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

import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.TableDefinition;

/**
 * Meta-information about column by annotation
 * {@link io.github.devastool.entity2jooq.annotation.Column}.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class EntityColumnDefinition extends DefaultColumnDefinition {
  /**
   * Constructs new instance of {@link EntityColumnDefinition}.
   *
   * @param table meta-information about column table
   * @param name  name of the column
   * @param type  type of the column
   */
  public EntityColumnDefinition(
      TableDefinition table,
      String name,
      DataTypeDefinition type
  ) {
    super(table, name, 0, type, false, "");
  }
}
