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
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link HSQLDbTypeMapper}.
 *
 * @author Sergey_Konovalov
 */
class HSQLDbTypeMapperTest {
  private final HSQLDbTypeMapper mapper = new HSQLDbTypeMapper();

  @Test
  void getDialectTest() {
    var dialect = mapper.getDialect();
    Assertions.assertEquals("HSQLDB", dialect);
  }

  @Test
  void getByteTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Byte.class);
    Assertions.assertEquals("tinyint", sqlType);
  }
  @Test
  void getShortTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Short.class);
    Assertions.assertEquals("smallint", sqlType);
  }

  @Test
  void getIntegerTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Integer.class);
    Assertions.assertEquals("integer", sqlType);
  }

  @Test
  void getLongTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Long.class);
    Assertions.assertEquals("bigint", sqlType);
  }

  @Test
  void getBigDecimalTest() {
    String sqlType = mapper.getSqlType("HSQLDB", BigDecimal.class);
    Assertions.assertEquals("decimal", sqlType);
  }

  @Test
  void getFloatTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Float.class);
    Assertions.assertEquals("float", sqlType);
  }

  @Test
  void getDoubleTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Double.class);
    Assertions.assertEquals("double", sqlType);
  }

  @Test
  void getCharacterTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Character.class);
    Assertions.assertEquals("char", sqlType);
  }

  @Test
  void getStringTest() {
    String sqlType = mapper.getSqlType("HSQLDB", String.class);
    Assertions.assertEquals("longvarchar", sqlType);
  }

  @Test
  void getEnumTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Enum.class);
    Assertions.assertEquals("varchar(100)", sqlType);
  }

  @Test
  void getDateTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Date.class);
    Assertions.assertEquals("date", sqlType);
  }
  @Test

  void getSqlDateTest() {
    String sqlType = mapper.getSqlType("HSQLDB", java.sql.Date.class);
    Assertions.assertEquals("date", sqlType);
  }

  @Test
  void getTimeTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Time.class);
    Assertions.assertEquals("time", sqlType);
  }

  @Test
  void getLocalTimeTest() {
    String sqlType = mapper.getSqlType("HSQLDB", LocalTime.class);
    Assertions.assertEquals("time", sqlType);
  }

  @Test
  void getOffsetTimeTest() {
    String sqlType = mapper.getSqlType("HSQLDB", OffsetTime.class);
    Assertions.assertEquals("time with time zone", sqlType);
  }

  @Test
  void getTimestampTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Timestamp.class);
    Assertions.assertEquals("timestamp", sqlType);
  }

  @Test
  void getOffsetDateTimeTest() {
    String sqlType = mapper.getSqlType("HSQLDB", OffsetDateTime.class);
    Assertions.assertEquals("timestamp with time zone", sqlType);
  }

  @Test
  void getZonedDateTimeTest() {
    String sqlType = mapper.getSqlType("HSQLDB", ZonedDateTime.class);
    Assertions.assertEquals("timestamp with time zone", sqlType);
  }

  @Test
  void getBooleanTest() {
    String sqlType = mapper.getSqlType("HSQLDB", Boolean.class);
    Assertions.assertEquals("boolean", sqlType);
  }

  @Test
  void getUuidTest() {
    String sqlType = mapper.getSqlType("HSQLDB", UUID.class);
    Assertions.assertEquals("uuid", sqlType);
  }

}