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

import static org.jooq.generated.test_schema.tables.TestEmbeddedEntity.TEST_EMBEDDED_ENTITY;

import io.github.devastool.entity2jooq.example.embedded.TestEmbeddedEntity;
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
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UpdateConditionStep;
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
 * Tests of {@link TestEmbeddedEntity} DDL.
 *
 * @author Sergey_Konovalov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEmbeddedEntityTest {
  private static JdbcConnectionPool pool;
  private static final String DB_URL = String.join(
      "",
      "jdbc:h2:mem:db1;",
      "DB_CLOSE_DELAY=-1;",
      "MODE=PostgreSQL;",
      "DATABASE_TO_LOWER=TRUE;",
      "DEFAULT_NULL_ORDERING=HIGH"
  );

  private static final List<TestEmbeddedEntity> DATA = Arrays.asList(
      new TestEmbeddedEntity("Pablo", "Pablo work street", "Pablo home street"),
      new TestEmbeddedEntity("Barry", "Barry work street", "Barry home street")
  );

  @BeforeAll
  static void init() throws SQLException {
    pool = JdbcConnectionPool.create(DB_URL, "", "");

    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.TEST_SCHEMA)
        .execute();

    Table<Record> table = TEST_EMBEDDED_ENTITY.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_EMBEDDED_ENTITY.NAME)
        .column(TEST_EMBEDDED_ENTITY.WORK)
        .column(TEST_EMBEDDED_ENTITY.HOME)
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
    DSLContext context = DSL.using(connection);

    var insert = context
        .insertInto(TEST_EMBEDDED_ENTITY)
        .columns(
            TEST_EMBEDDED_ENTITY.NAME,
            TEST_EMBEDDED_ENTITY.WORK,
            TEST_EMBEDDED_ENTITY.HOME
        );

    for (TestEmbeddedEntity entity : DATA) {
      insert.values(
          entity.getName(),
          entity.getWorkCity(),
          entity.getHomeCity()
      );
    }
    Assertions.assertDoesNotThrow(insert::execute);

    connection.close();
  }

  @Test
  @Order(2)
  void selectTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);

    var select = context
        .select(
            TEST_EMBEDDED_ENTITY.NAME,
            TEST_EMBEDDED_ENTITY.WORK,
            TEST_EMBEDDED_ENTITY.HOME
        )
        .from(TEST_EMBEDDED_ENTITY)
        .where(TEST_EMBEDDED_ENTITY.NAME.isNotNull());

    List<TestEmbeddedEntity> results = select.fetch(TEST_EMBEDDED_ENTITY::toEntity);

    for (TestEmbeddedEntity entity : DATA) {
      String name = entity.getName();
      String workCity = entity.getWorkCity();
      String homeCity = entity.getHomeCity();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(
                  result -> Objects.equals(result.getName(), name)
                      && Objects.equals(result.getWorkCity(), workCity)
                      && Objects.equals(result.getHomeCity(), homeCity)
              )
      );
    }
    connection.close();
  }

  @Test
  @Order(3)
  void updateTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);

    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEmbeddedEntity entity : DATA) {
      updates.add(
          context
              .update(TEST_EMBEDDED_ENTITY)
              .set(TEST_EMBEDDED_ENTITY.NAME, entity.getName())
              .set(TEST_EMBEDDED_ENTITY.WORK, entity.getWorkCity())
              .where(TEST_EMBEDDED_ENTITY.HOME.eq(entity.getHomeCity()))
      );
    }
    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());

    connection.close();
  }

  @Test
  @Order(4)
  void deleteTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);

    DeleteConditionStep<Record> delete = context
        .delete(TEST_EMBEDDED_ENTITY)
        .where(
            TEST_EMBEDDED_ENTITY.NAME.in(
                DATA
                    .stream()
                    .map(TestEmbeddedEntity::getName)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);

    connection.close();
  }
}