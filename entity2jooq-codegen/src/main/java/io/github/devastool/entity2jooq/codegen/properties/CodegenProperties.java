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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * The property set of code generation plugin.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class CodegenProperties {
  private final Map<CodegenProperty<?>, Object> properties = new HashMap<>();

  /**
   * Constructs new instance of {@link CodegenProperties}.
   *
   * @param init property set for initialization
   */
  public CodegenProperties(Map<CodegenProperty<?>, Object>... init) {
    for (Map<CodegenProperty<?>, Object> initProperties : init) {
      properties.putAll(initProperties);
    }
  }

  /**
   * Constructs new instance of {@link CodegenProperties}.
   *
   * @param parent parent property set
   * @param init   property set for initialization
   */
  public CodegenProperties(CodegenProperties parent, Map<CodegenProperty<?>, Object>... init) {
    properties.putAll(parent.properties);
    for (Map<CodegenProperty<?>, Object> initProperties : init) {
      properties.putAll(initProperties);
    }
  }

  /**
   * Returns value by property.
   *
   * @param <T>      value type
   * @param property information for value extracting
   * @return value by property, or empty container
   */
  public <T> Optional<T> get(CodegenProperty<T> property) {
    Class<T> type = property.getType();
    return Optional
        .ofNullable(properties.get(property))
        .map(type::cast);
  }

  /**
   * Returns value by property.
   *
   * @param <T>          value type
   * @param property     information for value extracting
   * @param defaultValue value that should be returned if value is not found by property
   * @return value by property, or default value
   */
  public <T> T get(CodegenProperty<T> property, T defaultValue) {
    return this
        .get(property)
        .orElse(defaultValue);
  }

  /**
   * Returns value by property.
   *
   * @param <T>      value type
   * @param property information for value extracting
   * @return value by property or throws exception
   * @throws NoSuchElementException if value is not found by property
   */
  public <T> T require(CodegenProperty<T> property) throws NoSuchElementException {
    return this
        .get(property)
        .orElseThrow(() -> new NoSuchElementException(
            String.join("", "Property [", property.getName(), "] is not found"))
        );
  }
}
