import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEtldataComponent } from './add-etldata.component';

describe('AddEtldataComponent', () => {
  let component: AddEtldataComponent;
  let fixture: ComponentFixture<AddEtldataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddEtldataComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddEtldataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
