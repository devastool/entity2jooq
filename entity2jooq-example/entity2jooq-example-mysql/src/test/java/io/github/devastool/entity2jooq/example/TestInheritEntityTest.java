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

import static org.jooq.generated.test_inherit_schema.Tables.TEST_INHERIT_ENTITY;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.UpdateSetMoreStep;
import org.jooq.generated.DefaultCatalog;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


/**
 * Tests of {@link TestInheritEntity} DDL.
 *
 * @author Filkov_Artem
 * @since 1.0.0
 */
@TestMethodOrder(OrderAnnotation.class)
public class TestInheritEntityTest {
  private static JdbcConnectionPool pool;
  private static final String DB_URL = String.join(
      "",
      "jdbc:h2:mem:db1;",
      "DB_CLOSE_DELAY=-1;",
      "MODE=MySQL;",
      "DATABASE_TO_LOWER=TRUE;",
      "DEFAULT_NULL_ORDERING=HIGH"
  );

  private static final List<TestInheritEntity> DATA = Arrays.asList(
      new TestInheritEntity(),
      new TestInheritEntity(),
      new TestInheritEntity()
  );

  @BeforeAll
  static void init() throws SQLException {
    pool = JdbcConnectionPool.create(DB_URL, "", "");

    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.TEST_INHERIT_SCHEMA)
        .execute();

    Table<Record> table = TEST_INHERIT_ENTITY.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_INHERIT_ENTITY.INHERIT_FIELD)
        .execute();

    connection.close();
  }

  @AfterAll
  static void destroy() {
    pool.dispose();
  }

  @Test
  @Order(1)
  void insertTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.MYSQL);

    var insert = context.insertInto(TEST_INHERIT_ENTITY);
    var iterator = DATA.iterator();
    while (iterator.hasNext()) {
      Record record = TEST_INHERIT_ENTITY.toRecord(iterator.next());
      InsertValuesStepN<Record> insertStep = insert
          .columns(record.fields())
          .values(record.intoList());

      if (!iterator.hasNext()) {
        Assertions.assertDoesNotThrow(insertStep::execute);
      }
    }

    connection.close();
  }

  @Test
  @Order(2)
  void selectTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.MYSQL);

    var select = context
        .select(
            TEST_INHERIT_ENTITY.INHERIT_FIELD
        )
        .from(TEST_INHERIT_ENTITY)
        .where(TEST_INHERIT_ENTITY.INHERIT_FIELD.isNotNull());

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
    connection.close();
  }

  @Test
  @Order(3)
  void updateTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.MYSQL);

    ArrayList<UpdateSetMoreStep<?>> updates = new ArrayList<>();
    for (TestInheritEntity entity : DATA) {
      updates.add(
          context
              .update(TEST_INHERIT_ENTITY)
              .set(TEST_INHERIT_ENTITY.INHERIT_FIELD, entity.getInheritField())
      );
    }
    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());

    connection.close();
  }

  @Test
  @Order(4)
  void deleteTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.MYSQL);

    DeleteConditionStep<Record> delete = context
        .delete(TEST_INHERIT_ENTITY)
        .where(
            TEST_INHERIT_ENTITY.INHERIT_FIELD.in(
                DATA
                    .stream()
                    .map(TestInheritEntity::getInheritField)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);

    connection.close();
  }
}
