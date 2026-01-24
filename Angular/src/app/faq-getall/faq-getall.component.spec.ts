import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FaqGetallComponent } from './faq-getall.component';

describe('FaqGetallComponent', () => {
  let component: FaqGetallComponent;
  let fixture: ComponentFixture<FaqGetallComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FaqGetallComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FaqGetallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
