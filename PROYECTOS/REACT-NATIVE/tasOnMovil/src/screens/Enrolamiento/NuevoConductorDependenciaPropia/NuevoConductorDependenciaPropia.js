import { Body, Card, CardItem, Container, Content, Icon } from 'native-base';
import React, { Component } from 'react';
import { CheckBox, Dimensions, FlatList, Modal, View } from "react-native";
import { Overlay } from 'react-native-elements';
import { Actions } from 'react-native-router-flux';
import { Field, reduxForm } from 'redux-form';
import { email, length, required } from 'redux-form-validators';
import { obtenerCatalogoItem } from "../../../actions/general.action";
import { createEmpresaCliente, emailUsuarioValidator, getLocalidadByPadre, numdocUsuarioValidator, userNameUsuarioValidator } from "../../../actions/public.actions";
import { AwesomeAlert, AwesomeButtonRick, CheckBoxField, CustomInputComponent, DropDownList, HTML, Loader, Text } from "../../../components";
import { idCatalogoBanco, idCatalogoCamion, idCatalogoCarga, idCatalogoLicencia, idCatalogoPesos, idCatalogoTipoCuenta, idLocalidadPadreEcuador, ID_RUC } from '../../../Constants';
import { defaultTasonColor, style } from '../../../styles';
import { onlyNums } from '../../../utils/normalize';
import { no_connected_info, TERMINOS_CONDICIONES_ENROLAMIENTO } from '../../../utils/tas-on-texts';

const  height  = Dimensions.get('window').height;
const renderCheckBox = ({
    input: {value, onChange, ...input},
    meta: {touched, error},
    ...restInput
  }) => {
    return (
      <View style={{padding: 5, alignItems: 'center'}}>
        <CheckBoxField {...input} onChange={onChange} />
        {touched && error && <Text style={style.errorText}>{error}</Text>}
      </View>
    );
  };
  

const renderPicker = ({
  meta: {touched, error},
  data,
  input: {onChange, value, ...restInput},

  placeholder,
  itemValue,
  itemLabel,description,
  ...inputProps
}) => {
  return (
    <View style={{paddingBottom:10}}>
      <DropDownList
        selectedValue={`${value}`}
        data={data}
        itemValue={itemValue}
        itemLabel={itemLabel}
        onValueChange={onChange}
        placeholder={placeholder}
        description={description}
      />
      {touched && error && <Text style={style.errorText}>{error}</Text>}
    </View>
  );
};


