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
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.sqlite.SQLiteDataSource;

import static org.jooq.generated.test_schema.Tables.TEST_ENTITY;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Tests of {@link TestEntity} DDL.
 *
 * @author Filkov_Artem
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEntityTest {
  private static final String DB_URL = "jdbc:sqlite::memory:";
  private static Connection connection;

  private static final List<TestEntity> DATA = Arrays.asList(
      new TestEntity(),
      new TestEntity(),
      new TestEntity()
  );

  @BeforeAll
  static void init() throws SQLException {
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl(DB_URL);
    connection = dataSource.getConnection();

    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);
    context
        .createTableIfNotExists(TEST_ENTITY.getName())
        .column(TEST_ENTITY.INT_FIELD.getName(), TEST_ENTITY.INT_FIELD.getDataType())
        .column(TEST_ENTITY.DOUBLE_FIELD.getName(), TEST_ENTITY.DOUBLE_FIELD.getDataType())
        .column(TEST_ENTITY.ENTITY_NAME.getName(), TEST_ENTITY.ENTITY_NAME.getDataType())
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
        .insertInto(table(TEST_ENTITY.getName()))
        .columns(
            field(TEST_ENTITY.INT_FIELD.getName()),
            field(TEST_ENTITY.DOUBLE_FIELD.getName()),
            field(TEST_ENTITY.ENTITY_NAME.getName())
        );

    for (TestEntity entity : DATA) {
      insert.values(
          entity.getIntField(),
          entity.getDoubleField(),
          entity.getStringField()
      );
    }

    Assertions.assertDoesNotThrow(insert::execute);
  }

  @Test
  @Order(2)
  void selectTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    List<TestEntity> results = context
        .select(
            field(TEST_ENTITY.INT_FIELD.getName()),
            field(TEST_ENTITY.DOUBLE_FIELD.getName()),
            field(TEST_ENTITY.ENTITY_NAME.getName())
        )
        .from(table(TEST_ENTITY.getName()))
        .where(field(TEST_ENTITY.INT_FIELD.getName()).isNotNull())
        .fetch(TEST_ENTITY::toEntity);

    for (TestEntity entity : DATA) {
      Integer intField = entity.getIntField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getIntField(), intField))
      );
    }
  }

  @Test
  @Order(3)
  void updateTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEntity entity : DATA) {
      updates.add(
          context
              .update(table(TEST_ENTITY.getName()))
              .set(field(TEST_ENTITY.ENTITY_NAME.getName()), entity.getStringField())
              .where(field(TEST_ENTITY.INT_FIELD.getName()).eq(entity.getIntField()))
      );
    }

    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());
  }

  @Test
  @Order(4)
  void deleteTest() {
    DSLContext context = DSL.using(connection, SQLDialect.SQLITE);

    DeleteConditionStep<Record> delete = context
        .delete(table(TEST_ENTITY.getName()))
        .where(
            field(TEST_ENTITY.INT_FIELD.getName()).in(
                DATA
                    .stream()
                    .map(TestEntity::getIntField)
                    .collect(Collectors.toList())
            )
        );

    Assertions.assertDoesNotThrow(delete::execute);
  }
}