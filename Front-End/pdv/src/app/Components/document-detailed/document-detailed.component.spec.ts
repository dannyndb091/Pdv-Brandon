import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentDetailedComponent } from './document-detailed.component';

describe('DocumentDetailedComponent', () => {
  let component: DocumentDetailedComponent;
  let fixture: ComponentFixture<DocumentDetailedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentDetailedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DocumentDetailedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
