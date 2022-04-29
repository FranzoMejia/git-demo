import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DomicilioComponent } from '../list/domicilio.component';
import { DomicilioDetailComponent } from '../detail/domicilio-detail.component';
import { DomicilioUpdateComponent } from '../update/domicilio-update.component';
import { DomicilioRoutingResolveService } from './domicilio-routing-resolve.service';

const domicilioRoute: Routes = [
  {
    path: '',
    component: DomicilioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomicilioDetailComponent,
    resolve: {
      domicilio: DomicilioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomicilioUpdateComponent,
    resolve: {
      domicilio: DomicilioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomicilioUpdateComponent,
    resolve: {
      domicilio: DomicilioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(domicilioRoute)],
  exports: [RouterModule],
})
export class DomicilioRoutingModule {}
