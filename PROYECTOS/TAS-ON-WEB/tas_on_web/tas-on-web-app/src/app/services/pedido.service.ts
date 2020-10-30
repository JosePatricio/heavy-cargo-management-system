import {Injectable} from '@angular/core';
import {appConfig} from '../app.config';
import {Headers, Http} from '@angular/http';
import 'rxjs/Rx';
import {AuthService} from "./auth.service";

@Injectable()
export class PedidoService {

  constructor(private _http: Http, public _authService: AuthService) {

  }

  getVendedores() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/vendedores/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getClientesBy(query: string){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/clientes-by${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  agendarVisita(visita){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/visita/nueva/', visita, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  agendarmeVisitas(visita){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/visita/agendarme/', visita, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getVisitasBy(query) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/visita/consultar-by/${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getVisitasAllBy(query) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/visita/all/consultar-by/${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  actualizarCliente(cliente){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/cliente/actualizar', cliente, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  guardarCliente(cliente){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/cliente/guardar', cliente, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getProductos() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/productos`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  guardarPedido(pedido){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/guardar', pedido, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getPedido(visitaId) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/by-visita/${visitaId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getAllPedidosBy(query) {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/all/consultar-by/${query}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getCategorias() {
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/categorias/`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  actualizarProducto(producto){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/producto/actualizar', producto, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  guardarProducto(producto){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.post(appConfig.apiUrlServicesBase + '/rest-pedido/pedido/producto/guardar', producto, {headers: headers}).map((data) => {
      return data.json();
    });
  }

  getDocumentosCredito(pedidoId){
    var headers = new Headers();
    headers.append('Authorization', 'Bearer ' + this._authService.getToken());
    return this._http.get(appConfig.apiUrlServicesBase + `/rest-pedido/pedido/credito/documentos/${pedidoId}`, {headers: headers}).map((data) => {
      return data.json();
    });
  }

}
