import { Formik } from 'formik';
import { Card, Container, Content, Icon } from 'native-base';
import React, { Component } from 'react';
import { CheckBox, Modal, View } from "react-native";
import { Actions } from 'react-native-router-flux';
import { reduxForm } from 'redux-form';
import * as Yup from 'yup';
import { obtenerCatalogoItem } from "../../../actions/general.action";
import { createEmpresaCliente, emailUsuarioValidator, getLocalidadByPadre, numdocUsuarioValidator, userNameUsuarioValidator } from "../../../actions/public.actions";
import { AwesomeAlert, AwesomeButtonRick, CheckBoxField, DropDownList, HTML, KeyboardAwareScrollView, Loader, MaterialInput, RadioButon, Text } from "../../../components";
import { idCatalogoCamion, idCatalogoCarga, idCatalogoDocumento, idCatalogoLicencia, idCatalogoPesos, idLocalidadPadreEcuador, idPersonaJuridica, idTipoClienteEmpresaTransportista, idUsuarioConductorAdmin, ID_RUC } from '../../../Constants';
import { defaultTasonColor, style } from '../../../styles';
import { no_connected_info, TERMINOS_CONDICIONES_ENROLAMIENTO } from '../../../utils/tas-on-texts';
import { connect } from "react-redux";

class NuevaCompaniaTransporte extends Component {
  constructor(props) {
    super(props);


    this.state = {
        clienteRazonSocial: '',
        existeEmpresa: false,
        isLoading: false,
        showAlert: false,
        textAlert: '',
        titleAlert: '',
        isErrorAlert: false,
        isPostedSuccessfully: false,
        isConfirmation:false,
        tiposLicencia: [],
        tiposCuentaBanco: [],
        tiposUnidadCapacidad: [],
        tiposCarga: [],
        tiposCamion: [],
        bancos: [],
        provincias: [],
        ciudades: [],
        ciudades2: [],
        tiposDocumento: [],
      
        isEmailUsuarioValid: true,
        isUserNameUsuarioValid: true,
        isNumdocUsuarioValid: true,
        isNumdocEmpresaValid: true,
        
        dniRequiredLength: 10,
        showForm1: true,
        showForm2: false,
        cliente:{},
        contacto:{},
        empresa:{},
        informacionBancaria:{},
        showModal:false,

        tipoPersona:'',
        provincia:'',
        ciudad:'',
        selectedProvinciaId:0,
        periodo:'',
        tipoproducto:'',
        reconocimiento:'',
        tipodocumento:'',
        showTermCondModal:false,
        vehiculos:[],
        vehiculoSeleccionado:{},
        isTermCondChecked:true,
        showTermCondError:false,
        selected:14
    };
    this.clienteRazonSocial = React.createRef();
    this.clienteProvinciaId = React.createRef();
    this.clienteDireccion = React.createRef();

    

    
   
    this.usuarioTipoDocumento= React.createRef();
    this.usuarioIdDocumento= React.createRef();
    this.usuarioNombres= React.createRef();
    this.usuarioApellidos= React.createRef();
    this.usuarioNombreUsuario= React.createRef();
    this.usuarioMail= React.createRef();
    this.usuarioProvinciaId= React.createRef();
    this.usuarioLocalidad= React.createRef();
    this.usuarioCelular= React.createRef();
  }

  componentDidMount() {
   this.loadDefaultData();
 }




