import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditresearcherComponent } from './editresearcher.component';

describe('EditresearcherComponent', () => {
  let component: EditresearcherComponent;
  let fixture: ComponentFixture<EditresearcherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditresearcherComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditresearcherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
