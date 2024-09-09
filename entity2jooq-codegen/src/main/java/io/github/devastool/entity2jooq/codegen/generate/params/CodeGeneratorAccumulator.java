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

package io.github.devastool.entity2jooq.codegen.generate.params;

import io.github.devastool.entity2jooq.codegen.generate.code.operator.OperatorCodeGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  This class accumulates instances of OperatorCodeGenerator associated with specific entity keys.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class CodeGeneratorAccumulator {
  private final Map<String, List<OperatorCodeGenerator>> codeGeneratorMap = new LinkedHashMap<>();

  /**
   * Accumulates an OperatorCodeGenerator instance associated with an entity key.
   *
   * @param key entity key
   * @param operator instance to be accumulated.
   */
  public void accumulate(String key, OperatorCodeGenerator operator) {
    List<OperatorCodeGenerator> operators = codeGeneratorMap
        .computeIfAbsent(key, entityKey -> new ArrayList<>());

    operators.add(operator);
  }

  /**
   * Retrieves the list of OperatorCodeGenerator instances associated with the given key.
   *
   * @param key the entity key to search
   * @return list of OperatorCodeGenerator instances associated with the key
   */
  public List<OperatorCodeGenerator> findByKey(String key) {
    return codeGeneratorMap.get(key);
  }

  /**
   * Retrieves the reversed list of all unique entity keys.
   *
   * @return list of entity keys
   */
  public List<String> getReversedKeys() {
    List<String> reversedKeys = new ArrayList<>(codeGeneratorMap.keySet());
    Collections.reverse(reversedKeys);
    return reversedKeys;
  }
}
