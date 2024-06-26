import {Component, OnInit, ViewChild} from '@angular/core';
import {ConfirmationDialogComponent} from 'src/app/components/util/confirmation-dialog/confirmation-dialog.component';
import {PlushToyListDto} from 'src/app/dtos/plushtoy';
import {AdminService} from 'src/app/services/admin.service';
import {NotificationService} from "../../../../services/notification.service";


@Component({
  selector: 'app-admin-plushtoy-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
})
export class AdminPlushtoyOverviewComponent implements OnInit {
  plushtoys: PlushToyListDto[] = [];
  @ViewChild(ConfirmationDialogComponent) confirmationDialog!: ConfirmationDialogComponent;
  selectedId: number | null = null;

  constructor(
    private service: AdminService,
    private notification: NotificationService
  ) {
  }

  ngOnInit() {
    this.loadPlushToys();
  }

  loadPlushToys() {
    this.service.getAllPlushToys()
      .subscribe({
        next: data => {
          this.plushtoys = data;
        },
        error: error => {
          console.error('Error fetching Plushtoys', error);
          this.notification.error('An error occurred while fetching Plushtoys', 'Error fetching Plushtoys');
        }
      });
  }

  onDelete(id: number) {
    this.service.delete(id)
      .subscribe({
        next: () => {
          this.plushtoys = this.plushtoys.filter(plustoy => plustoy.id !== id);
          this.notification.success('Plushtoy deleted successfully');
        },
        error: error => {
          console.error('Error deleting Plushtoys', error);
          this.notification.error('An error occurred while deleting Plushtoy', 'Error deleting Plushtoy');
        }
      });
  }

  selectForDeletion(id: number): void {
    this.selectedId = id;
    this.confirmationDialog.showModal();
  }

}
