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

package io.github.devastool.entity2jooq.example.embedded;

import io.github.devastool.entity2jooq.annotation.Embedded;

/**
 * Example Embedded entity, see tests.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@Embedded
public class TestEntityInfo {
  private TestEntityLocation work;
  private TestEntityLocation home;

  /**
   * Constructs new instance of {@link TestEntityInfo}.
   */
  public TestEntityInfo(TestEntityLocation work, TestEntityLocation home) {
    this.work = work;
    this.home = home;
  }

  public TestEntityInfo() {
  }

  public TestEntityLocation getWork() {
    return work;
  }

  public TestEntityLocation getHome() {
    return home;
  }

  public void setWork(TestEntityLocation work) {
    this.work = work;
  }

  public void setHome(TestEntityLocation home) {
    this.home = home;
  }
}
