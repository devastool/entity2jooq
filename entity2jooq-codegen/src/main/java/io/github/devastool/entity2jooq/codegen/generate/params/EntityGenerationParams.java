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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Container class for storing generate entity parameters.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
public class EntityGenerationParams {
  private final Map<String, Integer> nameCounter = new HashMap<>();
  private final Map<Field, String> nameResolver = new HashMap<>();
  private final Set<LinkPair> entityLinks = new HashSet<>();
  private final CodeGeneratorAccumulator codeAccumulator = new CodeGeneratorAccumulator();
  private final List<OperatorCodeGenerator> rootCodeAccumulator = new ArrayList<>();

  /**
   * Returns a map that counts the uniqueness of names.
   *
   * @return a map that counts
   */
  public Map<String, Integer> getNameCounter() {
    return nameCounter;
  }

  /**
   * Returns a map that resolves fields to their corresponding names.
   *
   * @return a map of Field to String name mappings
   */
  public Map<Field, String> getNameResolver() {
    return nameResolver;
  }

  /**
   * Returns a set of entity links represented by LinkPair objects.
   *
   * @return a set of LinkPair
   */
  public Set<LinkPair> getEntityLinks() {
    return entityLinks;
  }

  /**
   * Retrieves the collection of code generator accumulator for root entity.
   *
   * @return collection of code generator
   */
  public List<OperatorCodeGenerator> getRootCodeAccumulator() {
    return rootCodeAccumulator;
  }

  /**
   * Retrieves the map of code generator accumulators.
   *
   * @return map of code generator
   */
  public CodeGeneratorAccumulator getCodeAccumulator() {
    return codeAccumulator;
  }
}
