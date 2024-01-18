import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchsComponent } from './archs.component';

describe('ArchsComponent', () => {
  let component: ArchsComponent;
  let fixture: ComponentFixture<ArchsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArchsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArchsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
