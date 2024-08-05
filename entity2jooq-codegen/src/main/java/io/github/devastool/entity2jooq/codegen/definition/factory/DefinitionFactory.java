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

package io.github.devastool.entity2jooq.codegen.definition.factory;

import io.github.devastool.entity2jooq.codegen.properties.CodegenProperties;

/**
 * Abstract factory with custom context.
 *
 * @author Evgeniy_Gerasimov
 * @since 1.0.0
 */
public abstract class DefinitionFactory<T, R> {
  /**
   * Factory context.
   */
  private final FactoryContext context;

  /**
   * Constructs new instance of {@link DefinitionFactory}.
   *
   * @param context contains custom settings.
   */
  public DefinitionFactory(FactoryContext context) {
    this.context = context;
  }

  /**
   * Creates and returns new instance of {@link R}.
   *
   * @param data       required data for instance creating
   * @param properties additional properties
   * @return new instance of {@link R}
   */
  public abstract R build(T data, CodegenProperties properties);

  /**
   * Is it possible to create an instance of {@link R}.
   *
   * @param data required data for instance creating
   * @return true if it is possible to create instance, otherwise false
   */
  public boolean canBuild(T data) {
    return true;
  }

  /**
   * Getter.
   *
   * @return instance of {@link FactoryContext }.
   */
  public FactoryContext getContext() {
    return context;
  }
}
