import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import * as bootstrap from 'bootstrap';

@Component({
  standalone: true,
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
  @Output() confirm = new EventEmitter<void>();
  @ViewChild('confirmationModal') private confirmationModal!: ElementRef;
  @Input() dialogTitle: string;
  @Input() message: string;

  confirmAction(): void {
    this.confirm.emit();
    this.hideModal();
  }

  showModal(): void {
    console.log('show modal', this.confirmationModal);
    const modal = new bootstrap.Modal(this.confirmationModal.nativeElement);
    modal.show();
  }

  hideModal(): void {
    const modal = bootstrap.Modal.getInstance(this.confirmationModal.nativeElement);
    modal.hide();
  }
}
