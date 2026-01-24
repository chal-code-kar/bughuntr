import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowserDisplayCompatibilityComponent } from './browser-display-compatibility.component';

describe('BrowserDisplayCompatibilityComponent', () => {
  let component: BrowserDisplayCompatibilityComponent;
  let fixture: ComponentFixture<BrowserDisplayCompatibilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BrowserDisplayCompatibilityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowserDisplayCompatibilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
