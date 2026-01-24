import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BbprogdetlsComponent } from './bbprogdetls.component';

describe('BbprogdetlsComponent', () => {
  let component: BbprogdetlsComponent;
  let fixture: ComponentFixture<BbprogdetlsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BbprogdetlsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BbprogdetlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
