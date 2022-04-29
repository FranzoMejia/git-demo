import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICatalogo } from '../catalogo.model';

@Component({
  selector: 'jhi-catalogo-detail',
  templateUrl: './catalogo-detail.component.html',
})
export class CatalogoDetailComponent implements OnInit {
  catalogo: ICatalogo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogo }) => {
      this.catalogo = catalogo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
