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

import org.jooq.impl.DSL;
import org.jooq.meta.Database;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * Meta-information about type of {@link EntityColumnDefinition}.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public class EntityDataTypeDefinition extends DefaultDataTypeDefinition {
  private static final String JAVA_TYPE_REFERENCE_TEMPLATE =
      "new org.jooq.impl.DefaultDataType(org.jooq.SQLDialect.%s, %s.class, \"%s\")";

  /**
   * Constructs new instance of {@link EntityDataTypeDefinition}.
   *
   * @param schema   meta-information about schema
   * @param typeJava Java type class
   * @param typeSql  SQL type
   */
  public EntityDataTypeDefinition(SchemaDefinition schema, Class<?> typeJava, String typeSql) {
    super(
        schema.getDatabase(),
        schema,
        typeSql,
        null,
        null,
        null,
        null,
        null,
        DSL.name((String) null),
        null,
        null,
        typeJava.getCanonicalName()
    );
  }

  /**
   * Returns reference of the Java type.
   *
   * @return Java type reference
   */
  public String getJavaTypeReference() {
    Database database = getDatabase();
    return String.format(
        JAVA_TYPE_REFERENCE_TEMPLATE,
        database.getDialect(),
        getJavaType(),
        getType()
    );
  }
}
