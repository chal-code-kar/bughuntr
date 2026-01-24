import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageAddDropdownComponent } from './manage-add-dropdown.component';

describe('ManageAddDropdownComponent', () => {
  let component: ManageAddDropdownComponent;
  let fixture: ComponentFixture<ManageAddDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageAddDropdownComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageAddDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
