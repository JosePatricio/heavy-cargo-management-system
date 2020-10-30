import {Component, ComponentFactoryResolver, OnInit, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {EmpresaService} from "../../../services/empresa.service";
import {Router} from "@angular/router";
import {DatatableComponent} from "@swimlane/ngx-datatable";
import {EmpresasNuevoComponent} from "../empresas-nuevo/empresas-nuevo.component";
import {CatalogoItemService} from "../../../services/catalogo-item.service";
import {appConfig} from "../../../app.config";

@Component({
  selector: 'app-empresas-listar',
  templateUrl: './empresas-listar.component.html'
})
export class EmpresasListarComponent implements OnInit {

  public loading = false;

  idTypeEmpresa: any;
  listTypeEmpresa: any;

  //Para dataTable
  @ViewChild(DatatableComponent) table: DatatableComponent;
  @ViewChild('tmplIndex') tmplIndex: TemplateRef<any>;
  @ViewChild('linkButton') linkButton: TemplateRef<any>;
  empresas: any;
  columns = [];

  isListEmp: boolean = true;
  typeEmpresa: any;
  titleEmp: string = 'Empresas';

  @ViewChild('detailDynamic', {read: ViewContainerRef}) detailDynamic: ViewContainerRef;

  constructor(public _empresaService: EmpresaService, public _catalogoItemService: CatalogoItemService, private _router: Router, private viewContainerRef: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit() {

    this.columns = [
      {prop: 'localidad', name: '#', cellTemplate: this.tmplIndex, headerClass: 'table-header', width: 85},
      {prop: 'localidad', name: 'Info.', cellTemplate: this.linkButton, headerClass: 'table-header', width: 95},
      {prop: 'ruc', name: 'RUC', headerClass: 'table-header'},
      {prop: 'razonSocial', name: 'Raón social', headerClass: 'table-header'},
      {prop: 'companyType', name: 'Tipo', headerClass: 'table-header'}
    ];

    this.loadCatalogItem(appConfig.idCatalogoTipoEmpresa);
  }

  checkTypeEmp(typeEmpresa) {
    this.typeEmpresa = typeEmpresa;
    if (this.typeEmpresa === 1)
      this.titleEmp = 'Cliente / Transportista';
    else if (this.typeEmpresa === 2) {
      this.titleEmp = 'Proveedor';
      this.idTypeEmpresa = appConfig.idEmpresaProveedor;
    }

    if (this.typeEmpresa && this.idTypeEmpresa)
      this.loadData(this.idTypeEmpresa);
    else {
      this.empresas = null;
      this.idTypeEmpresa = null;
    }
  }

  loadCatalogItem(idCatalog) {
    this.loading = true;
    this._catalogoItemService.getCatalogoItemByCatalogo(idCatalog).subscribe(
      data => {
        this.listTypeEmpresa = data.filter(catItem => catItem.catalogoItemDescripcion !== 'EMPRESA ADMINISTRADORA');
        this.listTypeEmpresa = this.listTypeEmpresa.filter(catItem => catItem.catalogoItemDescripcion !== 'EMPRESA PROVEEDOR');
        this.loading = false;
      }, error => {
        this.loading = false;
      });
  }

  loadData(typEmp) {
    this.loading = true;
    this._empresaService.getAllEmpresaByType(typEmp).subscribe(
      data => {
        this.empresas = data;
        this.loading = false;
      }, error => {
        this.loading = false;
      });
  }

  callAddNew() {
    this.isListEmp = false;
    this.resolveContent('new', 1);
  }

  callEdit(ruc: string) {
    //this._router.navigate(['/panel/empresa/empresa/edit', ruc]);
    this.isListEmp = false;
    this.resolveContent('edit', ruc);
  }

  callDelete(ruc: string) {
    //this._router.navigate(['/panel/empresa/empresa/delete', ruc]);
    this.isListEmp = false;
    this.resolveContent('delete', ruc);
  }

  resolveContent(tipo: any, ruc: any) {
    this.loading = true;
    const componentFactory = this.componentFactoryResolver.resolveComponentFactory(EmpresasNuevoComponent);
    if (this.detailDynamic) {
      this.detailDynamic.clear();
      const componentDynamic = <EmpresasNuevoComponent>this.detailDynamic.createComponent(componentFactory).instance;
      componentDynamic.tipo = tipo;
      componentDynamic.ruc = ruc;
      componentDynamic.tipoEmp = this.idTypeEmpresa;
      componentDynamic.complete.subscribe(complete => {
        this.returnList(complete);
      });
    }
    this.loading = false;
  }

  returnList(isListEmp) {
    this.isListEmp = isListEmp;
    if (this.typeEmpresa && this.idTypeEmpresa)
      this.loadData(this.idTypeEmpresa);
  }
}
