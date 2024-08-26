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

package io.github.devastool.entity2jooq.example.converter;

import org.jooq.Converter;

/**
 * Converter entity, see tests.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class TestConverter implements Converter<String, Integer> {
  @Override
  public Integer from(String s) {
    return Integer.parseInt(s);
  }

  @Override
  public String to(Integer integer) {
    return String.valueOf(integer);
  }

  @Override
  public Class<String> fromType() {
    return String.class;
  }

  @Override
  public Class<Integer> toType() {
    return Integer.class;
  }
}
