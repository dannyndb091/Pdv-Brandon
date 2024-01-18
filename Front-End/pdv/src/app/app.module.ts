import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './Components/home/home.component';
import { DocumentsComponent } from './Components/documents/documents.component';
import { DocumentDetailedComponent } from './Components/document-detailed/document-detailed.component';
import { ArchsComponent } from './Components/archs/archs.component';
import { ArchDetailedComponent } from './Components/arch-detailed/arch-detailed.component';
import { ClientsComponent } from './Components/clients/clients.component';
import { ClientDetailedComponent } from './Components/client-detailed/client-detailed.component';
import { ProductsComponent } from './Components/products/products.component';
import { ProductDetailedComponent } from './Components/product-detailed/product-detailed.component';
import { ReportsComponent } from './Components/reports/reports.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    DocumentsComponent,
    DocumentDetailedComponent,
    ArchsComponent,
    ArchDetailedComponent,
    ClientsComponent,
    ClientDetailedComponent,
    ProductsComponent,
    ProductDetailedComponent,
    ReportsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
