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

package io.github.devastool.entity2jooq.example.enums;

import static org.jooq.generated.enums.tables.TestEntityEnum.TEST_ENTITY_ENUM;

import io.github.devastool.entity2jooq.annotation.type.converter.EnumConverter;
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
 * Tests of {@link TestEntityEnum} DDL.
 *
 * @author Andrey_Yurzanov
 */
@TestMethodOrder(OrderAnnotation.class)
class TestEntityEnumTest {
  private static JdbcConnectionPool pool;
  private static final String DB_URL = String.join(
      "",
      "jdbc:h2:mem:db1;",
      "DB_CLOSE_DELAY=-1;",
      "DATABASE_TO_LOWER=TRUE;",
      "DEFAULT_NULL_ORDERING=HIGH"
  );

  private static final List<TestEntityEnum> DATA = Arrays.asList(
      new TestEntityEnum(1, TestEnum.FIRST),
      new TestEntityEnum(2, TestEnum.SECOND)
  );

  @BeforeAll
  static void init() throws SQLException {
    pool = JdbcConnectionPool.create(DB_URL, "", "");

    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.H2);
    context
        .createSchemaIfNotExists(DefaultCatalog.DEFAULT_CATALOG.ENUMS)
        .execute();

    Table<Record> table = TEST_ENTITY_ENUM.asTable();
    context
        .createTableIfNotExists(table)
        .column(TEST_ENTITY_ENUM.INTEGER_FIELD)
        .column(TEST_ENTITY_ENUM.ENUM_FIELD)
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

    var insert = context.insertInto(TEST_ENTITY_ENUM);
    var iterator = DATA.iterator();
    while (iterator.hasNext()) {
      Record record = TEST_ENTITY_ENUM.toRecord(iterator.next());
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
    DSLContext context = DSL.using(connection, SQLDialect.H2);

    var select = context
        .select(
            TEST_ENTITY_ENUM.INTEGER_FIELD,
            TEST_ENTITY_ENUM.ENUM_FIELD
        )
        .from(TEST_ENTITY_ENUM)
        .where(TEST_ENTITY_ENUM.INTEGER_FIELD.isNotNull());

    List<TestEntityEnum> results = select.fetch(TEST_ENTITY_ENUM::toEntity);

    for (TestEntityEnum entity : DATA) {
      Integer integerField = entity.getIntegerField();
      Assertions.assertTrue(
          results
              .stream()
              .anyMatch(result -> Objects.equals(result.getIntegerField(), integerField))
      );
    }
    connection.close();
  }

  @Test
  @Order(3)
  void updateTest() throws SQLException {
    Connection connection = pool.getConnection();
    DSLContext context = DSL.using(connection, SQLDialect.H2);

    EnumConverter<TestEnum> converter = TEST_ENTITY_ENUM.TEST_ENUM_ENUM_CONVERTER;
    ArrayList<UpdateConditionStep<?>> updates = new ArrayList<>();
    for (TestEntityEnum entity : DATA) {
      updates.add(
          context
              .update(TEST_ENTITY_ENUM)
              .set(TEST_ENTITY_ENUM.INTEGER_FIELD, entity.getIntegerField())
              .set(TEST_ENTITY_ENUM.ENUM_FIELD, converter.to(entity.getEnumField()))
              .where(TEST_ENTITY_ENUM.INTEGER_FIELD.eq(entity.getIntegerField()))
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
        .delete(TEST_ENTITY_ENUM)
        .where(
            TEST_ENTITY_ENUM.INTEGER_FIELD.in(
                DATA
                    .stream()
                    .map(TestEntityEnum::getIntegerField)
                    .collect(Collectors.toList())
            )
        );
    Assertions.assertDoesNotThrow(delete::execute);

    connection.close();
  }
}