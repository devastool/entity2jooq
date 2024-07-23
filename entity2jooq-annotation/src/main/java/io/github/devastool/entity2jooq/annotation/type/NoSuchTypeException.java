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

package io.github.devastool.entity2jooq.annotation.type;

import java.util.NoSuchElementException;

/**
 * An exception throws when the type is not found.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class NoSuchTypeException extends NoSuchElementException {
  /**
   * Constructs new instance of exception.
   *
   * @param dialect SQL dialect
   * @param type    not found type
   */
  public NoSuchTypeException(String dialect, String type) {
    super(String.join("", "Type: [", type, "] is not found for SQL dialect: [", dialect, "]"));
  }

  /**
   * Constructs new instance of exception.
   *
   * @param dialect SQL dialect
   * @param type    not found type
   */
  public NoSuchTypeException(String dialect, Class<?> type) {
    super(
        String.join(
            "",
            "Type: [", String.valueOf(type), "] is not found for SQL dialect: [", dialect, "]"
        )
    );
  }
}
