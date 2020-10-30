import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DEFAULT_INTERRUPTSOURCES, Idle} from '@ng-idle/core';
import {Keepalive} from '@ng-idle/keepalive';

import {AuthService} from '../../services/auth.service';
import {appConfig} from "../../app.config";

declare var $: any;

@Component({
  selector: 'app-panel-control',
  templateUrl: './panel-control.component.html',
  styleUrls: ['./panel-control-style.css']
})
export class PanelControlComponent implements OnInit {

  isCollapsed: boolean = false;

  //Notifications
  options = {
    position: ["top", "right"],
    timeOut: 5000,
    showProgressBar: true,
    pauseOnHover: true,
    clickToClose: true,
    maxLength: 10
  };

  appConfig = appConfig;

  menu: any = [];

  adminMenu = [
    {
      id_: 'usuario',
      menu: 'Usuario',
      link: '',
      items: [
        {
          menuItem: 'Cliente',
          link: '/panel/cliente/listar'
        }, {
          menuItem: 'Conductor',
          link: '/panel/conductor/listar'
        }, {
          menuItem: 'Restablecer',
          link: '/panel/usuario/restablece-user'
        }, {
          menuItem: 'Desbloquear',
          link: '/panel/usuario/desbloquea-user'
        }
      ]
    },
    {
      id_: 'admin',
      menu: 'Administración',
      link: '',
      items: [
        {
          menuItem: 'Empresas',
          link: '/panel/empresa/empresa-listar'
        }, {
          menuItem: 'Catalogos',
          link: '/panel/catalogos/listacatal'
        }, {
          menuItem: 'Localidades',
          link: '/panel/catalogos/listalocal'
        }
      ]
    },
    {
      id_: 'admin_proc',
      menu: 'Procesos',
      link: '',
      items: [
        {
          menuItem: 'Solicitudes',
          link: '/panel/solicitudes/solicitud-admin'
        }, {
          menuItem: 'Ofertas',
          link: '/panel/ofertas/ofertas'
        }
      ]
    }
  ];

  clienteMenu = [
    {
      id_: 'solicitudes_',
      menu: 'Solicitudes',
      link: '',
      items: [
        {
          menuItem: 'Ingresar nuevas',
          link: '/panel/solicitudes/listar'
        }, {
          menuItem: 'Seleccionar ofertas',
          link: '/panel/solicitudes/listar-solicitud-oferta',
        }, {
          menuItem: 'Mis ofertas aprobadas',
          link: '/panel/solicitudes/solicitud-ofertas-aprobadas'
        }, {
          id: 'solicitudesVerestado_',
          menuItem: 'Ver estado',
          link: '',
          items: [
            {
              menuItem: 'Por despachar',
              link: '/panel/solicitudes/despachar'
            }, {
              menuItem: 'Proceso entrega',
              link: '/panel/solicitudes/solicitud-proceso-entrega'
            }, {
              menuItem: 'Entregada',
              link: '/panel/solicitudes/solicitud-entregada'
            }
          ]
        }, {
          menuItem: 'Por pagar',
          link: '/panel/solicitudes/solicitud-pagar'
        }, {
          menuItem: 'Pagadas',
          link: '/panel/solicitudes/solicitud-pagadas'
        }
      ]
    }
  ];

