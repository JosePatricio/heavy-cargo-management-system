import {Formik} from 'formik';
import {Card, Container, Content, Icon} from 'native-base';
import React, {Component} from 'react';
import {Modal, View,CheckBox,ScrollView} from 'react-native';
import {Overlay} from 'react-native-elements';
import {Actions} from 'react-native-router-flux';
import {Field, reduxForm} from 'redux-form';
import {required} from 'redux-form-validators';
import * as Yup from 'yup';
import { connect } from "react-redux";
import { compose } from "redux";

import {obtenerCatalogoItem} from '../../../actions/general.action';
import {
  createEmpresaCliente,
  emailUsuarioValidator,
  getEmpresaTransporteByRuc,
  getLocalidadByPadre,
  numdocUsuarioValidator,
  rucEmpresaValidator,
  userNameUsuarioValidator,
} from '../../../actions/public.actions';
import {
  AwesomeAlert,
  AwesomeButtonRick,
  CustomInputComponent,
  DropDownList,
  HTML,
  KeyboardAwareScrollView,
  Loader,
  MaterialInput,
  RadioButon,
  Text,
} from '../../../components';
import {
  idCatalogoDocumento,
  idCatalogoMedioDifusion,
  idCatalogoPeriodo,
  idCatalogoPersona,
  idCatalogoTipoProducto,
  idCatalogoTiposSubasta,
  idLocalidadPadreEcuador,
  ID_OPCION_CODIGO_VENDEDOR,
  ID_RUC,
} from '../../../Constants';
import {defaultTasonColor, style, styleLogin as styles} from '../../../styles';
import {
  no_connected_info,
  TERMINOS_CONDICIONES_ENROLAMIENTO,
} from '../../../utils/tas-on-texts';



class NuevaEmpresa extends Component {
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

      tiposDocumento: [],
      tiposPersona: [],
      tiposProducto: [],
      tiposDifucion: [],
      tiposSubasta: [],
      periocidad: [],
      provincias: [],
      ciudades: [],
      ciudades2: [],
      isEmailUsuarioValid: true,
      isUserNameUsuarioValid: true,
      isNumdocUsuarioValid: true,
      dniRequiredLength: 10,
      showForm1: true,
      showForm2: false,
      empresa: {},
      usuario: {},
      showModal: false,

