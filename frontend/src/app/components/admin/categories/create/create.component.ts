import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ProductCategoryCreationDto, ProductCategoryDto } from 'src/app/dtos/plushtoy';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-create-category',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss',
})
export class AdminCategoryCreateComponent {
  category: ProductCategoryCreationDto;  

  constructor(private fb: FormBuilder, private service: AdminService, private router: Router) {
    this.category = new ProductCategoryCreationDto();
  }

  submitForm() {
    this.service.createCategory(this.category)
      .subscribe({
        next: (cat: ProductCategoryDto) => {
          console.log(`Category created with id ${cat.id}`);
          this.router.navigate(['/admin/categories']);
        },
        error: error => {
          console.error('Error creating category', error);
        }
      });
  }
}
