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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sqlite.SQLiteDataSource;

import static org.jooq.generated.test_inherit_schema.Tables.TEST_INHERIT_ENTITY;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Tests of {@link TestInheritEntity} DDL.
 *
 * @author Filkov_Artem
 * @since 1.0.0
 */
@TestMethodOrder(OrderAnnotation.class)
class TestInheritEntityTest {
  private static final String DB_URL = "jdbc:sqlite::memory:";
  private static Connection connection;

  private static final List<TestInheritEntity> DATA = Arrays.asList(
      new TestInheritEntity(),
      new TestInheritEntity(),
      new TestInheritEntity()
  );

  @BeforeAll
  static void init() throws SQLException {
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl(DB_URL);
    connection = dataSource.getConnection();

    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);
    context
        .createTableIfNotExists(TEST_INHERIT_ENTITY.getName())
        .column(TEST_INHERIT_ENTITY.INHERIT_FIELD.getName(), TEST_INHERIT_ENTITY.INHERIT_FIELD.getDataType())
        .execute();
  }

  @AfterAll
  static void destroy() throws SQLException {
    connection.close();
  }

  @Test
  @Order(1)
  void insertTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    var insert = context
        .insertInto(table(TEST_INHERIT_ENTITY.getName()))
        .columns(
            field(TEST_INHERIT_ENTITY.INHERIT_FIELD.getName())
        );

    for (TestInheritEntity entity : DATA) {
      insert.values(
          entity.getInheritField()
      );
    }

    Assertions.assertDoesNotThrow(insert::execute);
  }

  @Test
  @Order(2)
  void selectTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    var select = context
        .select(
            field(TEST_INHERIT_ENTITY.INHERIT_FIELD.getName())
        )
        .from(table(TEST_INHERIT_ENTITY.getName()))
        .where(field(TEST_INHERIT_ENTITY.INHERIT_FIELD.getName()).isNotNull());

    List<TestInheritEntity> results = select.fetch(
        records -> new TestInheritEntity(
            records.get(TEST_INHERIT_ENTITY.INHERIT_FIELD)
        )
    );

    for (TestInheritEntity entity : DATA) {
      String inheritField = entity.getInheritField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getInheritField(), inheritField))
      );
    }
  }

  @Test
  @Order(3)
  void updateTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    ArrayList<UpdateSetMoreStep<?>> updates = new ArrayList<>();
    for (TestInheritEntity entity : DATA) {
      updates.add(
          context
              .update(table(TEST_INHERIT_ENTITY.getName()))
              .set(field(TEST_INHERIT_ENTITY.INHERIT_FIELD.getName()), entity.getInheritField())
      );
    }

    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());
  }

  @Test
  @Order(4)
  void deleteTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    DeleteConditionStep<Record> delete = context
        .delete(table(TEST_INHERIT_ENTITY.getName()))
        .where(
            field(TEST_INHERIT_ENTITY.INHERIT_FIELD.getName()).in(
                DATA
                    .stream()
                    .map(TestInheritEntity::getInheritField)
                    .collect(Collectors.toList())
            )
        );

    Assertions.assertDoesNotThrow(delete::execute);
  }
}