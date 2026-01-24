import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatenewprogrammeComponent } from './createnewprogramme.component';

describe('CreatenewprogrammeComponent', () => {
  let component: CreatenewprogrammeComponent;
  let fixture: ComponentFixture<CreatenewprogrammeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreatenewprogrammeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatenewprogrammeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
