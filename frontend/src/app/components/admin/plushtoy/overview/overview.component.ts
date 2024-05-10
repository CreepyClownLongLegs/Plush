import { Component, OnInit } from '@angular/core';
import { PlushToyListDto } from 'src/app/dtos/plushtoy';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-plushtoy-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class AdminPlushtoyOverviewComponent implements OnInit {
  plushtoys: PlushToyListDto[] = [];
  constructor(
    private service: AdminService,
  ) { }

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
        }
      });
  }

  onDelete(id: number) {
    this.service.delete(id)
      .subscribe({
        next: () => {
          this.plushtoys = this.plushtoys.filter(plustoy => plustoy.id !== id);
        },
        error: error => {
          console.error('Error deleting Plushtoys', error);
        }
      });
  }

}
