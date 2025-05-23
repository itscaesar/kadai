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

package io.kadai.task.internal;

import io.kadai.task.internal.models.TaskSummaryImpl;
import java.util.List;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.SelectProvider;

/** This class provides a mapper for all task queries. */
public interface TaskQueryMapper {

  @SelectProvider(type = TaskQuerySqlProvider.class, method = "queryTaskSummaries")
  @Result(property = "id", column = "ID")
  @Result(property = "externalId", column = "EXTERNAL_ID")
  @Result(property = "created", column = "CREATED")
  @Result(property = "claimed", column = "CLAIMED")
  @Result(property = "completed", column = "COMPLETED")
  @Result(property = "modified", column = "MODIFIED")
  @Result(property = "planned", column = "PLANNED")
  @Result(property = "received", column = "RECEIVED")
  @Result(property = "due", column = "DUE")
  @Result(property = "name", column = "NAME")
  @Result(property = "creator", column = "CREATOR")
  @Result(property = "note", column = "NOTE")
  @Result(property = "description", column = "DESCRIPTION")
  @Result(property = "priority", column = "PRIORITY")
  @Result(property = "manualPriority", column = "MANUAL_PRIORITY")
  @Result(property = "state", column = "STATE")
  @Result(property = "workbasketSummaryImpl.domain", column = "DOMAIN")
  @Result(property = "workbasketSummaryImpl.key", column = "WORKBASKET_KEY")
  @Result(property = "workbasketSummaryImpl.id", column = "WORKBASKET_ID")
  @Result(property = "classificationSummaryImpl.key", column = "CLASSIFICATION_KEY")
  @Result(property = "classificationSummaryImpl.id", column = "CLASSIFICATION_ID")
  @Result(property = "classificationSummaryImpl.domain", column = "DOMAIN")
  @Result(property = "classificationSummaryImpl.category", column = "CLASSIFICATION_CATEGORY")
  @Result(property = "businessProcessId", column = "BUSINESS_PROCESS_ID")
  @Result(property = "parentBusinessProcessId", column = "PARENT_BUSINESS_PROCESS_ID")
  @Result(property = "owner", column = "OWNER")
  @Result(property = "ownerLongName", column = "LONG_NAME")
  @Result(property = "primaryObjRefImpl.company", column = "POR_COMPANY")
  @Result(property = "primaryObjRefImpl.system", column = "POR_SYSTEM")
  @Result(property = "primaryObjRefImpl.systemInstance", column = "POR_INSTANCE")
  @Result(property = "primaryObjRefImpl.type", column = "POR_TYPE")
  @Result(property = "primaryObjRefImpl.value", column = "POR_VALUE")
  @Result(property = "isRead", column = "IS_READ")
  @Result(property = "isTransferred", column = "IS_TRANSFERRED")
  @Result(property = "isReopened", column = "IS_REOPENED")
  @Result(property = "groupByCount", column = "R_COUNT")
  @Result(property = "custom1", column = "CUSTOM_1")
  @Result(property = "custom2", column = "CUSTOM_2")
  @Result(property = "custom3", column = "CUSTOM_3")
  @Result(property = "custom4", column = "CUSTOM_4")
  @Result(property = "custom5", column = "CUSTOM_5")
  @Result(property = "custom6", column = "CUSTOM_6")
  @Result(property = "custom7", column = "CUSTOM_7")
  @Result(property = "custom8", column = "CUSTOM_8")
  @Result(property = "custom9", column = "CUSTOM_9")
  @Result(property = "custom10", column = "CUSTOM_10")
  @Result(property = "custom11", column = "CUSTOM_11")
  @Result(property = "custom12", column = "CUSTOM_12")
  @Result(property = "custom13", column = "CUSTOM_13")
  @Result(property = "custom14", column = "CUSTOM_14")
  @Result(property = "custom15", column = "CUSTOM_15")
  @Result(property = "custom16", column = "CUSTOM_16")
  @Result(property = "customInt1", column = "CUSTOM_INT_1")
  @Result(property = "customInt2", column = "CUSTOM_INT_2")
  @Result(property = "customInt3", column = "CUSTOM_INT_3")
  @Result(property = "customInt4", column = "CUSTOM_INT_4")
  @Result(property = "customInt5", column = "CUSTOM_INT_5")
  @Result(property = "customInt6", column = "CUSTOM_INT_6")
  @Result(property = "customInt7", column = "CUSTOM_INT_7")
  @Result(property = "customInt8", column = "CUSTOM_INT_8")
  @Result(property = "numberOfComments", column = "NUMBER_OF_COMMENTS")
  List<TaskSummaryImpl> queryTaskSummaries(TaskQueryImpl taskQuery);

