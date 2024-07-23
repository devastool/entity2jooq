package io.github.devastool.entity2jooq.codegen.definition.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Context with caching instance.
 *
 * @author Evgeniy_Gerasimov
 * @since 1.0.0
 */
public class FactoryContext {
  /**
   * Cache of context instances.
   */
  private final Map<Class<?>, Object> CACHE = new HashMap<>();

  /**
   * Getting instance of naming strategy.
   *
   * @param type Class of naming strategy.
   * @param args args for instance constructor.
   * @return NamingStrategy implementation.
   */
  public <T> T getInstance(Class<T> type, Object... args) {
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null");
    }

    return (T) CACHE.computeIfAbsent(type, v -> createInstance(type, args));
  }

  /**
   * Create new instance of type Class.
   *
   * @param type instance class.
   * @param args argument for constructor.
   * @return new instance.
   */
  private <T> T createInstance(Class<T> type, Object[] args) {
    try {
      var argsTypes = Arrays.stream(args)
          .map(Object::getClass)
          .toArray(Class[]::new);
      return type.getConstructor(argsTypes).newInstance(args);
    } catch (Exception e) {
      throw new RuntimeException(
          String.format("Failed to create an instance of %s", type.getName()), e
      );
    }
  }
}
