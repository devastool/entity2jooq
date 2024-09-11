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

package io.github.devastool.entity2jooq.example;

import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;
import java.util.Objects;

/**
 * Example inheritance entity, see tests.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@Table(inheritance = true)
@Schema("test_inherit_schema")
public class TestInheritEntity extends TestEntityParent {

  /**
   * Constructs new instance of {@link TestInheritEntity}.
   */
  public TestInheritEntity() {
    super();
  }

  /**
   * Constructs new instance of {@link TestInheritEntity}.
   */
  public TestInheritEntity(String inheritField) {
    super(inheritField);
  }

  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.getInheritField());
  }
}
