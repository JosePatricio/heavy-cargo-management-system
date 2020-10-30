import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {NuevoUsuarioRoutingModule} from "./nuevo-usuario-routing.module";
import {NuevoUsuarioComponent} from "./nuevo-usuario.component";
import {NuevaEmpresaComponent} from "./nueva-empresa/nueva-empresa.component";
import {NuevaEmpresaTransporteComponent} from "./nueva-empresa-transporte/nueva-empresa-transporte.component";
import {NuevoClienteLoginComponent} from "./nuevo-cliente/nuevo-cliente-login.component";
import {NuevoConductorLoginComponent} from "./nuevo-conductor/nuevo-conductor-login.component";
import {NuevoConductorDependenciaPropiaComponent} from "./nuevo-conductor-dependencia-propia/nuevo-conductor-dependencia-propia.component";
import {TerminosModule} from "../terminos/terminos.module";
import {AngularMultiSelectModule} from 'angular4-multiselect-dropdown/angular4-multiselect-dropdown';


@NgModule({
  imports: [
    CommonModule,
    NuevoUsuarioRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    FormsModule,
    TerminosModule,
    AngularMultiSelectModule,
  ],
  declarations: [
    NuevoUsuarioComponent,
    NuevaEmpresaComponent,
    NuevaEmpresaTransporteComponent,
    NuevoClienteLoginComponent,
    NuevoConductorLoginComponent,
    NuevoConductorDependenciaPropiaComponent,
  ],
  entryComponents: [

  ],
  providers: [
  ]
})
export class NuevoUsuarioModule { }
