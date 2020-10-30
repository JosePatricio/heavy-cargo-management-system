import {Component, OnInit, ViewChild} from '@angular/core';
import {IMyDpOptions} from "mydatepicker";
import {appConfig} from "../../../app.config";
import {NgForm} from "@angular/forms";
import {ClienteService} from "../../../services/cliente.service";
import {NotificationsService} from "angular2-notifications";
import {EmpresaService} from "../../../services/empresa.service";
import {PublicService} from "../../../services/public.service";
import {AuthService} from "../../../services/auth.service";
import {FacturaProveedorService} from "../../../services/factura-proveedor.service";
import {UtilsCommon} from "../../../utils/utils.common";

@Component({
  selector: 'app-facturas-gastos',
  templateUrl: './facturas-gastos.component.html'
})
export class FacturasGastosComponent implements OnInit {

  public loading: boolean = false;

  appConfig = appConfig;
  //Notifications
  options = {
    position: ["top", "right"],
    timeOut: 5000,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
    maxLength: 10
  };
  tipoPago: any = [];
  formaPago: any = [];
  ingresa: boolean = false;
  nowDate: number = Date.now();

  datosCompania: any = {};
  datosTasOn: any = {};
  invoiceData: any = {};

  @ViewChild('forma') forma: NgForm;
  public myDatePickerOptions: IMyDpOptions = {
    // other options...
    dateFormat: appConfig.formatoFechaDatePicker,
    editableDateField: false
  };

  constructor(private _clienteService: ClienteService, public _empresaService: EmpresaService, private _notification: NotificationsService,
              private _publicService: PublicService, private _authService: AuthService, private _facturaProveedorService: FacturaProveedorService) {
  }

  ngOnInit() {

  }

  loadCatalog() {
    //Datos TasOn
    this.loading = true;
    this._empresaService.readEmpresa(appConfig.rucTas).subscribe(data => {
      this.datosTasOn = data;
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
    });

    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoTipoCompra).subscribe(
      data => {
        this.tipoPago = data;
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
      }
    );

    this.loading = true;
    this._publicService.getCatalogoItemByCatalogo(appConfig.idCatalogoTipoPago).subscribe(
      data => {
        this.formaPago = data.filter(catItem => catItem.catalogoItemDescripcion !== 'COMPROBANTE RETENCION');
        this.loading = false;
      },
      error => {
        if(error.status == 403) this._authService.logout();
      }
    );
  }

  addFactGasto() {
    this.ingresa = true;
    this.loadCatalog();
  }

  onCloseDialog() {
    this.forma.resetForm();
    this.ingresa = false;
  }

  buscaCliente() {
    this.datosCompania.razonSocial = "";
    if (UtilsCommon.empty(this.datosCompania.ruc))
      return false;

    this.loading = true;
    this._clienteService.getClienteEmpresa(this.datosCompania.ruc).subscribe(data => {

      if (data.clienteTipoCliente === appConfig.idTipoEmpresaProveedor) {
        this.datosCompania.razonSocial = data.clienteRazonSocial;
      } else {
        this._notification.warn('El cliente no se encuentra registrado como proveedor.');
      }
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
      this.loading = false;
    });
  }

  setFactData() {
    this.invoiceData.facturaProveedorRucCliente = this.datosCompania.ruc;
    this.invoiceData.facturaProveedorFechaPago = this.invoiceData.facturaProveedorFechaPago.formatted
    this.invoiceData.facturaProveedorFechaEmision = this.invoiceData.facturaProveedorFechaEmision.formatted
    this.invoiceData.facturaProveedorFechaAutorizacion = this.invoiceData.facturaProveedorFechaAutorizacion.formatted

  }

  calcTotal() {
    try {
      this.invoiceData.facturaProveedorTotal =
        parseFloat(this.invoiceData.facturaProveedorSubTotal12 ? this.invoiceData.facturaProveedorSubTotal12 : 0) +
        parseFloat(this.invoiceData.facturaProveedorSubTotal0 ? this.invoiceData.facturaProveedorSubTotal0 : 0) +
        parseFloat(this.invoiceData.facturaProveedorIva ? this.invoiceData.facturaProveedorIva : 0);

      if (!parseFloat(this.invoiceData.facturaProveedorTotal))
        this.invoiceData.facturaProveedorTotal = parseFloat(this.invoiceData.facturaProveedorSubtotal);
    } catch (e) {
      console.log(e);
    }
  }

  saveInvoice() {
    this.loading = true;
    this.setFactData();
    this._facturaProveedorService.createFacturaProv(this.invoiceData).subscribe(data => {
      if(data.response == 'OK'){
        this._notification.success("").html = "<b>Operación exitosa:</b> <br/>" + data.responseMessage;
      } else {
        this._notification.warn("").html = "<b>Error:</b> <br/>" + data.responseMessage;
      }
      this.loading = false;
    }, error => {
      if(error.status == 403) this._authService.logout();
      this._notification.warn("").html = "<b>Error: </b> <br/> NO se pudo guardar documento...";
      this.loading = false;
    });
  }

}
