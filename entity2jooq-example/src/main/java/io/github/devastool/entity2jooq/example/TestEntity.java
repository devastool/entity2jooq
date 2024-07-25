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

import io.github.devastool.entity2jooq.annotation.AttributeOverride;
import io.github.devastool.entity2jooq.annotation.AttributeOverrides;
import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Example entity, see tests.
 *
 * @author Andrey_Yurzanov
 * @since 0.0.1
 */
@Table
@Schema("test_schema")
public class TestEntity {
  @Column
  private Integer id;
  @Column(value = "entity_name", type = "varchar")
  private String name;
  private Timestamp insertTime;

  private TestEntityInfo info;

  @AttributeOverrides({
      @AttributeOverride(name = "version", column = @Column("other_version")),
  })
  private TestEntityInfo otherInfo;

  public TestEntity() {
  }

  public TestEntity(Integer id, String name, Timestamp insertTime, TestEntityInfo info) {
    this.id = id;
    this.name = name;
    this.insertTime = insertTime;
    this.info = info;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Timestamp getInsertTime() {
    return insertTime;
  }

  public void setInsertTime(Timestamp insertTime) {
    this.insertTime = insertTime;
  }

  public TestEntityInfo getInfo() {
    return info;
  }

  public void setInfo(TestEntityInfo info) {
    this.info = info;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TestEntity that = (TestEntity) other;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
