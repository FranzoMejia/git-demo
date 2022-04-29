import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDomicilio, Domicilio } from '../domicilio.model';
import { DomicilioService } from '../service/domicilio.service';

@Injectable({ providedIn: 'root' })
export class DomicilioRoutingResolveService implements Resolve<IDomicilio> {
  constructor(protected service: DomicilioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDomicilio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((domicilio: HttpResponse<Domicilio>) => {
          if (domicilio.body) {
            return of(domicilio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Domicilio());
  }
}
