import { Injectable } from '@angular/core';
import Swal, { SweetAlertIcon } from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertsService {

  constructor() { }

  tinyAlert(text: string) {
    Swal.fire(text);
  }
  successNotification(title: string, body: string) {
    Swal.fire(title, body, 'success');
  }
  alertConfirmation(title: string, text: string, ico: SweetAlertIcon, confirmText: string, cancelText: string) {
    return Swal.fire({
      title: title,
      text: text,
      icon: ico,
      showCancelButton: true,
      confirmButtonText: confirmText,
      cancelButtonText: cancelText,
    });
  }
}
