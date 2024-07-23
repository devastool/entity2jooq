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

import org.jooq.meta.Database;
import org.jooq.meta.SchemaDefinition;

/**
 * Meta-information about schema by annotation
 * {@link io.github.devastool.entity2jooq.annotation.Schema}.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntitySchemaDefinition extends SchemaDefinition {
  /**
   * Constructs new instance of {@link EntitySchemaDefinition}.
   *
   * @param database meta-information provider
   * @param name     name of the schema
   */
  public EntitySchemaDefinition(Database database, String name) {
    super(database, name, "");
  }
}
