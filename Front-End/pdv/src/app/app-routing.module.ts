import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './Components/home/home.component';
import { DocumentsComponent } from './Components/documents/documents.component';
import { ArchsComponent } from './Components/archs/archs.component';
import { ClientsComponent } from './Components/clients/clients.component';
import { ProductsComponent } from './Components/products/products.component';
import { ReportsComponent } from './Components/reports/reports.component';

const routes: Routes = [
  {path : 'home', component:HomeComponent},
  {path : 'buys/documents', component:DocumentsComponent, data: {type: 0, cmpnt: 0}},
  {path : 'sells/documents', component:DocumentsComponent, data: {type: 1, cmpnt: 3}},
  {path : 'decreases/documents', component:DocumentsComponent, data: {type: 2, cmpnt: 6}},
  {path : 'archs/documents', component:ArchsComponent, data: { cmpnt: 2}},
  {path : 'clients/list', component:ClientsComponent, data: {type: 0, cmpnt: 1}},
  {path : 'prov/list', component:ClientsComponent, data: {type: 1, cmpnt: 4}},
  {path : 'products/list', component:ProductsComponent, data: { cmpnt: 5}},
  {path : 'reports/list', component:ReportsComponent, data: { cmpnt: 7}},
  
  {path : '**', component:HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
