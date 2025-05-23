/*
 * Copyright [2025] [envite consulting GmbH]
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
 *
 *
 */

package io.kadai.common.internal;

import io.kadai.common.api.KadaiEngine;
import io.kadai.spi.history.internal.HistoryEventManager;
import io.kadai.spi.priority.internal.PriorityServiceManager;
import io.kadai.spi.routing.internal.TaskRoutingManager;
import io.kadai.spi.task.internal.AfterRequestChangesManager;
import io.kadai.spi.task.internal.AfterRequestReviewManager;
import io.kadai.spi.task.internal.BeforeRequestChangesManager;
import io.kadai.spi.task.internal.BeforeRequestReviewManager;
import io.kadai.spi.task.internal.CreateTaskPreprocessorManager;
import io.kadai.spi.task.internal.ReviewRequiredManager;
import io.kadai.spi.task.internal.TaskDistributionManager;
import io.kadai.spi.task.internal.TaskEndstatePreprocessorManager;
import java.util.function.Supplier;
import org.apache.ibatis.session.SqlSession;

/**
 * FOR INTERNAL USE ONLY.
 *
 * <p>Contains all actions which are necessary within kadai.
 */
public interface InternalKadaiEngine {

  /**
   * Opens the connection to the database. Has to be called at the beginning of each Api call that
   * accesses the database
   */
  void openConnection();

  /**
   * Returns the database connection into the pool. In the case of nested calls, simply pops the
   * latest session from the session stack. Closes the connection if the session stack is empty. In
   * mode AUTOCOMMIT commits before the connection is closed. To be called at the end of each Api
   * call that accesses the database
   */
  void returnConnection();

  /**
   * Executes the given supplier after openConnection is called and then returns the connection.
   *
   * @param supplier a function that returns something of type T
   * @param <T> any type
   * @return the result of the supplier
   */
  <T> T executeInDatabaseConnection(Supplier<T> supplier);

  /**
   * Executes the given runnable after openConnection is called and then returns the connection.
   *
   * @see #executeInDatabaseConnection(Supplier)
   */
  @SuppressWarnings("checkstyle:JavadocMethod")
  default void executeInDatabaseConnection(Runnable runnable) {
    executeInDatabaseConnection(
        () -> {
          runnable.run();
          return null;
        });
  }

  /** Initializes the SqlSessionManager. */
  void initSqlSession();

  /**
   * Returns true if the given domain does exist in the configuration.
   *
   * @param domain the domain specified in the configuration
   * @return <code>true</code> if the domain exists
   */
  boolean domainExists(String domain);

  /**
   * retrieve the SqlSession used by kadai.
   *
   * @return the myBatis SqlSession object used by kadai
   */
  SqlSession getSqlSession();

  /**
   * Retrieve KadaiEngine.
   *
   * @return The nested KadaiEngine.
   */
  KadaiEngine getEngine();

  /**
   * Retrieve HistoryEventProducer.
   *
   * @return the HistoryEventProducer instance.
   */
  HistoryEventManager getHistoryEventManager();

  /**
   * Retrieve TaskRoutingProducer.
   *
   * @return the TaskRoutingProducer instance.
   */
  TaskRoutingManager getTaskRoutingManager();

  /**
   * Retrieve TaskDistributionManager.
   *
   * @return the TaskDistributionManager instance.
   */
  TaskDistributionManager getTaskDistributionManager();

  /**
   * Retrieve CreateTaskPreprocessorManager.
   *
   * @return the CreateTaskPreprocessorManager instance.
   */
  CreateTaskPreprocessorManager getCreateTaskPreprocessorManager();

  /**
   * Retrieves the {@linkplain PriorityServiceManager}.
   *
   * @return the {@linkplain PriorityServiceManager} instance
   */
  PriorityServiceManager getPriorityServiceManager();

  /**
   * Retrieves the {@linkplain ReviewRequiredManager}.
   *
   * @return the {@linkplain ReviewRequiredManager} instance
   */
  ReviewRequiredManager getReviewRequiredManager();

  /**
   * Retrieves the {@linkplain BeforeRequestReviewManager}.
   *
   * @return the {@linkplain BeforeRequestReviewManager} instance
   */
  BeforeRequestReviewManager getBeforeRequestReviewManager();

  /**
   * Retrieves the {@linkplain AfterRequestReviewManager}.
   *
   * @return the {@linkplain AfterRequestReviewManager} instance
   */
  AfterRequestReviewManager getAfterRequestReviewManager();

  /**
   * Retrieves the {@linkplain BeforeRequestChangesManager}.
   *
   * @return the {@linkplain BeforeRequestChangesManager} instance
   */
  BeforeRequestChangesManager getBeforeRequestChangesManager();

  /**
   * Retrieves the {@linkplain AfterRequestChangesManager}.
   *
   * @return the {@linkplain AfterRequestChangesManager} instance
   */
  AfterRequestChangesManager getAfterRequestChangesManager();

  /**
   * Retrieves the {@linkplain io.kadai.spi.task.internal.TaskEndstatePreprocessorManager}.
   *
   * @return the {@linkplain io.kadai.spi.task.internal.TaskEndstatePreprocessorManager} instance
   */
  TaskEndstatePreprocessorManager getTaskEndstatePreprocessorManager();
}
