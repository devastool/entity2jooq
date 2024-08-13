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

package io.github.devastool.entity2jooq.codegen.definition.factory.column;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Represents details about a being processed field.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class FieldDetails {
  private final Field processedField;
  private final Field parentfield;
  private final List<String> parentFieldsName;

  /**
   * Creates a new instance of FieldDetails using the specified being processed field name, parent
   * field name, and parent field chain names.
   *
   * @param processedField   the being processed field.
   * @param parentField      the parent field of the being processed field, or {@code null} if there
   *                         is no parent field
   * @param parentFieldsName the list of parent field names
   */
  public FieldDetails(Field processedField, Field parentField, List<String> parentFieldsName) {
    if (parentField != null) {
      parentFieldsName.add(parentField.getName());
    }
    this.processedField = processedField;
    this.parentfield = parentField;
    this.parentFieldsName = parentFieldsName;
  }

  /**
   * Returns the being processed field.
   *
   * @return the field
   */
  public Field getProcessedField() {
    return processedField;
  }

  /**
   * Returns the type of the being processed field.
   *
   * @return the class representing the type of the field
   */
  public Class<?> getProcessedType() {
    return processedField.getType();
  }

  /**
   * Returns the name of the field being processed.
   *
   * @return the name of the field
   */
  public String getName() {
    return processedField.getName();
  }

  /**
   * Returns the list of parent field names.
   *
   * @return the list of parent field names
   */
  public List<String> getParentFieldsName() {
    return parentFieldsName;
  }
}
