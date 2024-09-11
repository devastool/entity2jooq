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

package io.github.devastool.entity2jooq.example.converter;

import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.type.Type;
import java.util.Random;

/**
 * Example entity, see tests.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@Table
public class TestEntity {
  @Type(converter = TestConverter.class)
  private Integer intField;

  public TestEntity() {
    this(getRandomInt());
  }

  public TestEntity(Integer intField) {
    this.intField = intField;
  }

  public Integer getIntField() {
    return intField;
  }

  public void setIntField(Integer intField) {
    this.intField = intField;
  }

  private static int getRandomInt() {
    return new Random().nextInt(Integer.MAX_VALUE - 1);
  }
}
