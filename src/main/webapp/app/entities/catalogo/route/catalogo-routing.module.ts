import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CatalogoComponent } from '../list/catalogo.component';
import { CatalogoDetailComponent } from '../detail/catalogo-detail.component';
import { CatalogoUpdateComponent } from '../update/catalogo-update.component';
import { CatalogoRoutingResolveService } from './catalogo-routing-resolve.service';

const catalogoRoute: Routes = [
  {
    path: '',
    component: CatalogoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogoDetailComponent,
    resolve: {
      catalogo: CatalogoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogoUpdateComponent,
    resolve: {
      catalogo: CatalogoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogoUpdateComponent,
    resolve: {
      catalogo: CatalogoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(catalogoRoute)],
  exports: [RouterModule],
})
export class CatalogoRoutingModule {}
