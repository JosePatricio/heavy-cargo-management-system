import {
  Component,
  ComponentFactoryResolver,
  EventEmitter,
  Input,
  OnInit,
  Output,
  TemplateRef,
  ViewChild,
  ViewContainerRef
} from '@angular/core';
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {SolicitudEnvioService} from "../../../../services/solicitud-envio.service";
import {SolicitudPagarComponent} from "../../solicitud-pagar/solicitud-pagar.component";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {AuthService} from "../../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-seleccionar-oferta',
  templateUrl: './seleccionar-oferta.component.html'
})
export class SeleccionarOfertaComponent implements OnInit {

  public loading = false;
  offer: any;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';
  appConfig = appConfig;
  viewOffer: any = false;

  //Para dataTable
  @ViewChild(SolicitudPagarComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  @ViewChild('linkButtonAceptar') linkButtonAceptar: TemplateRef<any>;
  @ViewChild('tmplValorComic') tmplValorComic: TemplateRef<any>;
  @ViewChild('tmplValorAhorro') tmplValorAhorro: TemplateRef<any>;
  @ViewChild('tmplValorPorPieza') tmplValorPorPieza: TemplateRef<any>;
  @ViewChild('tmplBreakWord') tmplBreakWord: TemplateRef<any>;
  ofertas: any;
  columns = [];
  columnsSubastaInversaAbierta = [];
  columnsSubastaInversaValorObjetivo = [];

  @Input('idSolicitud') idSolicitud;
  @Output() complete: EventEmitter<any> = new EventEmitter<any>();

  constructor(public _oferta: OfertaService, public _solicitudEnvioService: SolicitudEnvioService,
              private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver,
              private _authService: AuthService) {
  }

  ngOnInit() {
    this.columnsSubastaInversaAbierta = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 60},
      {name: 'Seleccionar', cellTemplate: this.linkButtonAceptar, headerClass: 'table-header', width: 115},
      {prop: 'supplier', name: 'Transportista', headerClass: 'table-header'},
      {prop: 'numeroPiezas', name: 'Precio por pieza', cellTemplate: this.tmplValorPorPieza, headerClass: 'table-header'},
      {name: 'Monto', cellTemplate: this.tmplValorComic, headerClass: 'table-header', width: 120},
      {prop: 'comments', name: 'Comentario', cellTemplate: this.tmplBreakWord, headerClass: 'table-header'},
      {prop: 'date', name: 'Fecha', headerClass: 'table-header'}
    ];

    this.columnsSubastaInversaValorObjetivo = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 60},
      {name: 'Seleccionar', cellTemplate: this.linkButtonAceptar, headerClass: 'table-header', width: 115},
      {prop: 'supplier', name: 'Transportista', headerClass: 'table-header'},
      {prop: 'valorObjetivo', name: 'Valor objetivo', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor oferta', cellTemplate: this.tmplValorComic, headerClass: 'table-header'},
      {prop: 'numeroPiezas', name: 'Precio por pieza', cellTemplate: this.tmplValorPorPieza, headerClass: 'table-header'},
      {name: 'Ahorro', cellTemplate: this.tmplValorAhorro, headerClass: 'table-header', width: 120},
      {prop: 'comments', name: 'Comentario', cellTemplate: this.tmplBreakWord, headerClass: 'table-header'},
      {prop: 'date', name: 'Fecha', headerClass: 'table-header'}
    ];

    this.loadData();

    if(this._authService.getClienteNotaCredito() == "true"){
      this.columns = this.columnsSubastaInversaValorObjetivo;
    } else{
      this.columns = this.columnsSubastaInversaAbierta;
    }
  }

  loadData() {
    this.offer = {};
    this.loading = true;
    this._oferta.getOfertasBySolicitud(this.idSolicitud, appConfig.ofertasEstadoCreada).subscribe(data => {
      this.ofertas = data;
      this.loading = false;
    }, error => {
      this.loading = false;
    });
  }

  swalAlert(solicitud, idOferta, ammount){
    Swal.fire({
      text: 'Seleccionar oferta por $'+ammount +' como ganadora',
      //type: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si',
      cancelButtonText: 'No'
    }).then((result) => {
      if (result.value) {
        this.acceptOferta(solicitud, idOferta, ammount);
      }
    })
  }

  acceptOferta(solicitud, idOferta, ammount) {
    this.offer.idSolicitud = solicitud;
    this.offer.idOferta = idOferta;
    this.offer.amount = ammount;
    this.loading = true;
    this._solicitudEnvioService.acceptOfertaBySolicitud(this.offer).subscribe(
      data => {
        this.loading = false;
        this.operacionCorrecta = data.response == 'OK';
        this.mensajeOperacion = data.responseMessage;
        Swal.fire('', this.mensajeOperacion,(data.response == 'OK')? 'success' : 'error').then((result) => {
          this.navCorrectOP(true);
        });
      },
      error => {
        this.loading = false;
        Swal.fire('', 'Error al aceptar oferta, por favor intente nuevamente', 'error');
      });
  }

  closeDetail(event: boolean) {
    this.viewOffer = !event;
    this.loadData();
  }

  navCorrectOP(load) {
    this.complete.emit({complete: true, load: load});
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      this.navCorrectOP(true);
    }
  }

}
