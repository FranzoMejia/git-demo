import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';

@Component({
  templateUrl: './catalogo-delete-dialog.component.html',
})
export class CatalogoDeleteDialogComponent {
  catalogo?: ICatalogo;

  constructor(protected catalogoService: CatalogoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
