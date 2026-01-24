import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FaqInsertComponent } from './faq-insert.component';

describe('FaqInsertComponent', () => {
  let component: FaqInsertComponent;
  let fixture: ComponentFixture<FaqInsertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FaqInsertComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FaqInsertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
