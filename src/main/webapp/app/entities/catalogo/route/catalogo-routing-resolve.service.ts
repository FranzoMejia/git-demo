import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogo, Catalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';

@Injectable({ providedIn: 'root' })
export class CatalogoRoutingResolveService implements Resolve<ICatalogo> {
  constructor(protected service: CatalogoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICatalogo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((catalogo: HttpResponse<Catalogo>) => {
          if (catalogo.body) {
            return of(catalogo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Catalogo());
  }
}
