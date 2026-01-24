import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddhelpComponent } from './addhelp.component';

describe('AddhelpComponent', () => {
  let component: AddhelpComponent;
  let fixture: ComponentFixture<AddhelpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddhelpComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddhelpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
