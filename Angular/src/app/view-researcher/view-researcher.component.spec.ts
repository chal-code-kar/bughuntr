import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewResearcherComponent } from './view-researcher.component';

describe('ViewResearcherComponent', () => {
  let component: ViewResearcherComponent;
  let fixture: ComponentFixture<ViewResearcherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewResearcherComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewResearcherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
