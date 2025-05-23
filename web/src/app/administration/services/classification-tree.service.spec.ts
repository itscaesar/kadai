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

import { ClassificationTreeService } from './classification-tree.service';
import { TestBed } from '@angular/core/testing';
import { Classification } from '../../shared/models/classification';
import { TreeNodeModel } from '../models/tree-node';

describe('ClassificationTreeService', () => {
  let service: ClassificationTreeService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [ClassificationTreeService] });
    service = TestBed.inject(ClassificationTreeService);
  });

  it('transformToTreeNodes() should return treeNodeModel list sorted by key with empty children', () => {
    const classifications: Classification[] = [
      { classificationId: 'ID21', key: 'KEY1' },
      { classificationId: 'ID13', key: 'KEY3' },
      { classificationId: 'ID42', key: 'KEY2' }
    ];
    const expectedTreeNodes: TreeNodeModel[] = [
      { classificationId: 'ID21', key: 'KEY1', children: [] },
      { classificationId: 'ID42', key: 'KEY2', children: [] },
      { classificationId: 'ID13', key: 'KEY3', children: [] }
    ];
    expect(service.transformToTreeNode(classifications)).toStrictEqual(expectedTreeNodes);
  });

  it('transformToTreeNodes() should return treeNodeModel list sorted by key with children', () => {
    const classifications: Classification[] = [
      { classificationId: 'ID4', key: 'KEY4' },
      { classificationId: 'ID1', key: 'KEY1' },
      { classificationId: 'ID3', key: 'KEY3', parentId: 'ID1', parentKey: 'KEY1' },
      { classificationId: 'ID2', key: 'KEY2', parentId: 'ID3', parentKey: 'KEY3' }
    ];
    const expectedTreeNodes: TreeNodeModel[] = [
      {
        classificationId: 'ID1',
        key: 'KEY1',
        children: [
          {
            classificationId: 'ID3',
            key: 'KEY3',
            parentId: 'ID1',
            parentKey: 'KEY1',
            children: [
              {
                classificationId: 'ID2',
                key: 'KEY2',
                parentId: 'ID3',
                parentKey: 'KEY3',
                children: []
              }
            ]
          }
        ]
      },
      { classificationId: 'ID4', key: 'KEY4', children: [] }
    ];
    expect(service.transformToTreeNode(classifications)).toStrictEqual(expectedTreeNodes);
  });
});
