import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourcesEditcategoryComponent } from './resources-editcategory.component';

describe('ResourcesEditcategoryComponent', () => {
  let component: ResourcesEditcategoryComponent;
  let fixture: ComponentFixture<ResourcesEditcategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResourcesEditcategoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourcesEditcategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
