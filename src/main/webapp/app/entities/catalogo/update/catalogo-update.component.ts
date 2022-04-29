import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICatalogo, Catalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';

@Component({
  selector: 'jhi-catalogo-update',
  templateUrl: './catalogo-update.component.html',
})
export class CatalogoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    productoDesc: [],
    precioSugerido: [],
  });

  constructor(protected catalogoService: CatalogoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogo }) => {
      this.updateForm(catalogo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogo = this.createFromForm();
    if (catalogo.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogoService.update(catalogo));
    } else {
      this.subscribeToSaveResponse(this.catalogoService.create(catalogo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogo>>): void {
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

  protected updateForm(catalogo: ICatalogo): void {
    this.editForm.patchValue({
      id: catalogo.id,
      productoDesc: catalogo.productoDesc,
      precioSugerido: catalogo.precioSugerido,
    });
  }

  protected createFromForm(): ICatalogo {
    return {
      ...new Catalogo(),
      id: this.editForm.get(['id'])!.value,
      productoDesc: this.editForm.get(['productoDesc'])!.value,
      precioSugerido: this.editForm.get(['precioSugerido'])!.value,
    };
  }
}
