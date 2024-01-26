import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SelectClient } from '../DTOs/Clientes/select-client';

@Injectable({
  providedIn: 'root'
})
export class ClientsService {
  private baseURL = "http://localhost:8080/api/v1/clients/"

  constructor(private httpClient : HttpClient) { }

  //Obtener todos los documentos de venta.
  getToSell():Observable<HttpResponse<SelectClient[]>>{
    return this.httpClient.get<SelectClient[]>(`${this.baseURL + "get/toSell"}`, {observe: 'response'});
  }
}
