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

import io.github.devastool.entity2jooq.annotation.Table;
import java.util.Objects;

/**
 * Example entity parent.
 *
 * @since 1.0.0
 * @author Andrey_Yurzanov, Artem Filkov
 */
@Table
public class TestEntityParent {

  private String superField;

  public TestEntityParent() {
    superField = "TestEntityParent superField";
  }

  public TestEntityParent(String superField) {
    this.superField = superField;
  }

  public String getSuperField() {
    return superField;
  }

  public void setSuperField(String superField) {
    this.superField = superField;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TestEntityParent entityParent = (TestEntityParent) other;
    return Objects.equals(superField, entityParent.superField);
  }

  @Override
  public int hashCode() {
    return Objects.hash(superField);
  }
}
