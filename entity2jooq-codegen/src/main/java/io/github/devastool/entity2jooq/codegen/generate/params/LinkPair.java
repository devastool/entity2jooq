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

package io.github.devastool.entity2jooq.codegen.generate.params;

import java.util.Objects;

/**
 * Pair of entity name.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class LinkPair {
  private final String primary;
  private final String secondary;

  /**
   * Constructs new instance of {@link LinkPair}.
   *
   * @param primary   name of the first entity
   * @param secondary name of the second entity
   */
  public LinkPair(String primary, String secondary) {
    this.primary = primary;
    this.secondary = secondary;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    LinkPair linkPair = (LinkPair) other;
    return Objects.equals(primary, linkPair.primary)
        && Objects.equals(secondary, linkPair.secondary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primary, secondary);
  }
}
