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

package io.github.devastool.entity2jooq.example.embedded;

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.ColumnOverride;
import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;

/**
 * Example entity, see tests.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@Table(mapping = false)
@Schema("test_schema")
public class TestEmbeddedEntity {
  private String name;

  @ColumnOverride(name = "work.point", column = @Column("work"))
  @ColumnOverride(name = "home.point", column = @Column("home"))
  private TestEntityInfo info;

  /**
   * Constructs new instance of {@link TestEmbeddedEntity}.
   */
  public TestEmbeddedEntity(String name, String work, String home) {
    this.name = name;
    this.info = new TestEntityInfo(
        new TestEntityLocation(work),
        new TestEntityLocation(home)
    );
  }

  public String getName() {
    return name;
  }

  public String getHomeCity() {
    return info.getHome().getPoint();
  }

  public String getWorkCity() {
    return info.getWork().getPoint();
  }
}