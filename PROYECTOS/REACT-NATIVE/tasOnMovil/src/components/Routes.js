import React, { Component } from "react";
import { Router, Scene } from "react-native-router-flux";
import { Platform } from "react-native";
import Login from "../screens/Login";

import SolicitudesScreen from "../screens/Solicitudes/SolicitudesScreen";
import SolicitudOfertaScreen from "../screens/Solicitudes/SolicitudOfertaScreen";
import MiCuentaScreen from "../screens/MiCuentaScreen";
import FacturacionScreen from "../screens/FacturacionScreen";
import InicioScreen from "../screens/InicioScreen";

import MisFletesScreen from "../screens/MisFletes/MisFletesScreen";
import EnEsperaScreen from "../screens/MisFletes/EnEsperaScreen";
import AprobadasScreen from "../screens/MisFletes/AprobadasScreen";
import PorRecibirScreen from "../screens/MisFletes/PorRecibirScreen";
import PorEntregarScreen from "../screens/MisFletes/PorEntregarScreen";
import PorGenerarFacturaScreen from "../screens/MisFletes/PorGenerarFacturaScreen";
import PorEntregarFacturaScreen from "../screens/MisFletes/PorEntregarFacturaScreen";
import PorCobrarScreen from "../screens/MisFletes/PorCobrarScreen";
import PorFinalizarScreen from "../screens/MisFletes/PorFinalizarScreen";
import RecuperarClaveScreen from "../screens/RecuperarClaveScreen";
import CambioDeClaveScreen from "../screens/CambioDeClaveScreen";
import EnrolamientoScreen from "../screens/Enrolamiento/EnrolamientoScreen";
import MenuAdministacionScreen from "../screens/UsuarioConductor/MenuAdministacionScreen";
import AdministracionConductoresScreen from "../screens/UsuarioConductor/AdministracionConductoresScreen";
import AdministracionVehiculosScreen from "../screens/UsuarioConductor/AdministracionVehiculosScreen";
import AprobacionNuevoConductorScreen from "../screens/UsuarioConductor/AprobacionNuevoConductorScreen";
import InformacionBancariaScreen from "../screens/UsuarioConductor/InformacionBancariaScreen";

import NuevaCompaniaTransporte from "../screens/Enrolamiento/NuevaCompaniaTransporte/NuevaCompaniaTransporte";
import NuevaEmpresa from "../screens/Enrolamiento/NuevaEmpresa/NuevaEmpresa";
import NuevoConductor from "../screens/Enrolamiento/NuevoConductorLogin/NuevoConductor";



import NavBar from "../components/NavBar";
import DrawerContent from "../components/DrawerContent";
import { Icono } from '../components/Icon';

import { Icon } from 'native-base';
import {ESTADO_USUARIO_ACTIVO,ESTADO_USUARIO_CREADO,idUsuarioConductorAdmin} from '../Constants'



