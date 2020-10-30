import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {NotificationsService} from "angular2-notifications";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {SolicitudEnvioService} from "../../../../services/solicitud-envio.service";
import {OfertaService} from "../../../../services/oferta.service";
import {UtilsCommon} from "../../../../utils/utils.common";
import {DomSanitizer} from "@angular/platform-browser";
import {appConfig} from "../../../../app.config";
import {AuthService} from "../../../../services/auth.service";

declare var $: any;

@Component({
  selector: 'app-solicitudes-facturarse-detalle',
  templateUrl: './solicitudes-facturarse-detalle.component.html'
})
export class SolicitudesFacturarseDetalleComponent implements OnInit {

  public loading = false;
  solicitudes: any;
  solicitudesDetalle: any;
  appConfig = appConfig;

  imagenesRecoleccion = new Array<any>();
  imagenSeleccionada;
  imagenesEntrega = new Array<any>();

  //Detalle data table
  @ViewChild(DatatableComponent) table1: DatatableComponent;
  @ViewChild('date_') date_: TemplateRef<any>;
  @ViewChild('amount_') amount_: TemplateRef<any>;
  rows1 = [];
  columns1 = [];

  @Input('idSolicitud') idSolicitud;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private sanitizer: DomSanitizer, public _solicitudEnvio: SolicitudEnvioService,
              private _notification: NotificationsService, private _authService: AuthService,
              private _oferta: OfertaService) {
  }

  ngOnInit() {

    this.solicitudesDetalle = {};
    this.solicitudesDetalle.offer = {};
    this.solicitudesDetalle.offers = [];

    this.columns1 = [
      {prop: 'amount', name: 'Valor', cellTemplate: this.amount_, headerClass: 'table-header'},
      {prop: 'comments', name: 'Comentarios', headerClass: 'table-header'},
      {prop: 'date', name: 'Fecha', cellTemplate: this.date_, headerClass: 'table-header'}
    ];
    this.showDetail();
  }

  showDetail() {
    this.loading = true;
    this._solicitudEnvio.getSolicitudEnvioByIdSolicitud(this.idSolicitud).subscribe(data => {
      this.solicitudesDetalle = data;

      // push our inital complete list
      this.rows1 = this.solicitudesDetalle.offers;

      this.loading = false;
    }, error => {
    });
  }

  showFiles(imgElem: string, estado: string) {
    this.loading = true;
    this._oferta.getDocument(this.solicitudesDetalle.offer.idOferta, estado).subscribe(
      data => {
        UtilsCommon.image64Val(imgElem, data.document);
        this.loading = false;
        $("#myModal").modal('show');
      }, error2 => {
        this.loading = false;
        $("#myModal").modal('hide');
      });
  }

  closeDetail() {
    this.complete.emit(true);
  }


  verImagenesRecoleccion() {
    this.loading = true;
    let that = this;
    that.imagenesRecoleccion = new Array<any>();
    this._oferta.getDocument(this.solicitudesDetalle.offer.idOferta, appConfig.estadoFotoRecoleccion).subscribe(
      data => {
        this.loading = false;
        data.forEach(function(img){
          that.imagenesRecoleccion.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+img));
        });
      }, error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();

      });
  }

  verImagenesEntrega() {
    this.loading = true;
    let that = this;
    that.imagenesEntrega = new Array<any>();
    this._oferta.getDocument(this.solicitudesDetalle.offer.idOferta, appConfig.estadoFotoEntrega).subscribe(
      data => {
        this.loading = false;
        data.forEach(function(img){
          that.imagenesEntrega.push(that.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,'+img));
        });
      }, error => {
        this.loading = false;
        if(error.status == 403) this._authService.logout();
      });
  }

  verImagenRecoleccion(index){
    this.imagenSeleccionada = this.imagenesRecoleccion[index];
    $("#myModal").modal('show');
  }

  verImagenEntrega(index){
    this.imagenSeleccionada = this.imagenesEntrega[index];
    $("#myModal").modal('show');
  }
}
