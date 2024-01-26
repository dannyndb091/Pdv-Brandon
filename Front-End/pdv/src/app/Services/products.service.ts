import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private baseURL = "http://localhost:8080/api/v1/products/"

  constructor(private httpClient : HttpClient) { }

  //Obtener todos los productos disponibles para venta.
  getProductsSell(): Observable<HttpResponse<any[]>>{
    return this.httpClient.get<any[]>(`${this.baseURL + "get/selectableToSell/"}`, {observe: 'response'});
  }
}
