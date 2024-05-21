import { TestBed } from '@angular/core/testing';

import { PlushtoyService } from './plushtoy.service';

describe('PlushtoyService', () => {
  let service: PlushtoyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlushtoyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
