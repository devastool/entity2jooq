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

package io.github.devastool.entity2jooq.codegen.definition.type;

import io.github.devastool.entity2jooq.codegen.definition.EntityColumnDefinition;
import java.util.Arrays;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;

/**
 * Meta-information about type of {@link EntityColumnDefinition}.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class EntityDataTypeDefinition extends DefaultDataTypeDefinition {
  private SQLDialect dialect;
  private ConverterDefinition converterDefinition;

  private static final String JAVA_TYPE_REFERENCE_TEMPLATE =
      "new org.jooq.impl.DefaultDataType(org.jooq.SQLDialect.%s, %s.class, \"%s\")";

  /**
   * Constructs new instance of {@link EntityDataTypeDefinition}.
   *
   * @param schema   meta-information about schema
   * @param typeJava Java type class
   * @param typeSql  SQL type
   */
  public EntityDataTypeDefinition(
      SchemaDefinition schema,
      Class<?> typeJava,
      String typeSql
  ) {
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
    this.dialect = SQLDialect.DEFAULT;
  }

  /**
   * Sets SQL-dialect.
   *
   * @param dialect SQL-dialect
   */
  public void setDialect(String dialect) {
    this.dialect = Arrays
        .stream(SQLDialect.values())
        .filter(SQLDialect::supported)
        .filter(value -> value.getName().equalsIgnoreCase(dialect))
        .findFirst()
        .orElse(SQLDialect.DEFAULT);
  }

  /**
   * Returns instance of {@link ConverterDefinition}.
   *
   * @return instance of {@link ConverterDefinition}
   */
  public ConverterDefinition getConverterDefinition() {
    return converterDefinition;
  }

  /**
   * Sets instance of {@link ConverterDefinition}.
   *
   * @param converterDefinition instance of {@link ConverterDefinition}
   */
  public void setConverterDefinition(ConverterDefinition converterDefinition) {
    this.converterDefinition = converterDefinition;
  }

  /**
   * Returns reference of the Java type.
   *
   * @return Java type reference
   */
  public String getJavaTypeReference() {
    String javaType = getJavaType();
    if (converterDefinition != null) {
      javaType = converterDefinition
          .getConverter()
          .fromType()
          .getCanonicalName();
    }

    return String.format(
        JAVA_TYPE_REFERENCE_TEMPLATE,
        dialect,
        javaType,
        getType()
    );
  }
}
