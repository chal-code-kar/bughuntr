import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageGetalldropdownComponent } from './manage-getalldropdown.component';

describe('ManageGetalldropdownComponent', () => {
  let component: ManageGetalldropdownComponent;
  let fixture: ComponentFixture<ManageGetalldropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageGetalldropdownComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageGetalldropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
