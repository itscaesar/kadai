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

package acceptance.classification.update;

import static io.kadai.testapi.DefaultTestEntities.defaultTestClassification;
import static io.kadai.testapi.DefaultTestEntities.defaultTestObjectReference;
import static io.kadai.testapi.DefaultTestEntities.defaultTestWorkbasket;
import static io.kadai.testapi.builder.TaskBuilder.newTask;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.kadai.classification.api.ClassificationCustomField;
import io.kadai.classification.api.ClassificationService;
import io.kadai.classification.api.exceptions.ClassificationNotFoundException;
import io.kadai.classification.api.models.Classification;
import io.kadai.classification.api.models.ClassificationSummary;
import io.kadai.common.api.KadaiEngine;
import io.kadai.common.api.KadaiRole;
import io.kadai.common.api.WorkingTimeCalculator;
import io.kadai.common.api.exceptions.ConcurrencyException;
import io.kadai.common.api.exceptions.InvalidArgumentException;
import io.kadai.common.api.exceptions.NotAuthorizedException;
import io.kadai.common.api.security.CurrentUserContext;
import io.kadai.common.internal.jobs.JobRunner;
import io.kadai.common.internal.util.Pair;
import io.kadai.task.api.TaskService;
import io.kadai.task.api.models.Attachment;
import io.kadai.task.api.models.Task;
import io.kadai.task.internal.models.TaskImpl;
import io.kadai.testapi.KadaiInject;
import io.kadai.testapi.KadaiIntegrationTest;
import io.kadai.testapi.builder.TaskBuilder;
import io.kadai.testapi.builder.WorkbasketAccessItemBuilder;
import io.kadai.testapi.security.WithAccessId;
import io.kadai.workbasket.api.WorkbasketPermission;
import io.kadai.workbasket.api.WorkbasketService;
import io.kadai.workbasket.api.models.WorkbasketSummary;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.function.ThrowingConsumer;

/** Acceptance test for all "update classification" scenarios. */
@KadaiIntegrationTest
class UpdateClassificationAccTest {
  @KadaiInject ClassificationService classificationService;
  @KadaiInject KadaiEngine kadaiEngine;
  @KadaiInject TaskService taskService;
  @KadaiInject WorkbasketService workbasketService;
  @KadaiInject WorkingTimeCalculator workingTimeCalculator;
  @KadaiInject CurrentUserContext currentUserContext;

  private String createTaskWithExistingClassification(ClassificationSummary classificationSummary)
      throws Exception {
    WorkbasketSummary workbasketSummary =
        defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .workbasketId(workbasketSummary.getId())
        .accessId(currentUserContext.getUserid())
        .permission(WorkbasketPermission.OPEN)
        .permission(WorkbasketPermission.READ)
        .permission(WorkbasketPermission.READTASKS)
        .permission(WorkbasketPermission.APPEND)
        .buildAndStore(workbasketService, "businessadmin");

    return newTask()
        .classificationSummary(classificationSummary)
        .workbasketSummary(workbasketSummary)
        .primaryObjRef(defaultTestObjectReference().build())
        .buildAndStore(taskService)
        .getId();
  }

  private List<String> createTasksWithExistingClassificationInAttachment(
      ClassificationSummary classificationSummary, String serviceLevel, int priority, int amount)
      throws Exception {
    List<String> taskList = new ArrayList<>();
    WorkbasketSummary workbasketSummary =
        defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
    WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
        .workbasketId(workbasketSummary.getId())
        .accessId(currentUserContext.getUserid())
        .permission(WorkbasketPermission.OPEN)
        .permission(WorkbasketPermission.READ)
        .permission(WorkbasketPermission.READTASKS)
        .permission(WorkbasketPermission.APPEND)
        .buildAndStore(workbasketService, "businessadmin");
    ClassificationSummary classificationSummaryWithSpecifiedServiceLevel =
        defaultTestClassification()
            .serviceLevel(serviceLevel)
            .priority(priority)
            .buildAndStoreAsSummary(classificationService);
    for (int i = 0; i < amount; i++) {
      Attachment attachment = taskService.newAttachment();
      attachment.setClassificationSummary(classificationSummary);
      attachment.setObjectReference(defaultTestObjectReference().build());
      taskList.add(
          newTask()
              .classificationSummary(classificationSummaryWithSpecifiedServiceLevel)
              .workbasketSummary(workbasketSummary)
              .primaryObjRef(defaultTestObjectReference().build())
              .attachments(attachment)
              .buildAndStore(taskService)
              .getId());
    }
    return taskList;
  }

