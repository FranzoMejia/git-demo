import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DomicilioComponent } from './list/domicilio.component';
import { DomicilioDetailComponent } from './detail/domicilio-detail.component';
import { DomicilioUpdateComponent } from './update/domicilio-update.component';
import { DomicilioDeleteDialogComponent } from './delete/domicilio-delete-dialog.component';
import { DomicilioRoutingModule } from './route/domicilio-routing.module';

@NgModule({
  imports: [SharedModule, DomicilioRoutingModule],
  declarations: [DomicilioComponent, DomicilioDetailComponent, DomicilioUpdateComponent, DomicilioDeleteDialogComponent],
  entryComponents: [DomicilioDeleteDialogComponent],
})
export class DomicilioModule {}
