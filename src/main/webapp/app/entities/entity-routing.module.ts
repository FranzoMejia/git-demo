import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'adminApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'domicilio',
        data: { pageTitle: 'adminApp.domicilio.home.title' },
        loadChildren: () => import('./domicilio/domicilio.module').then(m => m.DomicilioModule),
      },
      {
        path: 'pedido',
        data: { pageTitle: 'adminApp.pedido.home.title' },
        loadChildren: () => import('./pedido/pedido.module').then(m => m.PedidoModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'adminApp.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'catalogo',
        data: { pageTitle: 'adminApp.catalogo.home.title' },
        loadChildren: () => import('./catalogo/catalogo.module').then(m => m.CatalogoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
