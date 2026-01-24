import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditprogrammeComponent } from './editprogramme.component';

describe('EditprogrammeComponent', () => {
  let component: EditprogrammeComponent;
  let fixture: ComponentFixture<EditprogrammeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditprogrammeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditprogrammeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
