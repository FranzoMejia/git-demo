import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CatalogoComponent } from './list/catalogo.component';
import { CatalogoDetailComponent } from './detail/catalogo-detail.component';
import { CatalogoUpdateComponent } from './update/catalogo-update.component';
import { CatalogoDeleteDialogComponent } from './delete/catalogo-delete-dialog.component';
import { CatalogoRoutingModule } from './route/catalogo-routing.module';

@NgModule({
  imports: [SharedModule, CatalogoRoutingModule],
  declarations: [CatalogoComponent, CatalogoDetailComponent, CatalogoUpdateComponent, CatalogoDeleteDialogComponent],
  entryComponents: [CatalogoDeleteDialogComponent],
})
export class CatalogoModule {}
