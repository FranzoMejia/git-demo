import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomicilio } from '../domicilio.model';
import { DomicilioService } from '../service/domicilio.service';
import { DomicilioDeleteDialogComponent } from '../delete/domicilio-delete-dialog.component';

@Component({
  selector: 'jhi-domicilio',
  templateUrl: './domicilio.component.html',
})
export class DomicilioComponent implements OnInit {
  domicilios?: IDomicilio[];
  isLoading = false;

  constructor(protected domicilioService: DomicilioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.domicilioService.query().subscribe({
      next: (res: HttpResponse<IDomicilio[]>) => {
        this.isLoading = false;
        this.domicilios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDomicilio): number {
    return item.id!;
  }

  delete(domicilio: IDomicilio): void {
    const modalRef = this.modalService.open(DomicilioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.domicilio = domicilio;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
