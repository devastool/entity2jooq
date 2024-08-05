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

package io.github.devastool.entity2jooq.codegen.properties;

import io.github.devastool.entity2jooq.codegen.definition.EntitySchemaDefinition;
import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import org.jooq.meta.Database;

/**
 * Property of code generation plugin.
 *
 * @param <T> property type
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class CodegenProperty<T> {
  private final String name;
  private final Class<T> type;

  /**
   * Classpath property.
   */
  public static final CodegenProperty<String> CLASSPATH =
      new CodegenProperty<>("classpath", String.class);

  /**
   * Property of classes directory.
   */
  public static final CodegenProperty<String> CLASSES =
      new CodegenProperty<>("classes", String.class);

  /**
   * Property of test classes' directory.
   */
  public static final CodegenProperty<String> TEST_CLASSES =
      new CodegenProperty<>("testClasses", String.class);

  /**
   * Property of SQL dialect.
   */
  public static final CodegenProperty<String> DIALECT =
      new CodegenProperty<>("dialect", String.class);

  /**
   * Property of schema.
   */
  public static final CodegenProperty<EntitySchemaDefinition> SCHEMA =
      new CodegenProperty<>("schema", EntitySchemaDefinition.class);

  /**
   * Property of table.
   */
  public static final CodegenProperty<EntityTableDefinition> TABLE =
      new CodegenProperty<>("table", EntityTableDefinition.class);

  /**
   * Property of database.
   */
  public static final CodegenProperty<Database> DATABASE =
      new CodegenProperty<>("database", Database.class);

  /**
   * Property of naming strategy.
   */
  public static final CodegenProperty<Class> NAMING_STRATEGY =
      new CodegenProperty<>("namingStrategy", Class.class);

  /**
   * Constructs new instance of {@link CodegenProperties}.
   *
   * @param name property name
   * @param type property type
   */
  public CodegenProperty(String name, Class<T> type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Returns name of the property.
   *
   * @return name of the property
   */
  public String getName() {
    return name;
  }

  /**
   * Returns type of the property.
   *
   * @return type of the property
   */
  public Class<T> getType() {
    return type;
  }
}
