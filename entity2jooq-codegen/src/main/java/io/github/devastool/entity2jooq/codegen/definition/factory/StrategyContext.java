package io.github.devastool.entity2jooq.codegen.definition.factory;

import io.github.devastool.entity2jooq.annotation.naming.NamingStrategy;
import io.github.devastool.entity2jooq.annotation.naming.SnakeCaseStrategy;
import java.util.HashMap;
import java.util.Map;

public class StrategyContext implements FactoryContext<NamingStrategy> {
  /**
   * Cache of strategy instances.
   */
  Map<Class<?>, NamingStrategy> CACHE = new HashMap<>();

  /**
   * Getting instance of naming strategy.
   *
   * @param type Class of naming strategy.
   * @return NamingStrategy implementation.
   */
  @Override
  public NamingStrategy getInstance(Class<?> type) {
    if (type != null) {
      if (CACHE.containsKey(type)) {
        return CACHE.get(type);
      } else {
        try {
          CACHE.put(type, (NamingStrategy) type.getConstructor().newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return new SnakeCaseStrategy();
  }
}
