import {CommonModule} from '@angular/common';
import {Component, OnInit, ViewChild} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ConfirmationDialogComponent} from 'src/app/components/util/confirmation-dialog/confirmation-dialog.component';
import {ProductCategoryDto} from 'src/app/dtos/plushtoy';
import {AdminService} from 'src/app/services/admin.service';
import {NotificationService} from "../../../../services/notification.service";

@Component({
  selector: 'app-admin-category-overview',
  standalone: true,
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
  imports: [CommonModule, RouterModule, ConfirmationDialogComponent],
  providers: [NotificationService]
})
export class AdminCategoryOverviewComponent implements OnInit {
  categories: ProductCategoryDto[] = [];
  @ViewChild(ConfirmationDialogComponent) confirmationDialog!: ConfirmationDialogComponent;
  selectedForDeleteId: number | null = null;

  constructor(
    private service: AdminService,
    private notification: NotificationService
  ) {
  }

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
          this.notification.error('An error occurred while fetching categories', 'Error fetching categories');
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
          console.error('Error deleting category', error);
          this.notification.error('An error occurred while deleting the category', 'Error deleting category');
        }
      });
  }

  selectForDeletion(id: number): void {
    this.selectedForDeleteId = id;
    this.confirmationDialog.showModal();
  }

}
