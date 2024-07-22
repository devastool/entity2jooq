package io.github.devastool.entity2jooq.codegen.definition.factory;

/**
 * Abstract factory with custom context.
 *
 * @author Evgeniy_Gerasimov
 * @since 1.0.0
 */
public abstract class ContextableFactory {
  /**
   * Factory context.
   */
  private final FactoryContext context;

  /**
   * Constructor.
   *
   * @param context factory context.
   */
  public ContextableFactory(FactoryContext context) {
    this.context = context;
  }

  /**
   * Getter
   *
   * @return context.
   */
  public FactoryContext getContext() {
    return context;
  }
}