  clienteAdminMenu = [
    {
      id_: 'solicitudes_',
      menu: 'Solicitudes',
      link: '',
      items: [
        {
          menuItem: 'Ingresar nuevas',
          link: '/panel/solicitudes/listar'
        }, {
          menuItem: 'Seleccionar ofertas',
          link: '/panel/solicitudes/listar-solicitud-oferta',
        }, {
          menuItem: 'Mis ofertas aprobadas',
          link: '/panel/solicitudes/solicitud-ofertas-aprobadas'
        }, {
          id: 'solicitudesVerestado_',
          menuItem: 'Ver estado',
          link: '',
          items: [
            {
              menuItem: 'Por despachar',
              link: '/panel/solicitudes/despachar'
            }, {
              menuItem: 'Proceso entrega',
              link: '/panel/solicitudes/solicitud-proceso-entrega'
            }, {
              menuItem: 'Entregada',
              link: '/panel/solicitudes/solicitud-entregada'
            },{
              menuItem: 'Canceladas',
              link: '/panel/solicitudes/canceladas'
            }
          ]
        }, {
          menuItem: 'Calificar transportistas',
          link: '/panel/solicitudes/calificar-transportista'
        }, {
          menuItem: 'Por pagar',
          link: '/panel/solicitudes/solicitud-pagar'
        }, {
          menuItem: 'Pagadas',
          link: '/panel/solicitudes/solicitud-pagadas'
        }
      ]
    },
    {
      id_: 'usersAdmin',
      menu: 'Usuarios',
      link: '',
      items: [
        {
          menuItem: 'Aprobar nuevos',
          link: '/panel/usuario/aprueba-user'
        }
      ]
    },
    {
      id_: 'clientUtils',
      menu: 'API',
      link: '',
      items: [
        {
          menuItem: 'Generar token',
          link: '/panel/apis/token'
        }
      ]
    }
  ];

  clienteReducidoMenu = [
    {
      id_: 'solicitudes_',
      menu: 'Solicitudes',
      link: '',
      items: [
        {
          menuItem: 'Ingresar nuevas',
          link: '/panel/solicitudes/listar'
        }, {
          id: 'clientereduceverestado_',
          menuItem: 'Ver estado',
          link: '',
          items: [
            {
              menuItem: 'Por despachar',
              link: '/panel/solicitudes/despachar'
            }, {
              menuItem: 'Proceso entrega',
              link: '/panel/solicitudes/solicitud-proceso-entrega'
            }, {
              menuItem: 'Entregada',
              link: '/panel/solicitudes/solicitud-entregada'
            }
          ]
        }]
    }
  ];

  conductorMenu = [
    {
      id_: 'oferta_',
      menu: 'Ofertar',
      link: '/panel/ofertas/puja',
      items: []
    }, {
      id_: 'ofertas_',
      menu: 'Ofertas',
      link: '',
      items: [
        {
          menuItem: 'Mis ofertas',
          link: '/panel/ofertas/mis-ofertas'
        }, {
          menuItem: 'Aprobadas',
          link: '/panel/ofertas/oferta-aprobada'
        }, {
          menuItem: 'Por recibir',
          link: '/panel/ofertas/oferta-en-curso'
        }, {
          menuItem: 'Por entregar',
          link: '/panel/ofertas/oferta-entregar'
        }, {
          menuItem: 'Por generar factura',
          link: '/panel/ofertas/oferta-generar-factura'
        }, {
          menuItem: 'Por entregar factura',
          link: '/panel/ofertas/oferta-entregar-factura'
        }, {
          menuItem: 'Por cobrar',
          link: '/panel/ofertas/oferta-factura'
        }, {
          menuItem: 'Finalizadas',
          link: '/panel/ofertas/oferta-finalizada'
        }
      ]
    }
  ];

