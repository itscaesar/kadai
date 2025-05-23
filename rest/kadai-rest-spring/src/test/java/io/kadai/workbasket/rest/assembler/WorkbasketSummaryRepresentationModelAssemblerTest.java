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

package io.kadai.workbasket.rest.assembler;

import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_1;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_2;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_3;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_4;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_5;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_6;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_7;
import static io.kadai.workbasket.api.WorkbasketCustomField.CUSTOM_8;
import static org.assertj.core.api.Assertions.assertThat;

import io.kadai.rest.test.KadaiSpringBootTest;
import io.kadai.workbasket.api.WorkbasketService;
import io.kadai.workbasket.api.WorkbasketType;
import io.kadai.workbasket.api.models.WorkbasketSummary;
import io.kadai.workbasket.internal.models.WorkbasketSummaryImpl;
import io.kadai.workbasket.rest.models.WorkbasketSummaryRepresentationModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Test for {@link WorkbasketSummaryRepresentationModelAssembler}. */
@KadaiSpringBootTest
class WorkbasketSummaryRepresentationModelAssemblerTest {

  private final WorkbasketSummaryRepresentationModelAssembler assembler;

  private final WorkbasketService workbasketService;

  @Autowired
  WorkbasketSummaryRepresentationModelAssemblerTest(
      WorkbasketSummaryRepresentationModelAssembler assembler,
      WorkbasketService workbasketService) {
    this.assembler = assembler;
    this.workbasketService = workbasketService;
  }

  static void testEquality(
      WorkbasketSummary summary, WorkbasketSummaryRepresentationModel repModel) {
    assertThat(summary).hasNoNullFieldsOrProperties();
    assertThat(repModel).hasNoNullFieldsOrProperties();
    assertThat(summary.getDescription()).isEqualTo(repModel.getDescription());
    assertThat(summary.getDomain()).isEqualTo(repModel.getDomain());
    assertThat(summary.getId()).isEqualTo(repModel.getWorkbasketId());
    assertThat(summary.getKey()).isEqualTo(repModel.getKey());
    assertThat(summary.getName()).isEqualTo(repModel.getName());
    assertThat(summary.getCustomField(CUSTOM_1)).isEqualTo(repModel.getCustom1());
    assertThat(summary.getCustomField(CUSTOM_2)).isEqualTo(repModel.getCustom2());
    assertThat(summary.getCustomField(CUSTOM_3)).isEqualTo(repModel.getCustom3());
    assertThat(summary.getCustomField(CUSTOM_4)).isEqualTo(repModel.getCustom4());
    assertThat(summary.getCustomField(CUSTOM_5)).isEqualTo(repModel.getCustom5());
    assertThat(summary.getCustomField(CUSTOM_6)).isEqualTo(repModel.getCustom6());
    assertThat(summary.getCustomField(CUSTOM_7)).isEqualTo(repModel.getCustom7());
    assertThat(summary.getCustomField(CUSTOM_8)).isEqualTo(repModel.getCustom8());
    assertThat(summary.getOrgLevel1()).isEqualTo(repModel.getOrgLevel1());
    assertThat(summary.getOrgLevel2()).isEqualTo(repModel.getOrgLevel2());
    assertThat(summary.getOrgLevel3()).isEqualTo(repModel.getOrgLevel3());
    assertThat(summary.getOrgLevel4()).isEqualTo(repModel.getOrgLevel4());
    assertThat(summary.getOwner()).isEqualTo(repModel.getOwner());
    assertThat(summary.getType()).isEqualTo(repModel.getType());
  }

  @Test
  void should_ReturnRepresentationModel_When_ConvertingEntityToRepresentationModel() {
    // given
    WorkbasketSummaryImpl workbasketSummary =
        (WorkbasketSummaryImpl) workbasketService.newWorkbasket("1", "DOMAIN_A").asSummary();
    workbasketSummary.setDescription("WorkbasketSummaryImplTes");
    workbasketSummary.setId("1");
    workbasketSummary.setName("WorkbasketSummary");
    workbasketSummary.setCustom1("custom1");
    workbasketSummary.setCustom2("custom2");
    workbasketSummary.setCustom3("custom3");
    workbasketSummary.setCustom4("custom4");
    workbasketSummary.setCustom5("custom5");
    workbasketSummary.setCustom6("custom6");
    workbasketSummary.setCustom7("custom7");
    workbasketSummary.setCustom8("custom8");
    workbasketSummary.setOrgLevel1("Org1");
    workbasketSummary.setOrgLevel2("Org2");
    workbasketSummary.setOrgLevel3("Org3");
    workbasketSummary.setOrgLevel4("Org4");
    workbasketSummary.setOwner("Lars");
    workbasketSummary.setType(WorkbasketType.PERSONAL);
    // when
    WorkbasketSummaryRepresentationModel repModel = assembler.toModel(workbasketSummary);
    // then
    testEquality(workbasketSummary, repModel);
  }

  @Test
  void should_ReturnEntity_When_ConvertingRepresentationModelToEntity() {
    WorkbasketSummaryRepresentationModel repModel = new WorkbasketSummaryRepresentationModel();
    repModel.setWorkbasketId("1");
    repModel.setCustom1("Custom1");
    repModel.setCustom2("Custom2");
    repModel.setCustom3("Custom3");
    repModel.setCustom4("Custom4");
    repModel.setCustom5("Custom5");
    repModel.setCustom6("Custom6");
    repModel.setCustom7("Custom7");
    repModel.setCustom8("Custom8");
    repModel.setDescription("Test Ressource");
    repModel.setDomain("DOMAIN_A");
    repModel.setKey("1");
    repModel.setName("Ressource");
    repModel.setOrgLevel1("Org1");
    repModel.setOrgLevel2("Org2");
    repModel.setOrgLevel3("Org3");
    repModel.setOrgLevel4("Org4");
    repModel.setOwner("Lars");
    repModel.setType(WorkbasketType.PERSONAL);
    // when
    WorkbasketSummary workbasket = assembler.toEntityModel(repModel);
    // then
    testEquality(workbasket, repModel);
  }

  @Test
  void should_Equal_When_ComparingEntityWithConvertedEntity() {
    WorkbasketSummaryImpl workbasketSummary =
        (WorkbasketSummaryImpl) workbasketService.newWorkbasket("1", "DOMAIN_A").asSummary();
    workbasketSummary.setDescription("WorkbasketSummaryImplTes");
    workbasketSummary.setId("1");
    workbasketSummary.setName("WorkbasketSummary");
    workbasketSummary.setCustom1("custom1");
    workbasketSummary.setCustom2("custom2");
    workbasketSummary.setCustom3("custom3");
    workbasketSummary.setCustom4("custom4");
    workbasketSummary.setCustom5("custom5");
    workbasketSummary.setCustom6("custom6");
    workbasketSummary.setCustom7("custom7");
    workbasketSummary.setCustom8("custom8");
    workbasketSummary.setOrgLevel1("Org1");
    workbasketSummary.setOrgLevel2("Org2");
    workbasketSummary.setOrgLevel3("Org3");
    workbasketSummary.setOrgLevel4("Org4");
    workbasketSummary.setOwner("Lars");
    workbasketSummary.setType(WorkbasketType.PERSONAL);

    WorkbasketSummaryRepresentationModel repModel = assembler.toModel(workbasketSummary);
    WorkbasketSummary workbasketSummary2 = assembler.toEntityModel(repModel);

    assertThat(workbasketSummary)
        .hasNoNullFieldsOrProperties()
        .isNotSameAs(workbasketSummary2)
        .isEqualTo(workbasketSummary2);
  }
}