  mostrarCiudades = async (idProvincia,form) => {
    try {
      this.setState({isLoading: true,selectedProvinciaId:idProvincia});

      const response = await this.props.dispatch(
        getLocalidadByPadre(idProvincia, 1),
      );

      if (response) {
        if(form === 'form1'){
        this.setState({isLoading: false, ciudades: response.responseBody});
      }
      if(form === 'form2'){
        this.setState({isLoading: false, ciudades2: response.responseBody});
      }
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


  loadDefaultData = async () => {
    try {
      this.setState({isLoading: true});

      const response = await this.props.dispatch(
        getLocalidadByPadre(idLocalidadPadreEcuador, 1),
      );
      const response1 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoLicencia),
      );
     
      const response4 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoPesos),
      );

      const response5 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoCarga),
      );
      const response6 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoCamion),
      );
      const response7 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoDocumento),
      );
   
    
      if (
        response &&
        response1 &&
        response4 &&
        response5 &&
        response6 &&
        response7
      ) {
        this.setState({
          isLoading: false,
          provincias: response.responseBody,
          tiposLicencia: response1.responseBody,
          tiposUnidadCapacidad: response4.responseBody,
          tiposCarga: response5.responseBody,
          tiposCamion: response6.responseBody,
          tiposDocumento: response7.responseBody,
        });

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
        isLoading: false,
      });
    }
  };

  
  numdocUsuarioValidator = async (ruc, tipo) => {
    try {
    
      const response = await this.props.dispatch(numdocUsuarioValidator(ruc));

    
  if(tipo === 'Empresa'){
  this.setState({isNumdocEmpresaValid: response.valid});
  }

  if(tipo === 'Cliente'){
      this.setState({isNumdocUsuarioValid: response.valid});
    }
     
      return response.valid;
    } catch (error) {
      console.log('ERRO VALIDACTION  ',error)
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText=JSON.stringify(errorText)
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
      });
    }
  };

  onEndEditingUserDni = value => {
    this.numdocUsuarioValidator(value,'Cliente');
  };

  onEndEditingEmpresaRuc = value => {
    this.numdocUsuarioValidator(value,'Empresa');
  };


  emailUsuarioValidator = async email => {
    
    try {
    
      const response = await this.props.dispatch(emailUsuarioValidator(email));
      this.setState({ isEmailUsuarioValid: response.valid});
    } catch (error) {
      console.log('error screen  ', error);

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

  userNameUsuarioValidator = async username => {
    try {
      const response = await this.props.dispatch(userNameUsuarioValidator(username));
      this.setState({isUserNameUsuarioValid: response.valid});
      return response.valid;
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

  
  createEmpresaCliente = async json => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(createEmpresaCliente(json));
     if (response) {

      this.setState({
        showAlert: true,
        textAlert: response.responseBody.responseMessage,
        titleAlert: 'Mensaje',
        isErrorAlert: false,
        isLoading: false,
        isPostedSuccessfully:
          response.responseBody.response == 'ERROR' ? false : true,
      });

      } else {
        throw response;
      }
    } catch (error) {
        console.log('ERROR SCEREN createEmpresaCliente ',error);
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText=JSON.stringify(error);
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
        isLoading: false,showModal:false
      });
    }
  };

  siguiente = values => {
    

    console.log('           PRIMER FORM                   ');
    console.log('values    ',values);
    console.log('                                         ');
  
      
    let payload = {clienteRuc:values.clienteRuc,
        clienteRazonSocial:values.clienteRazonSocial,
        clienteProvinciaId:String(values.clienteProvinciaId),  
        clienteLocalidadId:String(values.clienteLocalidadId),  
        clienteDireccion: values.clienteDireccion};
        
    this.setState({showForm1: false, showForm2: true, empresa:payload});
   
    console.log('empresa    ',this.state.empresa);
  };

 

