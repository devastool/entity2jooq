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

package io.github.devastool.entity2jooq.annotation.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link SnakeCaseStrategy}.
 *
 * @author Andrey_Yurzanov
 */
class SnakeCaseStrategyTest {
  private static final String RESOLVE_TEST_VALUE = "myVariableName";
  private static final String RESOLVE_TEST_EXPECTED = "my_variable_name";

  @Test
  void resolveSuccessTest() {
    SnakeCaseStrategy strategy = new SnakeCaseStrategy();
    Assertions.assertEquals(RESOLVE_TEST_EXPECTED, strategy.resolve(RESOLVE_TEST_VALUE));
  }

  @Test
  void resolveAlreadyResolvedValueSuccessTest() {
    SnakeCaseStrategy strategy = new SnakeCaseStrategy();
    Assertions.assertEquals(RESOLVE_TEST_EXPECTED, strategy.resolve(RESOLVE_TEST_VALUE));
  }
}