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
  Map<Class<?>, Object> CACHE = new HashMap<>();

  /**
   * Getting instance of naming strategy.
   *
   * @param type Class of naming strategy.
   * @return NamingStrategy implementation.
   */
  public <T> T getInstance(Class<T> type, Object... args) {
    T instance = null;
    if (type != null) {
      if (CACHE.containsKey(type)) {
        instance = (T) CACHE.get(type);
      } else {
        try {
          var argsTypes = Arrays.stream(args)
              .map(Object::getClass)
              .toArray(Class[]::new);
          instance = type.getConstructor(argsTypes).newInstance(args);
          CACHE.put(type, instance);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return instance;
  }
}
