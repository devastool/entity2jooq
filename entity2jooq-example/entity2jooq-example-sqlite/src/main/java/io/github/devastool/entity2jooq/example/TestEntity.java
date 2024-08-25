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

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.type.Type;
import java.util.Objects;
import java.util.Random;

/**
 * Example entity, see tests.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
@Table
@Schema("test_schema")
public class TestEntity {
  @Column
  private Integer intField;
  private Double doubleField;
  @Type("text")
  @Column(value = "entity_name")
  private String stringField;

  /**
   * Constructs new instance of {@link TestEntity}.
   */
  public TestEntity() {
    intField = getRandomInt();
    doubleField = (double) intField;
    stringField = "TestEntity-" + intField;
  }

  /**
   * Constructs new instance of {@link TestEntity}.
   */
  public TestEntity(
      Integer intField,
      Double doubleField,
      String stringField
  ) {
    this.intField = intField;
    this.doubleField = doubleField;
    this.stringField = stringField;
  }

  public Integer getIntField() {
    return intField;
  }

  public void setIntField(Integer intField) {
    this.intField = intField;
  }

  public Double getDoubleField() {
    return doubleField;
  }

  public void setDoubleField(Double doubleField) {
    this.doubleField = doubleField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TestEntity entity = (TestEntity) other;
    return Objects.equals(intField, entity.intField)
        && Objects.equals(doubleField, entity.doubleField)
        && Objects.equals(stringField, entity.stringField);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        intField,
        doubleField,
        stringField
    );
  }

  private static int getRandomInt() {
    return new Random().nextInt();
  }
}