class NuevoConductorDependenciaPropia extends Component {
  constructor(props) {
    super(props);

    this.vehiculosArray = [];

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

        isEmailUsuarioValid: false,
        isUserNameUsuarioValid: false,
        isNumdocUsuarioValid: false,
        dniRequiredLength: 10,
        showForm1: true,
        showForm2: false,
        showForm3: false,
        cliente:{},
        contacto:{},
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
        modalVehiculoVisible: false,
        vehiculos:[],
        modalVehiculoInfoVisible: false,
        vehiculoSeleccionado:{},
        isTermCondChecked:true,
        showTermCondError:false,
    };


  }

  componentDidMount() {
  this.props.reset();
   this.loadDefaultData();
 }


  mostrarCiudades = async idProvincia => {
    try {
      this.setState({isLoading: true,selectedProvinciaId:idProvincia});

      const response = await this.props.dispatch(
        getLocalidadByPadre(idProvincia, 1),
      );

      if (response) {
        this.setState({isLoading: false, ciudades: response.responseBody});
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
      const response2 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoTipoCuenta),
      );
      const response3 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoBanco),
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
   
    
      if (
        response &&
        response1 &&
        response2 &&
        response3 &&
        response4 &&
        response5 &&
        response6
      ) {
        this.setState({
          isLoading: false,
          provincias: response.responseBody,
          tiposLicencia: response1.responseBody,
          tiposCuentaBanco: response2.responseBody,
          bancos: response3.responseBody,
          tiposUnidadCapacidad: response4.responseBody,
          tiposCarga: response5.responseBody,
          tiposCamion: response6.responseBody,
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

  numdocUsuarioValidator = async ruc => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(numdocUsuarioValidator(ruc));

      if (!response.valid) {
        this.setState({
          isLoading: false,
          isNumdocUsuarioValid: response.valid,
          showAlert: true,
          textAlert:
            'El número de documento ingresado ya se encuentra registrado',
          titleAlert: 'Alerta',
          isErrorAlert: true,
        });
      } else {
        this.setState({isLoading: false, isNumdocUsuarioValid: response.valid});
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

  onEndEditingUserDni = value => {
    this.numdocUsuarioValidator(value);
  };

  emailUsuarioValidator = async email => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(emailUsuarioValidator(email));

      if (!response.valid) {
        this.setState({
          isLoading: false,
          isEmailUsuarioValid: response.valid,
          showAlert: true,
          textAlert: 'El correo electrónico ya se encuentra registrado',
          titleAlert: 'Alerta',
          isErrorAlert: true,
        });
      } else {
        this.setState({isLoading: false, isEmailUsuarioValid: response.valid});
      }
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
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        userNameUsuarioValidator(username),
      );

      if (!response.valid) {
        this.setState({
          isLoading: false,
          isUserNameUsuarioValid: response.valid,
          showAlert: true,
          textAlert: 'El nombre de usuario ya se encuentra registrado',
          titleAlert: 'Alerta',
          isErrorAlert: true,
        });
      } else {
        this.setState({
          isLoading: false,
          isUserNameUsuarioValid: response.valid,
        });
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
    this.props.change(
      'usuarioIdDocumento',
      values.usuarioIdDocumento ? values.usuarioIdDocumento : null,
    );

    if (values.usuarioIdDocumento &&
      values.conductorTipoLicencia &&
      values.usuarioNombres &&
      values.usuarioApellidos &&
      values.usuarioNombreUsuario &&
      values.usuarioMail) {
      
    let payload = {usuarioIdDocumento: String(values.usuarioIdDocumento),
        conductorTipoLicencia:String(values.conductorTipoLicencia),  
        usuarioNombres: values.usuarioNombres,
        usuarioApellidos: values.usuarioApellidos,
        usuarioNombreUsuario: values.usuarioNombreUsuario,
        clienteDireccion: values.clienteDireccion,
        usuarioMail: values.usuarioMail};
        
    this.setState({showForm1: false, showForm2: true,showForm3: false, cliente:payload});
   
    }
  };

  siguiente2 = values => {

    this.props.change('clienteBanco',  values.clienteBanco ? values.clienteBanco : null);
    
    if (values.clienteLocalidadId &&
      values.clienteDireccion &&
      values.usuarioCelular &&
      values.clienteBanco &&
      values.clienteTipoCuenta &&
      values.clienteCuenta) {

        let payload = {clienteLocalidadId: String(values.clienteLocalidadId),
          clienteDireccion:values.clienteDireccion,  
          usuarioCelular: values.usuarioCelular};

          
        let payload1 = {clienteBanco: values.clienteBanco,
          clienteTipoCuenta: values.clienteTipoCuenta,
          clienteCuenta: values.clienteCuenta};


    this.setState({showForm1: false, showForm2: false,showForm3: true,contacto:payload,informacionBancaria:payload1});
}
}

 

onSubmit = values => {
 
  const {cliente,contacto,informacionBancaria,vehiculos,isTermCondChecked,showTermCondError}=this.state;
 
  if(!isTermCondChecked){
this.setState({showTermCondError:true});
return;
  }

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

    if(this.vehiculosArray.length == 0 ){
      this.setState({
        textAlert: 'Debe ingresar al menos un vehículo',
        titleAlert: 'Mensaje',
        showAlert: true,
        isErrorAlert: true,
        isConfirmation:false
      });
      return;
    }    

  let payload={
      cliente: {
        usuarioIdDocumento: cliente.usuarioIdDocumento,
        usuarioNombres: cliente.usuarioNombres,
        usuarioApellidos: cliente.usuarioApellidos,
        usuarioNombreUsuario: cliente.usuarioNombreUsuario,
        usuarioMail: cliente.usuarioMail,
        usuarioLocalidad: contacto.clienteLocalidadId,
        usuarioCelular: contacto.usuarioCelular,
        conductorsByUsuarioIdDocumento: [
          {
            conductorApellido: cliente.usuarioApellidos,
            conductorEmail: cliente.usuarioMail,
            conductorNombre: cliente.usuarioNombres,
            conductorTelefono: contacto.usuarioCelular,
            conductorUsuario: cliente.usuarioIdDocumento,
            conductorTipoLicencia: cliente.conductorTipoLicencia
          }
        ],
        usuarioEstado: 5,
        usuarioPrincipal: true,
        usuarioTipoDocumento: 13,
        usuarioRuc: cliente.usuarioIdDocumento,
        usuarioTipoUsuario: 77,
        vehiculosByUsuarioIdDocumento: 
          this.vehiculosArray
        
      },
      empresa: {
        clienteDireccion: contacto.clienteDireccion,
        clienteBanco: informacionBancaria.clienteBanco,
        clienteTipoCuenta: informacionBancaria.clienteTipoCuenta,
        clienteCuenta: informacionBancaria.clienteCuenta,
        clienteRazonSocial: cliente.usuarioNombres+" "+cliente.usuarioApellidos,
        clienteRuc: cliente.usuarioIdDocumento,
        clienteTipoCliente: "276",
        clienteTipoId: 11,
        clienteLocalidadId: contacto.clienteLocalidadId,
        clienteComision: 0,
        clienteDiasCredito: 0,
        clienteDiasPeriodicidad: 0
      }
  };

console.log(' payload   ',payload);
 
 this.createEmpresaCliente(JSON.stringify(payload));
  
};


  anteriorForm2 = () => {
    this.setState({showForm1: true, showForm2: false, showForm3: false});
  };

  anteriorForm3= () => {
    this.setState({showForm1: false, showForm2: true, showForm3: false});
  };
  
  onProvinceValueChange = value => {
    this.mostrarCiudades(value);
  };


  

  onEndEditingUserName = value => {
    this.userNameUsuarioValidator(value);
  };
  onEndEditingEmail = value => {
    this.emailUsuarioValidator(value);
  };

  onCancel() {
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
      if (this.state.isConfirmation) {
        const { vehiculos,vehiculoSeleccionado } = this.state;
   let array = [...this.state.vehiculos]; 
   let index = array.indexOf(this.state.vehiculoSeleccionado)
  
   if (index !== -1) {
    array.splice(index, 1);
    this.setState({ vehiculos: array});
    this.vehiculosArray=array;   
     }
      }
  
}

  dismissAlert = () => {
    if (this.state.showAlert) {
      this.setState({
        showAlert: false,
      });
    }
    if (!this.state.isNumdocUsuarioValid) {
      this.props.change('usuarioIdDocumento', '');
    }

    if (!this.state.isEmailUsuarioValid) {
      this.props.change('usuarioMail', '');
    }
    if (!this.state.isUserNameUsuarioValid) {
      this.props.change('usuarioNombreUsuario', '');
    }
  };

  closeModal=()=>{
      this.setState({showModal:false});
  }

  hideTermCondModal=()=>{
    this.setState({showTermCondModal:false});
  }

    abrirModalVehiculo=()=>{
        this.setModalmodalVehiculoVisible(true);
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
        modalVehiculoInfoVisible:true,vehiculoSeleccionado:item});
    }

   

    setModalmodalVehiculoVisible = (visible) => {
    this.setState({modalVehiculoVisible: visible});
    }

    agregarVehiculo=(values)=>{

      console.log('VEHICULO  ',values);
    let vehiculo={
    vehiculoCertificadoArcsa: (values.vehiculoCertificadoArcsa==true?true:false),
    vehiculoModelo: values.vehiculoModelo,
    vehiculoAnio: values.vehiculoAnio,
    vehiculoPlaca: values.vehiculoPlaca,
    vehiculoCapacidad: String(values.vehiculoCapacidad),
    vehiculoTipoCapacidad: values.vehiculoTipoCapacidad,
    vehiculoTipoCarga: String(values.vehiculoTipoCarga),
    vehiculoTipoCamion: String(values.vehiculoTipoCamion)
}

this.vehiculosArray.push(vehiculo);
this.setState({ vehiculos: [...this.vehiculosArray] ,modalVehiculoVisible:false}); 

this.props.change('vehiculoModelo',null);
this.props.change('vehiculoAnio',null);
this.props.change('vehiculoPlaca',null);
this.props.change('vehiculoCertificadoArcsa',null);
this.props.change('vehiculoCapacidad',null);
this.props.change('vehiculoTipoCapacidad',null);
this.props.change('vehiculoTipoCarga',null);
this.props.change('vehiculoTipoCamion',null);


    }
    
    cerrarModalVehiculo=()=>{
    this.setState({modalVehiculoVisible:false});
    }
    
    onCancelAll=()=>{
      this.props.reset();
      Actions.reset('root');
    } 

  renderFormDatosEmpresa() {
    const {handleSubmit} = this.props;
    const {
        tiposCuentaBanco,
        tiposUnidadCapacidad,
        tiposCarga,
        tiposCamion,
        bancos,
        provincias,
        ciudades,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,
    } = this.state;

    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Datos de contacto
            </Text>

            <Field
              name="provinces"
              component={renderPicker}
              data={provincias}
              placeholder=""
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              description={'Seleccione la provincia'}
              validate={[required({msg: 'Campo requerido'})]}
              onChange={this.onProvinceValueChange}
            />

            <Field
              name="clienteLocalidadId"
              component={renderPicker}
              data={ciudades}
              placeholder=""
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              description={'Seleccione la ciudad'}
              validate={[required({msg: 'Campo requerido'})]}
            />
            
            <Field
              component={CustomInputComponent}
              name="clienteDireccion"
              type="text"
              autoCapitalize={'words'}
              maxLength={100}
              placeholder="Dirección"
              validate={[required({msg: 'Campo requerido'})]}
            />

            <Field
              component={CustomInputComponent}
              name="usuarioCelular"
              type="text"
              normalize={onlyNums}
              placeholder="Teléfono"
              keyboardType={'numeric'}
              maxLength={10}
              validate={[length({
              msg: 'Mínimo de caracteres 6',
              min: 6,
              max: 10,
              }),required({msg: 'Campo requerido'})]}
              />


<Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
Información bancaria
            </Text>

            <Field
              name="clienteBanco"
              component={renderPicker}
              data={bancos}
              placeholder=""
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione banco'}
              validate={[required({msg: 'Campo requerido'})]}
            />

            
            <Field
              name="clienteTipoCuenta"
              component={renderPicker}
              data={tiposCuentaBanco}
              placeholder=""
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione tipo de cuenta'}
              validate={[required({msg: 'Campo requerido'})]}
            />

<Field
              component={CustomInputComponent}
              name="clienteCuenta"
              type="text"
              placeholder="Número de cuenta"
              keyboardType={'numeric'}
              maxLength={20}
              normalize={onlyNums}
              validate={[length({
              max: 20,
              }),required({msg: 'Campo requerido'})]}
              />

          </View>
        </Card>


        <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.anteriorForm2()} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.siguiente2)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Siguiente</Text>
             <Icon name='right' type='AntDesign' style={style.defaultIconButton}/>
             </AwesomeButtonRick>
          </View>


  
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
    } = this.state;

    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
              Datos del usuario 
            </Text>

            <Field
              component={CustomInputComponent}
              name="usuarioIdDocumento"
              type="text"
              placeholder="Número de documento (RUC)"
              keyboardType={'numeric'}
              maxLength={13}
              normalize={onlyNums}
              validate={[
                required({msg: 'Campo requerido'}),
                length({
                  msg: 'Mínimo de caracteres 13' ,
                  min: 13,
                  max: 13,
                }),
              ]}
              onEndEditing={evt =>
                this.onEndEditingUserDni(evt.nativeEvent.text)
              }
            />

            <Field
              name="conductorTipoLicencia"
              component={renderPicker}
              data={tiposLicencia}
              placeholder=""
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione tipo de licencia'}
              validate={[required({msg: 'Campo requerido'})]}
              />

            <Field
              component={CustomInputComponent}
              name="usuarioNombres"
              type="text"
              placeholder="Nombres"
              autoCapitalize={'words'}
              validate={[required({msg: 'Campo requerido'})]}
            />
            <Field
              component={CustomInputComponent}
              name="usuarioApellidos"
              type="text"
              placeholder="Apellidos"
              autoCapitalize={'words'}
              validate={[required({msg: 'Campo requerido'})]}
            />
            <Field
              component={CustomInputComponent}
              name="usuarioNombreUsuario"
              type="text"
              maxLength={12}
              placeholder="Nombre de usuario"
              validate={[required({msg: 'Campo requerido'})]}
              onEndEditing={evt =>
                this.onEndEditingUserName(evt.nativeEvent.text)
              }
            />

            <Field
              component={CustomInputComponent}
              name="usuarioMail"
              type="text"
              placeholder="Correo electrónico"
              onEndEditing={evt => this.onEndEditingEmail(evt.nativeEvent.text)}
              validate={[
                required({msg: 'Campo requerido'}),
                email({msg: 'La dirección ingresada no es un correo valido'}),
              ]}
            />

        </View>
        </Card>

        <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.onCancel()} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.siguiente)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Siguiente</Text>
             <Icon name='right' type='AntDesign' style={style.defaultIconButton}/>
             </AwesomeButtonRick>
          </View>

  
      </View>
    );
  }


  renderFormDatosVehiculo() {
    const {handleSubmit} = this.props;
    const {
        tiposCuentaBanco,
        tiposUnidadCapacidad,
        tiposCarga,
        tiposCamion,
        bancos,
        provincias,
        ciudades,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,
    } = this.state;

    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Datos del vehículo
            </Text>

            <Field
              component={CustomInputComponent}
              name="vehiculoModelo"
              type="text"
              maxLength={20}
              autoCapitalize={'words'}
              placeholder="Marca"
              validate={[required({msg: 'Campo requerido'})]}/>

             <Field
              component={CustomInputComponent}
              name="vehiculoAnio"
              type="text" 
              maxLength={4}
              keyboardType={'numeric'}
              normalize={onlyNums}
              placeholder="Año"
              validate={[required({msg: 'Campo requerido'})]}/>

             <Field
              component={CustomInputComponent}
              name="vehiculoPlaca"
              type="text"
              maxLength={8}
              placeholder="Placa"
              validate={[required({msg: 'Campo requerido'})]}/>


            <Text style={{color: '#4A5F8E'}}>Certificado Arcsa</Text>
             <View style={{alignSelf:'flex-start'}}>
            <Field name="vehiculoCertificadoArcsa" type="checkbox" component={renderCheckBox}/>
                </View>
           
           <View style={{flexDirection:'row',justifyContent:'space-between'}}>
         <View style={{width:'30%',paddingTop:15}}>
           <Field
              component={CustomInputComponent}
              name="vehiculoCapacidad"
              type="text"
              maxLength={6}
              keyboardType={'numeric'}
              normalize={onlyNums}
              placeholder="Capacidad"
              validate={[required({msg: 'Campo requerido'})]}/>
              </View>
            <View style={{width:'60%'}}>
            <Field
              name="vehiculoTipoCapacidad"
              component={renderPicker}
              placeholder=""
              data={tiposUnidadCapacidad}
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Unidad de capacidad'}
              validate={[required({msg: 'Campo requerido'})]}
            /> 
           </View>
           </View>
            
            
            <Field
              name="vehiculoTipoCarga"
              component={renderPicker}
              data={tiposCarga}
              placeholder=""
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione tipo de carga'}
              validate={[required({msg: 'Campo requerido'})]}
            />

            <Field
              name="vehiculoTipoCamion"
              component={renderPicker}
              data={tiposCamion}
              placeholder=""
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione tipo de camión'}
              validate={[required({msg: 'Campo requerido'})]}
            />

          </View>
        </Card>

        <View style={ [style.twoButtonsArea,{paddingTop:0,padding:5}]}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={handleSubmit(this.agregarVehiculo)} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Agregar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={() => this.cerrarModalVehiculo()} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Cerrar</Text>
             </AwesomeButtonRick>
          </View>

      </View>
    );
  }

  renderFormVehiculos() {
    const {handleSubmit} = this.props;
    const {
      tiposLicencia,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,vehiculos,isTermCondChecked,showTermCondError
    } = this.state;

    
    const vehiculosLength=this.vehiculosArray.length;

    const fitSize=(height/2)/2;
  return (

      <View>
        <Card style={{  height:(height-fitSize)}} elevation={4}>
          <View style={[style.cardStyle,{flex: 1,
        flexDirection: 'column',
        justifyContent: 'space-between'}]}>
       
        <View>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Datos de vehículos
            </Text>
            <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={() => this.abrirModalVehiculo()} backgroundColor={defaultTasonColor}>
  <Icon name="md-add" type="Ionicons" style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Agregar</Text>
    </AwesomeButtonRick>
            <FlatList style={{marginTop:10}}
            data={this.vehiculosArray}
            style={{height:'70%'}}
            renderItem={({ item }) =>
            
            <Card collapsable={true} style={({elevation: 4})}>
            <CardItem
              button
              style={style.noPadding}
              onPress={() => this.openVehiculoInfo(item)} onLongPress={()=>this.vehiculoDeleteConfirmation(item)}
              >
              <Body style={style.noPadding}>
               
              <View style={style.viewInfo1}>
                <View style={{padding: 5}}>
                  <Text style={[style.textInfo,{textAlign:'center'}]}>Modelo</Text>
                  <Text style={style.textData}>
                  {item.vehiculoModelo}
                  </Text>
                </View>
              
                <View style={{padding: 5}}>
                  <Text style={style.textInfo}>Año</Text>
                  <Text style={style.textData}>
                  {item.vehiculoAnio}
                      </Text>
                </View>
                <View style={{padding: 5}}>
                  <Text style={style.textInfo}>Placa</Text>
                  <Text style={style.textData}>
                  {item.vehiculoPlaca} 
                  </Text>
                </View>
                <View style={{padding: 5}}>
                  <Text style={style.textInfo}>Capacidad</Text>
                  <Text style={style.textData}>
                  {item.vehiculoCapacidad} 
                  </Text>
                </View>


                
              </View>
              </Body>
            </CardItem>
          </Card>
                }
            keyExtractor={(item, index) => String(index)}
            />
        </View>       
        </View>

<View >

{(vehiculosLength > 0) && 
            (<View style={{alignContent: 'center', marginTop: 20, width: '100%'}}>
                
                    <CheckBox style={{alignSelf:'center'}} value={isTermCondChecked} onValueChange={() => this.setState({ isTermCondChecked: !isTermCondChecked,showTermCondError:isTermCondChecked })}></CheckBox>
                 {(showTermCondError ) &&
                    (<Text style={[style.errorText,{alignSelf:'center'}]}>Debe aceptar los términos y condiciones</Text>)
                  }
                    <Text style={{color: 'blue', textAlign: 'center'}} onPress={() => this.openTermCondModal()}>Términos y condiciones</Text>

          
  <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={()=>this.onSubmit()} backgroundColor={defaultTasonColor}>
  <Text style={style.defaultTextButton}>Registrar</Text></AwesomeButtonRick>
      
                  </View>)}

        <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.anteriorForm3()} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Regresar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={()=>this.onCancelAll()} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Cancelar</Text>
             </AwesomeButtonRick>
          </View>

          </View>
      
        </Card>

          </View>
    );
  }

  

  renderModalVehiculoInfo(){
   const {unidadCapacidad,tipoCarga,tipoCamion,vehiculoSeleccionado} = this.state;
 return(
 <View>
 <Card elevation={4}>
   <View style={style.cardStyle}>
     <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
       Información del vehículo
     </Text>

     <View style={style.flatContainer}>
                 <View style={style.flatContainerItem}>
                   <Text style={style.textInfo}>Modelo</Text>
                   <Text style={style.textData}>{vehiculoSeleccionado.vehiculoModelo}</Text>
                 </View>

                 <View style={style.flatContainerItem}>
                   <Text style={style.textInfo}>Año</Text>
                   <Text style={style.textData}>{vehiculoSeleccionado.vehiculoAnio}</Text>
                 </View>
         </View>

         <View style={style.flatContainer}>
                 <View style={style.flatContainerItem}>
                   <Text style={style.textInfo}>Placa</Text>
                   <Text style={style.textData}>{vehiculoSeleccionado.vehiculoPlaca}</Text>
                 </View>

                 <View style={{padding: 5}}>
                   <Text style={style.textInfo}>Certificado Arcsa</Text>
                   <Text style={style.textData}>{vehiculoSeleccionado.vehiculoCertificadoArcsa?'✔':'X'}</Text>
                 </View>
         </View>

      
         <View style={style.flatContainer}>
                 <View style={style.flatContainerItem}>
                   <Text style={style.textInfo}>Capacidad</Text>
                   <Text style={style.textData}>{vehiculoSeleccionado.vehiculoCapacidad}</Text>
                 </View>
       
                 <View style={{padding: 5}}>
                   <Text style={style.textInfo}>Unidad capacidad</Text>
                   <Text style={style.textData}>{unidadCapacidad}</Text>
                 </View>
         </View>
       
         <View style={style.flatContainer}>
                 <View style={style.flatContainerItem}>
                   <Text style={style.textInfo}>Tipo de carga</Text>
                   <Text style={style.textData}>{tipoCarga}</Text>
                 </View>
                 <View style={{padding: 5}}>
                   <Text style={style.textInfo}>Tipo camión</Text>
                   <Text style={style.textData}>{tipoCamion}</Text>
                 </View>
         </View>
      

       <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={() => this.setState({modalVehiculoInfoVisible:false})} backgroundColor={defaultTasonColor}><Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
      

   </View>
 </Card>

</View>

)
} 


