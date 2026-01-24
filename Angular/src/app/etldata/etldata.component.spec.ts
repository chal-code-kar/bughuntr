import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ETLDataComponent } from './etldata.component';

describe('ETLDataComponent', () => {
  let component: ETLDataComponent;
  let fixture: ComponentFixture<ETLDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ETLDataComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ETLDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
