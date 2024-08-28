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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents details about a being processed field.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class FieldDetails {
  private final Field processedField;
  private final Field parentField;
  private final List<String> parentFieldNames;

  private static final int FIRST_INDEX = 0;
  private static final int SECOND_INDEX = 1;

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
    this.parentField = parentField;
    this.parentFieldNames = parentFieldsName;
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
   * Returns the parent field.
   *
   * @return the field
   */
  public Field getParentField() {
    return parentField;
  }

  /**
   * Returns the parent field.
   *
   * @return the field
   */
  public boolean isEmbedded() {
    return Objects.nonNull(parentField);
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
  public List<String> getParentFieldNames() {
    return parentFieldNames;
  }

  /**
   * Returns the entity name.
   *
   * @return the entity name
   */
  public String getEntityName() {
    return joinToCamelCase(parentFieldNames);
  }

  /**
   * Returns the parent entity name.
   *
   * @return the parent entity name
   */
  public String getParentEntityName() {
    var fieldsNames = parentFieldNames.subList(
            FIRST_INDEX,
            parentFieldNames.size() - SECOND_INDEX
        );
    return joinToCamelCase(fieldsNames);
  }

  // Joins a collection of strings into a camel case format.
  private String joinToCamelCase(Collection<String> names) {
    String name = names
        .stream()
        .map(
            word -> word
                .substring(FIRST_INDEX, SECOND_INDEX)
                .toUpperCase()
                .concat(word.substring(SECOND_INDEX))
        )
        .collect(Collectors.joining());

    return name
        .substring(FIRST_INDEX, SECOND_INDEX)
        .toLowerCase()
        .concat(name.substring(SECOND_INDEX));
  }
}
