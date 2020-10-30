import {Component, OnInit} from '@angular/core';
import {appConfig} from "../../../app.config";
import {UtilService} from "../../../services/util.service";
import {EbillingService} from "../../../services/ebilling.service";
import {AuthService} from "../../../services/auth.service";
import Swal from "sweetalert2/dist/sweetalert2.all.min.js";

@Component({
  selector: 'app-update-info',
  templateUrl: './update-info.component.html',
  styleUrls: ['./update-info.component.css']
})
export class UpdateInfoComponent implements OnInit {

  public loading = false;
  appConfig = appConfig;
  datosRegistrados: boolean = false;
  formatoImagenCorrecto: boolean = true;
  formatoFirmaCorrecto: boolean = true;
  existeFirma: boolean = false;
  usuarioEbilling: any;
  usuarioIDusuario: any;
  usuarioRUC : any;
  usuarioDireccion: any;
  usuarioActividadComercial: any;
  usuarioPuntoEmision: any;
  usuarioLogo: any;
  fileStart: any;
  usuarioFirma: any;
  usuarioFechaFirma: any;
  fileSignatureStart: any;
  usuarioEbillingRequest: any;
  actualizacionDatosCorrecta: boolean = false;
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  constructor(private _authService: AuthService, private _utilService: UtilService,
              private _ebillingService: EbillingService) { }

  ngOnInit() {

    this.usuarioEbilling = {};
    this.fileStart = {};
    this.fileSignatureStart = {};
    this.usuarioLogo = {};
    this.usuarioFirma = {};

    this.consultarCliente();
  }

  consultarCliente(){
    this.loading = true;
    this._utilService.obtenerUsuariobyNombre(localStorage.getItem('id_user')).subscribe(
      data => {
        this.usuarioIDusuario = data.usuarioIdDocumento;
        this.usuarioDireccion = data.usuarioDireccion;
        this.usuarioRUC = data.usuarioRuc;
        this.consultarUsuarioEbilling(data.usuarioRuc);
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  consultarUsuarioEbilling(userID: any){
    this.loading = true;
    this._ebillingService.getEBillingUserByUserID(userID).subscribe(
      data => {
        this.loading = false;
        if(data == null){
          this.usuarioPuntoEmision = "002";
          this.usuarioEbilling = {};
          return;
        }
        this.datosRegistrados = true;
        this.usuarioEbilling = data;
        this.usuarioLogo.file = this.usuarioEbilling.usuarioEbillingLogo;
        this.usuarioFechaFirma = this.usuarioEbilling.usuarioEbillingFechaFirma;
        this.usuarioActividadComercial = this.usuarioEbilling.usuarioEBillingActividadComercial;
        this.usuarioPuntoEmision = this.usuarioEbilling.usuarioEbillingPuntoEmision;
        this.existeFirma = true;
      },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }

  onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        if(file.type == "image/jpg" || file.type == "image/jpeg" || file.type == "image/png")
        {
          this.usuarioLogo.file = reader.result.split(',')[1];
          this.usuarioLogo.fileType = file.type;
          this.usuarioLogo.fileName = file.name;
          this.usuarioLogo.fileSize = file.size;
          this.usuarioEbilling.usuarioEbillingLogo = this.usuarioLogo.file;
          this.formatoImagenCorrecto = true;
        } else{
          this.operacionCorrecta = false;
          this.formatoImagenCorrecto = false;
          Swal.fire('','Solo puede subir imágenes en formato jpg, jpeg o png', 'warning');
        }
      };
    }
  }

  onFileSignatureChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        if(file.type == "application/x-pkcs12")
        {
          this.usuarioFirma.file = reader.result.split(',')[1];
          this.usuarioFirma.fileType = file.type;
          this.usuarioFirma.fileName = file.name;
          this.usuarioFirma.fileSize = file.size;
          this.formatoFirmaCorrecto = true;
          this.existeFirma = true;
        } else{
          this.operacionCorrecta = false;
          this.formatoFirmaCorrecto = false;
          Swal.fire('','Solo puede subir archivos en formato .p12', 'warning');
        }
      };
    }
  }

  showLogo(imgElem) {
    if(this.usuarioEbilling == {} || this.usuarioEbilling.usuarioEbillingLogo == "" ){return}
    Swal.fire({
      imageUrl: 'data:image/jpg;base64,'+this.usuarioEbilling.usuarioEbillingLogo,
      imageAlt: 'Logo',
      showConfirmButton: false
    });
  }

  actualizarDatos(){
    this.loading = true;
    this.usuarioEbillingRequest = { };
    this.usuarioEbillingRequest.usuarioEbillingId = this.usuarioRUC;
    this.usuarioEbillingRequest.usuarioEbillingIdUsuario = this.usuarioIDusuario;
    this.usuarioEbillingRequest.usuarioEbillingFirma = this.usuarioFirma;
    this.usuarioEbillingRequest.usuarioEbillingLogo = this.usuarioLogo;
    this.usuarioEbillingRequest.usuarioEbillingDireccion = this.usuarioDireccion;
    this.usuarioEbillingRequest.usuarioEBillingActividadComercial = this.usuarioActividadComercial;
    this.usuarioEbillingRequest.usuarioEbillingPuntoEmision = this.usuarioPuntoEmision;

    this._ebillingService.createUser(this.usuarioEbillingRequest).subscribe(
      data => {
        this.loading = false;
        this.actualizacionDatosCorrecta = data.response == 'OK';
        Swal.fire('', data.responseMessage, (data.response == 'OK')? 'success' : 'error').then((result) => {
          this.consultarCliente();
        });
        },
      error => {
        if(error.status == 403) this._authService.logout();
        this.loading = false;
      }
    );
  }
}
