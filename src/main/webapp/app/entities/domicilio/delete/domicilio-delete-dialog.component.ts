import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomicilio } from '../domicilio.model';
import { DomicilioService } from '../service/domicilio.service';

@Component({
  templateUrl: './domicilio-delete-dialog.component.html',
})
export class DomicilioDeleteDialogComponent {
  domicilio?: IDomicilio;

  constructor(protected domicilioService: DomicilioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.domicilioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
