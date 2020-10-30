import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {OfertaService} from "../../../../services/oferta.service";
import {appConfig} from "../../../../app.config";
import {FacturaService} from "../../../../services/factura.service";
import {EmpresaService} from "../../../../services/empresa.service";
import {UtilsCommon} from "../../../../utils/utils.common"
import {AuthService} from "../../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";
import {Location} from "@angular/common";

declare var $: any;

@Component({
  selector: 'app-oferta-generar-factura-list',
  providers: [],
  templateUrl: './oferta-generar-factura-list.component.html'
})
export class OfertaGenerarFacturaListComponent extends UtilsCommon implements OnInit {
  appConfig = appConfig;
  public loading = false;
  selOfert: any[] = [];
  selOfert_: any[] = [];
  numberDoc: string;
  subTotalFactura: any = 0;
  totalFactura: any = 0;
  descuentos: any = 0;
  datosCompania: any = {};
  datosTasOn: any = {};
  actualDate = new Date();
  typePay: any;
  titlePay = 'Por generar factura';
  ofertas: any;
  columns: any;

  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplAmount') tmplAmount: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;

  mensjOpGenFact: string = 'Debe seleccionar al menos una oferta';

  constructor(public _router: Router, public _oferta: OfertaService, public _facturaService: FacturaService,
              public _empresaService: EmpresaService, private _authService: AuthService, private _location: Location) {
    super();
  }

  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 80},
      {
        sortable: false,
        canAutoResize: false,
        draggable: false,
        resizeable: false,
        headerCheckboxable: true,
        checkboxable: true,
        width: 75,
        headerClass: 'table-header'
      },
      {prop: 'idSolicitud', name: 'Solicitud', headerClass: 'table-header'},
      {prop: 'idOferta', name: 'No. oferta', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {name: 'Peso', cellTemplate: this.tmpPeso, headerClass: 'table-header'},
      {prop: 'amount', name: 'Valor', cellTemplate: this.tmplAmount, headerClass: 'table-header'},
    ];

    this.puedeCrearPrefactura();
  }

  puedeCrearPrefactura(){
    this.loading = true;
    this._facturaService.canCreatePreInvoice().subscribe(data => {
      this.loading = false;
      if(data.response == 'OK') this.loadOffertsData();
      else Swal.fire('', data.responseMessage, 'error').then((result) => {
        if (this._authService.getTypeUser() === appConfig.idUsuarioConductorAdmin)
          this._router.navigate(['/panel/usuario-conductor/informacion-bancaria']);
        else
          this._location.back();
      });
      }
    , error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
    });
  }

  initDatosEnFact() {
    //Datos Compania
    this.loading = true;
    this._empresaService.getClienteByToken().subscribe(data => {
      this.datosCompania = data;
      this.loading = false;
    }, error => {
    });

    //Datos TasOn
    this.loading = true;
    this._empresaService.readEmpresa(appConfig.rucTas).subscribe(data => {
      this.datosTasOn = data;
      this.loading = false;
    }, error => {
    });

    this.getNumDoc();
  }

  loadOffertsData() {
    this.loading = true;
    this._oferta.obtenerOfertasByEstado(appConfig.ofertaEstadoGenerarFactura).subscribe(
      data => {
        this.ofertas = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
      }
    );
    this.selOfert = [];
  }

  getNumDoc() {
    this.loading = true;
    this._facturaService.getCodeFactura().subscribe(data => {
        this.numberDoc = data.code;
        this.genMess();
        this.loading = false;
      },
      erro => {
        this.numberDoc = '-';
        this.genMess();
        this.loading = false;
      });
  }

  genMess() {
    this.selOfert_ = [{
      ofertaObservacion: 'Código de documento: ' + this.numberDoc + ', transporte de mercadería pesada',
      amount: this.subTotalFactura
    }];
    $("#modPreviewInvoice").modal('show');
  }

  onSelect({selected}) {
    this.selOfert.splice(0, this.selOfert.length);
    this.selOfert.push(...selected);

    this.totalFactura = 0;
    this.subTotalFactura = 0;
    this.descuentos = 0;
    this.selOfert.forEach(oferta => {
      if (this.typePay === this.appConfig.idPagoNormal) {
        this.totalFactura = this.totalFactura + oferta.amount;
        this.subTotalFactura = this.totalFactura;
        this.descuentos = 0;
      } else if (this.typePay === this.appConfig.idPagoInmediato) {
        this.subTotalFactura = this.subTotalFactura + oferta.amount;
        this.descuentos = this.descuentos + oferta.descuento;
        this.totalFactura = this.subTotalFactura - this.descuentos;
      }
    });
  }

  getIdOfferts() {
    var selIdOfert: any[] = [];
    this.selOfert.forEach(oferta => {
      selIdOfert.push(oferta.idOferta);
    });
    return selIdOfert;
  }

  previewFactMod() {
    if (this.selOfert.length > 0) {
      this.initDatosEnFact();
      this.mensjOpGenFact = '';
    } else {
      Swal.fire('', 'Debe seleccionar al menos una oferta', 'warning');
    }
  }

  savePreFact() {
    this.loading = true;
    var invoice: any = {
      numberInvoice: this.numberDoc,
      rucSupplier: appConfig.rucTas,
      amount: this.subTotalFactura,
      offersId: this.getIdOfferts(),
      invoiceTypePay: this.typePay
    };

    if (this.typePay === this.appConfig.idPagoInmediato) {
      invoice.invoiceDiscount = this.descuentos;
    } else {
      invoice.invoiceDiscount = 0;
    }

    this._facturaService.createPreInvoice(invoice).subscribe(data => {
      this.loading = false;
      if (this.typePay === this.appConfig.idPagoNormal)
        this.mensjOpGenFact = data.responseMessage;
      else if (this.typePay === this.appConfig.idPagoInmediato)
        this.mensjOpGenFact = 'Estimado Socio, el valor de su factura será acreditado directamente en su cuenta bancaria el día laborable posterior a la entrega del documento físico en nuestras oficinas.';
      Swal.fire('', this.mensjOpGenFact, 'success').then((result) => {
        this.loadOffertsData();
      });
    }, error => {
      this.loading = false;
      if(error.status == 403) this._authService.logout();
      Swal.fire('', error.message, 'error');
    });

  }

  tipoFactua(idTipo) {
    if (idTipo === appConfig.idPagoNormal) {
      this.titlePay = 'Facturación normal';
    } else if (idTipo === appConfig.idPagoInmediato) {
      this.titlePay = 'Facturación pago inmediato';
    } else {
      this.titlePay = 'Por generar factura';
      this.selOfert.splice(0, this.selOfert.length);
    }
    this.typePay = idTipo;
  }

}
