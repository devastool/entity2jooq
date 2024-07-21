package io.github.devastool.entity2jooq.codegen.definition.factory;

/**
 * FactoryContext interface.
 * @param <T>
 */
public interface FactoryContext<T> {
  /**
   * Get new or cached instance from context.
   * @param type strategy class
   * @return new or cached instance.
   */
  T getInstance(Class<?> type);
}
