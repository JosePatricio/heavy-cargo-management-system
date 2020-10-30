import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LoadingModule} from "ngx-loading";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {DateTimePickerModule} from "ng-pick-datetime";
import {MyDatePickerModule} from 'mydatepicker';
import {FormsModule} from "@angular/forms";
import {ChartsModule} from "ng2-charts";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications/dist";
import {EbillingRoutingModule} from "./ebilling-routing.module";
import {UpdateInfoComponent} from "./update-info/update-info.component";
import {ListEbillingsComponent} from "./list-ebillings/list-ebillings.component";


@NgModule({
  imports: [
    CommonModule,
    EbillingRoutingModule,
    LoadingModule,
    NgxDatatableModule,
    MyDatePickerModule,
    DateTimePickerModule,
    SimpleNotificationsModule.forRoot(),
    FormsModule,
    ChartsModule,
  ],
  declarations: [
    UpdateInfoComponent,
    ListEbillingsComponent
  ],
  entryComponents: [

  ],
  providers: [
    NotificationsService,
  ]
})
export class EbillingModule { }
