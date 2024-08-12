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

package io.github.devastool.entity2jooq.codegen.generate.code;

/**
 * Abstraction of source code destination.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public interface CodeTarget {
  /**
   * Writes raw string without preprocessing and postprocessing.
   *
   * @param value raw string for writing
   * @return current instance
   */
  CodeTarget rawWrite(String value);

  /**
   * Writes string, method has preprocessing and postprocessing.
   *
   * @param value string for writing
   * @return current instance
   */
  CodeTarget write(String value);

  /**
   * Writes string and append new line after it.
   *
   * @param value string for writing
   * @return current instance
   */
  CodeTarget writeln(String value);

  /**
   * Writes new line only.
   *
   * @return current instance
   */
  default CodeTarget writeln() {
    return writeln("");
  }

  /**
   * Writes space.
   *
   * @return current instance
   */
  default CodeTarget space() {
    return rawWrite(" ");
  }
}