  @SelectProvider(type = TaskQuerySqlProvider.class, method = "queryTaskSummariesDb2")
  @Result(property = "id", column = "ID")
  @Result(property = "externalId", column = "EXTERNAL_ID")
  @Result(property = "created", column = "CREATED")
  @Result(property = "claimed", column = "CLAIMED")
  @Result(property = "completed", column = "COMPLETED")
  @Result(property = "modified", column = "MODIFIED")
  @Result(property = "planned", column = "PLANNED")
  @Result(property = "received", column = "RECEIVED")
  @Result(property = "due", column = "DUE")
  @Result(property = "name", column = "NAME")
  @Result(property = "creator", column = "CREATOR")
  @Result(property = "note", column = "NOTE")
  @Result(property = "description", column = "DESCRIPTION")
  @Result(property = "priority", column = "PRIORITY")
  @Result(property = "manualPriority", column = "MANUAL_PRIORITY")
  @Result(property = "state", column = "STATE")
  @Result(property = "workbasketSummaryImpl.domain", column = "DOMAIN")
  @Result(property = "workbasketSummaryImpl.key", column = "WORKBASKET_KEY")
  @Result(property = "workbasketSummaryImpl.id", column = "WORKBASKET_ID")
  @Result(property = "classificationSummaryImpl.key", column = "CLASSIFICATION_KEY")
  @Result(property = "classificationSummaryImpl.id", column = "CLASSIFICATION_ID")
  @Result(property = "classificationSummaryImpl.domain", column = "DOMAIN")
  @Result(property = "classificationSummaryImpl.category", column = "CLASSIFICATION_CATEGORY")
  @Result(property = "businessProcessId", column = "BUSINESS_PROCESS_ID")
  @Result(property = "parentBusinessProcessId", column = "PARENT_BUSINESS_PROCESS_ID")
  @Result(property = "owner", column = "OWNER")
  @Result(property = "ownerLongName", column = "ULONG_NAME")
  @Result(property = "primaryObjRefImpl.company", column = "POR_COMPANY")
  @Result(property = "primaryObjRefImpl.system", column = "POR_SYSTEM")
  @Result(property = "primaryObjRefImpl.systemInstance", column = "POR_INSTANCE")
  @Result(property = "primaryObjRefImpl.type", column = "POR_TYPE")
  @Result(property = "primaryObjRefImpl.value", column = "POR_VALUE")
  @Result(property = "isRead", column = "IS_READ")
  @Result(property = "isTransferred", column = "IS_TRANSFERRED")
  @Result(property = "isReopened", column = "IS_REOPENED")
  @Result(property = "custom1", column = "CUSTOM_1")
  @Result(property = "custom2", column = "CUSTOM_2")
  @Result(property = "custom3", column = "CUSTOM_3")
  @Result(property = "custom4", column = "CUSTOM_4")
  @Result(property = "custom5", column = "CUSTOM_5")
  @Result(property = "custom6", column = "CUSTOM_6")
  @Result(property = "custom7", column = "CUSTOM_7")
  @Result(property = "custom8", column = "CUSTOM_8")
  @Result(property = "custom9", column = "CUSTOM_9")
  @Result(property = "custom10", column = "CUSTOM_10")
  @Result(property = "custom11", column = "CUSTOM_11")
  @Result(property = "custom12", column = "CUSTOM_12")
  @Result(property = "custom13", column = "CUSTOM_13")
  @Result(property = "custom14", column = "CUSTOM_14")
  @Result(property = "custom15", column = "CUSTOM_15")
  @Result(property = "custom16", column = "CUSTOM_16")
  @Result(property = "customInt1", column = "CUSTOM_INT_1")
  @Result(property = "customInt2", column = "CUSTOM_INT_2")
  @Result(property = "customInt3", column = "CUSTOM_INT_3")
  @Result(property = "customInt4", column = "CUSTOM_INT_4")
  @Result(property = "customInt5", column = "CUSTOM_INT_5")
  @Result(property = "customInt6", column = "CUSTOM_INT_6")
  @Result(property = "customInt7", column = "CUSTOM_INT_7")
  @Result(property = "customInt8", column = "CUSTOM_INT_8")
  @Result(property = "numberOfComments", column = "NUMBER_OF_COMMENTS")
  List<TaskSummaryImpl> queryTaskSummariesDb2(TaskQueryImpl taskQuery);

  @SelectProvider(type = TaskQuerySqlProvider.class, method = "countQueryTasks")
  Long countQueryTasks(TaskQueryImpl taskQuery);

  @SelectProvider(type = TaskQuerySqlProvider.class, method = "countQueryTasksDb2")
  Long countQueryTasksDb2(TaskQueryImpl taskQuery);

  @SelectProvider(type = TaskQuerySqlProvider.class, method = "queryTaskColumnValues")
  List<String> queryTaskColumnValues(TaskQueryImpl taskQuery);
}
