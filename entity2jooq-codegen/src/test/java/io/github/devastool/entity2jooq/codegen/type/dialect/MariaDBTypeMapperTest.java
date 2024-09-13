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

package io.github.devastool.entity2jooq.codegen.type.dialect;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link MariaDBTypeMapper}.
 *
 * @author Sergey_Konovalov
 */
class MariaDBTypeMapperTest {
  private final MariaDBTypeMapper mapper = new MariaDBTypeMapper();
  private static final String MARIA_DB = "MariaDB";

  @Test
  void getDialectTest() {
    var dialect = mapper.getDialect();
    Assertions.assertEquals(MARIA_DB, dialect);
  }

  @Test
  void getByteSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Byte.class);
    Assertions.assertEquals("tinyint", sqlType);
  }

  @Test
  void getShortSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Short.class);
    Assertions.assertEquals("smallint", sqlType);
  }

  @Test
  void getIntegerSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Integer.class);
    Assertions.assertEquals("integer", sqlType);
  }

  @Test
  void getLongSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Long.class);
    Assertions.assertEquals("bigint", sqlType);
  }

  @Test
  void getFloatSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Float.class);
    Assertions.assertEquals("float", sqlType);
  }

  @Test
  void getDoubleSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Double.class);
    Assertions.assertEquals("double precision", sqlType);
  }

  @Test
  void getBigDecimalSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, BigDecimal.class);
    Assertions.assertEquals("decimal", sqlType);
  }

  @Test
  void getStringSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, String.class);
    Assertions.assertEquals("text", sqlType);
  }

  @Test
  void getCharacterSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Character.class);
    Assertions.assertEquals("char", sqlType);
  }

  @Test
  void getDateSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Date.class);
    Assertions.assertEquals("date", sqlType);
  }

  @Test
  void getLocalDateSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, LocalDate.class);
    Assertions.assertEquals("date", sqlType);
  }

  @Test
  void getSqlDateSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, java.sql.Date.class);
    Assertions.assertEquals("date", sqlType);
  }

  @Test
  void getLocalTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, LocalTime.class);
    Assertions.assertEquals("time", sqlType);
  }

  @Test
  void getTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Time.class);
    Assertions.assertEquals("time", sqlType);
  }

  @Test
  void getOffsetTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, OffsetTime.class);
    Assertions.assertEquals("time", sqlType);
  }

  @Test
  void getLocalDateTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, LocalDateTime.class);
    Assertions.assertEquals("datetime", sqlType);
  }

  @Test
  void getTimestampSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Timestamp.class);
    Assertions.assertEquals("timestamp", sqlType);
  }

  @Test
  void getOffsetDateTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, OffsetDateTime.class);
    Assertions.assertEquals("datetime with time zone", sqlType);
  }

  @Test
  void getZonedDateTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, ZonedDateTime.class);
    Assertions.assertEquals("datetime with time zone", sqlType);
  }

  @Test
  void getBooleanSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, Boolean.class);
    Assertions.assertEquals("boolean", sqlType);
  }

  @Test
  void getUUIDSqlTypeTest() {
    String sqlType = mapper.getSqlType(MARIA_DB, UUID.class);
    Assertions.assertEquals("varchar(36)", sqlType);
  }
}