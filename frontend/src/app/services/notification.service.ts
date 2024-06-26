import {Injectable} from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(private toastr: ToastrService) {
  }

  public error(message: string, title: string = '') {
    this.toastr.error(message, title, {
      enableHtml: true,
      positionClass: 'toast-bottom-right'
    });
  }

  public success(message: string, title: string = '') {
    this.toastr.success(message, title, {
      enableHtml: true,
      positionClass: 'toast-bottom-right'
    });
  }

  public info(message: string, title: string = '', enableHtml: boolean = false) {
    this.toastr.info(message, title, {enableHtml: enableHtml, positionClass: 'toast-bottom-right'});
  }
}