renderForm() {
    const {showForm1, showForm2,showForm3 } = this.state;
    return (
      <Container style={{paddingTop: 10}}>
        <Content>
      
          {showForm1 && this.renderFormDatosUsuario()}
           {showForm2 && this.renderFormDatosEmpresa()} 
           {showForm3 && this.renderFormVehiculos()} 
        </Content>
      </Container>
    );
  }



  render() {
    const {onSubmit} = this.props;
    const {modalVehiculoVisible,
      showAlert,
      textAlert,
      titleAlert,
      isErrorAlert,
      clienteRazonSocial,
      existeEmpresa,isConfirmation,
      isLoading,showModal,showTermCondModal,modalVehiculoInfoVisible
    } = this.state;
    
    return (
      <View style={[style.container, {padding: 15}]}>
        {isLoading && <Loader />}
        <Text style={style.formTitle}>Conductor dependencia propia</Text>
     
        {this.renderForm()}

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


<Overlay width={'95%'}
  isVisible={modalVehiculoVisible}
  onBackdropPress={() => this.setState({ modalVehiculoVisible: false })}
><Content>
  {this.renderFormDatosVehiculo()}
  </Content>
</Overlay>

<Overlay width={'95%'} height={height/2}
  isVisible={modalVehiculoInfoVisible}
  onBackdropPress={() => this.setState({ modalVehiculoInfoVisible: false })}
><Content>
  {this.renderModalVehiculoInfo()}
  </Content>
</Overlay>



      </View>
    );
  }

}



const validate = values => {
  const errors = {};
  if (!values.clienteTipoId) {
    errors.clienteTipoId = 'Campo requerido';
  }
  return errors;
};



export default reduxForm({
  form: 'NuevoConductorDependenciaPropiaForm', // a unique identifier for this form
  destroyOnUnmount: false, // <------ preserve form data
  validate,
  asyncBlurFields: ['userDni'],
})(NuevoConductorDependenciaPropia)
 
 