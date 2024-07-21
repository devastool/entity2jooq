package io.github.devastool.entity2jooq.annotation.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link NamingStrategy}.
 *
 * @author Evgeniy_Gerasimov
 */
class NamingStrategyTest {

  @Test
  void getInstanceWhenStrategyNotSpecifyTest() {
    Class<? extends NamingStrategy> strategy = null;
    Assertions.assertEquals(SnakeCaseStrategy.class,
        NamingStrategy.getInstance(strategy).getClass());
  }

  @Test
  void getInstanceWhenStrategySpecifyTest() {
    Class<? extends NamingStrategy> strategy = SnakeCaseStrategy.class;
    Assertions.assertEquals(SnakeCaseStrategy.class,
        NamingStrategy.getInstance(strategy).getClass());
  }

  @Test
  void getInstanceWhenInstancePresentInCacheTest() {
    Class<? extends NamingStrategy> strategy = SnakeCaseStrategy.class;
    var snakeCaseInstance = new SnakeCaseStrategy();
    NamingStrategy.CACHE.put(strategy, snakeCaseInstance);
    Assertions.assertEquals(snakeCaseInstance, NamingStrategy.getInstance(strategy));
  }
}