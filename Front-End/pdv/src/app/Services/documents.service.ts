import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Docs } from '../DTOs/Documentos/docs';
import { CancelDoc } from '../DTOs/Documentos/cancel-doc';
import { SelectClient } from '../DTOs/Clientes/select-client';
import { AddItem } from '../Components/product-select/add-item';
import { Mov } from '../DTOs/Documentos/mov';
import { SelectProduct } from '../DTOs/Productos/select-product';
import { DelItem } from '../DTOs/Documentos/del-item';
import { DocumentDetailed } from '../DTOs/Documentos/document-detailed';

@Injectable({
  providedIn: 'root'
})
export class DocumentsService {
  private baseURL = "http://localhost:8080/api/v1/sells/"
  private baseURLCompras = "http://localhost:8080/api/v1/buys/"

  constructor(private httpClient : HttpClient) { }

  //Obtener todos los documentos de venta.
  obtenerVentas():Observable<HttpResponse<Docs[]>>{
    return this.httpClient.get<Docs[]>(`${this.baseURL + "get/all"}`, {observe: 'response'});
  }

  //Obtener todos los documentos de venta.
  obtenerCompras():Observable<HttpResponse<Docs[]>>{
    return this.httpClient.get<Docs[]>(`${this.baseURLCompras + "get/all"}`, {observe: 'response'});
  }

  //Obtener todos los documentos de venta.
  obtenerDoc(invoice: number):Observable<HttpResponse<DocumentDetailed>>{
    return this.httpClient.get<DocumentDetailed>(`${this.baseURL + "get/doc/" + invoice}`, {observe: 'response'});
  }

  //Obtener un movimiento de la venta.
  obtenerMov(i: number, m: number):Observable<HttpResponse<Mov>>{
    return this.httpClient.get<Mov>(`${this.baseURL + "get/mov/" + i + "/" + m}`, {observe: 'response'},);
  }

  //Obtener un datos del producto de la venta.
  obtenerMovProd(sku: string):Observable<HttpResponse<SelectProduct>>{
    return this.httpClient.get<SelectProduct>(`${this.baseURL + "get/mov/" + sku}`, {observe: 'response'},);
  }

  //Eliminar una Venta
  deleteSell(cancelDoc: CancelDoc):Observable<HttpResponse<Object>>{
    return this.httpClient.delete<Object>(`${this.baseURL + "delete"}`, {observe: 'response', body: cancelDoc});
  }

  //Eliminar un producto del documento
  deleteItem(delItem: DelItem):Observable<HttpResponse<DelItem>>{
    return this.httpClient.delete<DelItem>(`${this.baseURL + "delItem"}`, {observe: 'response', body: delItem});
  }

  //Cancelar una Venta
  cancelSell(cancelDoc: CancelDoc):Observable<Object>{
    return this.httpClient.put<Object>(`${this.baseURL + "cancel"}`, cancelDoc);
  }

  //Cancelar una Venta
  createSell(selectClient: SelectClient):Observable<Object>{
    return this.httpClient.post<Object>(`${this.baseURL + "new"}`, selectClient);
  }

  //Agregar un producto a un documento
  addItem(addItem: AddItem):Observable<Object>{
    return this.httpClient.post<Object>(`${this.baseURL + "addItem"}`, addItem);
  }

  //Agregar un producto a un documento
  modifyItem(addItem: AddItem):Observable<Object>{
    return this.httpClient.put<Object>(`${this.baseURL + "updateItem"}`, addItem);
  }
}
