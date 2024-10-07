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

package io.github.devastool.entity2jooq.codegen.definition.factory;

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
  private final List<Field> parentFields;
  private final List<String> nameSegments;

  private static final int INDEX_LAST = 1;

  /**
   * Creates a new instance of FieldDetails using the specified being processed field name, parent
   * field name, and parent field chain names.
   *
   * @param processedField   the being processed field
   * @param parentFields     the parent fields of the being processed field
   * @param nameSegments     the list containing the field name segments
   */
  public FieldDetails(Field processedField, List<Field> parentFields, List<String> nameSegments) {
    this.processedField = processedField;
    this.parentFields = parentFields;
    this.nameSegments = nameSegments;
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
   * Returns the last parent field.
   *
   * @return the field
   */
  public Field getLastParentField() {
    return parentFields.get(parentFields.size() - INDEX_LAST);
  }

  /**
   * Checks if the current object is embedded.
   *
   * @return true if the current object is embedded, false otherwise
   */
  public boolean isEmbedded() {
    return !parentFields.isEmpty();
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
   * Retrieves the list of field name segments.
   *
   * @return the list containing the field name segments
   */
  public List<String> getNameSegments() {
    return nameSegments;
  }

  /**
   * Returns the list of parent fields.
   *
   * @return the list of parent fields
   */
  public List<Field> getParentFields() {
    return parentFields;
  }
}
