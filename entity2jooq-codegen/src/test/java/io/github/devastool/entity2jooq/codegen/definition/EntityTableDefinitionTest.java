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

package io.github.devastool.entity2jooq.codegen.definition;

import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import java.sql.SQLException;
import java.util.List;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link EntityTableDefinition}.
 *
 * @author Evgeniy_Gerasimov
 */
class EntityTableDefinitionTest {
  private final EntitySchemaDefinition schema =
      new EntitySchemaDefinition(new Entity2JooqDatabase(), "schema");
  private final EntityDataTypeDefinition type =
      new EntityDataTypeDefinition(schema, String.class, SQLDialect.POSTGRES.getName(), "text");
  private final EntityColumnDefinition column = new EntityColumnDefinition(
      new EntityTableDefinition(schema, "name", List.of()),
      "name",
      type
  );
  private final EntityTableDefinition tableDefinition =
      new EntityTableDefinition(schema, "table", List.of(column));

  @Test
  void getElements0Test() throws SQLException {
    var elements0 = tableDefinition.getElements0();
    Assertions.assertEquals(column, elements0.get(0));
  }
}