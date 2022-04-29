import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { IPedido } from 'app/entities/pedido/pedido.model';
import { PedidoService } from 'app/entities/pedido/service/pedido.service';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;

  pedidosSharedCollection: IPedido[] = [];

  editForm = this.fb.group({
    id: [],
    producto: [null, [Validators.required]],
    precio: [],
    pedido: [],
  });

  constructor(
    protected productoService: ProductoService,
    protected pedidoService: PedidoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const producto = this.createFromForm();
    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  trackPedidoById(_index: number, item: IPedido): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  protected updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      producto: producto.producto,
      precio: producto.precio,
      pedido: producto.pedido,
    });

    this.pedidosSharedCollection = this.pedidoService.addPedidoToCollectionIfMissing(this.pedidosSharedCollection, producto.pedido);
  }

  protected loadRelationshipsOptions(): void {
    this.pedidoService
      .query()
      .pipe(map((res: HttpResponse<IPedido[]>) => res.body ?? []))
      .pipe(map((pedidos: IPedido[]) => this.pedidoService.addPedidoToCollectionIfMissing(pedidos, this.editForm.get('pedido')!.value)))
      .subscribe((pedidos: IPedido[]) => (this.pedidosSharedCollection = pedidos));
  }

  protected createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      producto: this.editForm.get(['producto'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      pedido: this.editForm.get(['pedido'])!.value,
    };
  }
}
