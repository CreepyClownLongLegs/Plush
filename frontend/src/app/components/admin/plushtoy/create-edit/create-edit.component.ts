import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormsModule} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  PlushToyColor,
  PlushToySize,
  ProductCategoryDto,
  PlushToy
} from 'src/app/dtos/plushtoy';
import {AdminService} from 'src/app/services/admin.service';
import {PlushtoyService} from "../../../../services/plushtoy.service";

export enum PlushToyCreateEditMode {
  create,
  edit
}

@Component({
  selector: 'app-admin-plushtoy-create-edit',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create-edit.component.html',
  styleUrl: './create-edit.component.scss',
})
export class AdminPlushtoyCreateEditComponent implements OnInit {
  mode: PlushToyCreateEditMode = PlushToyCreateEditMode.create;
  plushToy: PlushToy;
  colors = Object.values(PlushToyColor).filter(value => typeof value === 'string') as string[];
  sizes = Object.values(PlushToySize).filter(value => typeof value === 'string') as string[];
  categories: ProductCategoryDto[];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private plushtoyService: PlushtoyService,
    private router: Router,
    private route: ActivatedRoute) {
    this.plushToy = new PlushToy();
  }

  get modeIsCreate(): boolean {
    return this.mode === PlushToyCreateEditMode.create;
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data.mode;
    });

    if (!this.modeIsCreate) {
      this.route.params.subscribe(params => {
        this.plushtoyService.getById(params.id).subscribe({
          next: (pt: PlushToy) => {
            this.plushToy = pt;
          },
          error: error => {
            console.error('Error getting plush toy', error);
          }
        });
      });
    }

    this.adminService.getCategories().subscribe({
      next: (categories: ProductCategoryDto[]) => {
        this.categories = categories;
      },
      error: error => {
        console.error('Error getting categories', error);
      }
    });
  }

  public get submitButtonText(): string {
    switch (this.mode) {
      case PlushToyCreateEditMode.create:
        return 'Create';
      case PlushToyCreateEditMode.edit:
        return 'Save';
      default:
        return '';
    }
  }

  public get heading(): string {
    switch (this.mode) {
      case PlushToyCreateEditMode.create:
        return 'Add a new Plush Toy';
      case PlushToyCreateEditMode.edit:
        return 'Edit Plush Toy';
      default:
        return '';
    }
  }

  public get actionModeError(): string {
    switch (this.mode) {
      case PlushToyCreateEditMode.create:
        return 'creating';
      case PlushToyCreateEditMode.edit:
        return 'editing';
      default:
        return '';
    }
  }

  public get actionModeFinished(): string {
    switch (this.mode) {
      case PlushToyCreateEditMode.create:
        return 'created';
      case PlushToyCreateEditMode.edit:
        return 'edited';
      default:
        return '';
    }
  }

  compareCategories(c1: ProductCategoryDto, c2: ProductCategoryDto): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }

  submitForm() {
    console.log(this.plushToy);
    if (this.modeIsCreate) {
      this.adminService.create(this.plushToy)
        .subscribe({
          next: (pt: PlushToy) => {
            console.log(`PlushToy ${this.actionModeFinished} with id ${pt.id}`);
            this.router.navigate(['/admin']);
          },
          error: error => {
            console.error(`Error ${this.actionModeError} PlushToy`, error);
          }
        });
    } else {
      this.adminService.edit(this.plushToy)
        .subscribe({
          next: (pt: PlushToy) => {
            console.log(`PlushToy ${this.actionModeFinished} with id ${pt.id}`);
            this.router.navigate(['/admin']);
          },
          error: error => {
            console.error(`Error ${this.actionModeError} PlushToy`, error);
          }
        });
    }

  }

  // Used for enum readability (https://stackoverflow.com/a/196991/8517948)
  toTitleCase(str) {
    return str.replace(
      /\w\S*/g,
      function (txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
      }
    );
  }
}
