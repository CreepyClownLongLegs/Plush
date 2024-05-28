import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AdminPlushtoyCreateEditComponent} from './create-edit.component';

describe('CreateComponent', () => {
  let component: AdminPlushtoyCreateEditComponent;
  let fixture: ComponentFixture<AdminPlushtoyCreateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPlushtoyCreateEditComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AdminPlushtoyCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
