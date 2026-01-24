import { TestBed } from '@angular/core/testing';

import { TssvcommonvalidatorService } from './tssvcommonvalidator.service';

describe('TssvcommonvalidatorService', () => {
  let service: TssvcommonvalidatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TssvcommonvalidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
