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

package io.github.devastool.entity2jooq.codegen.filesystem.classload;

import java.net.URLClassLoader;
import java.util.Optional;

/**
 * Classes loader by paths.
 *
 * @author Andrey_Yurzanov
 * @since 1.0.0
 */
public class PathClassLoader extends URLClassLoader {
  private final PathClassLoaderContext context;

  /**
   * Constructs new instance of {@link PathClassLoader}.
   *
   * @param context loader context
   */
  public PathClassLoader(PathClassLoaderContext context) {
    super(context.getClasspath(), Thread.currentThread().getContextClassLoader());

    this.context = context;
  }

  /**
   * Loads class by path description {@link PathClassLoaderContextElement}.
   *
   * @param element path description
   * @return loaded class
   * @throws ClassNotFoundException when class is not found
   */
  public Class<?> loadClass(PathClassLoaderContextElement element) throws ClassNotFoundException {
    String className = element.getCanonicalClassName();

    Class<?> defined = findLoadedClass(className);
    if (defined == null) {
      try {
        byte[] classData = element.getClassData();
        defined = defineClass(className, classData, 0, classData.length);
        resolveClass(defined);
      } catch (Exception exception) {
        throw new ClassNotFoundException(
            String.join("", "Loading error of class [", className, "]"),
            exception
        );
      }
    }
    return defined;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    Optional<PathClassLoaderContextElement> element = context.getElement(name);
    if (element.isPresent()) {
      return loadClass(element.get());
    }

    try {
      return super.loadClass(name);
    } catch (Exception exception) {
      throw new ClassNotFoundException(
          String.join("", "Loading error of class [", name, "]"),
          exception
      );
    }
  }
}