onSubmit =  values => {
 
  const {empresa,cliente,contacto,vehiculos,isTermCondChecked,showTermCondError}=this.state;
    
  if(!isTermCondChecked){
this.setState({showTermCondError:true});
return;
  }

  if(this.props.props.generalReducer.netWorkInfo.status==false){
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



    console.log('           SEGUNDO FORM                   ');
    console.log('values    ',values);
    console.log('                                         ');
    

        let payloadCliente = {
            usuarioTipoDocumento: String(values.usuarioTipoDocumento),
            usuarioIdDocumento:values.usuarioIdDocumento,  
            usuarioNombres: values.usuarioNombres,
            usuarioApellidos: values.usuarioApellidos,
            usuarioNombreUsuario: values.usuarioNombreUsuario,
            usuarioMail: values.usuarioMail};

            
        let payload1 = {
          usuarioProvinciaId: values.usuarioProvinciaId,
          usuarioLocalidad: values.usuarioLocalidad,
            usuarioCelular: values.usuarioCelular};


    this.setState({cliente:payloadCliente,contacto:payload1});



 
  let payload={
      cliente: {
        usuarioTipoDocumento: this.state.selected,
        usuarioIdDocumento: this.state.cliente.usuarioIdDocumento,
        usuarioNombres: this.state.cliente.usuarioNombres,
        usuarioApellidos: this.state.cliente.usuarioApellidos,
        usuarioNombreUsuario: this.state.cliente.usuarioNombreUsuario,
        usuarioMail: this.state.cliente.usuarioMail,
        usuarioLocalidad: this.state.contacto.usuarioLocalidad,
        usuarioCelular: this.state.contacto.usuarioCelular,
        usuarioEstado: 5,
        usuarioPrincipal: true,
        usuarioRuc: this.state.empresa.usuarioRuc,
        usuarioTipoUsuario: idUsuarioConductorAdmin,
        conductorsByUsuarioIdDocumento: []
        
      },
      empresa: {
        clienteRuc: this.state.empresa.clienteRuc,
        clienteRazonSocial: this.state.empresa.clienteRazonSocial,
        clienteLocalidadId: this.state.empresa.clienteLocalidadId,
        clienteDireccion: this.state.empresa.clienteDireccion,
        clienteTipoCliente: String(idTipoClienteEmpresaTransportista),
        clienteTipoId: idPersonaJuridica,
        clienteComision: 0,
        clienteDiasCredito: 0,
        clienteDiasPeriodicidad: 0
      }
  };

  
   this.createEmpresaCliente(JSON.stringify(payload));
  
  
};


  anteriorForm2 = (values) => {
  
   let payloadCliente = {
    usuarioTipoDocumento: String(this.state.selected),
    usuarioIdDocumento:values.usuarioIdDocumento.props.value,  
    usuarioNombres: values.usuarioNombres.props.value,
    usuarioApellidos: values.usuarioApellidos.props.value,
    usuarioNombreUsuario: values.usuarioNombreUsuario.props.value,
    usuarioMail: values.usuarioMail.props.value};

    
let payload1 = {
  usuarioProvinciaId: values.usuarioProvinciaId.props.selectedValue,
  usuarioLocalidad: values.usuarioLocalidad.props.selectedValue,
    usuarioCelular: values.usuarioCelular.props.value};


    
     
this.setState({showForm1: true, showForm2: false,cliente:payloadCliente,contacto:payload1});


  };

  
  
  onProvinceValueChange = (value,form) => {
    this.mostrarCiudades(value,form);
  };


  
  
 
onCancelAll() {
  Actions.reset('root');

}


  state = {
    selected: 'null',
  };

  onPress(selected: string) {
    this.setState({
      selected,
      dniRequiredLength: ID_RUC == selected ? 13 : 10,
    });
  }


  openTermCondModal=()=>{
this.setState({showTermCondModal:true});
  }
  getDescriptionFromId(list :[], id){
    let res='';
    list.forEach(item=>{
        if(item.catalogoItemId == id){
            res=item.catalogoItemDescripcion;
        }
    });
  return  res;
  }
 
  hideAlert = () => {
    this.setState({
      showAlert: false,
    });

    if (this.state.isPostedSuccessfully) {
      Actions.reset('root');
    }
  };

  confirmationAlert= () => {
    this.setState({
        showAlert: false,
      });
}

  dismissAlert = () => {
    if (this.state.showAlert) {
      this.setState({
        showAlert: false,
      });
    }
     
  };

  closeModal=()=>{
      this.setState({showModal:false});
  }

  hideTermCondModal=()=>{
    this.setState({showTermCondModal:false});
  }

   
    vehiculoDeleteConfirmation=(item)=>{
        this.setState({
            showAlert: true,
            textAlert: '¿Está seguro de eliminar este vehículo?',
            titleAlert: 'Confirmación',
            isErrorAlert: false,
            isConfirmation: true,
            vehiculoSeleccionado: item
          });
    }

    openVehiculoInfo=(item)=>{
    const {tiposUnidadCapacidad,tiposCarga,tiposCamion} = this.state;
    const unidadCapacidad_= this.getDescriptionFromId(tiposUnidadCapacidad,item.vehiculoTipoCapacidad);
    const tipoCarga_= this.getDescriptionFromId(tiposCarga,item.vehiculoTipoCarga);
    const tipoCamion_= this.getDescriptionFromId(tiposCamion,item.vehiculoTipoCamion);
    this.setState({
        unidadCapacidad:unidadCapacidad_,tipoCarga:tipoCarga_,tipoCamion:tipoCamion_,
        vehiculoSeleccionado:item});
    }

 
    

  renderFormDatosEmpresa() {
    const {handleSubmit} = this.props;
    const {
        tiposUnidadCapacidad,
        tiposCarga,
        tiposCamion,
        provincias,
        ciudades,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,isNumdocEmpresaValid,empresa
    } = this.state;

    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
          
          <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Datos de la empresa
            </Text>
          <KeyboardAwareScrollView keyboardShouldPersistTaps="always"
            contentContainerStyle={style.container}>
            <Formik
              initialValues={{
                clienteRuc: empresa.clienteRuc,
                clienteRazonSocial: empresa.clienteRazonSocial,
                clienteProvinciaId: empresa.clienteProvinciaId,
                clienteLocalidadId: empresa.clienteLocalidadId,
                clienteDireccion: empresa.clienteDireccion
              }}
             validationSchema={ Yup.object().shape({
                clienteRuc: Yup.string().required('Campo requerido').
                min(13,'Mínimo  de caracteres 13').matches(/^[0-9\b]+$/,'Ingrese solo número').
                test("isValidEmpresaDni","El número de documento ingresado ya se encuentra registrado",
                  value => {
                   return isNumdocEmpresaValid
                  }
                ),
                clienteRazonSocial: Yup.string().required('Campo requerido'),
                clienteProvinciaId:Yup.string().required('Campo requerido').nullable(true),
                clienteLocalidadId: Yup.string().required('Campo requerido').nullable(true),
                clienteDireccion: Yup.string().required('Campo requerido'), 
              })
              } 

              onSubmit={(values, {resetForm,setSubmitting}) => {
                this.siguiente(values);
              }}>
              {({
                handleSubmit,
                handleBlur,
                handleChange,
                errors,
                values,
                touched,
                isSubmitting,
                isValid,
                setFieldValue,
                setValues
              }) => (
                <View>
                  <MaterialInput
                    name="clienteRuc"
                    autoFocus
                    label='Número de documento (RUC)'
                    value={values.clienteRuc}
                    keyboardType={'numeric'}
                    maxLength={13}
                    placeholder="Número de documento"
                    error={ errors.clienteRuc && touched.clienteRuc?errors.clienteRuc:undefined }
                    onSubmitEditing={() => {
                      this.clienteRazonSocial.focus();
                    }}
                    onBlur={handleBlur('clienteRuc')}
                    onChangeText={ handleChange('clienteRuc')}
                    onEndEditing={(evt)=>{
                       this.numdocUsuarioValidator(evt.nativeEvent.text,'Empresa').then(res=>{
                      
                      }); 
                    }}
                  />
              
                  <MaterialInput
                    label='Razón social'
                    name="clienteRazonSocial"
                    autoCapitalize={'words'}
                    value={values.clienteRazonSocial}
                    placeholder="Razón social"
                    error={!values.clienteRazonSocial && errors.clienteRazonSocial && touched.clienteRazonSocial?errors.clienteRazonSocial:undefined }
                    refField={input => {
                      this.clienteRazonSocial = input;
                    }}   
                    onBlur={handleBlur('clienteRazonSocial')}
                    onChangeText={handleChange('clienteRazonSocial')}
                  />
              
              <DropDownList name="clienteProvinciaId"
              data={provincias}
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              placeholder=""
              error={ errors.clienteProvinciaId && touched.clienteProvinciaId?errors.clienteProvinciaId:undefined }
              selectedValue={empresa.clienteProvinciaId}
              description={'Seleccione la provincia'}
              refField={input => {
                this.clienteProvinciaId = input;
              }}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('clienteProvinciaId',itemValue);
               this.onProvinceValueChange(itemValue,'form1') ;
               }
            }/> 

              <DropDownList  name="clienteLocalidadId"
              data={ciudades}
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              selectedValue={empresa.clienteLocalidadId}
              error={ errors.clienteLocalidadId && touched.clienteLocalidadId?errors.clienteLocalidadId:undefined }
              placeholder=""
              description={'Seleccione la ciudad'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('clienteLocalidadId',itemValue);
              }
            }/> 

            <MaterialInput
                    label='Dirección'
                    name="clienteDireccion"
                    value={values.clienteDireccion}
                    placeholder="Dirección"
                    autoCapitalize={'words'}
                    error={!values.clienteDireccion && errors.clienteDireccion && touched.clienteDireccion?errors.clienteDireccion:undefined }
                    refField={input => {
                      this.clienteDireccion = input;
                    }}   
                    onBlur={handleBlur('clienteDireccion')}
                    onChangeText={handleChange('clienteDireccion')}
                  />


              <View style={ style.twoButtonsArea} /* pointerEvents={ isSubmitting?'none':'auto'} */ >
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>  Actions.pop()  } backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Siguiente</Text>
             <Icon name='right' type='AntDesign' style={style.defaultIconButton}/>
             </AwesomeButtonRick>
          </View>

                </View>
              )}
            </Formik>
          </KeyboardAwareScrollView> 

          
          </View>
        </Card>

      
          
  
       
      </View>
    );
  }

  renderFormDatosUsuario() {
    const {handleSubmit} = this.props;
    const {
      tiposLicencia,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,
      tiposDocumento,
      provincias,isTermCondChecked,showTermCondError,cliente,contacto,
      ciudades2,dniRequiredLength,selected
    } = this.state;


    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
          
          <View style={{display:'none'}}><MaterialInput name="clienteAux" autoFocus/></View>
           
          <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Datos de usuario administrador
            </Text>
         
          <KeyboardAwareScrollView keyboardShouldPersistTaps="always"
            contentContainerStyle={style.container}>
            <Formik
              initialValues={{
                usuarioTipoDocumento: cliente.usuarioTipoDocumento,
                usuarioIdDocumento: cliente.usuarioIdDocumento,
                usuarioNombres: cliente.usuarioNombres,
                usuarioApellidos: cliente.usuarioApellidos,
                usuarioNombreUsuario: cliente.usuarioNombreUsuario,
                usuarioMail: cliente.usuarioMail,
                usuarioProvinciaId: contacto.usuarioProvinciaId,
                usuarioLocalidad: contacto.usuarioLocalidad,
                usuarioCelular: contacto.usuarioCelular              
              }}
             validationSchema={ Yup.object().shape({
                usuarioIdDocumento: Yup.string().required(' Campo requerido').
                min(dniRequiredLength ,'Mínimo  de caracteres '+dniRequiredLength).matches(/^[0-9\b]+$/,'Ingrese solo números').
                test("isValidUsuarioDni","El número de documento ingresado ya se encuentra registrado",
                  value => {
                   return isNumdocUsuarioValid
                  }
                ),
              
                
                usuarioNombres: Yup.string().required('Campo requerido'),
                usuarioApellidos: Yup.string().required('Campo requerido'),
                usuarioNombreUsuario: Yup.string().required('Campo requerido').
                test("isValidNombreUsuario","El nombre de usuario ya se encuentra registrado",
                value => {
                 return isUserNameUsuarioValid
                }
              ),

                usuarioMail: Yup.string().required('Campo requerido').email('La dirección ingresada no es un correo valido').
                test("isValidEmailUsuario","El correo electrónico ya se encuentra registrado",
                value => {
                 return isEmailUsuarioValid
                }
              ),
                
                usuarioLocalidad: Yup.string().required('Campo requerido'),
                usuarioCelular: Yup.string().required('Campo requerido').min(6,'Mínimo de caracteres 6'),
     
              })
              } 

              onSubmit={(values, {resetForm,setSubmitting}) => {
              this.onSubmit(values);
              }}>
              {({
                handleSubmit,
                handleBlur,
                handleChange,
                errors,
                values,
                touched,
                isSubmitting,
                isValid,
                setFieldValue,
                setValues
              }) => (
                <View>

            <Text
              style={{color: '#9C9C9C', fontSize: 16, fontWeight: 'normal'}}>
              Tipo de documento
            </Text>

            <View
              style={{
                paddingBottom: 5,
                paddingTop: 5,
                flexDirection: 'row',
                justifyContent: 'space-between',
                width: '75%',
              }}>
                

                <RadioButon 
                 name={'usuarioTipoDocumento'}
                 label={
                  tiposDocumento[0]
                    ? tiposDocumento[0].catalogoItemDescripcion
                    : ''
                }
          value={(selected === 14)}
          status={selected === 14 ? 'checked' : 'unchecked'}
          ref={input => {this.usuarioTipoDocumento = input;}}   
          onPress={() => { 
            setFieldValue('usuarioTipoDocumento',14);
            this.setState({ selected: 14,dniRequiredLength:10 }); }}
        />
        <RadioButon
         name={'usuarioTipoDocumento'}
         label={
          tiposDocumento[1]
            ? tiposDocumento[1].catalogoItemDescripcion
            : ''
          }
          ref={input => {this.usuarioTipoDocumento = input;}}   
          value={selected === ID_RUC}
          status={selected === ID_RUC ? 'checked' : 'unchecked'}
          onPress={() => { 
            setFieldValue('usuarioTipoDocumento',13);
             this.setState({ selected: ID_RUC,dniRequiredLength:13 }); }}
        />

            
            </View>

                  
                  <MaterialInput
                    name="usuarioIdDocumento"
                    autoFocus
                    label='Número de documento'
                    value={values.usuarioIdDocumento}
                    keyboardType={'numeric'}
                    maxLength={dniRequiredLength}
                    placeholder="Número de documento"
                    refField={input => {this.usuarioIdDocumento = input;}}   
                    error={ errors.usuarioIdDocumento && touched.usuarioIdDocumento?errors.usuarioIdDocumento:undefined }
                    onSubmitEditing={() => {this.usuarioNombres.focus();}}
                    onChangeText={handleChange('usuarioIdDocumento')}
                    onEndEditing={(evt)=>{
                       this.numdocUsuarioValidator(evt.nativeEvent.text,'Cliente').then(res=>{
                      }); 
                    }}
                  />
          
              
                  <MaterialInput
                    label='Nombres'
                    name="usuarioNombres"
                    autoCapitalize={'words'}
                    value={values.usuarioNombres}
                    placeholder="Nombres"
                    error={!values.usuarioNombres && errors.usuarioNombres && touched.usuarioNombres?errors.usuarioNombres:undefined }
                    refField={input => {this.usuarioNombres = input;}}   
                    onSubmitEditing={() => {this.usuarioApellidos.focus();}}
                    onChangeText={handleChange('usuarioNombres')}
                  />
              
              <MaterialInput
                    label='Apellidos'
                    name="usuarioApellidos"
                    autoCapitalize={'words'}
                    value={values.usuarioApellidos}
                    placeholder="Apellidos"
                    error={!values.usuarioApellidos && errors.usuarioApellidos && touched.usuarioApellidos?errors.usuarioApellidos:undefined }
                    refField={input => {this.usuarioApellidos = input;}}   
                    onSubmitEditing={() => {this.usuarioNombreUsuario.focus();}}
                    onChangeText={handleChange('usuarioApellidos')}
                  />

                  <MaterialInput
                    label='Nombre de usuario'
                    name="usuarioNombreUsuario"
                    value={values.usuarioNombreUsuario}
                    maxLength={12}
                    placeholder="Nombre de usuario"
                    error={errors.usuarioNombreUsuario && touched.usuarioNombreUsuario?errors.usuarioNombreUsuario:undefined }
                    refField={input => {this.usuarioNombreUsuario = input}}   
                    onChangeText={handleChange('usuarioNombreUsuario')}
                    onSubmitEditing={() => {this.usuarioMail.focus();}}
                    onEndEditing={(evt)=>{
                      this.userNameUsuarioValidator(evt.nativeEvent.text).then(res=>{
                     }); 
                   }}
                    
                  />

                  <MaterialInput
                    label='Correo electrónico'
                    name="usuarioMail"
                    keyboardType={'email-address'}
                    value={values.usuarioMail}
                    placeholder="Correo electrónico"
                    error={ errors.usuarioMail && touched.usuarioMail?errors.usuarioMail:undefined }
                    refField={input => {this.usuarioMail = input;}}   
                    onChangeText={handleChange('usuarioMail')}
                    onEndEditing={(evt)=>{
                      this.emailUsuarioValidator(evt.nativeEvent.text).then(res=>{
                     }); 
                   }}
                  />

<Text style={[style.collapseHeaderText, {paddingTop:10,paddingBottom: 10}]}>
             Datos de contacto
            </Text>


              <DropDownList name="usuarioProvinciaId"
              data={provincias}
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              placeholder=""
              refField={input => {this.usuarioProvinciaId = input;}}   
              selectedValue={contacto.usuarioProvinciaId}
              description={'Seleccione la provincia'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('usuarioProvinciaId',itemValue);
               this.onProvinceValueChange(itemValue,'form2') ;
               }
            }/> 

              <DropDownList name="usuarioLocalidad"
              data={ciudades2}
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              error={ errors.usuarioLocalidad && touched.usuarioLocalidad?errors.usuarioLocalidad:undefined }
              selectedValue={contacto.usuarioLocalidad}
              placeholder=""
              refField={input => {this.usuarioLocalidad = input;}}
              description={'Seleccione la ciudad'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('usuarioLocalidad',itemValue);
              }
            }/> 
              <MaterialInput
                    label='Teléfono'
                    name="usuarioCelular"
                    value={values.usuarioCelular}
                    refField={input => {this.usuarioCelular = input;}}
                    keyboardType={'numeric'}
                    maxLength={10}
                    placeholder="Teléfono"
                    error={!values.usuarioCelular && errors.usuarioCelular && touched.usuarioCelular?errors.usuarioCelular:undefined }
                    onChangeText={handleChange('usuarioCelular')}
                  />


        <View style={{alignContent: 'center', marginTop: 20, width: '100%'}}>
            <CheckBox style={{alignSelf:'center'}} value={isTermCondChecked} onValueChange={() => this.setState({ isTermCondChecked: !isTermCondChecked,showTermCondError:isTermCondChecked })}></CheckBox>
         {(showTermCondError ) &&
            (<Text style={[style.errorText,{alignSelf:'center'}]}>Debe aceptar los términos y condiciones</Text>)
          }
            <Text style={{color: 'blue', textAlign: 'center'}} onPress={() => this.openTermCondModal()}>Términos y condiciones</Text>
            </View>

{/* {errors.usuarioTipoDocumento?<Text>ERROR 1</Text>:null}
{errors.usuarioIdDocumento?<Text>ERROR 2</Text>:null}
{errors.usuarioNombres?<Text>ERROR 3</Text>:null}
{errors.usuarioApellidos?<Text>ERROR 4</Text>:null}
{errors.usuarioNombreUsuario?<Text>ERROR 5</Text>:null}
{errors.usuarioMail?<Text>ERROR 6</Text>:null}
{errors.usuarioProvinciaId?<Text>ERROR 7</Text>:null}
{errors.usuarioLocalidad?<Text>ERROR 8</Text>:null}
{errors.usuarioTipoDocumento?<Text>ERROR 9</Text>:null}
{errors.usuarioCelular?<Text>ERROR 10</Text>:null}
           */}

              <View style={ style.twoButtonsArea} /* pointerEvents={ isSubmitting?'none':'auto'} */ >
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.anteriorForm2(this)} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Registrar</Text>
             </AwesomeButtonRick>
          </View>

                </View>
              )}
            </Formik>
          </KeyboardAwareScrollView> 



        </View>

        
   

        </Card>
     
      </View>
    );
  }