  @TestInstance(Lifecycle.PER_CLASS)
  @Nested
  class UpdatePriorityAndServiceLevelTest {

    @WithAccessId(user = "businessadmin")
    @Test
    void should_ChangeDueDate_When_ServiceLevelOfClassificationHasChanged() throws Exception {
      Classification classification =
          defaultTestClassification()
              .priority(1)
              .serviceLevel("P1D")
              .buildAndStore(classificationService);
      WorkbasketSummary workbasketSummary =
          defaultTestWorkbasket().buildAndStoreAsSummary(workbasketService);
      WorkbasketAccessItemBuilder.newWorkbasketAccessItem()
          .workbasketId(workbasketSummary.getId())
          .accessId(currentUserContext.getUserid())
          .permission(WorkbasketPermission.OPEN)
          .permission(WorkbasketPermission.READ)
          .permission(WorkbasketPermission.READTASKS)
          .permission(WorkbasketPermission.APPEND)
          .buildAndStore(workbasketService, "businessadmin");

      Task task =
          new TaskBuilder()
              .classificationSummary(classification.asSummary())
              .workbasketSummary(workbasketSummary)
              .primaryObjRef(defaultTestObjectReference().build())
              .planned(Instant.parse("2021-04-27T15:34:00.000Z"))
              .due(null)
              .buildAndStore(taskService);

      classificationService.updateClassification(classification);
      runAssociatedJobs();
      // read again the task from DB
      task = taskService.getTask(task.getId());
      assertThat(task.getClassificationSummary().getServiceLevel()).isEqualTo("P1D");
      assertThat(task.getDue()).isAfterOrEqualTo("2021-04-28T15:33:59.999Z");

      classification.setServiceLevel("P3D");
      classificationService.updateClassification(classification);
      runAssociatedJobs();

      // read again the task from DB
      task = taskService.getTask(task.getId());
      assertThat(task.getClassificationSummary().getServiceLevel()).isEqualTo("P3D");
      assertThat(task.getDue()).isEqualTo("2021-04-30T15:33:59.999Z");
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_NotThrowException_When_UpdatingClassificationWithEmptyServiceLevel()
        throws Exception {
      Classification classification =
          defaultTestClassification().serviceLevel("P1D").buildAndStore(classificationService);
      classification.setServiceLevel("");
      assertThatCode(() -> classificationService.updateClassification(classification))
          .doesNotThrowAnyException();
      assertThat(classificationService.getClassification(classification.getId()).getServiceLevel())
          .isEqualTo("P0D");
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest>
        should_SetDefaultServiceLevel_When_TryingToUpdateClassificationWithMissingServiceLevel()
            throws Exception {
      Classification classification =
          defaultTestClassification().serviceLevel("P1D").buildAndStore(classificationService);
      List<Pair<Classification, String>> inputList =
          List.of(Pair.of(classification, null), Pair.of(classification, ""));

      ThrowingConsumer<Pair<Classification, String>> test =
          input -> {
            input.getLeft().setServiceLevel(input.getRight());
            classificationService.updateClassification(input.getLeft());
            assertThat(
                    classificationService
                        .getClassification(input.getLeft().getId())
                        .getServiceLevel())
                .isEqualTo("P0D");
          };

      return DynamicTest.stream(
          inputList.iterator(), i -> String.format("for %s", i.getRight()), test);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_UpdateTaskServiceLevel_When_UpdateClassificationInTask() throws Exception {
      final Instant before = Instant.now();
      Classification classification =
          defaultTestClassification()
              .priority(1)
              .serviceLevel("P13D")
              .buildAndStore(classificationService);
      final List<String> directLinkedTask =
          List.of(createTaskWithExistingClassification(classification.asSummary()));

      classification.setServiceLevel("P15D");
      classificationService.updateClassification(classification);
      runAssociatedJobs();

      validateTaskProperties(before, directLinkedTask, taskService, workingTimeCalculator, 15, 1);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_UpdateTaskPriority_When_UpdateClassificationInTask() throws Exception {
      final Instant before = Instant.now();
      Classification classification =
          defaultTestClassification()
              .priority(1)
              .serviceLevel("P13D")
              .buildAndStore(classificationService);
      final List<String> directLinkedTask =
          List.of(createTaskWithExistingClassification(classification.asSummary()));

      classification.setPriority(1000);
      classificationService.updateClassification(classification);
      runAssociatedJobs();

      validateTaskProperties(
          before, directLinkedTask, taskService, workingTimeCalculator, 13, 1000);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_UpdateTaskPriorityAndServiceLevel_When_UpdateClassificationInTask()
        throws Exception {
      final Instant before = Instant.now();
      Classification classification =
          defaultTestClassification()
              .priority(1)
              .serviceLevel("P13D")
              .buildAndStore(classificationService);
      final List<String> directLinkedTask =
          List.of(createTaskWithExistingClassification(classification.asSummary()));

      classification.setServiceLevel("P15D");
      classification.setPriority(1000);
      classificationService.updateClassification(classification);
      runAssociatedJobs();

      validateTaskProperties(
          before, directLinkedTask, taskService, workingTimeCalculator, 15, 1000);
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest> should_UpdateTaskServiceLevel_When_UpdateClassificationInAttachment() {
      List<Pair<String, Integer>> inputs =
          List.of(Pair.of("P5D", 2), Pair.of("P8D", 3), Pair.of("P16D", 4));

      List<Pair<Integer, Integer>> outputs = List.of(Pair.of(1, 2), Pair.of(1, 3), Pair.of(1, 4));

      List<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> zippedTestInputList =
          IntStream.range(0, inputs.size())
              .mapToObj(i -> Pair.of(inputs.get(i), outputs.get(i)))
              .toList();

      ThrowingConsumer<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> test =
          input -> {
            final Instant before = Instant.now();
            Classification classification =
                defaultTestClassification()
                    .priority(1)
                    .serviceLevel("P15D")
                    .buildAndStore(classificationService);
            ClassificationSummary classificationSummary = classification.asSummary();
            final List<String> indirectLinkedTasks =
                createTasksWithExistingClassificationInAttachment(
                    classificationSummary,
                    input.getLeft().getLeft(),
                    input.getLeft().getRight(),
                    5);

            classification.setServiceLevel("P1D");
            classificationService.updateClassification(classification);
            runAssociatedJobs();

            validateTaskProperties(
                before,
                indirectLinkedTasks,
                taskService,
                workingTimeCalculator,
                input.getRight().getLeft(),
                input.getRight().getRight());
          };

      return DynamicTest.stream(
          zippedTestInputList.iterator(),
          i ->
              String.format(
                  "for Task with ServiceLevel %s and Priority %s",
                  i.getLeft().getLeft(), i.getLeft().getRight()),
          test);
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest> should_NotUpdateTaskServiceLevel_When_UpdateClassificationInAttachment() {
      List<Pair<String, Integer>> inputs =
          List.of(Pair.of("P5D", 2), Pair.of("P8D", 3), Pair.of("P14D", 4));

      List<Pair<Integer, Integer>> outputs = List.of(Pair.of(5, 2), Pair.of(8, 3), Pair.of(14, 4));

      List<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> zippedTestInputList =
          IntStream.range(0, inputs.size())
              .mapToObj(i -> Pair.of(inputs.get(i), outputs.get(i)))
              .toList();

      ThrowingConsumer<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> test =
          input -> {
            final Instant before = Instant.now();
            Classification classification =
                defaultTestClassification()
                    .priority(1)
                    .serviceLevel("P1D")
                    .buildAndStore(classificationService);
            ClassificationSummary classificationSummary = classification.asSummary();
            final List<String> indirectLinkedTasks =
                createTasksWithExistingClassificationInAttachment(
                    classificationSummary,
                    input.getLeft().getLeft(),
                    input.getLeft().getRight(),
                    5);

            classification.setServiceLevel("P15D");
            classificationService.updateClassification(classification);
            runAssociatedJobs();

            validateTaskProperties(
                before,
                indirectLinkedTasks,
                taskService,
                workingTimeCalculator,
                input.getRight().getLeft(),
                input.getRight().getRight());
          };

      return DynamicTest.stream(
          zippedTestInputList.iterator(),
          i ->
              String.format(
                  "for Task with ServiceLevel %s and Priority %s",
                  i.getLeft().getLeft(), i.getLeft().getRight()),
          test);
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest> should_UpdateTaskPriority_When_UpdateClassificationInAttachment() {
      List<Pair<String, Integer>> inputs =
          List.of(Pair.of("P1D", 1), Pair.of("P8D", 2), Pair.of("P14D", 999));

      List<Pair<Integer, Integer>> outputs =
          List.of(Pair.of(1, 1000), Pair.of(8, 1000), Pair.of(14, 1000));

      List<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> zippedTestInputList =
          IntStream.range(0, inputs.size())
              .mapToObj(i -> Pair.of(inputs.get(i), outputs.get(i)))
              .toList();

      ThrowingConsumer<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> test =
          input -> {
            final Instant before = Instant.now();
            Classification classification =
                defaultTestClassification()
                    .priority(1)
                    .serviceLevel("P13D")
                    .buildAndStore(classificationService);
            ClassificationSummary classificationSummary = classification.asSummary();
            final List<String> indirectLinkedTasks =
                createTasksWithExistingClassificationInAttachment(
                    classificationSummary,
                    input.getLeft().getLeft(),
                    input.getLeft().getRight(),
                    5);

            classification.setServiceLevel("P15D");
            classification.setPriority(1000);
            classificationService.updateClassification(classification);
            runAssociatedJobs();

            validateTaskProperties(
                before,
                indirectLinkedTasks,
                taskService,
                workingTimeCalculator,
                input.getRight().getLeft(),
                input.getRight().getRight());
          };

      return DynamicTest.stream(
          zippedTestInputList.iterator(),
          i ->
              String.format(
                  "for Task with ServiceLevel %s and Priority %s",
                  i.getLeft().getLeft(), i.getLeft().getRight()),
          test);
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest> should_NotUpdateTaskPriority_When_UpdateClassificationInAttachment() {
      List<Pair<String, Integer>> inputs =
          List.of(Pair.of("P1D", 2), Pair.of("P8D", 3), Pair.of("P14D", 999));

      List<Pair<Integer, Integer>> outputs =
          List.of(Pair.of(1, 2), Pair.of(8, 3), Pair.of(14, 999));

      List<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> zippedTestInputList =
          IntStream.range(0, inputs.size())
              .mapToObj(i -> Pair.of(inputs.get(i), outputs.get(i)))
              .toList();

      ThrowingConsumer<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> test =
          input -> {
            final Instant before = Instant.now();
            Classification classification =
                defaultTestClassification()
                    .priority(1000)
                    .serviceLevel("P13D")
                    .buildAndStore(classificationService);
            ClassificationSummary classificationSummary = classification.asSummary();
            final List<String> indirectLinkedTasks =
                createTasksWithExistingClassificationInAttachment(
                    classificationSummary,
                    input.getLeft().getLeft(),
                    input.getLeft().getRight(),
                    5);

            classification.setServiceLevel("P15D");
            classification.setPriority(1);
            classificationService.updateClassification(classification);
            runAssociatedJobs();

            validateTaskProperties(
                before,
                indirectLinkedTasks,
                taskService,
                workingTimeCalculator,
                input.getRight().getLeft(),
                input.getRight().getRight());
          };

      return DynamicTest.stream(
          zippedTestInputList.iterator(),
          i ->
              String.format(
                  "for Task with ServiceLevel %s and Priority %s",
                  i.getLeft().getLeft(), i.getLeft().getRight()),
          test);
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest>
        should_UpdateTaskPriorityAndServiceLevel_When_UpdateClassificationInAttachment() {
      List<Pair<String, Integer>> inputs = List.of(Pair.of("P1D", 5), Pair.of("P14D", 98));

      List<Pair<Integer, Integer>> outputs = List.of(Pair.of(1, 99), Pair.of(1, 99));

      List<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> zippedTestInputList =
          IntStream.range(0, inputs.size())
              .mapToObj(i -> Pair.of(inputs.get(i), outputs.get(i)))
              .toList();

      ThrowingConsumer<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> test =
          input -> {
            final Instant before = Instant.now();
            Classification classification =
                defaultTestClassification()
                    .priority(1)
                    .serviceLevel("P13D")
                    .buildAndStore(classificationService);
            ClassificationSummary classificationSummary = classification.asSummary();
            final List<String> indirectLinkedTasks =
                createTasksWithExistingClassificationInAttachment(
                    classificationSummary,
                    input.getLeft().getLeft(),
                    input.getLeft().getRight(),
                    3);

            classification.setServiceLevel("P1D");
            classification.setPriority(99);
            classificationService.updateClassification(classification);
            runAssociatedJobs();

            validateTaskProperties(
                before,
                indirectLinkedTasks,
                taskService,
                workingTimeCalculator,
                input.getRight().getLeft(),
                input.getRight().getRight());
          };

      return DynamicTest.stream(
          zippedTestInputList.iterator(),
          i ->
              String.format(
                  "for Task with ServiceLevel %s and Priority %s",
                  i.getLeft().getLeft(), i.getLeft().getRight()),
          test);
    }

    @WithAccessId(user = "businessadmin")
    @TestFactory
    Stream<DynamicTest>
        should_NotUpdateTaskPriorityAndServiceLevel_When_UpdateClassificationInAttachment() {
      List<Pair<String, Integer>> inputs = List.of(Pair.of("P1D", 5), Pair.of("P14D", 98));

      List<Pair<Integer, Integer>> outputs = List.of(Pair.of(1, 5), Pair.of(14, 98));

      List<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> zippedTestInputList =
          IntStream.range(0, inputs.size())
              .mapToObj(i -> Pair.of(inputs.get(i), outputs.get(i)))
              .toList();

      ThrowingConsumer<Pair<Pair<String, Integer>, Pair<Integer, Integer>>> test =
          input -> {
            final Instant before = Instant.now();
            Classification classification =
                defaultTestClassification()
                    .priority(1000)
                    .serviceLevel("P1D")
                    .buildAndStore(classificationService);
            ClassificationSummary classificationSummary = classification.asSummary();
            final List<String> indirectLinkedTasks =
                createTasksWithExistingClassificationInAttachment(
                    classificationSummary,
                    input.getLeft().getLeft(),
                    input.getLeft().getRight(),
                    3);

            classification.setServiceLevel("P15D");
            classification.setPriority(1);
            classificationService.updateClassification(classification);
            runAssociatedJobs();

            validateTaskProperties(
                before,
                indirectLinkedTasks,
                taskService,
                workingTimeCalculator,
                input.getRight().getLeft(),
                input.getRight().getRight());
          };

      return DynamicTest.stream(
          zippedTestInputList.iterator(),
          i ->
              String.format(
                  "for Task with ServiceLevel %s and Priority %s",
                  i.getLeft().getLeft(), i.getLeft().getRight()),
          test);
    }

    private void runAssociatedJobs() throws Exception {
      Thread.sleep(10);
      // run the ClassificationChangedJob
      JobRunner runner = new JobRunner(kadaiEngine);
      // run the TaskRefreshJob that was scheduled by the ClassificationChangedJob.
      runner.runJobs();
      Thread.sleep(
          10); // otherwise the next runJobs call intermittently doesn't find the Job created
      // by the previous step (it searches with DueDate < CurrentTime)
      runner.runJobs();
    }

    private void validateTaskProperties(
        Instant before,
        List<String> tasksUpdated,
        TaskService taskService,
        WorkingTimeCalculator workingTimeCalculator,
        int serviceLevel,
        int priority)
        throws Exception {
      for (String taskId : tasksUpdated) {
        Task task = taskService.getTask(taskId);

        Instant expDue =
            workingTimeCalculator
                .addWorkingTime(task.getPlanned(), Duration.ofDays(serviceLevel))
                .minusMillis(1);
        assertThat(task.getModified())
            .describedAs("Task " + task.getId() + " has not been refreshed.")
            .isAfter(before);
        assertThat(task.getDue()).isEqualTo(expDue);
        assertThat(task.getPriority()).isEqualTo(priority);
      }
    }
  }

  @TestInstance(Lifecycle.PER_CLASS)
  @Nested
  class UpdateClassificationExceptionTest {
    /**
     * This BeforeAll method is needed for this {@linkplain
     * #should_ThrowException_When_UserIsNotAuthorized test} and {@linkplain
     * #should_ThrowException_When_UserRoleIsNotAdminOrBusinessAdmin test} since it can't create an
     * own classification.
     *
     * @throws Exception for errors in the building or reading process of entities.
     */
    @WithAccessId(user = "businessadmin")
    @BeforeAll
    void createClassifications() throws Exception {
      defaultTestClassification()
          .key("BeforeAllClassification")
          .buildAndStore(classificationService);
    }

    @Test
    void should_ThrowException_When_UserIsNotAuthorized() throws Exception {
      Classification classification =
          classificationService.getClassification("BeforeAllClassification", "DOMAIN_A");
      classification.setCustomField(ClassificationCustomField.CUSTOM_1, "newCustom1");

      NotAuthorizedException expectedException =
          new NotAuthorizedException(
              currentUserContext.getUserid(), KadaiRole.BUSINESS_ADMIN, KadaiRole.ADMIN);
      assertThatThrownBy(() -> classificationService.updateClassification(classification))
          .usingRecursiveComparison()
          .isEqualTo(expectedException);
    }

    @WithAccessId(user = "taskadmin")
    @WithAccessId(user = "user-1-1")
    @TestTemplate
    void should_ThrowException_When_UserRoleIsNotAdminOrBusinessAdmin() throws Exception {
      Classification classification =
          classificationService.getClassification("BeforeAllClassification", "DOMAIN_A");

      classification.setApplicationEntryPoint("updated EntryPoint");
      classification.setName("updated Name");

      NotAuthorizedException expectedException =
          new NotAuthorizedException(
              currentUserContext.getUserid(), KadaiRole.BUSINESS_ADMIN, KadaiRole.ADMIN);
      assertThatThrownBy(() -> classificationService.updateClassification(classification))
          .usingRecursiveComparison()
          .isEqualTo(expectedException);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_ThrowException_When_UpdatingClassificationConcurrently() throws Exception {
      Classification classification =
          defaultTestClassification().buildAndStore(classificationService);
      final Classification classificationSecondUpdate =
          classificationService.getClassification(
              classification.getKey(), classification.getDomain());

      classification.setApplicationEntryPoint("Application Entry Point");
      classification.setDescription("Description");
      classification.setName("Name");
      Thread.sleep(20); // to avoid identity of modified timestamps between classification and base
      classificationService.updateClassification(classification);
      classificationSecondUpdate.setName("Name again");
      classificationSecondUpdate.setDescription("Description again");

      ConcurrencyException expectedException =
          new ConcurrencyException(classificationSecondUpdate.getId());
      assertThatThrownBy(
              () -> classificationService.updateClassification(classificationSecondUpdate))
          .usingRecursiveComparison()
          .isEqualTo(expectedException);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_ThrowException_When_TryingToUpdateClassificationWithInvalidParentId()
        throws Exception {
      Classification classification =
          defaultTestClassification().buildAndStore(classificationService);

      classification.setParentId("NON EXISTING ID");

      ClassificationNotFoundException expectedException =
          new ClassificationNotFoundException("NON EXISTING ID");
      assertThatThrownBy(() -> classificationService.updateClassification(classification))
          .usingRecursiveComparison()
          .isEqualTo(expectedException);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_ThrowException_When_TryingToUpdateClassificationWithInvalidParentKey()
        throws Exception {
      Classification classification =
          defaultTestClassification().buildAndStore(classificationService);

      classification.setParentKey("NON EXISTING KEY");

      ClassificationNotFoundException expectedException =
          new ClassificationNotFoundException("NON EXISTING KEY", "DOMAIN_A");
      assertThatThrownBy(() -> classificationService.updateClassification(classification))
          .usingRecursiveComparison()
          .isEqualTo(expectedException);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_ThrowException_When_TryingToUpdateClassificationWithOwnKeyAsParentKey()
        throws Exception {
      Classification classification =
          defaultTestClassification().buildAndStore(classificationService);

      classification.setParentKey(classification.getKey());

      InvalidArgumentException expectedException =
          new InvalidArgumentException(
              String.format(
                  "The Classification '%s' has the same key and parent key",
                  classification.getName()));
      assertThatThrownBy(() -> classificationService.updateClassification(classification))
          .usingRecursiveComparison()
          .isEqualTo(expectedException);
    }
  }

  @TestInstance(Lifecycle.PER_CLASS)
  @Nested
  class UpdateClassificationCategoryTest {
    Classification classification;
    Task task;
    Instant createdBefore;
    Instant modifiedBefore;

    @WithAccessId(user = "businessadmin")
    @BeforeEach
    void createClassificationAndTask() throws Exception {
      classification =
          defaultTestClassification()
              .category("MANUAL")
              .type("TASK")
              .buildAndStore(classificationService);
      createdBefore = classification.getCreated();
      modifiedBefore = classification.getModified();
      String taskId = createTaskWithExistingClassification(classification.asSummary());
      task = taskService.getTask(taskId);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_UpdateTask_When_UpdatingClassificationCategory() throws Exception {
      classification.setCategory("PROCESS");
      classificationService.updateClassification(classification);
      final Task updatedTask = taskService.getTask(task.getId());

      TaskImpl expectedUpdatedTask = (TaskImpl) task.copy();
      expectedUpdatedTask.setId(task.getId());
      expectedUpdatedTask.setClassificationCategory("PROCESS");
      expectedUpdatedTask.setClassificationSummary(
          classificationService.getClassification(classification.getId()).asSummary());
      expectedUpdatedTask.setExternalId(task.getExternalId());
      assertThat(expectedUpdatedTask)
          .usingRecursiveComparison()
          .ignoringFields("modified")
          .isEqualTo(updatedTask);
      assertThat(expectedUpdatedTask.getModified()).isAfterOrEqualTo(modifiedBefore);
    }

    @WithAccessId(user = "businessadmin")
    @Test
    void should_UpdateClassification_When_UpdatingClassificationCategory() throws Exception {
      classification.setCategory("PROCESS");
      classificationService.updateClassification(classification);

      Classification updatedClassification =
          classificationService.getClassification(classification.getId());
      assertThat(updatedClassification)
          .usingRecursiveComparison()
          .ignoringFields("modified")
          .isEqualTo(classification);
      assertThat(updatedClassification.getModified()).isAfterOrEqualTo(modifiedBefore);
    }
  }
}