  conductorAdminMenu = [
    {
      id_: 'oferta_',
      menu: 'Ofertar',
      link: '/panel/ofertas/puja',
      items: []
    }, {
      id_: 'ofertas_',
      menu: 'Ofertas',
      link: '',
      items: [
        {
          menuItem: 'Mis ofertas',
          link: '/panel/ofertas/mis-ofertas'
        }, {
          menuItem: 'Aprobadas',
          link: '/panel/ofertas/oferta-aprobada'
        }, {
          menuItem: 'Por recibir',
          link: '/panel/ofertas/oferta-en-curso'
        }, {
          menuItem: 'Por entregar',
          link: '/panel/ofertas/oferta-entregar'
        }, {
          menuItem: 'Por generar factura',
          link: '/panel/ofertas/oferta-generar-factura'
        }, {
          menuItem: 'Por entregar factura',
          link: '/panel/ofertas/oferta-entregar-factura'
        }, {
          menuItem: 'Por cobrar',
          link: '/panel/ofertas/oferta-factura'
        }, {
          menuItem: 'Finalizadas',
          link: '/panel/ofertas/oferta-finalizada'
        }
      ]
    },/* {
      id_: 'eBilling_',
      menu: 'Facturación electrónica',
      link: '',
      items: [
        {
          menuItem: 'Actualización de datos',
          link: '/panel/ebilling/update-info'
        }, {
          menuItem: 'Facturas generadas',
          link: '/panel/ebilling/list-ebillings'
        }
      ]
    }, */{
      id_: 'userCondAdm_',
      menu: 'Administración',
      link: '',
      items: [
        {
          menuItem: 'Aprobar nuevos usuarios',
          link: '/panel/usuario-conductor/aprobar-conductor'
        },
        {
          menuItem: 'Conductores',
          link: '/panel/usuario-conductor/administrar-conductores'
        },
        {
          menuItem: 'Vehículos',
          link: '/panel/usuario-conductor/administrar-vehiculos'
        },
        {
          menuItem: 'Información bancaria',
          link: '/panel/usuario-conductor/informacion-bancaria'
        }
      ]
    }
  ];

  contadorMenu = [
    {
      id_: 'contacxp_',
      menu: 'Contabilidad CxP',
      link: '',
      items: [
        {
          id: 'registroGastos',
          menuItem: 'Gastos',
          link: '',
          items: [
            {
              menuItem: 'Registro de facturas de gasto',
              link: '/panel/facturas-prov/gastos'
            }, {
              menuItem: 'Facturas de gasto por pagar',
              link: '/panel/facturas-prov/gastos-pagar'
            }, {
              menuItem: 'En tránsito de pago',
              link: '/panel/facturas-prov/gastos-transito-pago'
            }, {
              menuItem: 'Pagadas',
              link: '/panel/facturas-prov/pagadas'
            }
          ]
        },
        {
          menuItem: 'Facturas por recibir',
          link: '/panel/facturas/facturas-por-recibir'
        }, {
          menuItem: 'Pendientes de pago',
          link: '/panel/facturas/facturas-pendiente-pago'
        }, {
          menuItem: 'Listas para el pago',
          link: '/panel/ofertas/ofertas-listas-pago'
        }, {
          menuItem: 'En tránsito de pago',
          link: '/panel/ofertas/ofertas-transito-pago'
        }, {
          menuItem: 'Pagadas',
          link: '/panel/ofertas/ofertas-pagadas'
        }, {
          menuItem: 'Retenciones emitidas',
          link: '/panel/retenciones/retenciones-listar'
        }
      ]
    }, {
      id_: 'contacxc_',
      menu: 'Contabilidad CxC',
      link: '',
      items: [
        {
          menuItem: 'Solicitudes por facturarse',
          link: '/panel/solicitudes/solicitud-facturarse'
        }, {
          menuItem: 'Facturas por cobrar',
          link: '/panel/facturas/facturas-por-cobrar'
        }, {
          menuItem: 'Facturas cobradas',
          link: '/panel/facturas/facturas-cobradas'
        }
      ]
    }, {
      id_: 'contacfacm_',
      menu: 'Facturación Manual',
      link: '',
      items: [
        {
          menuItem: 'Crear factura',
          link: '/panel/facturas/facturas-manuales'
        }, {
          menuItem: 'Facturas generadas',
          link: '/panel/facturas/facturas-manuales-listar'
        }
      ]
    }
  ];

  administradorEnviosMenu = [
    {
      id_: 'admin_envios',
      menu: 'Envios',
      link: '',
      items: [{
          menuItem: 'Cargar',
          link: '/panel/envio/cargar'
        }, {
          menuItem: 'Consultar',
          link: '/panel/envio/consultar'
      }]
    }, {
      id_: 'userCondAdm_',
      menu: 'Administración',
      link: '',
      items: [
        {
          menuItem: 'Aprobar nuevos usuarios',
          link: '/panel/usuario-conductor/aprobar-conductor'
        },
        {
          menuItem: 'Conductores',
          link: '/panel/usuario-conductor/administrar-conductores'
        },
        {
          menuItem: 'Vehículos',
          link: '/panel/usuario-conductor/administrar-vehiculos'
        }
      ]
    }
  ];

