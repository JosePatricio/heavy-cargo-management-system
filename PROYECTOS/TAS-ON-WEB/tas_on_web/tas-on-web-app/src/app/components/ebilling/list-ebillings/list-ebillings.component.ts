import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {EbillingService} from "../../../services/ebilling.service";
import {appConfig} from "../../../app.config";
import {NotificationsService} from "angular2-notifications";

declare var $: any;

@Component({
  selector: 'app-list-ebillings',
  templateUrl: './list-ebillings.component.html',
  styleUrls: ['./list-ebillings.component.css']
})
export class ListEbillingsComponent implements OnInit {

  public loading = false;
  appConfig = appConfig;
  claveFirma: any;
  ebillings: any;
  facturaSeleccionada: any;
  columns = [];
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplTypePay') tmplTypePay: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmplDiscount') tmplDiscount: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('tmplBreakWord') tmplBreakWord: TemplateRef<any>;
  @ViewChild('tmplDownloadFiles') tmplDownloadFiles: TemplateRef<any>;

  constructor(private _ebillingService: EbillingService, private _authService: AuthService, private _notification: NotificationsService) { }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'ebillingId', name: 'No. factura', headerClass: 'table-header'},
      {prop: 'emisionDate',name: 'Fecha emisión', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'usuarioCreador',name: 'Usuario', headerClass: 'table-header'},
      {prop: 'authorizationDate',name: 'Fecha autorización', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'claveAcceso', name: 'Clave de acceso', cellTemplate: this.tmplBreakWord, headerClass: 'table-header'},
      {prop: 'state', name: 'Estado', headerClass: 'table-header'},
      {prop: 'total', name: 'Total', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'adquiriente', name: 'Adquiriente', headerClass: 'table-header'},
      {prop: 'razonSocialAdquiriente', name: 'Razón Social', headerClass: 'table-header'},
      {prop: 'state', name: 'Documentos electrónicos',cellTemplate: this.tmplDownloadFiles, headerClass: 'table-header'},
    ];
    this.consultarFacturas();
  }

  consultarFacturas(){
    this.loading = true;
    this._ebillingService.getEbillings().subscribe(
      data => {
        this.ebillings = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  downloadRIDE(invoice) {
    var popup = window.open();
    popup.document.write('Descargando archivo ...');

    this.loading = true;
    this._ebillingService.getRIDE(invoice.claveAcceso).subscribe(data => {
      const url = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.download = invoice.claveAcceso +".pdf";
      anchor.href = url;
      anchor.target = '_blank';
      anchor.click();
      popup.location.href = url;
      this.loading = false;
      //popup.close();
    }, error => {
      this.loading = false;
    });
  }

  downloadXML(invoice) {
    var popup = window.open();
    popup.document.write('Descargando archivo ...');

    this.loading = true;
    this._ebillingService.getXML(invoice.claveAcceso).subscribe(data => {
      const url = window.URL.createObjectURL(data);
      var anchor = document.createElement("a");
      anchor.download = invoice.claveAcceso +".xml";
      anchor.href = url;
      anchor.target = '_blank';
      anchor.click();
      popup.location.href = url;
      this.loading = false;
      //popup.close();
    }, error => {
      this.loading = false;
    });
  }

  send(invoice) {
    this.facturaSeleccionada = invoice;
    this.claveFirma = "";
    $("#modalPassword").modal('show');
  }

  reenviarFactura(){
    console.log(this.claveFirma);
    this.loading = true;
    var ebillingRequest = {
      claveAcceso:"",
      claveFirma:""
    };
    ebillingRequest.claveAcceso = this.facturaSeleccionada.claveAcceso;
    ebillingRequest.claveFirma = this.claveFirma;

    this._ebillingService.sendEbilling(ebillingRequest).subscribe(
      data => {
        if(data.response == "OK"){
          this.facturaSeleccionada.state = "ENVIADO";
          this._notification.success(data.responseMessage);
        } else{
          this._notification.warn(data.responseMessage);
        }
        console.log(data);
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this._notification.error(error);
        this.loading = false;
      }
    );
  }

}
