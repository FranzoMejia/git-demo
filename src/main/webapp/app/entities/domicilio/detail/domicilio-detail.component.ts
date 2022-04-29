import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDomicilio } from '../domicilio.model';

@Component({
  selector: 'jhi-domicilio-detail',
  templateUrl: './domicilio-detail.component.html',
})
export class DomicilioDetailComponent implements OnInit {
  domicilio: IDomicilio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domicilio }) => {
      this.domicilio = domicilio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
