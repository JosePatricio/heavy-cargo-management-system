import { Body, Card, CardItem, Content } from 'native-base';
import React, { Component } from "react";
import { FlatList, Keyboard, RefreshControl, View } from "react-native";
import { Overlay } from 'react-native-elements';
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { reduxForm } from "redux-form";
import { activateClient } from "../../actions/auth.actions";
import { getAllUserDetailByEmpresaTipoAndStatus, getClienteConductor } from "../../actions/driverVehicle.action";
import { AwesomeAlert, AwesomeButtonRick, Loader, Text } from "../../components/";
import { idEstadoUsuarioCreado, idEstadoUsuarioEliminado, idEstadoUsuarioPendiente, idUsuarioConductor, idUsuarioEnvios, idUsuarioEnviosAdmin } from '../../Constants';
import { defaultTasonColor, style, styleLogin as styles } from '../../styles/';
import { no_connected_info } from '../../utils/tas-on-texts';
import { deviceHeight } from '../../utils/util';

const utf8 = require('utf8');



class AprobacionNuevoConductorScreen extends Component {
  constructor(props) {
    super(props);
    
  const  tipoUsuario = this.props.userReducer.tipoUsuario == idUsuarioEnviosAdmin ? idUsuarioEnvios : idUsuarioConductor;
  const  rucEmpresa=this.props.userReducer.rucEmpresa;
  const  estado=idEstadoUsuarioPendiente;
    
    this.state = {
     currentUser:{},
     showAlert: false,
     textAlert: "",
     titleAlert: "",
     isErrorAlert:false,
     isPostedSuccessfully: false,
     tiposLicencia: [],
     isLoading:false,
     refreshing:false,
     drivers:[],
     modalDriverInfoVisible:false,
     modalDriverFormVisible:false,
     selectedDriver:{},
     aprobacion:false,
     tipoUsuario:tipoUsuario,
     rucEmpresa:rucEmpresa,
     estado:estado
    
     
    }
  }


  componentDidMount() {
 const {tipoUsuario,rucEmpresa,estado}=this.state;
   this.loadData(rucEmpresa,tipoUsuario,estado);
  }
  

  

  getClienteConductor = async (idDocumento) => {
    try {
    
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getClienteConductor(idDocumento));
  
      if (response) {
        this.setState({isLoading:false,selectedDriver:response.responseBody });
        return response.responseBody;
      } else {
        throw response;
      }
    } catch (error) {
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
      });
    }
  };

  loadData = async (rucEmpresa,tipoUsuario,estado) => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getAllUserDetailByEmpresaTipoAndStatus(rucEmpresa,tipoUsuario,estado));

      if (response ) {
        this.setState({isLoading:false,drivers:response.responseBody });

      } else {
        throw response;
      }
    } catch (error) {
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
      });
    }
  };


 
 
  activateClient = async values => {
  
    Keyboard.dismiss();
  this.setState({isLoading:true});
     try {
      const response = await this.props.dispatch(activateClient(values));
   
      if(response.success){

        this.setState({
          showAlert: true,
          textAlert: response.responseBody.responseMessage,
          titleAlert: 'Mensaje',
          isErrorAlert: false,
          isLoading: false,
          modalDriverFormVisible:false,
          isPostedSuccessfully:
            response.responseBody.response == 'ERROR' ? false : true,
            isConfirmation:false
        });

      }
      else {
        throw response;
      }
    
    } catch (error) {
    console.log('ERRRO screen  ',error);
   
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
     
     this.setState({
        showAlert: true,
        textAlert: utf8.decode(errorText),
        titleAlert: "Error",
        isErrorAlert:true
      }); 

    } 
  };


  
  
  onSubmit = isAproved => {
   
    const {selectedDriver}=this.state;
    if(this.props.generalReducer.netWorkInfo.status==false){
      this.setState({
        showAlert: true,
        textAlert: no_connected_info,
        titleAlert: "Error",
        isErrorAlert:true,
        refreshing:false,
        isLoading:false,
      });
        return;
    }


let opcion = isAproved? 'ACEPTAR' : 'RECHAZAR';

console.log('cvvvvvvvvvvv   ',this.state.selectedDriver);
this.setState({
  showAlert: true,
  textAlert: '¿Está seguro en '+opcion+' el registro de '+this.state.selectedDriver.usuarioNombres+' '+this.state.selectedDriver.usuarioApellidos+'?',
  titleAlert: 'Confirmación',
  isErrorAlert: false,
  isConfirmation: true,
  aprobacion: isAproved,
  modalDriverInfoVisible:false
});
 

  };

  
  cancel=()=>{
    Actions.pop();
  }
  onInputSubmit = () => {
   
    };


    confirmationAlert= () => {
      this.setState({
          showAlert: false,
        });
        if (this.state.isConfirmation) {
        console.log('borarrara  ',this.state.aprobacion);

        const payload = {
          usuarioIdDocumento: this.state.selectedDriver.usuarioIdDocumento,
          usuarioEstado: this.state.aprobacion ? idEstadoUsuarioCreado : idEstadoUsuarioEliminado,
          usuarioTipoDocumento: null,
          usuarioNombres: null,
          usuarioApellidos: null,
          usuarioCelular: null,
          usuarioConvecional: null,
          usuarioRuc: null,
          usuarioDireccion: null,
          usuarioLocalidad: null,
          usuarioNombreUsuario: null,
          usuarioContrasenia: null,
          usuarioMail: null,
          usuarioTipoUsuario: null,
          usuarioPrincipal: null
        };

        this.activateClient(JSON.stringify(payload));

        }
    
  }
  

  hideAlert = () => {
    this.setState({
      showAlert: false
    });
   
    if(this.state.isPostedSuccessfully){
   
      const {tipoUsuario,rucEmpresa,estado}=this.state;
   this.loadData(rucEmpresa,tipoUsuario,estado);
  }
};


  dismissAlert= () => {
    if(this.state.showAlert){
  this.setState({
    showAlert: false
  });
}
 // Actions.pop();

};

