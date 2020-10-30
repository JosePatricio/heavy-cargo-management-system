import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UpdateInfoComponent} from "./update-info/update-info.component";
import {ListEbillingsComponent} from "./list-ebillings/list-ebillings.component";

const routes: Routes = [
  {path: 'update-info', component: UpdateInfoComponent},
  {path: 'list-ebillings', component: ListEbillingsComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'login'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EbillingRoutingModule { }
