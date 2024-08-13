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

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link H2TypeMapper}.
 *
 * @author Evgeniy_Gerasimov
 */
class H2TypeMapperTest {
  private final H2TypeMapper mapper = new H2TypeMapper();

  @Test
  void getDialectTest() {
    var dialect = mapper.getDialect();
    Assertions.assertEquals("H2", dialect);
  }

  @Test
  void getSqlTypeTest() {
    String sqlType = mapper.getSqlType("H2", LocalDateTime.class);
    Assertions.assertEquals("timestamp", sqlType);
  }
}