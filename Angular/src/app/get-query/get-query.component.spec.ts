import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetQueryComponent } from './get-query.component';

describe('GetQueryComponent', () => {
  let component: GetQueryComponent;
  let fixture: ComponentFixture<GetQueryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetQueryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
