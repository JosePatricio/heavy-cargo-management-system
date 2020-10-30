import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CatalogosService} from "../../../services/catalogos.service";
import {CatalogoItemService} from "../../../services/catalogo-item.service";

declare var $: any;

@Component({
  selector: 'app-nuevo-catalogo',
  templateUrl: './nuevo-catalogo.component.html'
})
export class NuevoCatalogoComponent implements OnInit {
  public loading: any = false;
  catalogo: any;
  itemcatalogo: any = {};

  //Mensaje de Operacion exitosa o erronea
  operacionCorrecta: boolean = false;
  mensajeOperacion: string = '';

  catalogos: any[];
  catalogoItem: any[];

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, private _catalogosService: CatalogosService, public _catalogoItemService: CatalogoItemService) {
    this.loading = true;
    this._activatedRoute.params.subscribe(
      parametros => {
        if (null != parametros.idCatalogo && parametros.idCatalogo > 0) {
          this.catalogo = this._catalogosService.getCatalogo(parametros.idCatalogo).subscribe(
            data => {
              this.catalogo = data;
            },
            error => {
              console.log(error);
              this.loading = false;
            });
          this._catalogoItemService.getCatalogoItemByCatalogo(parametros.idCatalogo).subscribe(data => {
              this.catalogoItem = data;
              this.loading = false;
            },
            error => {
              console.log(error);
              this.loading = false;
            });
        }
        if (parametros.idCatalogo == 0) {
          this.catalogo = {
            catalogoDescripcion: "",
            catalogoEstado: 1,
            catalogoId: 0
          }
          this._catalogoItemService.getCatalogoItemByCatalogo(parametros.idCatalogo).subscribe(data => {
              this.catalogoItem = data;
              this.loading = false;
            },
            error => {
              console.log(error);
              this.loading = false;
            });
        }
      });
  }

  ngOnInit() {
  }

  cancelaItem() {
    this._router.navigate(['/panel/catalogos/listacatal']);
  }

  newItem() {
    $("#modNewItem").modal('show');
  }

  grabarItemCatalog() {
    this.itemcatalogo.catalogoItemId = 0;
    this.itemcatalogo.catalogoItemIdCatalogo = this.catalogo.catalogoId;
    this.itemcatalogo.catalogoItemEstado = 1;

    this._catalogoItemService.addItemCatalogo(this.itemcatalogo).subscribe(data => {
      this.operacionCorrecta = true;
      this.mensajeOperacion = 'Item Catalogo creado correctamente';
      $("#modalResult").modal('show');
      this._catalogoItemService.getCatalogoItemByCatalogo(this.catalogo.catalogoId).subscribe(data => {
      });
    }, error => {
      console.log('Error : ', error);
      this.operacionCorrecta = false;
      this.mensajeOperacion = 'El Item Catalogo no pudo ser creado';
      $("#modalResult").modal('show');
    });
  }

  grabarCatalogo() {
    //Inisialice variable
    this.catalogo.catalogoId = null;
    var bandera = true;
    //GetAllCatalogos
    this._catalogosService.getAllCatalogos().subscribe(data => {
      this.catalogos = data;
    })
    //UPPERCASE catalogoDescripcion
    this.catalogo.catalogoDescripcion = this.catalogo.catalogoDescripcion.trim().toUpperCase();
    //Check Catalog Name
    for (let catag of this.catalogos) {
      console.log(catag);
      if (catag.catalogoDescripcion.localeCompare(this.catalogo.catalogoDescripcion) == 0)
        bandera = false;
    }
    //It's ok
    if (bandera) {
      this._catalogosService.addCatalogo(this.catalogo).subscribe(data => {
        this.operacionCorrecta = true;
        this.mensajeOperacion = 'Catalogo creado correctamente';
        $("#modalResult").modal('show');
      }, error => {
        console.log('Error : ', error);
        this.operacionCorrecta = false;
        this.mensajeOperacion = 'El Catalogo no pudo ser creado';
        $("#modalResult").modal('show');
      });
    } else {
      this.operacionCorrecta = false;
      this.mensajeOperacion = '"Nombre de catalogo ya se encuentra registrado..."';
      $("#modalResult").modal('show');
    }
  }

  getCatalog() {
    this.loading = true;
    let catag: any = {};
    this._catalogosService.getAllCatalogos().subscribe(data => {
      this.catalogos = data;
      //Check Catalog Name
      for (let catag_ of this.catalogos) {
        if (catag_.catalogoDescripcion.localeCompare(this.catalogo.catalogoDescripcion) == 0)
          catag = catag_;
      }
      this.loading = false;
    }, error => {
      this.loading = false;
    });
    return catag;
  }

  cerrarMensaje() {
    if (this.operacionCorrecta) {
      //GetAllCatalogos
      var cat = this.getCatalog();
      this._router.navigate(['/panel/catalogos/catitem', cat.catalogoId]);
      $("#modNewItem").modal('hide');
    }
  }
}
