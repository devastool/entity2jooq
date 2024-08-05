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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Represents details about a field.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class FieldDetails {
  private final Field field;
  private final Field parentfield;
  private final List<String> parentFieldsName;

  /**
   * Constructs a new FieldDetails instance with the specified field, parent field, and parent field names.
   *
   * @param field the field this instance will represent
   * @param parentField the parent field of the field, or {@code null} if there is no parent field
   * @param parentFieldsName the list of parent field names
   */
  public FieldDetails(Field field, Field parentField, List<String> parentFieldsName) {
    this.field = field;
    this.parentfield = parentField;
    this.parentFieldsName = parentFieldsName;
  }

  /**
   * Returns the field this instance represents.
   *
   * @return the field
   */
   public Field getField() {
    return field;
  }

  /**
   * Returns the type of the field.
   *
   * @return the class representing the type of the field
   */
  public Class<?> getType() {
    return field.getType();
  }

  /**
   * Returns the name of the field.
   *
   * @return the name of the field
   */
  public String getName() {
    return field.getName();
  }

  /**
   *
   * @return
   */
  public Field getParentField() {
    return parentfield;
  }

  /**
   * Returns the list of parent field names.
   *
   * @return the list of parent field names
   */
  public List<String> getParentFieldsName() {
    return parentFieldsName;
  }

  /**
   * Returns the parent field of the field, or {@code null} if there is no parent field.
   *
   * @return the parent field, or {@code null} if there is no parent field
   */
  public String getParentFieldName() {
    if (parentfield == null) {
      return null;
    }
    return parentfield.getName();
  }

  /**
   * Returns the annotations declared on the parent field, or {@code null} if there is no parent field.
   *
   * @return an array of the annotations declared on the parent field, or {@code null} if there is no parent field
   */
  public Annotation[] getParentAnnotations() {
    if (parentfield == null) {
      return null;
    }
    return parentfield.getDeclaredAnnotations();
  }
}
