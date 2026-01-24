import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SmconfigErrorComponent } from './smconfig-error.component';

describe('SmconfigErrorComponent', () => {
  let component: SmconfigErrorComponent;
  let fixture: ComponentFixture<SmconfigErrorComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SmconfigErrorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SmconfigErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
