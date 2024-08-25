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

import io.github.devastool.entity2jooq.codegen.type.TypePair;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link DialectTypeMapper} for mapping type of SQLite dialect.
 *
 * @author Artem_Filkov
 * @since 1.0.0
 */
public class SQLiteTypeMapper extends DialectTypeMapper {
  private static final String DIALECT = "SQLite";

  /**
   * Constructs new instance of {@link DialectTypeMapper}.
   */
  public SQLiteTypeMapper() {
    super(
        DIALECT,
        Set.of(
            // Numeric types
            new TypePair(Short.class, "integer"),
            new TypePair(Byte.class, "integer"),
            new TypePair(Integer.class, "integer"),
            new TypePair(Long.class, "integer"),
            new TypePair(BigDecimal.class, "integer"),
            new TypePair(Float.class, "real"),
            new TypePair(Double.class, "real"),
            // Character types
            new TypePair(String.class, "text"),
            // Date/Time types
            new TypePair(LocalDate.class, "text"),
            new TypePair(Date.class, "text"),
            new TypePair(java.sql.Date.class, "text"),
            new TypePair(LocalTime.class, "text"),
            new TypePair(Time.class, "text"),
            new TypePair(OffsetTime.class, "text"),
            new TypePair(LocalDateTime.class, "text"),
            new TypePair(Timestamp.class, "text"),
            new TypePair(OffsetDateTime.class, "text"),
            // Boolean types
            new TypePair(Boolean.class, "integer"),
            // UUID types
            new TypePair(UUID.class, "text")
        )
    );
  }
}
