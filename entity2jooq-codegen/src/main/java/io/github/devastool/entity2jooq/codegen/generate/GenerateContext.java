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

package io.github.devastool.entity2jooq.codegen.generate;

import io.github.devastool.entity2jooq.codegen.definition.EntityTableDefinition;
import io.github.devastool.entity2jooq.codegen.generate.code.CodeTarget;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for data transferring between code generation chains.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class GenerateContext {
  private final EntityTableDefinition table;
  private final CodeTarget target;
  private final Map<Object, Object> variables;

  /**
   * Constructs new instance of {@link GenerateContext}.
   *
   * @param table  information about table
   * @param target target for generated source code
   */
  public GenerateContext(EntityTableDefinition table, CodeTarget target) {
    this.table = table;
    this.target = target;
    this.variables = new HashMap<>();
  }

  /**
   * Returns information about table.
   *
   * @return information about table
   */
  public EntityTableDefinition getTable() {
    return table;
  }

  /**
   * Returns target for generated source code.
   *
   * @return target for generated source code
   */
  public CodeTarget getTarget() {
    return target;
  }

  /**
   * Returns variable from context.
   *
   * @param key  name of variable
   * @param type type for casting
   * @param <T>  expected type
   * @return value of the variable or null
   * @throws ClassCastException if variable can't cast to expected type
   */
  public <T> T getVariable(Object key, Class<T> type) throws ClassCastException {
    Object value = variables.get(key);
    if (value != null) {
      return type.cast(value);
    }
    return null;
  }

  /**
   * Sets variable to context.
   *
   * @param key   name of variable
   * @param value value of variable
   */
  public void setVariable(Object key, Object value) {
    variables.put(key, value);
  }
}