export default class Routes extends Component {
  render() {
    const prefix = Platform.OS === "android" ? "mychat://mychat/" : "mychat://";
    const TabIcon = ({ focused, title, customIcon,iconReference }) => {
      return  <Icon type={iconReference} android={customIcon} name={customIcon} style={{ color: focused ? "#0E4EF8" : "#17233D" }}/>;
    };
    
    let logOutInitial=false,loggedInitial=false,createdUserInitial=false;

    if(!this.props.isLoggedIn && !this.props.userStatus ) {
      logOutInitial=true,loggedInitial=false,createdUserInitial=false;
    }
    if(this.props.isLoggedIn && this.props.userStatus == ESTADO_USUARIO_CREADO) {
      logOutInitial=false,loggedInitial=false,createdUserInitial=true;
    }
    if(this.props.isLoggedIn && this.props.userStatus == ESTADO_USUARIO_ACTIVO) {
      logOutInitial=false,loggedInitial=true,createdUserInitial=false;
    }

    return (

      <Router>
        <Scene>
          <Scene key="root" hideNavBar={true} initial={!this.props.isLoggedIn && !this.props.userStatus}>
            <Scene key="login" component={Login} initial={true} />
            <Scene key="RecuperarClave" childTitle='Recuperar contraseña' component={RecuperarClaveScreen} initial={false} />
            <Scene key="Enrolamiento" childTitle='Registro' component={EnrolamientoScreen} initial={false} />
        
            <Scene key="NuevaCompaniaTransporte" childTitle='Nueva compañía de transporte' component={NuevaCompaniaTransporte} initial={false} />
            <Scene key="NuevaEmpresa" childTitle='Nueva empresa' component={NuevaEmpresa} initial={false} />
            <Scene key="NuevoConductor" childTitle='Nuevo conductor' component={NuevoConductor} initial={false} />
        
          </Scene>

          <Scene
            sceneStyle={styles.scene}
            uriPrefix={prefix}
            navBar={NavBar}
            key="app"
            hideNavBar={true}
            initial={this.props.isLoggedIn}
           // drawer
         //   contentComponent={DrawerContent}
            drawerWidth={250}
            hideDrawerButton={true}>

           <Scene key="CambiarClave" childTitle='Cambio de contraseña' component={CambioDeClaveScreen} initial={this.props.userStatus == ESTADO_USUARIO_CREADO} />
       

           <Scene
              key="tabbar"
              tabs={true}
              initial={this.props.userStatus == ESTADO_USUARIO_ACTIVO}  
              tabBarStyle={{ backgroundColor: "#FFFFFF" }}>

                  <Scene key="um1" initial={false} title="Mis Fletes" icon={TabIcon} customIcon='magnifying-glass' iconReference='Entypo'>
                    <Scene key="MisSolicitudes" childTitle='Mis fletes' initial={true}  component={MisFletesScreen} />
                    <Scene key="EnEspera"  childTitle='Solicitud en espera' initial={false} component={EnEsperaScreen} />
                    <Scene key="Aprobadas" childTitle='Solicitud aprobado' initial={false} component={AprobadasScreen} />
                    <Scene key="PorRecibir" childTitle='Detalle' initial={false} component={PorRecibirScreen} />
                    <Scene key="PorEntregar" childTitle='Detalle' initial={false} component={PorEntregarScreen} />
                    <Scene key="PorGenerarFactura" childTitle='Por Generar Factura' initial={false} component={PorGenerarFacturaScreen} />
                    <Scene key="PorEntregarFactura" childTitle='Por Entregar Factura' component={PorEntregarFacturaScreen} />
                    <Scene key="PorCobrar" childTitle='Por Cobrar' component={PorCobrarScreen} />
                    <Scene key="PorFinalizar" childTitle='Por Finalizar' component={PorFinalizarScreen} />
                  </Scene>


                  <Scene key="um" initial={true} title="Solicitudes" icon={TabIcon}  customIcon='truck' iconReference='MaterialCommunityIcons'>
                    <Scene key="Solicitudes" childTitle='Solicitudes' initial={true} component={SolicitudesScreen} />
                    <Scene key="SolicitudOferta" childTitle='Mi oferta' initial={false} component={SolicitudOfertaScreen} />
                  </Scene>
              
       
        {(String(this.props.userRole) == idUsuarioConductorAdmin)&&
                  <Scene key="Administración" initial={false}  icon={TabIcon} customIcon='clipboard-text' iconReference='MaterialCommunityIcons'>
                    <Scene key="menuAdminConducores" childTitle='Administración' initial={true} component={MenuAdministacionScreen} />
                    <Scene key="adminConductores" childTitle='Administración de conductores' initial={false} component={AdministracionConductoresScreen} />
                    <Scene key="adminVehiculos" childTitle='Administración de vehículos' initial={false} component={AdministracionVehiculosScreen} />
                    <Scene key="adminAprobacionConductores" childTitle='Aprobación de nuevos conductores' initial={false} component={AprobacionNuevoConductorScreen} />
                    <Scene key="adminInformacionBancaria" childTitle='Información bancaria' initial={false} component={InformacionBancariaScreen} />
                  </Scene>
                }
            </Scene>
         
          </Scene>
        
        
        </Scene>

      </Router>
    );
  }
}

const styles = {
  barButtonIconStyle: {
    tintColor: "white"
  },
  scene: {
    backgroundColor: "#F5FCFF",
    shadowOpacity: 1,
    shadowRadius: 3
  }
};
