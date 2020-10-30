import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {appConfig} from "../../../../app.config";
import {UtilService} from "../../../../services/util.service";
import {ClienteService} from "../../../../services/cliente.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-informacion-bancaria-actualizar',
  templateUrl: './informacion-bancaria-actualizar.component.html',
  styleUrls: ['./informacion-bancaria-actualizar.component.css']
})
export class InformacionBancariaActualizarComponent implements OnInit {

  @Input('informacionBancaria') informacionBancaria: any;
  @Output() complete: EventEmitter<boolean> = new EventEmitter<boolean>();

  loading: boolean = false;
  appConfig = appConfig;
  tiposCuenta: any;
  bancos: any;
  tiposDocumento: any;

  constructor(private _utilService: UtilService, private _clienteService: ClienteService) { }

  ngOnInit() {
    this.loadTypeBankAcc();
    this.loadBanks();
    this.loadTiposDocumento();
  }

  loadTypeBankAcc() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoTipoCuenta).subscribe(
      data => {
        this.loading = false;
        this.tiposCuenta = data;
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadBanks() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoBanco).subscribe(
      data => {
        this.loading = false;
        this.bancos = data;
      },
      error => {
        this.loading = false;
      }
    );
  }

  loadTiposDocumento() {
    this.loading = true;
    this._utilService.obtenerCatalogoItem(appConfig.idCatalogoDocumento).subscribe(
      data => {
        this.loading = false;
        this.tiposDocumento = data;
      },
      error => {
        this.loading = false;
      }
    );
  }

  actualizar(){
    this.loading = true;
    this._clienteService.updateClienteInfoBancaria(this.informacionBancaria).subscribe(
      data => {
        this.loading = false;
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          this.volver();
        });
      },
      error => {
        this.loading = false;
      }
    );
  }

  volver() {
    this.complete.emit(true);
  }

}
