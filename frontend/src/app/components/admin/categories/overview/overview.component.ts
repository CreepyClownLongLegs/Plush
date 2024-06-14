import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ProductCategoryDto } from 'src/app/dtos/plushtoy';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-category-overview',
  standalone: true,
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  imports: [CommonModule, RouterModule],
})
export class AdminCategoryOverviewComponent implements OnInit {
  categories: ProductCategoryDto[] = [];
  constructor(
    private service: AdminService,
    private notification: ToastrService
  ) { }

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.service.getCategories()
      .subscribe({
        next: data => {
          this.categories = data;
        },
        error: error => {
          console.error('Error fetching categories', error);
        }
      });
  }

  deleteCategory(id: number) {
    this.service.deleteCategory(id)
      .subscribe({
        next: () => {
          this.notification.success('Category deleted successfully');
          this.loadCategories();
        },
        error: error => {
          this.notification.error('Error deleting category', error.error.message);
          console.error('Error deleting category', error);
        }
      });
  }

}
