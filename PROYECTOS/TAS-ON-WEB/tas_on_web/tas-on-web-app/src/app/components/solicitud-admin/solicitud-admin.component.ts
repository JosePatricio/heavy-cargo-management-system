import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {appConfig} from "../../app.config";
import {DatatableComponent} from "@swimlane/ngx-datatable/src/index";
import {Router} from "@angular/router";
import {SolicitudEnvioService} from "../../services/solicitud-envio.service";
import {ListarSolicitudComponent} from "../solicitud/listar-solicitud/listar-solicitud.component";
import {UtilService} from "../../services/util.service";
import {AuthService} from "../../services/auth.service";
import {BaseChartDirective} from "ng2-charts";
import {ClienteService} from "../../services/cliente.service";

@Component({
  selector: 'app-solicitud-admin',
  templateUrl: './solicitud-admin.component.html'
})
export class SolicitudAdminComponent implements OnInit {

  public loading = false;
  appConfig = appConfig;
  estados: any;
  estadoSelect: any;
  clienteSelect: any;
  clientes: any;
  estadoSolicitudes: any;
  estadoSolicitudesCliente: any;
  listaSolicitudes: any;
  clienteRazonSocial: any;

  /**************** CHART **********************/
  @ViewChild("baseChart") chart: BaseChartDirective;
  public barChartOptions = {
    responsive: true,
    scaleShowVerticalLines: false,
    scales: {
      //xAxes: [{stacked: true}],
      //yAxes: [{stacked: true}]
    },
    onClick: () => {
      try{
        this.estadoSelect = this.estadoSolicitudes[this.chart.chart.getElementAtEvent(event)[0]._index].idEstado;
        if(this.chart.chart.getElementAtEvent(event)[0]._datasetIndex == 0) this.getSolicitudes();
        else this.getSolicitudes(true);
      } catch (e) {}
    }
  };
  public barChartType = 'bar';
  public barChartLabels = [];
  public barChartLegend = true;
  public barChartData = [{data: [], label: 'Solicitudes'},{data: [], label: 'Cliente'}];
  /**************** /CHART *********************/

  //Para dataTable
  @ViewChild(ListarSolicitudComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('tmplDate') tmplDate: TemplateRef<any>;
  @ViewChild('tmplDateTime') tmplDateTime: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  @ViewChild('tmpPeso') tmpPeso: TemplateRef<any>;
  columns = [];

  @ViewChild('dynamicInsert', {read: ViewContainerRef}) dynamicInsert: ViewContainerRef;

  constructor(private _router: Router, public _solicitudEnvioService: SolicitudEnvioService,
              private viewContainerRef: ViewContainerRef, public _clienteService: ClienteService,
              private componentFactoryResolver: ComponentFactoryResolver, private _utilService: UtilService,
              private _authService: AuthService) {
  }
  ngOnInit() {
    this.columns = [
      {name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'idSolicitud', name: 'Solicitud', headerClass: 'table-header'},
      {prop: 'cliente', name: 'Cliente', headerClass: 'table-header'},
      {prop: 'peso', name: 'Peso', headerClass: 'table-header'},
      {prop: 'unidadPeso', name: 'Unidad peso', headerClass: 'table-header'},
      {prop: 'volumen', name: 'Volumen', headerClass: 'table-header'},
      {prop: 'unidadVolumen', name: 'Unidad volumen', headerClass: 'table-header'},
      {prop: 'piezas', name: 'Piezas', headerClass: 'table-header'},
      {prop: 'tipoProducto', name: 'Tipo producto', headerClass: 'table-header'},
      {prop: 'estibadores', name: 'Estibadores', headerClass: 'table-header'},
      {prop: 'origen', name: 'Origen', headerClass: 'table-header'},
      {prop: 'destino', name: 'Destino', headerClass: 'table-header'},
      {prop: 'fechaCreacion', name: 'Fecha creación', cellTemplate: this.tmplDateTime, headerClass: 'table-header'},
      {prop: 'fechaCaducidad', name: 'Fecha caducidad', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'fechaRecoleccion', name: 'Fecha recolección', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'fechaEntrega', name: 'Fecha entrega', cellTemplate: this.tmplDate, headerClass: 'table-header'},
      {prop: 'observaciones', name: 'Observaciones', headerClass: 'table-header'},
      {prop: 'ofertasRecibidas', name: 'Ofertas recibidas', headerClass: 'table-header'},
      {prop: 'valorMaximaOferta', name: 'Valor máxima oferta', headerClass: 'table-header'},
      {prop: 'valorMinimaOferta', name: 'Valor mínima oferta', headerClass: 'table-header'},
      {prop: 'valorOfertaGanadora', name: 'Valor oferta ganadora', headerClass: 'table-header'},
      {prop: 'transportistaGanador', name: 'Transportista ganador', headerClass: 'table-header'},
      {prop: 'estado', name: 'Estado', headerClass: 'table-header'},
    ];
    this.loadData();
  }

  loadData() {
    this.llenarComboEstados();
    this.consultarDatosChart();
    this.llenarComboEmpresas();
  }

  private llenarComboEstados(){
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoEnvios).subscribe(
      data => {
        this.estados = data;
        this.loading = false;
      },
      error => {
        this.manejarError(error);
      }
    );
  }

  private consultarDatosChart(){
    this.loading = true;
    var that = this;
    this._solicitudEnvioService.getEstadoSolicitudes('').subscribe(data => {
        this.estadoSolicitudes = data;
        data.forEach(function (value) {
          that.barChartLabels.push(value.estado);
          that.barChartData[0].data.push(value.cantidadSolicitudes);
        });
        this.updateChart();
        this.loading = false;
      },
      error => {
        this.manejarError(error);
      }
    );
  }

  public consultarDatosChartEmpresa(){
    this.loading = true;
    var that = this;
    let queryRucCliente = '?ruc='+this.clienteSelect;
    this.barChartData[1].label = this.clientes.find(cliente => cliente.ruc == this.clienteSelect).razonSocial;
    this._solicitudEnvioService.getEstadoSolicitudes(queryRucCliente).subscribe(data => {
        that.barChartData[1].data = [];
        that.estadoSolicitudesCliente = data;
        that.barChartLabels.forEach(function(value){
          let index = data.findIndex(solicitud => solicitud.estado == value);
          if(index == -1) that.barChartData[1].data.push(0);
          else that.barChartData[1].data.push(data[index].cantidadSolicitudes);
        });
        this.updateChart();
        this.loading = false;
      },
      error => {
        this.manejarError(error);
      }
    );
  }

  private llenarComboEmpresas(){
    this.loading = true;
    this._clienteService.getClientebyTipoCliente(appConfig.idTipoClienteEmpresaCliente).subscribe(data => {
      this.clientes = data;
      this.loading = false;
    }, error => {
      this.manejarError(error);
    });
  }

  getSolicitudes(consultarPorCliente: boolean = false) {
    this.loading = true;
    let query:string = "?estado=".concat(this.estadoSelect);
    if(consultarPorCliente) query = query.concat("&ruc="+this.clienteSelect);
    this._solicitudEnvioService.getSolicitudesBy(query).subscribe(data => {
      this.listaSolicitudes = data;
      this.loading = false;
    });
  }

  private updateChart(){
    if(this.chart !== undefined){
      this.chart.ngOnDestroy();
      this.chart.chart = this.chart.getChartBuilder(this.chart.ctx);
    }
  }

  private manejarError(error:any){
    if(error.status == 403) this._authService.logout();
    this.loading = false;
  }

}
