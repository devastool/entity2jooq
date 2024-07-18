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

import static io.github.devastool.entity2jooq.codegen.Tables.TEST_ENTITY;

import io.github.devastool.entity2jooq.codegen.DefaultCatalog;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.InsertValuesStep3;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests of {@link TestEntity} DDL.
 *
 * @author Andrey_Yurzanov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEntityTest {
  private static JdbcConnectionPool pool;
  private static final String DB_URL = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";
  private static final List<TestEntity> DATA = Arrays.asList(
      new TestEntity(1, "TestEntity1", Timestamp.valueOf(LocalDateTime.now())),
      new TestEntity(2, "TestEntity2", Timestamp.valueOf(LocalDateTime.now())),
      new TestEntity(3, "TestEntity3", Timestamp.valueOf(LocalDateTime.now()))
  );

  @BeforeAll
  static void init() throws SQLException {
    pool = JdbcConnectionPool.create(DB_URL, "", "");

    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.TEST_SCHEMA)
        .execute();

    Table<Record> table = TEST_ENTITY.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_ENTITY.ID)
        .column(TEST_ENTITY.ENTITY_NAME)
        .column(TEST_ENTITY.INSERT_TIME)
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

    InsertValuesStep3<Record, Integer, String, Timestamp> insert = context
        .insertInto(TEST_ENTITY)
        .columns(TEST_ENTITY.ID, TEST_ENTITY.ENTITY_NAME, TEST_ENTITY.INSERT_TIME);

    for (TestEntity entity : DATA) {
      insert.values(entity.getId(), entity.getName(), entity.getInsertTime());
    }
    Assertions.assertDoesNotThrow(insert::execute);

    connection.close();
  }

  @Test
  @Order(2)
  void selectTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);

    List<TestEntity> results = context
        .select(
            TEST_ENTITY.ID,
            TEST_ENTITY.ENTITY_NAME,
            TEST_ENTITY.INSERT_TIME
        )
        .from(TEST_ENTITY)
        .fetch(
            records -> new TestEntity(
                records.get(TEST_ENTITY.ID),
                records.get(TEST_ENTITY.ENTITY_NAME),
                records.get(TEST_ENTITY.INSERT_TIME)
            )
        );

    for (TestEntity entity : DATA) {
      Assertions.assertTrue(results.contains(entity));
    }
    connection.close();
  }

  @Test
  @Order(3)
  void updateTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection);

    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEntity entity : DATA) {
      updates.add(
          context
              .update(TEST_ENTITY)
              .set(TEST_ENTITY.ENTITY_NAME, entity.getName())
              .set(TEST_ENTITY.INSERT_TIME, entity.getInsertTime())
              .where(TEST_ENTITY.ID.eq(entity.getId()))
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
        .delete(TEST_ENTITY)
        .where(
            TEST_ENTITY.ID.in(
                DATA
                    .stream()
                    .map(TestEntity::getId)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);

    connection.close();
  }
}