  conductorEnviosMenu = [
    {
      id_: 'conductor_envios',
      menu: 'Envios',
      link: '',
      items: [{
        menuItem: 'Envíos pendientes',
        link: '/panel/envio/pendientes'
      }, {
        menuItem: 'Consultar',
        link: '/panel/envio/consultar'
      }
      ]
    }
  ];

  clienteAdminEnviosMenu = [
    /*{
      id_: 'conductor_envios',
      menu: 'Envios',
      link: '',
      items: [{
        menuItem: 'Consultar',
        link: '/panel/envio/consultar'
      }
      ]
    },*/
    {
      id_: 'conductor_pedidos',
      menu: 'Visitas',
      link: '',
      items: [{
        menuItem: 'Visitas',
        link: '/panel/pedido/visita-nueva'
      }, {
        menuItem: 'Pedidos',
        link: '/panel/pedido/all/consultar'
      }, {
        menuItem: 'Clientes',
        link: '/panel/pedido/clientes'
      }, {
        menuItem: 'Productos',
        link: '/panel/pedido/productos'
      }


      ]
    }
  ];

  vendedorClienteMenu = [
    {
      id_: 'vendedor_cliente',
      menu: 'Visitas',
      link: '',
      items: [{
        menuItem: 'Consultar',
        link: '/panel/pedido/consultar'
      }, {
        menuItem: 'Clientes',
        link: '/panel/pedido/clientes'
      }
      ]
    }
  ];

  constructor(private _router: Router, private idle: Idle, private keepalive: Keepalive, private _authService: AuthService) {

    // sets an idle timeout of 5 seconds, for testing purposes.
    idle.setIdle(5);
    // sets a timeout period of 5 seconds. after 10 minutos of inactivity, the user will be considered timed out.
    //60 Minutosts ()
    idle.setTimeout(3595);
    // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
    idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    idle.onIdleEnd.subscribe(() => this.idleState = 'No longer idle.');
    idle.onTimeout.subscribe(() => {
      this.idleState = 'Timed out!';
      this.timedOut = true;

      //
      _authService.logoutSesion();

    });
    idle.onIdleStart.subscribe(() => this.idleState = 'You\'ve gone idle!');
    idle.onTimeoutWarning.subscribe((countdown) => this.idleState = 'You will time out in ' + countdown + ' seconds!');

    // sets the ping interval to 15 seconds
    keepalive.interval(15);

    keepalive.onPing.subscribe(() => this.lastPing = new Date());

    this.reset();

  }

  ngOnInit() {
    this.checkAcceso();
  }

  checkAcceso() {
    if (this._authService.getTypeUser() === appConfig.idUsuarioAdmin)
      this.menu = this.adminMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioCliente)
      this.menu = this.clienteMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioClienteAdmin)
      this.menu = this.clienteAdminMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioClienteReducido)
      this.menu = this.clienteReducidoMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioConductor)
      this.menu = this.conductorMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioConductorAdmin)
      this.menu = this.conductorAdminMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioContador)
      this.menu = this.contadorMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioEnviosAdmin)
      this.menu = this.administradorEnviosMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioEnvios)
      this.menu = this.conductorEnviosMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioClienteAdminEnvios)
      this.menu = this.clienteAdminEnviosMenu;
    else if (this._authService.getTypeUser() === appConfig.idUsuarioVendedorCliente)
      this.menu = this.vendedorClienteMenu;
  }

  idleState = 'Not started.';
  timedOut = false;
  lastPing?: Date = null;


  reset() {
    this.idle.watch();
    this.idleState = 'Started.';
    this.timedOut = false;
  }

  colapsarMenu(){
    this.isCollapsed = !this.isCollapsed;
  }

}