      tipoPersona: '',
      provincia: '',
      ciudad: '',
      selectedProvinciaId: 0,
      periodo: '',
      tipoproducto: '',
      reconocimiento: '',
      tipodocumento: '',
      showTermCondModal: false,
      showSellerCodeInbox: false,
      selected: 14,
      showTermCondError:false,
      isTermCondChecked:true
    };

    this.clienteTipoId = React.createRef();
    this.clienteRuc = React.createRef();
    this.clienteRazonSocial = React.createRef();
    this.clienteDireccion = React.createRef();
    this.clienteProvinciaId = React.createRef();
    this.clienteLocalidadId = React.createRef();
    this.clienteDiasPeriodicidad = React.createRef();
    this.clienteTipoProducto = React.createRef();
    this.clienteConocimientoRegistro = React.createRef();
    this.clienteCodigoVendedor = React.createRef();

    this.usuarioTipoDocumento = React.createRef();
    this.usuarioNombres = React.createRef();
    this.usuarioApellidos = React.createRef();
    this.usuarioIdDocumento = React.createRef();
    this.usuarioNombreUsuario = React.createRef();
    this.usuarioCelular = React.createRef();
    this.usuarioMail = React.createRef();
  }

  componentDidMount() {
    
    this.loadPersonTypeProvincesInvoicePeriodTasonInfo();
  
  }

  onChangeTextDniEmpresa(value) {
    if (String(value).length == 13) {
      this.getEmpresaTransporteByRuc(value);
    }
  }

  mostrarCiudades = async idProvincia => {
    try {
      this.setState({isLoading: true, selectedProvinciaId: idProvincia});

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

  loadPersonTypeProvincesInvoicePeriodTasonInfo = async () => {
    try {
      this.setState({isLoading: true});

      const response = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoPersona),
      );

      const response1 = await this.props.dispatch(
        getLocalidadByPadre(idLocalidadPadreEcuador, 1),
      );

      const response2 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoPeriodo),
      );

      const response3 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoDocumento),
      );
      const response4 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoTipoProducto),
      );

      const response5 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoMedioDifusion),
      );

      const response6 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoTiposSubasta),
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
          tiposPersona: response.responseBody,
          provincias: response1.responseBody,
          periocidad: response2.responseBody,
          tiposDocumento: response3.responseBody,
          tiposProducto: response4.responseBody,
          tiposDifucion: response5.responseBody,
          tiposSubasta: response6.responseBody,
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
      });
    }
  };

  numdocUsuarioValidator = async ruc => {
    try {
      const response = await this.props.dispatch(numdocUsuarioValidator(ruc));

      this.setState({isNumdocUsuarioValid: response.valid});
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

  getEmpresaTransporteByRuc = async dni => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        getEmpresaTransporteByRuc(dni),
      );
      if (response) {
        if (response.responseBody.response == 'ERROR') {
          this.setState({
            isLoading: false,
            existeEmpresa: false,
            clienteRazonSocial: 'Empresa de transporte no registrada',
          });
        } else {
          this.setState({
            isLoading: false,
            existeEmpresa: true,
            clienteRazonSocial: response.responseBody.clienteRazonSocial,
          });
        }
      } else {
        throw response;
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

  emailUsuarioValidator = async email => {
    try {
      const response = await this.props.dispatch(emailUsuarioValidator(email));

      this.setState({isEmailUsuarioValid: response.valid});
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
      const response = await this.props.dispatch(
        userNameUsuarioValidator(username),
      );

      this.setState({
        isUserNameUsuarioValid: response.valid,
      });
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

  rucEmpresaValidator = async ruc => {
    try {
      const response = await this.props.dispatch(rucEmpresaValidator(ruc));
      this.setState({isNumdocUsuarioValid: response.valid});
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
      console.log('response  ',response)
      if (response) {
        this.setState({
          showAlert: true,
          showModal: false,
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
      console.log('ERROR SCEREN createEmpresaCliente ', error);
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText=JSON.stringify(errorText);
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
        isLoading: false,
        showModal: false,
      });
    }
  };

  siguiente = values => {
    let payload = {
      clienteTipoId: String(values.clienteTipoId),
      clienteRazonSocial: values.clienteRazonSocial,
      clienteRuc: values.clienteRuc,
      clienteDireccion: values.clienteDireccion,
      clienteProvinciaId: String(values.clienteProvinciaId),
      clienteLocalidadId: String(values.clienteLocalidadId),
      clienteDiasPeriodicidad: String(values.clienteDiasPeriodicidad),
      clienteConocimientoRegistro: String(values.clienteConocimientoRegistro),
      clienteTipoProducto: String(values.clienteTipoProducto),
      clienteCodigoVendedor: values.clienteCodigoVendedor
        ? values.clienteCodigoVendedor
        : null,
      clienteTipoCliente: '60',
    };

    console.log('payload  ', payload);

    this.setState({showForm1: false, showForm2: true, empresa: payload});
  };

  confirmar = values => {

    console.log('values  ',values);
    let payload = {};

    payload = {
      usuarioTipoDocumento: String(this.state.selected),
      usuarioNombres: values.usuarioNombres,
      usuarioApellidos: values.usuarioApellidos,
      usuarioIdDocumento: values.usuarioIdDocumento,
      usuarioNombreUsuario: values.usuarioNombreUsuario,
      usuarioCelular: values.usuarioCelular,
      usuarioMail: values.usuarioMail,
      usuarioTipoUsuario: 75,
      usuarioEstado: 5,
      usuarioPrincipal: true,
    };


    console.log('payload  ',payload);

    const {
      tiposDocumento,
      tiposDifucion,
      tiposProducto,
      periocidad,
      tiposPersona,
      usuario,
      empresa,
      selectedProvinciaId,
      provincias,
      ciudades,
    } = this.state;

    const tipopersona = this.getDescriptionFromId(
      tiposPersona,
      empresa.clienteTipoId,
    );

    const provincia_ = this.getLocalityFromId(provincias, selectedProvinciaId);
    const ciudad_ = this.getLocalityFromId(
      ciudades,
      empresa.clienteLocalidadId,
    );
    const periodo_ = this.getDescriptionFromId(
      periocidad,
      empresa.clienteDiasPeriodicidad,
    );
    const tipoproducto_ = this.getDescriptionFromId(
      tiposProducto,
      empresa.clienteTipoProducto,
    );
    const reconocimiento_ = this.getDescriptionFromId(
      tiposDifucion,
      empresa.clienteConocimientoRegistro,
    );
    const tipodocumento_ = this.getDescriptionFromId(
      tiposDocumento,
      values.usuarioTipoDocumento,
    );

    this.setState({
      showModal: true,
      usuario: payload,
      tipoPersona: tipopersona,
      provincia: provincia_,
      ciudad: ciudad_,
      periodo: periodo_,
      tipoproducto: tipoproducto_,
      reconocimiento: reconocimiento_,
      tipodocumento: tipodocumento_,
    });
  };

  anteriorForm2 = values => {
    let payload = {};

    payload = {
      usuarioTipoDocumento: String(this.state.selected),
      usuarioNombres: values.usuarioNombres.props.value,
      usuarioApellidos: values.usuarioApellidos.props.value,
      usuarioIdDocumento: values.usuarioIdDocumento.props.value,
      usuarioNombreUsuario: values.usuarioNombreUsuario.props.value,
      usuarioCelular: values.usuarioCelular.props.value,
      usuarioMail: values.usuarioMail.props.value,
      usuarioTipoUsuario: 75,
      usuarioEstado: 5,
      usuarioPrincipal: true,
    };

    console.log('payload  ', payload);

    const {
      tiposDocumento,
      tiposDifucion,
      tiposProducto,
      periocidad,
      tiposPersona,
      usuario,
      empresa,
      selectedProvinciaId,
      provincias,
      ciudades,
    } = this.state;

    const tipopersona = this.getDescriptionFromId(
      tiposPersona,
      empresa.clienteTipoId,
    );

    const provincia_ = this.getLocalityFromId(provincias, selectedProvinciaId);
    const ciudad_ = this.getLocalityFromId(
      ciudades,
      empresa.clienteLocalidadId,
    );
    const periodo_ = this.getDescriptionFromId(
      periocidad,
      empresa.clienteDiasPeriodicidad,
    );
    const tipoproducto_ = this.getDescriptionFromId(
      tiposProducto,
      empresa.clienteTipoProducto,
    );
    const reconocimiento_ = this.getDescriptionFromId(
      tiposDifucion,
      empresa.clienteConocimientoRegistro,
    );
    const tipodocumento_ = this.getDescriptionFromId(
      tiposDocumento,
      values.usuarioTipoDocumento,
    );

    this.setState({
      usuario: payload,
      tipoPersona: tipopersona,
      provincia: provincia_,
      ciudad: ciudad_,
      periodo: periodo_,
      tipoproducto: tipoproducto_,
      reconocimiento: reconocimiento_,
      tipodocumento: tipodocumento_,
      showForm1: true,
      showForm2: false,
    });
  };

  onSubmit = values => {
    const {usuario, empresa,isTermCondChecked,showTermCondError} = this.state;

    if (this.props.props.generalReducer.netWorkInfo.status == false) {
      this.setState({
        showAlert: true,
        textAlert: no_connected_info,
        titleAlert: 'Error',
        isErrorAlert: true,
        refreshing: false,
        isLoading: false,
      });
      return;
    }
    

    if(!isTermCondChecked){
      return;
    }

    
    delete empresa.clienteProvinciaId;

    const cliente = usuario;
    let payload = {empresa, cliente};
console.log('payload  ',JSON.stringify(payload));
    this.createEmpresaCliente(JSON.stringify(payload));
  };

  onProvinceValueChange = value => {
    this.mostrarCiudades(value);
  };

  onSourceValueChange = value => {
    this.setState({showSellerCodeInbox: ID_OPCION_CODIGO_VENDEDOR == value});
  };

  onEndEditingUserName = value => {
    this.userNameUsuarioValidator(value);
  };
  onEndEditingEmail = value => {
    this.emailUsuarioValidator(value);
  };

  
  

  onPress(selected: string) {
    this.setState({
      selected,
      dniRequiredLength: ID_RUC == selected ? 13 : 10,
    });
  }

  renderFormDatosEmpresa() {
    const {handleSubmit} = this.props;
    const {
      tiposDocumento,
      tiposPersona,
      tiposProducto,
      tiposDifucion,
      tiposSubasta,
      periocidad,
      provincias,
      ciudades,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,
      showSellerCodeInbox,
      empresa,
    } = this.state;

    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
            <View style={{display: 'none'}}>
              <Field
                component={CustomInputComponent}
                name="usuarioAux"
                autoFocus
              />
            </View>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
              Datos de la empresa
            </Text>

            <KeyboardAwareScrollView
              keyboardShouldPersistTaps="always"
              contentContainerStyle={style.container}>
              <Formik
                initialValues={{
                  clienteTipoId: empresa.clienteTipoId,
                  clienteRuc: empresa.clienteRuc,
                  clienteRazonSocial: empresa.clienteRazonSocial,
                  clienteDireccion: empresa.clienteDireccion,
                  clienteProvinciaId: empresa.clienteProvinciaId,
                  clienteLocalidadId: empresa.clienteLocalidadId,
                  clienteDiasPeriodicidad: empresa.clienteDiasPeriodicidad,
                  clienteTipoProducto: empresa.clienteTipoProducto,
                  clienteConocimientoRegistro: empresa.clienteConocimientoRegistro,
                  clienteCodigoVendedor: empresa.clienteCodigoVendedor, 
                }}

                validationSchema={Yup.object().shape({
                  clienteRuc: Yup.string()
                    .required('Campo requerido')
                    .min(13, 'Mínimo  de caracteres 13')
                    .matches(/^[0-9\b]+$/, 'Ingrese solo número')
                    .test(
                      'isValidEmpresaDni',
                      'El número de documento ingresado ya se encuentra registrado',
                      value => {
                        return isNumdocUsuarioValid;
                      },
                    ),
                  clienteTipoId: Yup.string().required('Campo requerido'),
                  clienteRazonSocial: Yup.string().required('Campo requerido'),
                  clienteDireccion: Yup.string().required('Campo requerido'),
                  clienteProvinciaId: Yup.string().required('Campo requerido').nullable(true),
                  clienteLocalidadId: Yup.string().required('Campo requerido').nullable(true),
                  clienteDiasPeriodicidad: Yup.string().required(
                    'Campo requerido',
                  ),
                  clienteTipoProducto: Yup.string().required('Campo requerido'),
                  clienteConocimientoRegistro: Yup.string().required(
                    'Campo requerido',
                  ),
                  clienteCodigoVendedor:showSellerCodeInbox?Yup.string().required('Campo requerido' ):undefined
             
                })}
                onSubmit={(values, {resetForm, setSubmitting}) => {
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
                  setValues,
                }) => (
                  <View>
                    <DropDownList
                      name="clienteTipoId"
                      data={tiposPersona}
                      itemValue="catalogoItemId"
                      itemLabel="catalogoItemDescripcion"
                      placeholder=""
                      error={
                        errors.clienteTipoId && touched.clienteTipoId
                          ? errors.clienteTipoId
                          : undefined
                      }
                      selectedValue={empresa.clienteTipoId}
                      description={'Seleccione tipo persona'}
                      onValueChange={(itemValue, itemIndex) => {
                        setFieldValue('clienteTipoId', itemValue);
                      }}
                    />

                    <MaterialInput
                      name="clienteRuc"
                      autoFocus
                      label="Número de documento (RUC)"
                      value={values.clienteRuc}
                      keyboardType={'numeric'}
                      maxLength={13}
                      placeholder="Número de documento"
                      error={
                        errors.clienteRuc && touched.clienteRuc
                          ? errors.clienteRuc
                          : undefined
                      }
                      onSubmitEditing={() => {
                        this.clienteRazonSocial.focus();
                      }}
                      onBlur={handleBlur('clienteRuc')}
                      onChangeText={handleChange('clienteRuc')}
                      onEndEditing={evt => {
                        this.rucEmpresaValidator(
                          evt.nativeEvent.text,
                        ).then(res => {});
                      }}
                    />

                    <MaterialInput
                      label="Razón social"
                      name="clienteRazonSocial"
                      autoCapitalize={'words'}
                      value={values.clienteRazonSocial}
                      placeholder="Razón social"
                      error={
                        !values.clienteRazonSocial &&
                        errors.clienteRazonSocial &&
                        touched.clienteRazonSocial
                          ? errors.clienteRazonSocial
                          : undefined
                      }
                      refField={input => {
                        this.clienteRazonSocial = input;
                      }}
                      onBlur={handleBlur('clienteRazonSocial')}
                      onChangeText={handleChange('clienteRazonSocial')}
                    />

                    <DropDownList
                      name="clienteProvinciaId"
                      data={provincias}
                      itemValue="localidadId"
                      itemLabel="localidadDescripcion"
                      placeholder=""
                      error={
                        errors.clienteProvinciaId && touched.clienteProvinciaId
                          ? errors.clienteProvinciaId
                          : undefined
                      }
                      selectedValue={empresa.clienteProvinciaId}
                      description={'Seleccione la provincia'}
                      onValueChange={(itemValue, itemIndex) => {
                        setFieldValue('clienteProvinciaId', itemValue);
                        this.onProvinceValueChange(itemValue);
                      }}
                    />

                    <DropDownList
                      name="clienteLocalidadId"
                      data={ciudades}
                      itemValue="localidadId"
                      itemLabel="localidadDescripcion"
                      selectedValue={empresa.clienteLocalidadId}
                      error={
                        errors.clienteLocalidadId && touched.clienteLocalidadId
                          ? errors.clienteLocalidadId
                          : undefined
                      }
                      placeholder=""
                      description={'Seleccione la ciudad'}
                      onValueChange={(itemValue, itemIndex) => {
                        setFieldValue('clienteLocalidadId', itemValue);
                      }}
                    />

                    <MaterialInput
                      label="Dirección"
                      name="clienteDireccion"
                      autoCapitalize={'words'}
                      value={values.clienteDireccion}
                      placeholder="Dirección"
                      error={
                        errors.clienteDireccion && touched.clienteDireccion
                          ? errors.clienteDireccion
                          : undefined
                      }
                      refField={input => {
                        this.clienteDireccion = input;
                      }}
                      onBlur={handleBlur('clienteDireccion')}
                      onChangeText={handleChange('clienteDireccion')}
                    />

                    <DropDownList
                      name="clienteDiasPeriodicidad"
                      data={periocidad}
                      itemValue="catalogoItemId"
                      itemLabel="catalogoItemDescripcion"
                      selectedValue={empresa.clienteDiasPeriodicidad}
                      error={
                        errors.clienteDiasPeriodicidad &&
                        touched.clienteDiasPeriodicidad
                          ? errors.clienteDiasPeriodicidad
                          : undefined
                      }
                      placeholder=""
                      description={'Seleccione periodo de facturación'}
                      onValueChange={(itemValue, itemIndex) => {
                        setFieldValue('clienteDiasPeriodicidad', itemValue);
                      }}
                    />

                    <DropDownList
                      name="clienteTipoProducto"
                      data={tiposProducto}
                      itemValue="catalogoItemId"
                      itemLabel="catalogoItemDescripcion"
                      selectedValue={empresa.clienteTipoProducto}
                      error={
                        errors.clienteTipoProducto &&
                        touched.clienteTipoProducto
                          ? errors.clienteTipoProducto
                          : undefined
                      }
                      placeholder=""
                      description={'Seleccione tipo de producto'}
                      onValueChange={(itemValue, itemIndex) => {
                        setFieldValue('clienteTipoProducto', itemValue);
                      }}
                    />

                    <DropDownList
                      name="clienteConocimientoRegistro"
                      data={tiposDifucion}
                      itemValue="catalogoItemId"
                      itemLabel="catalogoItemDescripcion"
                      selectedValue={empresa.clienteConocimientoRegistro}
                      error={
                        errors.clienteConocimientoRegistro &&
                        touched.clienteConocimientoRegistro
                          ? errors.clienteConocimientoRegistro
                          : undefined
                      }
                      placeholder=""
                      description={'Cómo conoció TAS-ON'}
                      onValueChange={(itemValue, itemIndex) => {
                        setFieldValue('clienteConocimientoRegistro', itemValue);
                        this.setState({
                          showSellerCodeInbox:
                            ID_OPCION_CODIGO_VENDEDOR == itemValue,
                        });

                        if (ID_OPCION_CODIGO_VENDEDOR == itemValue) {
                          this.clienteCodigoVendedor.focus();
                        }
                      }}
                    />

                    <MaterialInput
                      label="Código del vendedor"
                      name="clienteCodigoVendedor"
                      value={values.clienteCodigoVendedor}
                      hidden={!showSellerCodeInbox}
                      maxLength={13}
                      error={
                        errors.clienteCodigoVendedor && touched.clienteCodigoVendedor
                          ? errors.clienteCodigoVendedor
                          : undefined
                      }
                      refField={input => {
                        this.clienteCodigoVendedor = input;
                      }}
                      onBlur={handleBlur('clienteCodigoVendedor')}
                      onChangeText={handleChange('clienteCodigoVendedor')}
                    />

                    <View
                      style={
                        style.twoButtonsArea
                      } /* pointerEvents={ isSubmitting?'none':'auto'} */
                    >
                      <AwesomeButtonRick
                        height={50}
                        stretch={true}
                        style={style.defaultButtonInArea1}
                        onPress={() =>  Actions.pop()}
                        backgroundColor={defaultTasonColor}>
                        <Icon
                          name="left"
                          type="AntDesign"
                          style={style.defaultIconButton}
                        />
                        <Text style={style.defaultTextButton}>Anterior</Text>
                      </AwesomeButtonRick>
                      <AwesomeButtonRick
                        height={50}
                        stretch={true}
                        style={style.defaultButtonInArea2}
                        onPress={handleSubmit}
                        backgroundColor={defaultTasonColor}>
                        <Text style={style.defaultTextButton}>Siguiente</Text>
                        <Icon
                          name="right"
                          type="AntDesign"
                          style={style.defaultIconButton}
                        />
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
      dniRequiredLength,
      tiposDocumento,
      tiposPersona,
      tiposProducto,
      tiposDifucion,
      tiposSubasta,
      periocidad,
      provincias,
      ciudades,
      isEmailUsuarioValid,
      isUserNameUsuarioValid,
      isNumdocUsuarioValid,
      usuario,
      selected,
    } = this.state;

    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
            <View style={{display: 'none'}}>
              <Field
                component={CustomInputComponent}
                name="usuarioAux"
                autoFocus
              />
            </View>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
              Datos del usuario principal
            </Text>

            <KeyboardAwareScrollView
              keyboardShouldPersistTaps="always"
              contentContainerStyle={style.container}>
              <Formik
                initialValues={{
                  usuarioTipoDocumento: usuario.usuarioTipoDocumento,
                  usuarioIdDocumento: usuario.usuarioIdDocumento,
                  usuarioNombres: usuario.usuarioNombres,
                  usuarioApellidos: usuario.usuarioApellidos,
                  usuarioNombreUsuario: usuario.usuarioNombreUsuario,
                  usuarioMail: usuario.usuarioMail,
                  usuarioCelular: usuario.usuarioCelular, 

                }}
                validationSchema={Yup.object().shape({
                  usuarioIdDocumento: Yup.string()
                    .required(' Campo requerido')
                    .min(
                      dniRequiredLength,
                      'Mínimo  de caracteres ' + dniRequiredLength,
                    )
                    .matches(/^[0-9\b]+$/, 'Ingrese solo números')
                    .test(
                      'isValidUsuarioDni',
                      'El número de documento ingresado ya se encuentra registrado',
                      value => {
                        return isNumdocUsuarioValid;
                      },
                    ),

                  
                  usuarioNombres: Yup.string().required('Campo requerido'),
                  usuarioApellidos: Yup.string().required('Campo requerido'),
                  usuarioNombreUsuario: Yup.string()
                    .required('Campo requerido')
                    .test(
                      'isValidNombreUsuario',
                      'El nombre de usuario ya se encuentra registrado',
                      value => {
                        return isUserNameUsuarioValid;
                      },
                    ),

                  usuarioMail: Yup.string()
                    .required('Campo requerido')
                    .email('La dirección ingresada no es un correo valido')
                    .test(
                      'isValidEmailUsuario',
                      'El correo electrónico ya se encuentra registrado',
                      value => {
                        return isEmailUsuarioValid;
                      },
                    ),

                  usuarioCelular: Yup.string()
                    .required('Campo requerido')
                    .min(6, 'Mínimo de caracteres 6'),
                })}
                onSubmit={(values, {resetForm, setSubmitting}) => {
             
                
                  this.confirmar(values);
             
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
                  setValues,
                }) => (
                  <View>
                    <Text
                      style={{
                        color: '#9C9C9C',
                        fontSize: 16,
                        fontWeight: 'normal',
                      }}>
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
                        value={selected === 14}
                        status={selected === 14 ? 'checked' : 'unchecked'}
                        ref={input => {
                          this.usuarioTipoDocumento = input;
                        }}
                        onPress={() => {
                          setFieldValue('usuarioTipoDocumento', 14);
                          this.setState({selected: 14, dniRequiredLength: 10});
                        }}
                      />
                      <RadioButon
                        name={'usuarioTipoDocumento'}
                        label={
                          tiposDocumento[1]
                            ? tiposDocumento[1].catalogoItemDescripcion
                            : ''
                        }
                        ref={input => {
                          this.usuarioTipoDocumento = input;
                        }}
                        value={selected === ID_RUC}
                        status={selected === ID_RUC ? 'checked' : 'unchecked'}
                        onPress={() => {
                          setFieldValue('usuarioTipoDocumento', 13);
                          this.setState({
                            selected: ID_RUC,
                            dniRequiredLength: 13,
                          });
                        }}
                      />
                    </View>

                    <MaterialInput
                      name="usuarioIdDocumento"
                      autoFocus
                      label="Número de documento"
                      value={values.usuarioIdDocumento}
                      keyboardType={'numeric'}
                      maxLength={dniRequiredLength}
                      placeholder="Número de documento"
                      refField={input => {
                        this.usuarioIdDocumento = input;
                      }}
                      error={
                        errors.usuarioIdDocumento && touched.usuarioIdDocumento
                          ? errors.usuarioIdDocumento
                          : undefined
                      }
                      onSubmitEditing={() => {
                        this.usuarioNombres.focus();
                      }}
                      onChangeText={handleChange('usuarioIdDocumento')}
                      onEndEditing={evt => {
                        this.numdocUsuarioValidator(
                          evt.nativeEvent.text,
                        ).then(res => {});
                      }}
                    />

                    <MaterialInput
                      label="Nombres"
                      name="usuarioNombres"
                      autoCapitalize={'words'}
                      value={values.usuarioNombres}
                      placeholder="Nombres"
                      error={
                        !values.usuarioNombres &&
                        errors.usuarioNombres &&
                        touched.usuarioNombres
                          ? errors.usuarioNombres
                          : undefined
                      }
                      refField={input => {
                        this.usuarioNombres = input;
                      }}
                      onSubmitEditing={() => {
                        this.usuarioApellidos.focus();
                      }}
                      onChangeText={handleChange('usuarioNombres')}
                    />

                    <MaterialInput
                      label="Apellidos"
                      name="usuarioApellidos"
                      autoCapitalize={'words'}
                      value={values.usuarioApellidos}
                      placeholder="Apellidos"
                      error={
                        !values.usuarioApellidos &&
                        errors.usuarioApellidos &&
                        touched.usuarioApellidos
                          ? errors.usuarioApellidos
                          : undefined
                      }
                      refField={input => {
                        this.usuarioApellidos = input;
                      }}
                      onSubmitEditing={() => {
                        this.usuarioNombreUsuario.focus();
                      }}
                      onChangeText={handleChange('usuarioApellidos')}
                    />

                    <MaterialInput
                      label="Nombre de usuario"
                      name="usuarioNombreUsuario"
                      value={values.usuarioNombreUsuario}
                      maxLength={12}
                      placeholder="Nombre de usuario"
                      error={
                        errors.usuarioNombreUsuario &&
                        touched.usuarioNombreUsuario
                          ? errors.usuarioNombreUsuario
                          : undefined
                      }
                      refField={input => {
                        this.usuarioNombreUsuario = input;
                      }}
                      onChangeText={handleChange('usuarioNombreUsuario')}
                      onSubmitEditing={() => {
                        this.usuarioMail.focus();
                      }}
                      onEndEditing={evt => {
                        this.userNameUsuarioValidator(
                          evt.nativeEvent.text,
                        ).then(res => {});
                      }}
                    />

                    <MaterialInput
                      label="Correo electrónico"
                      name="usuarioMail"
                      value={values.usuarioMail}
                      keyboardType={'email-address'}
                      placeholder="Correo electrónico"
                      error={
                        errors.usuarioMail && touched.usuarioMail
                          ? errors.usuarioMail
                          : undefined
                      }
                      refField={input => {
                        this.usuarioMail = input;
                      }}
                      onChangeText={handleChange('usuarioMail')}
                      onSubmitEditing={() => {
                        this.usuarioCelular.focus();
                      }}
                      onEndEditing={evt => {
                        this.emailUsuarioValidator(
                          evt.nativeEvent.text,
                        ).then(res => {});
                      }}
                    />

                    <MaterialInput
                      label="Teléfono"
                      name="usuarioCelular"
                      value={values.usuarioCelular}
                      refField={input => {
                        this.usuarioCelular = input;
                      }}
                      keyboardType={'numeric'}
                      maxLength={10}
                      placeholder="Teléfono"
                      error={
                        !values.usuarioCelular &&
                        errors.usuarioCelular &&
                        touched.usuarioCelular
                          ? errors.usuarioCelular
                          : undefined
                      }
                      onChangeText={handleChange('usuarioCelular')}
                    />


                    <View
                      style={
                        style.twoButtonsArea
                      } /* pointerEvents={ isSubmitting?'none':'auto'} */
                    >
                      <AwesomeButtonRick
                        height={50}
                        stretch={true}
                        style={style.defaultButtonInArea1}
                        onPress={() => this.anteriorForm2(this)}
                        backgroundColor={defaultTasonColor}>
                        <Icon
                          name="left"
                          type="AntDesign"
                          style={style.defaultIconButton}
                        />
                        <Text style={style.defaultTextButton}>Anterior</Text>
                      </AwesomeButtonRick>
                      <AwesomeButtonRick
                        height={50}
                        stretch={true}
                        style={style.defaultButtonInArea2}
                        onPress={handleSubmit}
                        backgroundColor={defaultTasonColor}>
                        <Text style={style.defaultTextButton}>Confirmar</Text>
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

  openTermCondModal = () => {
    this.setState({showTermCondModal: true});
  };
  getDescriptionFromId(list: [], id) {
    let res = '';
    list.forEach(item => {
      if (item.catalogoItemId == id) {
        res = item.catalogoItemDescripcion;
      }
    });
    return res;
  }
  getLocalityFromId(list: [], id) {
    let res = '';
    list.forEach(item => {
      if (item.localidadId == id) {
        res = item.localidadDescripcion;
      }
    });
    return res;
  }

  renderFormConfirmacion() {
    const {handleSubmit} = this.props;
    const {
      tipodocumento,
      periodo,
      tipoproducto,
      reconocimiento,
      empresa,
      usuario,
      tiposPersona,
      tipoPersona,
      provincia,
      ciudad,showTermCondError,isTermCondChecked
    } = this.state;
    return (
      <View>
        <Card elevation={4}>
          <View style={style.cardStyle}>
            <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
              Datos de la empresa
            </Text>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Tipo</Text>
                <Text style={style.textData}>{tipoPersona}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>RUC</Text>
                <Text style={style.textData}>{empresa.clienteRuc}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Razón social</Text>
                <Text style={style.textData}>{empresa.clienteRazonSocial}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Dirección</Text>
                <Text style={style.textData}>{empresa.clienteDireccion}</Text>
              </View>
            </View>
            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Provincia</Text>
                <Text style={style.textData}>{provincia}</Text>
              </View>
            </View>
            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Ciudad</Text>
                <Text style={style.textData}>{ciudad}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Periodo de facturación</Text>
                <Text style={style.textData}>{periodo}</Text>
              </View>
            </View>
            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Tipo de producto</Text>
                <Text style={style.textData}>{tipoproducto}</Text>
              </View>
            </View>
            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Cómo conoció TAS-ON</Text>
                <Text style={style.textData}>{reconocimiento}</Text>
              </View>
            </View>

            <Text
              style={[
                style.collapseHeaderText,
                {paddingBottom: 10, paddingTop: 10},
              ]}>
              Datos del usuario principal
            </Text>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Tipo de documento</Text>
                <Text style={style.textData}>{tipodocumento}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>No. documento</Text>
                <Text style={style.textData}>{usuario.usuarioIdDocumento}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Nombres</Text>
                <Text style={style.textData}>{usuario.usuarioNombres}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Apellidos</Text>
                <Text style={style.textData}>{usuario.usuarioApellidos}</Text>
              </View>
            </View>

            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Usuario</Text>
                <Text style={style.textData}>
                  {usuario.usuarioNombreUsuario}
                </Text>
              </View>
            </View>
            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Correo electrónico</Text>
                <Text style={style.textData}>{usuario.usuarioMail}</Text>
              </View>
            </View>
            <View style={style.viewInfo}>
              <View style={{padding: 5, width: '100%'}}>
                <Text style={style.textInfo}>Teléfono</Text>
                <Text style={style.textData}>{usuario.usuarioCelular}</Text>
              </View>
            </View>

          
     
           <View
              style={{alignContent: 'center', marginTop: 20, width: '100%'}}>
              <CheckBox
                style={{alignSelf: 'center'}}
                value={isTermCondChecked}
                onValueChange={() =>
                  this.setState({
                    isTermCondChecked: !isTermCondChecked,
                    showTermCondError: isTermCondChecked,
                  })
                }></CheckBox>
              {showTermCondError && (
                <Text style={[style.errorText, {alignSelf: 'center'}]}>
                  Debe aceptar los términos y condiciones
                </Text>
              )}
              <Text
                style={{color: 'blue', textAlign: 'center'}}
                onPress={() => this.openTermCondModal()}>
                Términos y condiciones
              </Text>
            </View> 

            <View style={style.twoButtonsArea}>
              <AwesomeButtonRick
                height={50}
                stretch={true}
                style={style.defaultButtonInArea1}
                onPress={() => this.closeModal()}
                backgroundColor={defaultTasonColor}>
                <Text style={style.defaultTextButton}>Cerrar</Text>
              </AwesomeButtonRick>
              <AwesomeButtonRick
                height={50}
                stretch={true}
                style={style.defaultButtonInArea2}
                onPress={()=>this.onSubmit(null)}
                backgroundColor={defaultTasonColor}>
                <Text style={style.defaultTextButton}>Guardar</Text>
              </AwesomeButtonRick>
            </View>
          </View>
        </Card>
      </View>
    );
  }

  renderForm() {
    const {showForm1, showForm2} = this.state;
    return (
      <Container style={{paddingTop: 10}}>
         <ScrollView keyboardShouldPersistTaps='always'>
          {showForm1 && this.renderFormDatosEmpresa()}
          {showForm2 && this.renderFormDatosUsuario()}
          </ScrollView>
      </Container>
  );
  }

  hideAlert = () => {
    this.setState({
      showAlert: false,
    });

    if (this.state.isPostedSuccessfully) {
      Actions.pop();
    }
  };

  dismissAlert = () => {
    if (this.state.showAlert) {
      this.setState({
        showAlert: false,
      });
    }
   
  };

  closeModal = () => {
    this.setState({showModal: false});
  };

  hideTermCondModal = () => {
    this.setState({showTermCondModal: false});
  };

  render() {
    const {onSubmit} = this.props;
    const {
      showAlert,
      textAlert,
      titleAlert,
      isErrorAlert,
      clienteRazonSocial,
      existeEmpresa,
      isLoading,
      showModal,
      showTermCondModal,
    } = this.state;
    return (
      <Container style={[style.container]}>
        {isLoading && <Loader />}
      <View style={style.formView}>
      <Text style={[style.formTitle,{paddingTop:15}]}>Empresa cliente</Text>
       
     {this.renderForm()} 
     

      </View>
   
        {showAlert && (
          <AwesomeAlert
            show={showAlert}
            showProgress={false}
            title={titleAlert}
            message={textAlert}
            closeOnTouchOutside={!this.state.isPostedSuccessfully}
            closeOnHardwareBackPress={false}
            showCancelButton={false}
            showConfirmButton={true}
            confirmText="Ok"
            contentContainerStyle={style.alertContentContainerStyle}
            confirmButtonStyle={style.alertConfirmButtonStyle}
            confirmButtonTextStyle={
              isErrorAlert
                ? style.alertConfirmButtonErrorTextStyle
                : style.alertConfirmButtonTextStyle
            }
            titleStyle={style.alertTitleStyle}
            onConfirmPressed={() => {
              this.hideAlert();
            }}
            onDismiss={() => {
              this.dismissAlert();
            }}
          />
        )}

        <Overlay
          width={'95%'}
          isVisible={showModal}
          onBackdropPress={() => this.setState({showModal: false})}>
          <Content>{this.renderFormConfirmacion()}</Content>
        </Overlay>

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

              <AwesomeButtonRick
                height={50}
                stretch={true}
                onPress={() => this.hideTermCondModal()}
                backgroundColor={defaultTasonColor}>
                <Text style={style.defaultTextButton}>Cerrar</Text>
              </AwesomeButtonRick>
            </Content>
          </Modal>
        )}
      </Container>
    );
  }
}



export default reduxForm({
  form: 'NuevaEmpresa', // a unique identifier for this form
 })(NuevaEmpresa)

 


