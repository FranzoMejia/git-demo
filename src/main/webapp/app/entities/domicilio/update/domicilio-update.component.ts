import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDomicilio, Domicilio } from '../domicilio.model';
import { DomicilioService } from '../service/domicilio.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-domicilio-update',
  templateUrl: './domicilio-update.component.html',
})
export class DomicilioUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    descripcion: [null, [Validators.required]],
    ubicacion: [],
    cliente: [],
  });

  constructor(
    protected domicilioService: DomicilioService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domicilio }) => {
      this.updateForm(domicilio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domicilio = this.createFromForm();
    if (domicilio.id !== undefined) {
      this.subscribeToSaveResponse(this.domicilioService.update(domicilio));
    } else {
      this.subscribeToSaveResponse(this.domicilioService.create(domicilio));
    }
  }

  trackClienteById(_index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomicilio>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(domicilio: IDomicilio): void {
    this.editForm.patchValue({
      id: domicilio.id,
      descripcion: domicilio.descripcion,
      ubicacion: domicilio.ubicacion,
      cliente: domicilio.cliente,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, domicilio.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }

  protected createFromForm(): IDomicilio {
    return {
      ...new Domicilio(),
      id: this.editForm.get(['id'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      ubicacion: this.editForm.get(['ubicacion'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
