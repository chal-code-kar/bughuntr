import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageEditDropdownComponent } from './manage-edit-dropdown.component';

describe('ManageEditDropdownComponent', () => {
  let component: ManageEditDropdownComponent;
  let fixture: ComponentFixture<ManageEditDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageEditDropdownComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageEditDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
