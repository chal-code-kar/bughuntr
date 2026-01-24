import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowqueryComponent } from './showquery.component';

describe('ShowqueryComponent', () => {
  let component: ShowqueryComponent;
  let fixture: ComponentFixture<ShowqueryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowqueryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowqueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
