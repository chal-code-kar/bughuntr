import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropDownTableComponent } from './drop-down-table.component';

describe('DropDownTableComponent', () => {
  let component: DropDownTableComponent;
  let fixture: ComponentFixture<DropDownTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DropDownTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DropDownTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
