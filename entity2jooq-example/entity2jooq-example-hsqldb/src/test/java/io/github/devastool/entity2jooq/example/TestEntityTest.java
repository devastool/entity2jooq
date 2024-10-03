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

import static org.jooq.generated.test_schema.Tables.TEST_ENTITY;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.SQLDialect;
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
 * Tests of {@link TestEntity} DDL.
 *
 * @author Sergey_Konovalov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEntityTest {
  private static Connection connection;
  private static final String DB_USER = "SA";
  private static final String DB_PASSWORD = "";
  private static final String DB_URL = String.join(
      "",
      "jdbc:hsqldb:mem:mydb;",
      "shutdown=true;"
  );
  private static final List<TestEntity> DATA = Arrays.asList(
      new TestEntity(),
      new TestEntity(),
      new TestEntity()
  );

  @BeforeAll
  static void init() throws SQLException {
    connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.TEST_SCHEMA)
        .execute();

    Table<Record> table = TEST_ENTITY.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_ENTITY.SHORT_FIELD)
        .column(TEST_ENTITY.INT_FIELD)
        .column(TEST_ENTITY.LONG_FIELD)
        .column(TEST_ENTITY.BIG_DECIMAL_FIELD)
        .column(TEST_ENTITY.FLOAT_FIELD)
        .column(TEST_ENTITY.DOUBLE_FIELD)
        .column(TEST_ENTITY.ENTITY_NAME)
        .column(TEST_ENTITY.LOCAL_DATE_FIELD)
        .column(TEST_ENTITY.DATE_FIELD)
        .column(TEST_ENTITY.SQL_DATE_FIELD)
        .column(TEST_ENTITY.LOCAL_TIME_FIELD)
        .column(TEST_ENTITY.TIME_FIELD)
        .column(TEST_ENTITY.OFFSET_TIME_FIELD)
        .column(TEST_ENTITY.LOCAL_DATE_TIME_FIELD)
        .column(TEST_ENTITY.TIMESTAMP_FIELD)
        .column(TEST_ENTITY.OFFSET_DATE_TIME_FIELD)
        .column(TEST_ENTITY.BOOLEAN_FIELD)
        .column(TEST_ENTITY.UUID_FIELD)
        .execute();
  }

  @AfterAll
  static void destroy() throws SQLException {
    connection.close();
  }

  @Test
  @Order(1)
  void insertTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    var insert = context.insertInto(TEST_ENTITY);
    var iterator = DATA.iterator();
    while (iterator.hasNext()) {
      Record record = TEST_ENTITY.toRecord(iterator.next());
      InsertValuesStepN<Record> insertStep = insert
          .columns(record.fields())
          .values(record.intoList());

      if (!iterator.hasNext()) {
        Assertions.assertDoesNotThrow(insertStep::execute);
      }
    }
  }

  @Test
  @Order(2)
  void selectTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    List<TestEntity> results = context
        .select(
            TEST_ENTITY.SHORT_FIELD,
            TEST_ENTITY.INT_FIELD,
            TEST_ENTITY.LONG_FIELD,
            TEST_ENTITY.BIG_DECIMAL_FIELD,
            TEST_ENTITY.FLOAT_FIELD,
            TEST_ENTITY.DOUBLE_FIELD,
            TEST_ENTITY.ENTITY_NAME,
            TEST_ENTITY.LOCAL_DATE_FIELD,
            TEST_ENTITY.DATE_FIELD,
            TEST_ENTITY.SQL_DATE_FIELD,
            TEST_ENTITY.LOCAL_TIME_FIELD,
            TEST_ENTITY.TIME_FIELD,
            TEST_ENTITY.OFFSET_TIME_FIELD,
            TEST_ENTITY.LOCAL_DATE_TIME_FIELD,
            TEST_ENTITY.TIMESTAMP_FIELD,
            TEST_ENTITY.OFFSET_DATE_TIME_FIELD,
            TEST_ENTITY.BOOLEAN_FIELD,
            TEST_ENTITY.UUID_FIELD
        )
        .from(TEST_ENTITY)
        .where(TEST_ENTITY.SHORT_FIELD.isNotNull())
        .fetch(TEST_ENTITY::toEntity);

    for (TestEntity entity : DATA) {
      Short shortField = entity.getShortField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getShortField(), shortField))
      );
    }
  }

  @Test
  @Order(3)
  void updateTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEntity entity : DATA) {
      updates.add(
          context
              .update(TEST_ENTITY)
              .set(TEST_ENTITY.ENTITY_NAME, entity.getStringField())
              .set(TEST_ENTITY.LOCAL_DATE_TIME_FIELD, entity.getLocalDateTimeField())
              .where(TEST_ENTITY.INT_FIELD.eq(entity.getIntField()))
      );
    }
    Assertions.assertDoesNotThrow(() -> context.batch(updates).execute());
  }

  @Test
  @Order(4)
  void deleteTest() {
    DSLContext context = DSL.using(connection, SQLDialect.HSQLDB);

    DeleteConditionStep<Record> delete = context
        .delete(TEST_ENTITY)
        .where(
            TEST_ENTITY.INT_FIELD.in(
                DATA
                    .stream()
                    .map(TestEntity::getIntField)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);
  }
}