getDescriptionFromId(list :[], id){
  let res='';

  if(list.length>0){
  list.forEach(item=>{
      if(item.catalogoItemId == id){
          res=item.catalogoItemDescripcion;
      }
  });}
  else{
    return  res;    
  }
return  res;
}



onRefresh=()=>{
  const {tipoUsuario,rucEmpresa,estado}=this.state;
  this.loadData(rucEmpresa,tipoUsuario,estado);
}

bindDriversFlatList(data) {

  const { driverVehicleReducer,handleSubmit } = this.props;
  const { tiposLicencia } = this.state;
  

const {isLoading}= this.state;
  if(data.length==0){
    const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
    return (  
    <View style={[{marginBottom:20}]}>
    <FlatList
      data={emptyData}
      renderItem={({item}) => (
        <View style={style.cardStyle}>
      {(!isLoading && !driverVehicleReducer.getDriversReducer.isFetching) && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
      </View>
      )
    }
    keyExtractor={ (item, index) => String(index) }
      refreshControl={
        <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
      }
      onEndReachedThreshold={(0, 4)}
    />
  </View>

    );
  }else{
  return (
    <View style={{paddingBottom:70}}>
      <FlatList
        data={data}
        renderItem={({item}) => (
          <Card collapsable={true} style={({elevation: 4}, style.cardStyle)}>
            <CardItem
              button
              style={style.noPadding}
              disabled={this.state.isButtonDisabled}
              onPress={() => this.openModalInfoDriver(item) }>
              <Body style={style.noPadding}>
               
              <View style={style.viewHeader}>
              <Text style={style.viewHeaderText}>{item.idDocumento}</Text>
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Nombre</Text>
                  <Text style={style.textData}>
                  {item.nombres}
                  </Text>
                </View>

                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Apellidos</Text>
                  <Text style={style.textData}>
                {item.apellidos}
                  </Text>
                </View>
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Usuario</Text>
                  <Text style={style.textData}>
                  {item.usuario} 
                  </Text>
                </View>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Estado</Text>
                  <Text style={style.textData}>
                  {item.estado} 
                  </Text>
                </View>
              </View>
              </Body>
            </CardItem>
          </Card>
        )}
        keyExtractor={ ( item, index ) => `${index}` }
        refreshControl={
          <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
        }
        onEndReachedThreshold={(0, 4)}
      />
    </View>
  );
}}


renderInfoDriver(){
  const { handleSubmit } = this.props;
  const {tiposLicencia,selectedDriver}=this.state;
  return(
    <View style={[style.cardStyle,{flex: 1,flexDirection: 'column', justifyContent: 'space-between'}]}>

     <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
          Datos del conductor
     </Text>
       
         <View style={{ width: '100%',paddingBottom:10}}>
            <Text style={style.textInfoDisabled}>Cédula</Text>
            <Text style={style.textDataDisabled}>{selectedDriver.usuarioIdDocumento}</Text>
          </View>

          <View style={{ width: '100%',paddingBottom:10}}>
            <Text style={style.textInfoDisabled}>Nombres</Text>
            <Text style={style.textDataDisabled}>{selectedDriver.usuarioNombres} {selectedDriver.usuarioApellidos}</Text>
          </View>

          <View style={{ width: '100%',paddingBottom:10}}>
            <Text style={style.textInfoDisabled}>Nombre usuario</Text>
            <Text style={style.textDataDisabled}>{selectedDriver.usuarioNombreUsuario}</Text>
          </View>

          <View style={{ width: '100%',paddingBottom:10}}>
            <Text style={style.textInfoDisabled}>Correo electrónico</Text>
            <Text style={style.textDataDisabled}>{selectedDriver.usuarioMail}</Text>
          </View>

          <View style={{ width: '100%',paddingBottom:10}}>
            <Text style={style.textInfoDisabled}>Teléfono celular</Text>
            <Text style={style.textDataDisabled}>{selectedDriver.usuarioCelular}</Text>
          </View>
        
       
<View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={{maxWidth: '30%',marginRight:10}} onPress={()=>this.setState({modalDriverInfoVisible:false})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={{maxWidth: '30%',marginRight:10}} onPress={()=>this.onSubmit(true)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Aprobar</Text>
             </AwesomeButtonRick>
             <AwesomeButtonRick height={50} stretch={true} style={{maxWidth: '30%',marginRight:10}} onPress={()=>this.onSubmit(false)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Rechazar</Text>
             </AwesomeButtonRick>
          </View>

    </View>
  );
}




openModalInfoDriver=(item)=>{
this.getClienteConductor(item.idDocumento).then(res=>{
  this.setState({ modalDriverInfoVisible: true,selectedDriver:res });
});




}
render() {
    const { driverVehicleReducer,handleSubmit } = this.props;
    const {isConfirmation,modalDriverInfoVisible,modalDriverFormVisible, drivers,showAlert,textAlert,titleAlert,isErrorAlert,isLoading } = this.state;
 
    return (
      <View style={styles.container}>
        {isLoading && <Loader />}

     
     {this.bindDriversFlatList(drivers)} 

     {showAlert &&<AwesomeAlert
          show={showAlert}
          showProgress={false}
          title={titleAlert}
          message={textAlert}
          closeOnTouchOutside={!this.state.isPostedSuccessfully}
          closeOnHardwareBackPress={false}
          showCancelButton={isConfirmation}
          showConfirmButton={true}
          confirmText={isConfirmation ? 'Aceptar' : 'Ok'}
          cancelText="Cerrar"
          contentContainerStyle={style.alertContentContainerStyle}
          confirmButtonStyle={style.alertConfirmButtonStyle}
          confirmButtonTextStyle={
            isErrorAlert
              ? style.alertConfirmButtonErrorTextStyle
              : style.alertConfirmButtonTextStyle
          }
          cancelButtonStyle={style.alertConfirmButtonStyle}
          cancelButtonTextStyle={
            isErrorAlert
              ? style.alertConfirmButtonErrorTextStyle
              : style.alertConfirmButtonTextStyle
          }
          titleStyle={style.alertTitleStyle}
          onConfirmPressed={() => {
            isConfirmation ? this.confirmationAlert() : this.hideAlert();
          }}
          onDismiss={() => {
            this.dismissAlert();
          }}
          onCancelPressed={() => this.hideAlert()}
        />}


<Overlay width={'95%'} height={deviceHeight/1.90}
  isVisible={modalDriverInfoVisible} 
  onBackdropPress={() => this.setState({ modalDriverInfoVisible: false })}
><Content>
  {this.renderInfoDriver()}
  </Content>
</Overlay>




       
      </View>
    );
  }
}




const mapStateToProps = state => {
  return {
    driverVehicleReducer: state.driverVehicleReducer,
    generalReducer: state.generalReducer,
    authReducer:state.authReducer,
    userReducer:state.userReducer.getUser.userDetails
  };
};

const mapDispatchToProps = dispatch => {
  return {
      dispatch
  };
};

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  reduxForm({
    form: "AprobacionNuevoConductorForm",
    enableReinitialize: true
  })
)(AprobacionNuevoConductorScreen);
