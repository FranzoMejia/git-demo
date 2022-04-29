import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPedido, Pedido } from '../pedido.model';
import { PedidoService } from '../service/pedido.service';
import { IDomicilio } from 'app/entities/domicilio/domicilio.model';
import { DomicilioService } from 'app/entities/domicilio/service/domicilio.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';

@Component({
  selector: 'jhi-pedido-update',
  templateUrl: './pedido-update.component.html',
})
export class PedidoUpdateComponent implements OnInit {
  isSaving = false;

  domiciliosCollection: IDomicilio[] = [];
  clientesSharedCollection: ICliente[] = [];

  editForm = this.fb.group({
    id: [],
    total: [],
    fechaGenerado: [],
    fechaEntrega: [],
    preparado: [],
    entregado: [],
    foraneo: [],
    domicilio: [],
    cliente: [],
  });

  constructor(
    protected pedidoService: PedidoService,
    protected domicilioService: DomicilioService,
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedido }) => {
      this.updateForm(pedido);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedido = this.createFromForm();
    if (pedido.id !== undefined) {
      this.subscribeToSaveResponse(this.pedidoService.update(pedido));
    } else {
      this.subscribeToSaveResponse(this.pedidoService.create(pedido));
    }
  }

  trackDomicilioById(_index: number, item: IDomicilio): number {
    return item.id!;
  }

  trackClienteById(_index: number, item: ICliente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedido>>): void {
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

  protected updateForm(pedido: IPedido): void {
    this.editForm.patchValue({
      id: pedido.id,
      total: pedido.total,
      fechaGenerado: pedido.fechaGenerado,
      fechaEntrega: pedido.fechaEntrega,
      preparado: pedido.preparado,
      entregado: pedido.entregado,
      foraneo: pedido.foraneo,
      domicilio: pedido.domicilio,
      cliente: pedido.cliente,
    });

    this.domiciliosCollection = this.domicilioService.addDomicilioToCollectionIfMissing(this.domiciliosCollection, pedido.domicilio);
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, pedido.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.domicilioService
      .query({ filter: 'pedido-is-null' })
      .pipe(map((res: HttpResponse<IDomicilio[]>) => res.body ?? []))
      .pipe(
        map((domicilios: IDomicilio[]) =>
          this.domicilioService.addDomicilioToCollectionIfMissing(domicilios, this.editForm.get('domicilio')!.value)
        )
      )
      .subscribe((domicilios: IDomicilio[]) => (this.domiciliosCollection = domicilios));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }

  protected createFromForm(): IPedido {
    return {
      ...new Pedido(),
      id: this.editForm.get(['id'])!.value,
      total: this.editForm.get(['total'])!.value,
      fechaGenerado: this.editForm.get(['fechaGenerado'])!.value,
      fechaEntrega: this.editForm.get(['fechaEntrega'])!.value,
      preparado: this.editForm.get(['preparado'])!.value,
      entregado: this.editForm.get(['entregado'])!.value,
      foraneo: this.editForm.get(['foraneo'])!.value,
      domicilio: this.editForm.get(['domicilio'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
