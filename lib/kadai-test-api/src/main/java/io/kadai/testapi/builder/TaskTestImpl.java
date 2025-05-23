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

package io.kadai.testapi.builder;

import io.kadai.task.api.TaskState;
import io.kadai.task.internal.models.TaskImpl;
import java.time.Instant;

class TaskTestImpl extends TaskImpl {

  private boolean freezeState = false;
  private boolean freezeCreated = false;
  private boolean freezeModified = false;
  private boolean freezeRead = false;
  private boolean freezeTransferred = false;
  private boolean freezeReopened = false;
  private boolean freezePriority = false;

  @Override
  public void setState(TaskState state) {
    if (!freezeState) {
      super.setState(state);
    }
  }

  public void setStateIgnoreFreeze(TaskState state) {
    super.setState(state);
  }

  @Override
  public void setCreated(Instant created) {
    if (!freezeCreated) {
      super.setCreated(created);
    }
  }

  public void setCreatedIgnoreFreeze(Instant created) {
    super.setCreated(created);
  }

  @Override
  public void setModified(Instant modified) {
    if (!freezeModified) {
      super.setModified(modified);
    }
  }

  public void setModifiedIgnoreFreeze(Instant modified) {
    super.setModified(modified);
  }

  @Override
  public void setRead(boolean isRead) {
    if (!freezeRead) {
      super.setRead(isRead);
    }
  }

  public void setReadIgnoreFreeze(boolean isRead) {
    super.setRead(isRead);
  }

  @Override
  public void setTransferred(boolean isTransferred) {
    if (!freezeTransferred) {
      super.setTransferred(isTransferred);
    }
  }

  public void setTransferredIgnoreFreeze(boolean isTransferred) {
    super.setTransferred(isTransferred);
  }

  public void setReopened(boolean isReopened) {
    if (!freezeReopened) {
      super.setReopened(isReopened);
    }
  }

  public void setReopenedIgnoreFreeze(boolean isReopened) {
    super.setReopened(isReopened);
  }

  @Override
  public void setPriority(int priority) {
    if (!freezePriority) {
      super.setPriority(priority);
    }
  }

  public void setPriorityIgnoreFreeze(int priority) {
    super.setPriority(priority);
  }

  public void freezeState() {
    freezeState = true;
  }

  public void unfreezeState() {
    freezeState = false;
  }

  public void freezeCreated() {
    freezeCreated = true;
  }

  public void unfreezeCreated() {
    freezeCreated = false;
  }

  public void freezeModified() {
    freezeModified = true;
  }

  public void unfreezeModified() {
    freezeModified = false;
  }

  public void freezeRead() {
    freezeRead = true;
  }

  public void unfreezeRead() {
    freezeRead = false;
  }

  public void freezeTransferred() {
    freezeTransferred = true;
  }

  public void unfreezeTransferred() {
    freezeTransferred = false;
  }

  public void freezeReopened() {
    freezeReopened = true;
  }

  public void unfreezeReopened() {
    freezeReopened = false;
  }

  public void freezePriority() {
    freezePriority = true;
  }

  public void unfreezePriority() {
    freezePriority = false;
  }
}
