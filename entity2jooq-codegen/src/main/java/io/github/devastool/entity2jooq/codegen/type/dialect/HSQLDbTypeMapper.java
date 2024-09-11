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
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link DialectTypeMapper} for mapping type of HSQLDB dialect.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class HSQLDbTypeMapper extends DialectTypeMapper {
  private static final String DIALECT = "HSQLDB";

  /**
   * Constructs new instance of {@link DialectTypeMapper}.
   */
  public HSQLDbTypeMapper() {
    // TODO: blob, clob, bit, other, interval, array
    super(DIALECT,
        Set.of(
            // Numeric types
            new TypePair(Byte.class, "tinyint"),
            new TypePair(Short.class, "smallint"),
            new TypePair(Integer.class, "integer"),
            new TypePair(Long.class, "bigint"),
            new TypePair(BigDecimal.class, "decimal"),
            new TypePair(Float.class, "float"),
            new TypePair(Double.class, "double"),
            // Character types
            new TypePair(Character.class, "char"),
            new TypePair(String.class, "varchar"),
            new TypePair(Enum.class, "varchar"),
            // Date/Time types
            new TypePair(Date.class, "date"),
            new TypePair(java.sql.Date.class, "date"),
            new TypePair(LocalDate.class, "date"),
            new TypePair(Time.class, "time"),
            new TypePair(LocalTime.class, "time"),
            new TypePair(OffsetTime.class, "time with time zone"),
            new TypePair(Timestamp.class, "timestamp"),
            new TypePair(LocalDateTime.class, "timestamp"),
            new TypePair(OffsetDateTime.class, "timestamp with time zone"),
            new TypePair(ZonedDateTime.class, "timestamp with time zone"),
            // Boolean types
            new TypePair(Boolean.class, "boolean"),
            // UUID types
            new TypePair(UUID.class, "uuid")
        )
    );
  }
}