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

package io.github.devastool.entity2jooq.annotation.naming;

import java.util.HashMap;
import java.util.Map;

/**
 * A Naming strategy for a data extraction.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
public interface NamingStrategy {
  /**
   * Cache of strategy instances.
   */
  Map<Class<?>, NamingStrategy> CACHE = new HashMap<>();

  /**
   * Getting instance of naming strategy.
   *
   * @param namingStrategy Class of naming strategy.
   * @return NamingStrategy implementation.
   */
  static NamingStrategy getInstance(Class<? extends NamingStrategy> namingStrategy) {
    if (namingStrategy != null) {
      if (CACHE.containsKey(namingStrategy)) {
        return CACHE.get(namingStrategy);
      } else {
        try {
          CACHE.put(namingStrategy, namingStrategy.getConstructor().newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return new SnakeCaseStrategy();
  }

  /**
   * Getting the name for the data extraction.
   *
   * @param original the original names
   * @return the modified name for the data extraction
   */
  String resolve(String... original);
}
