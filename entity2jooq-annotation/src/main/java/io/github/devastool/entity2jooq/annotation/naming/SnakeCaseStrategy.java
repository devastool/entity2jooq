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

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link NamingStrategy} for a data extraction. The name represents in the form
 * of snake_case.
 *
 * @since 1.0.0
 * @author Andrey_Yurzanov
 */
public class SnakeCaseStrategy implements NamingStrategy {
  private final boolean upper;

  private static final char SEPARATOR = '_';

  /**
   * Constructs new instance of {@link SnakeCaseStrategy}, resolved name will lowercase.
   */
  public SnakeCaseStrategy() {
    this(false);
  }

  /**
   * Constructs new instance of {@link SnakeCaseStrategy}.
   *
   * @param upper flag of uppercase/lowercase
   */
  public SnakeCaseStrategy(boolean upper) {
    this.upper = upper;
  }

  @Override
  public String resolve(String... original) {
    StringBuilder buffer = new StringBuilder();

    Iterator<String> iterator = List.of(original).iterator();
    while (iterator.hasNext()) {
      String name = iterator.next();
      char[] symbols = name.toCharArray();
      for (int i = 0; i < symbols.length; i++) {
        boolean isUpper = Character.isUpperCase(symbols[i]);
        if (isUpper && i > 0) {
          buffer.append(SEPARATOR);
        }

        if (upper) {
          buffer.append(Character.toUpperCase(symbols[i]));
        } else {
          buffer.append(Character.toLowerCase(symbols[i]));
        }
      }

      if (iterator.hasNext()) {
        buffer.append(SEPARATOR);
      }
    }
    return buffer.toString();
  }
}
