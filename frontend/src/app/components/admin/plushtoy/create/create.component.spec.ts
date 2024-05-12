import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPlushtoyCreateComponent } from './create.component';

describe('CreateComponent', () => {
  let component: AdminPlushtoyCreateComponent;
  let fixture: ComponentFixture<AdminPlushtoyCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPlushtoyCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminPlushtoyCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
