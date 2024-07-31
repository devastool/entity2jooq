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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Context with caching instance.
 *
 * @author Evgeniy_Gerasimov
 * @since 1.0.0
 */
public class FactoryContext {
  /**
   * Cache of context instances.
   */
  private final Map<Class<?>, Object> CACHE = new HashMap<>();

  /**
   * Getting instance of naming strategy.
   *
   * @param type Class of naming strategy.
   * @param args argument for instance constructor.
   * @return NamingStrategy implementation.
   */
  public <T> T getInstance(Class<T> type, Object... args) {
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null");
    }

    return type.cast(CACHE.computeIfAbsent(type, value -> createInstance(type, args)));
  }

  /**
   * Create new instance of type Class.
   *
   * @param type Class of naming strategy.
   * @param args argument for instance constructor.
   * @return new instance.
   */
  private <T> T createInstance(Class<T> type, Object[] args) {
    try {
      var argsTypes = Arrays
          .stream(args)
          .map(Object::getClass)
          .toArray(Class[]::new);
      return type.getConstructor(argsTypes).newInstance(args);
    } catch (Exception exception) {
      throw new RuntimeException(
          String.format("Failed to create an instance of %s", type.getName()),
          exception
      );
    }
  }
}
