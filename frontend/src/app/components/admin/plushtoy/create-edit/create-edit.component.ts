import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {
  PlushToyColor,
  PlushToySize,
  ProductCategoryDto,
  PlushToy,
  ProductAttributeDto,
  PlushToyAttributeDistributionDto,
  PlushToyAttributeDtoWithDistribution
} from 'src/app/dtos/plushtoy';
import {AdminService} from 'src/app/services/admin.service';
import {PlushtoyService} from "../../../../services/plushtoy.service";
import {NotificationService} from "../../../../services/notification.service";

export enum PlushToyCreateEditMode {
  create,
  edit
}

@Component({
  selector: 'app-admin-plushtoy-create-edit',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './create-edit.component.html',
  styleUrl: './create-edit.component.scss',
})
export class AdminPlushtoyCreateEditComponent implements OnInit {
  mode: PlushToyCreateEditMode = PlushToyCreateEditMode.create;
  plushToy: PlushToy;
  colors = Object.values(PlushToyColor).filter(value => typeof value === 'string') as string[];
  sizes = Object.values(PlushToySize).filter(value => typeof value === 'string') as string[];
  categories: ProductCategoryDto[];
  plushToyAttributeDtoWithDistribution: PlushToyAttributeDtoWithDistribution[] = [];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private plushtoyService: PlushtoyService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: NotificationService) {
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
            console.log('Got plush toy', pt);
            this.plushToy = pt;
            this.updatePlushToyAttributes();
          },
          error: error => {
            console.error('Error getting plush toy', error);
            this.notification.error("Plush Toy not found");
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
        this.notification.error("Categories not found");
      }
    });
  }

  navigateBack() {
    this.router.navigate(['/admin']);
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

  syncPlushToyAttributesDistributions(): void {
    this.plushToy.attributesDistributions = [];

    this.plushToyAttributeDtoWithDistribution.forEach(attr => {
      attr.distributions.forEach(dist => {
        this.plushToy.attributesDistributions.push({
          attribute: {
            ...attr
          },
          ...dist
        });
      });
    });
  }

  syncPlushToyAttributes(attribute: PlushToyAttributeDtoWithDistribution): void {
    attribute.distributions.forEach((dist) => {
        const newAttribute = {...attribute}
        delete newAttribute.distributions;// remove cyclic reference
        dist.attribute = newAttribute;
      }
    );
  }

  removeAttribute(attributeName: string): void {
    this.plushToy.attributesDistributions = this.plushToy.attributesDistributions.filter(ad => ad.attribute.name !== attributeName);
    this.updatePlushToyAttributes();
  }

  removeDistribution(attribute: ProductAttributeDto, idx: number): void {
    const distributionsWithAttribute = this.plushToy.attributesDistributions.filter(ad => ad.attribute.id === attribute.id || ad.attribute.name === attribute.name);
    const toDelete = distributionsWithAttribute[idx]
    const index = this.plushToy.attributesDistributions.indexOf(toDelete);
    this.plushToy.attributesDistributions.splice(index, 1);
    this.updatePlushToyAttributes();
  }

  updatePlushToyAttributes(): PlushToyAttributeDtoWithDistribution[] {
    const uniqueAttributes = this.plushToy.attributesDistributions.reduce((uniqueAttributes: PlushToyAttributeDtoWithDistribution[], currentDistribution) => {
      const isDuplicate = uniqueAttributes.find(attr => (attr.id && attr.id == currentDistribution.attribute.id) || attr.name === currentDistribution.attribute.name);
      if (!isDuplicate) {
        uniqueAttributes.push({...currentDistribution.attribute, distributions: [currentDistribution]});
      } else {
        isDuplicate.distributions.push(currentDistribution);
      }
      return uniqueAttributes;
    }, []);
    uniqueAttributes.forEach(attr => attr.distributions.sort((a, b) => a.name > b.name ? 1 : -1));
    this.plushToyAttributeDtoWithDistribution = uniqueAttributes.sort((a, b) => a.name > b.name ? 1 : -1);
    return uniqueAttributes;
  }

  getFilteredDistributions(attribute: ProductAttributeDto): PlushToyAttributeDistributionDto[] {
    return this.plushToy.attributesDistributions.filter(ad =>
      ad.attribute.id === attribute.id || ad.attribute.name === attribute.name).sort((a, b) => a.quantityPercentage > (b.quantityPercentage) ? 1 : -1);
  }

  addAttribute(): void {
    const newAttribute = {
      name: 'New Attribute'
    };

    this.plushToy.attributesDistributions.push({
      attribute: newAttribute,
      quantityPercentage: 100,
      name: "super attribute"
    });
    this.updatePlushToyAttributes();
  }

  addDistribution(attribute: ProductAttributeDto): void {
    this.plushToy.attributesDistributions.push({
      attribute: attribute,
      quantityPercentage: 100,
      name: "super attribute"
    });
    this.updatePlushToyAttributes();
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
            this.notification.success(`PlushToy ${this.actionModeFinished} successfully!`, 'Success');
            this.router.navigate(['/admin']);
          },
          error: error => {
            console.error(`Error ${this.actionModeError} PlushToy`, error);
            this.notification.error(`Error ${this.actionModeError} PlushToy: ${error.message}`, 'Error');
          }
        });
    } else {
      this.adminService.edit(this.plushToy)
        .subscribe({
          next: (pt: PlushToy) => {
            console.log(`PlushToy ${this.actionModeFinished} with id ${pt.id}`);
            this.notification.success(`PlushToy ${this.actionModeFinished} successfully!`, 'Success');
            this.router.navigate(['/admin']);
          },
          error: error => {
            console.error(`Error ${this.actionModeError} PlushToy`, error);
            this.notification.error(`Error ${this.actionModeError} PlushToy: ${error.message}`, 'Error');
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
