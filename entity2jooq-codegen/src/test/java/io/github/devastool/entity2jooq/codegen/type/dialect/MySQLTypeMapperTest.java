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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link MySQLTypeMapper}.
 *
 * @author Artem_Filkov
 */
public class MySQLTypeMapperTest {

  private final MySQLTypeMapper mapper = new MySQLTypeMapper();

  @Test
  void getDialectTest() {
    var dialect = mapper.getDialect();
    Assertions.assertEquals("MySQL", dialect);
  }

  @Test
  void getLocalDateTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType("MySQL", LocalDateTime.class);
    Assertions.assertEquals("datetime", sqlType);
  }

  @Test
  void getOffsetDateTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType("MySQL", OffsetDateTime.class);
    Assertions.assertEquals("datetime", sqlType);
  }

  @Test
  void getTimestampSqlTypeTest() {
    String sqlType = mapper.getSqlType("MySQL", Timestamp.class);
    Assertions.assertEquals("timestamp", sqlType);
  }

  @Test
  void getZonedDateTimeSqlTypeTest() {
    String sqlType = mapper.getSqlType("MySQL", ZonedDateTime.class);
    Assertions.assertEquals("datetime", sqlType);
  }
}
