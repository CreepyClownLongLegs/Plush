import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PlushToyDetailsDto as PlushToyDetailsDto, PlushToyColor, PlushToyCreationDto, PlushToySize } from 'src/app/dtos/plushtoy';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss',
})
export class AdminPlushtoyCreateComponent {
  plushToy: PlushToyCreationDto;
  colors = Object.values(PlushToyColor).filter(value => typeof value === 'string') as string[];
  sizes = Object.values(PlushToySize).filter(value => typeof value === 'string') as string[];
  

  constructor(private fb: FormBuilder, private service: AdminService, private router: Router) {
    this.plushToy = new PlushToyCreationDto();
  }
  submitForm() {
    console.log(this.plushToy);
    this.service.create(this.plushToy)
      .subscribe({
        next: (pt: PlushToyDetailsDto) => {
          console.log(`PlushToy created with id ${pt.id}`);
          this.router.navigate(['/admin']);
        },
        error: error => {
          console.error('Error creating PlushToy', error);
        }
      });
  }

  // Used for enum readability (https://stackoverflow.com/a/196991/8517948)
  toTitleCase(str) {
    return str.replace(
      /\w\S*/g,
      function(txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
      }
    );
  }
}
