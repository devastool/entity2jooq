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
 * @author Evgeniy_Gerasimov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEntityTest {
  private static JdbcConnectionPool pool;
  private static final String DB_URL = String.join(
      "",
      "jdbc:h2:mem:db1;",
      "DB_CLOSE_DELAY=-1;",
      "DATABASE_TO_LOWER=TRUE;",
      "DEFAULT_NULL_ORDERING=HIGH"
  );

  private static final List<TestEntity> DATA = Arrays.asList(
      new TestEntity(),
      new TestEntity(),
      new TestEntity()
  );

  @BeforeAll
  static void init() throws SQLException {
    pool = JdbcConnectionPool.create(DB_URL, "", "");

    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.H2);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.TEST_SCHEMA)
        .execute();

    Table<Record> table = TEST_ENTITY.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_ENTITY.BYTE_FIELD)
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
    DSLContext context = DSL.using(connection, SQLDialect.H2);

    var insert = context
        .insertInto(TEST_ENTITY)
        .columns(
            TEST_ENTITY.BYTE_FIELD,
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
        );

    for (TestEntity entity : DATA) {
      insert.values(
          entity.getByteField(),
          entity.getShortField(),
          entity.getIntField(),
          entity.getLongField(),
          entity.getBigDecimalField(),
          entity.getFloatField(),
          entity.getDoubleField(),
          entity.getStringField(),
          entity.getLocalDateField(),
          entity.getDateField(),
          entity.getSqlDateField(),
          entity.getLocalTimeField(),
          entity.getTimeField(),
          entity.getOffsetTimeField(),
          entity.getLocalDateTimeField(),
          entity.getTimestampField(),
          entity.getOffsetDateTimeField(),
          entity.getBooleanField(),
          entity.getUuidField()
      );
    }
    Assertions.assertDoesNotThrow(insert::execute);

    connection.close();
  }

  @Test
  @Order(2)
  void selectTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.H2);

    var select = context
        .select(
            TEST_ENTITY.BYTE_FIELD,
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
        .where(TEST_ENTITY.SHORT_FIELD.isNotNull());

    List<TestEntity> results = select.fetch(
        records -> new TestEntity(
            records.get(TEST_ENTITY.BYTE_FIELD),
            records.get(TEST_ENTITY.SHORT_FIELD),
            records.get(TEST_ENTITY.INT_FIELD),
            records.get(TEST_ENTITY.LONG_FIELD),
            records.get(TEST_ENTITY.BIG_DECIMAL_FIELD),
            records.get(TEST_ENTITY.FLOAT_FIELD),
            records.get(TEST_ENTITY.DOUBLE_FIELD),
            records.get(TEST_ENTITY.ENTITY_NAME),
            records.get(TEST_ENTITY.LOCAL_DATE_FIELD),
            records.get(TEST_ENTITY.DATE_FIELD),
            records.get(TEST_ENTITY.SQL_DATE_FIELD),
            records.get(TEST_ENTITY.LOCAL_TIME_FIELD),
            records.get(TEST_ENTITY.TIME_FIELD),
            records.get(TEST_ENTITY.OFFSET_TIME_FIELD),
            records.get(TEST_ENTITY.LOCAL_DATE_TIME_FIELD),
            records.get(TEST_ENTITY.TIMESTAMP_FIELD),
            records.get(TEST_ENTITY.OFFSET_DATE_TIME_FIELD),
            records.get(TEST_ENTITY.BOOLEAN_FIELD),
            records.get(TEST_ENTITY.UUID_FIELD)
        )
    );

    for (TestEntity entity : DATA) {
      Byte byteField = entity.getByteField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getByteField(), byteField))
      );
    }
    connection.close();
  }

  @Test
  @Order(3)
  void updateTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.H2);

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

    connection.close();
  }

  @Test
  @Order(4)
  void deleteTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.H2);

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

    connection.close();
  }
}