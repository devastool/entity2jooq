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

package io.github.devastool.entity2jooq.codegen.model;

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.ColumnOverride;
import io.github.devastool.entity2jooq.annotation.Table;

/**
 * Example entity with embedded field, see tests.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
@Table
public class TestEntityOverrideEmbedded {
  @ColumnOverride(name = "intField", column = @Column("number"))
  @ColumnOverride(name = "stringField", column = @Column("string"))
  private TestEmbeddable otherEmbeddable;
}
