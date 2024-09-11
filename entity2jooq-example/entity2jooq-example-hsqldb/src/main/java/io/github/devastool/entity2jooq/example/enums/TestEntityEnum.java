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

package io.github.devastool.entity2jooq.example.enums;

import io.github.devastool.entity2jooq.annotation.Table;

/**
 * Example entity, see tests.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@Table
public class TestEntityEnum {
  private Integer integerField;
  private TestEnum enumField;

  public TestEntityEnum() {
  }

  public TestEntityEnum(Integer integerField, TestEnum enumField) {
    this.integerField = integerField;
    this.enumField = enumField;
  }

  public Integer getIntegerField() {
    return integerField;
  }

  public void setIntegerField(Integer integerField) {
    this.integerField = integerField;
  }

  public TestEnum getEnumField() {
    return enumField;
  }

  public void setEnumField(TestEnum enumField) {
    this.enumField = enumField;
  }
}
