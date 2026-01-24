import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryInsertComponent } from './history-insert.component';

describe('HistoryInsertComponent', () => {
  let component: HistoryInsertComponent;
  let fixture: ComponentFixture<HistoryInsertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryInsertComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoryInsertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
