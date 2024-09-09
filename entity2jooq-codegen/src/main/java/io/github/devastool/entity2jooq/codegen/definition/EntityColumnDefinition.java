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

import io.github.devastool.entity2jooq.codegen.definition.factory.column.FieldDetails;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.TableDefinition;

/**
 * Meta-information about column by annotation
 * {@link io.github.devastool.entity2jooq.annotation.Column}.
 *
 * @author Andrey_Yurzanov, Sergey_Konovalov
 * @since 1.0.0
 */
public class EntityColumnDefinition extends DefaultColumnDefinition
    implements Comparable<EntityColumnDefinition> {
  private final FieldDetails fieldDetails;

  private static final String GETTER_PREFIX = "get";
  private static final String SETTER_PREFIX = "set";
  private static final int FIRST_INDEX = 0;
  private static final int SECOND_INDEX = 1;

  /**
   * Constructs new instance of {@link EntityColumnDefinition}.
   *
   * @param table meta-information about column table
   * @param name  name of the column
   * @param type  type of the column
   */
  public EntityColumnDefinition(
      TableDefinition table,
      FieldDetails fieldDetails,
      String name,
      DataTypeDefinition type
  ) {
    super(table, name, 0, type, false, "");
    this.fieldDetails = fieldDetails;
  }

  @Override
  public int compareTo(EntityColumnDefinition other) {
    return getName().compareTo(other.getName());
  }

  /**
   * Returns name of field getter.
   *
   * @return name of field getter
   */
  public String getGetterName() {
    String name = getMethodNamePostfix(fieldDetails.getName());
    return GETTER_PREFIX.concat(name);
  }

  /**
   * Returns name of field setter.
   *
   * @return name of field setter
   */
  public String getSetterName() {
    String name = getMethodNamePostfix(fieldDetails.getName());
    return SETTER_PREFIX.concat(name);
  }

  /**
   * Checks if the field are embedded.
   *
   * @return returns true if embedded
   */
  public Boolean isEmbedded() {
    return this.fieldDetails.isEmbedded();
  }

  /**
   * Returns FieldDetails.
   *
   * @return returns FieldDetails
   */
  public FieldDetails getFieldDetails() {
    return this.fieldDetails;
  }

  private String getMethodNamePostfix(String name) {
    if (name.length() > SECOND_INDEX) {
      return name
          .substring(FIRST_INDEX, SECOND_INDEX)
          .toUpperCase()
          .concat(name.substring(SECOND_INDEX));
    }
    return name.toUpperCase();
  }
}
