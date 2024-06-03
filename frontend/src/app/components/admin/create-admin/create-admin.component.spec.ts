import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCreateAdminComponent } from './create-admin.component';

describe('CreateAdminComponent', () => {
  let component: AdminCreateAdminComponent;
  let fixture: ComponentFixture<AdminCreateAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCreateAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCreateAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