renderForm() {
    const {showForm1, showForm2 } = this.state;
    return (
      <Container style={{paddingTop: 10}}>
        <Content>
      
          {showForm1 && this.renderFormDatosEmpresa()}
           {showForm2 && this.renderFormDatosUsuario() } 
        </Content>
      </Container>
    );
  }



  render() {
    const {onSubmit} = this.props;
    const {
      showAlert,
      textAlert,
      titleAlert,
      isErrorAlert,
      clienteRazonSocial,
      existeEmpresa,isConfirmation,
      isLoading,showModal,showTermCondModal
    } = this.state;
    
    return (
      <View style={[style.container]}>
        {isLoading && <Loader />}
        <Text style={[style.formTitle,{paddingTop:10}]}>Registrar compañía de transporte</Text>
     
    <View style={style.formView}>
    {this.renderForm()}
    </View>

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

        {showTermCondModal && (
          <Modal
            style={style.modalStyle}
            isVisible={showTermCondModal}
            backdropColor={'white'}
            backdropOpacity={1}
            animationIn={'slideInLeft'}
            animationOut={'slideOutRight'}>
            <Content
              style={{marginLeft: 15, marginBottom: 15}}
              contentContainerStyle={{alignItems: 'center'}}>
              <HTML html={TERMINOS_CONDICIONES_ENROLAMIENTO} />
            
              <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={()=> this.hideTermCondModal()} backgroundColor={defaultTasonColor}><Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
      
            </Content>
          </Modal>
        )}
      </View>
    );
  }

}








export default reduxForm({
  form: 'NuevaCompaniaTransporte', // a unique identifier for this form
 })(NuevaCompaniaTransporte)

 

