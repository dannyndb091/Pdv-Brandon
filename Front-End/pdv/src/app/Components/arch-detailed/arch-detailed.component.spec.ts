import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchDetailedComponent } from './arch-detailed.component';

describe('ArchDetailedComponent', () => {
  let component: ArchDetailedComponent;
  let fixture: ComponentFixture<ArchDetailedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArchDetailedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArchDetailedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
