import { Card, Container, Content, Icon } from 'native-base';
import React, { Component } from 'react';
import { View ,ScrollView} from "react-native";
import { Actions } from 'react-native-router-flux';
import { Field, reduxForm } from 'redux-form';
import { email, length, required } from 'redux-form-validators';
import { obtenerCatalogoItem } from "../../../actions/general.action";
import { createUsuarioEmpresa, emailUsuarioValidator, getEmpresaTransporteByRuc, getLocalidadByPadre, numdocUsuarioValidator, userNameUsuarioValidator } from "../../../actions/public.actions";
import { KeyboardAwareScrollView,MaterialInput,AwesomeAlert, AwesomeButtonRick, CustomInputComponent, DropDownList, Loader, RadioButon, Text } from "../../../components";
import { idCatalogoDocumento, idCatalogoLicencia, idLocalidadPadreEcuador, ID_RUC } from '../../../Constants';
import { defaultTasonColor, style, styleLogin as styles } from '../../../styles';
import { onlyNums } from '../../../utils/normalize';
import * as Yup from 'yup';
import { Formik } from 'formik';

import { connect } from "react-redux";
import { compose } from "redux";






class NuevoConductor extends Component {
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
      tiposLicencia: [],
      provincias: [],
      ciudades: [],
      isEmailUsuarioValid:true,
      isUserNameUsuarioValid:true,
      isNumdocUsuarioValid:true,
      dniRequiredLength:10,
      selected:14
    };

    this.name= React.createRef();
    this.lastname= React.createRef();
    this.licenseTypes= React.createRef();
    this.username= React.createRef();
    this.email= React.createRef();
    this.provinces= React.createRef();
    this.city= React.createRef();
    this.phone= React.createRef();
  }

  componentDidMount() {

   this.loadTypesDocProvinciasTypesLic();
    }

    
  onChangeTextDniEmpresa(value) {
   
    if (String(value).length == 13) {
      this.getEmpresaTransporteByRuc(value);
    }
  }


  mostrarCiudades = async (idProvincia) => {
    try {
      this.setState({isLoading: true});

      const response = await this.props.dispatch(
        getLocalidadByPadre(idProvincia,1),
      );
  
      if (response ) {
        this.setState({isLoading:false,ciudades:response.responseBody});
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

  
  
  loadTypesDocProvinciasTypesLic = async () => {
    try {
      this.setState({isLoading: true});

      const response = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoDocumento,1),
      );

      const response1 = await this.props.dispatch(
        getLocalidadByPadre(idLocalidadPadreEcuador,1),
      );

      const response2 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoLicencia),
      );
  
      if (response && response1 && response2) {
        this.setState({isLoading:false,tiposDocumento :response.responseBody,provincias:response1.responseBody,tiposLicencia:response2.responseBody});
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


  getEmpresaTransporteByRuc = async dni => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        getEmpresaTransporteByRuc(dni),
      );

      if (response) {
        this.setState({
          isLoading: false,
          existeEmpresa: (response.responseBody.response !== 'ERROR'),
          clienteRazonSocial: (response.responseBody.response == 'ERROR')?'Empresa de transporte no registrada':response.responseBody.clienteRazonSocial,
        });
        return response;
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
      const response = await this.props.dispatch(
        emailUsuarioValidator(email),
      );
      this.setState({isEmailUsuarioValid:response.valid});
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
      this.setState({isLoading: false,isUserNameUsuarioValid:response.valid});

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
      const response = await this.props.dispatch(
        numdocUsuarioValidator(ruc),
      );
      this.setState({isNumdocUsuarioValid:response.valid});
    
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

  createUsuarioEmpresa = async (json) => {
    try {
      this.setState({isLoading: true});

      const response = await this.props.dispatch(
        createUsuarioEmpresa(json),
      );
  
      if (response ) {
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

  
  
  onProvinceValueChange=(value)=>{
  this.mostrarCiudades(value);
  }

  onEndEditingUserDni=(value)=>{
  this.numdocUsuarioValidator(value);
  }

  onEndEditingUserName=(value)=>{
    this.userNameUsuarioValidator(value);
  }
  onEndEditingEmail=(value)=>{
    this.emailUsuarioValidator(value);
  }


  
  

  
  state = {
    selected: 'null'
  }

  onPress (selected: string) {  
    this.setState({
      selected,
      dniRequiredLength:(ID_RUC == selected)?13:10
    })
  }

  renderForm() {
    const {handleSubmit} = this.props;
    const {selected,dniRequiredLength,provincias,ciudades,tiposLicencia,tiposDocumento,isEmailUsuarioValid,isUserNameUsuarioValid,isNumdocUsuarioValid}= this.state;

    
    return (
      <Container >
      <ScrollView keyboardShouldPersistTaps='always'>
       <Card elevation={4}>
         <View style={style.cardStyle}>
        
         <KeyboardAwareScrollView keyboardShouldPersistTaps="always"
            contentContainerStyle={style.container}>
            <Formik
              initialValues={{
                dniType: '',
                userDni: '',
                name: '',
                lastname:'',
                licenseTypes: '',
                email: '',
                provinceId: '',
                city: '',
                phone: ''
              }}
         
             validationSchema={ Yup.object().shape({
              userDni: Yup.string().required('Campo requerido').
                min(dniRequiredLength,'Mínimo  de caracteres '+dniRequiredLength).matches(/^[0-9\b]+$/,'Ingrese solo número').
                test("isValidUsuarioDni","El número de documento ingresado ya se encuentra registrado",
                  value => {
                   return isNumdocUsuarioValid
                  }
                ),
                name: Yup.string().required('Campo requerido'),
                lastname: Yup.string().required('Campo requerido'),
                licenseTypes: Yup.string().required('Campo requerido'),
                email: Yup.string().required('Campo requerido').email('La dirección ingresada no es un correo valido').
                test("isValidEmailUsuario","El correo electrónico ya se encuentra registrado",
                value => {
                 return isEmailUsuarioValid
                }
              ),
               provinceId: Yup.string().required('Campo requerido').nullable(true),
                city: Yup.string().required('Campo requerido').nullable(true),
                phone: Yup.string().required('Campo requerido'),
                username: Yup.string().required('Campo requerido').
                test("isValidNombreUsuarioo","El nombre de usuario ya se encuentra registrado",
                value => {
                 return isUserNameUsuarioValid
                }
              )
                
              })
              } 

              onSubmit={(values, {resetForm,setSubmitting}) => {
              
   
    let Payload= {
      usuarioRuc: String(values.userDni),
      usuarioTipoDocumento: String(selected),
      usuarioIdDocumento: String(values.userDni),
      usuarioNombres: String(values.name),
      usuarioApellidos: String(values.lastname),
      usuarioNombreUsuario: String(values.username),
      usuarioMail: String(values.email),
      usuarioCelular: String(values.phone),
      usuarioLocalidad: String(values.city),
      usuarioEstado: 6,
      usuarioTipoUsuario: "9",
      vehiculosByUsuarioIdDocumento: [],
      conductorsByUsuarioIdDocumento: [
        {
          conductorApellido: String(values.lastname),
          conductorEmail: String(values.email),
          conductorNombre: String(values.name),
          conductorTelefono: String(values.phone),
          conductorTipoLicencia: String(values.licenseTypes),
          conductorUsuario: String(values.userDni)
        }
      ]
    }

    setSubmitting(true);
this.createUsuarioEmpresa(JSON.stringify(Payload)).then(res=>{
  setSubmitting(false)
}); 
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
                 name={'dniType'}
                 label={
                  tiposDocumento[0]
                    ? tiposDocumento[0].catalogoItemDescripcion
                    : ''
                }
          value={(selected === 14)}
          status={selected === 14 ? 'checked' : 'unchecked'}
          ref={input => {this.usuarioTipoDocumento = input;}}   
          onPress={() => { 
            setFieldValue('dniType',14);
            this.setState({ selected: 14,dniRequiredLength:10 }); }}
        />
        <RadioButon
         name={'dniType'}
         label={
          tiposDocumento[1]
            ? tiposDocumento[1].catalogoItemDescripcion
            : ''
          }
          ref={input => {this.usuarioTipoDocumento = input;}}   
          value={selected === ID_RUC}
          status={selected === ID_RUC ? 'checked' : 'unchecked'}
          onPress={() => { 
            setFieldValue('dniType',13);
             this.setState({ selected: ID_RUC,dniRequiredLength:13 }); }}
        />

            
            </View>



                  <MaterialInput
                    name="userDni"
                    autoFocus
                    label='Número de documento'
                    value={values.userDni}
                    keyboardType={'numeric'}
                    maxLength={dniRequiredLength}
                    placeholder="Número de documento"
                    error={ errors.userDni && touched.userDni?errors.userDni:undefined }
                    onSubmitEditing={() => {this.name.focus(); }}
                    onChangeText={handleChange('userDni')}
                    onBlur={ (e) => {
                      handleBlur('userDni');
                  }}
                  
                    onEndEditing={(evt)=>{
                       this.numdocUsuarioValidator(evt.nativeEvent.text).then(res=>{
                      }); 
                    }}
                  />
          
              
                  <MaterialInput
                    label='Nombres'
                    name="name"
                    autoCapitalize={'words'}
                    value={values.name}
                    placeholder="Nombres"
                    error={ errors.name && touched.name?errors.name:undefined }
                    refField={input => {this.name = input;  }}  
                    onSubmitEditing={() => {this.lastname.focus(); }} 
                    onBlur={handleBlur('name')}
                    onChangeText={handleChange('name')}
                  />
              
              <MaterialInput
                    label='Apellidos'
                    name="lastname"
                    autoCapitalize={'words'}
                    value={values.lastname}
                    placeholder="Apellidos"
                    error={!values.lastname && errors.lastname && touched.lastname?errors.lastname:undefined }
                    refField={input => {this.lastname = input;  }}   
                    onChangeText={handleChange('lastname')}
                    onBlur={handleBlur('lastname')}
                    onSubmitEditing={() => {this.username.focus(); }} 
                  />

<MaterialInput
                    label='Nombre de usuario'
                    name="username"
                    value={values.username}
                    maxLength={12}
                    placeholder="Nombre de usuario"
                    error={errors.username && touched.username?errors.username:undefined }
                    refField={input => {this.username = input}}   
                    onChangeText={handleChange('username')}
                    onBlur={handleBlur('username')}
                    onEndEditing={(evt)=>{
                      this.userNameUsuarioValidator(evt.nativeEvent.text).then(res=>{
                     }); 
                   }}
                    
                  />



              <DropDownList  name="licenseTypes"
              data={tiposLicencia}
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              error={ errors.licenseTypes && touched.licenseTypes?errors.licenseTypes:undefined }
              placeholder=""
              description={'Seleccione tipo de licencia'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('licenseTypes',itemValue);
              }
            }/> 


              <MaterialInput
                    label='Correo electrónico'
                    name="email"
                    keyboardType={'email-address'}
                    value={values.email}
                    placeholder="Correo electrónico"
                    error={ errors.email && touched.email?errors.email:undefined }
                    refField={input => {this.email = input;}}   
                    onChangeText={handleChange('email')}
                    onBlur={handleBlur('email')}
                    onEndEditing={(evt)=>{
                      this.emailUsuarioValidator(evt.nativeEvent.text).then(res=>{
                     }); 
                   }}
                  />


<Text style={[style.collapseHeaderText,{paddingTop:10,paddingBottom:10}]}>Datos de contacto</Text>




<DropDownList name="provinceId"
              data={provincias}
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              error={ errors.provinceId && touched.provinceId?errors.provinceId:undefined }
              placeholder=""
              refField={input => {this.provinces = input;}}   
              description={'Seleccione la provincia'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('provinceId',itemValue);
               this.onProvinceValueChange(itemValue) ;
               
               }
            }/> 

              <DropDownList name="city"
              data={ciudades}
              itemValue="localidadId"
              itemLabel="localidadDescripcion"
              error={ errors.city && touched.city?errors.city:undefined }
              placeholder=""
              refField={input => {this.city = input;}}
              description={'Seleccione la ciudad'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('city',itemValue);
              }
            }/> 

         
<MaterialInput
                    label='Teléfono'
                    name="phone"
                    value={values.phone}
                    refField={input => {this.phone = input;}}
                    keyboardType={'numeric'}
                    maxLength={10}
                    placeholder="Teléfono"
                    error={!values.phone && errors.phone && touched.phone?errors.phone:undefined }
                    onChangeText={handleChange('phone')}
                    onBlur={handleBlur('phone')}
                  />

{/* {errors.dniType?<Text>ERRO 1</Text>:null}
{errors.userDni?<Text>ERRO 2</Text>:null}
{errors.name?<Text>ERRO 3</Text>:null}
{errors.licenseTypes?<Text>ERRO 4</Text>:null}
{errors.email?<Text>ERRO 5</Text>:null}
{errors.provinces?<Text>ERRO 6</Text>:null}
{errors.city?<Text>ERRO 7</Text>:null}
{errors.phone?<Text>ERRO 8s</Text>:null} */}
      
      

              <View style={ style.twoButtonsArea}  /* pointerEvents={ isSubmitting?'none':'auto'} */  >
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>Actions.pop() 
          } backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Guardar</Text>
             </AwesomeButtonRick>
          </View>

                </View>
              )}
            </Formik>
          </KeyboardAwareScrollView> 
      
         </View>
       </Card>
       </ScrollView>
    

      </Container>
    );
  }


  hideAlert = () => {
    this.setState({
      showAlert: false
    });

    if (this.state.isPostedSuccessfully) {
      Actions.reset('root');
    }
};

  dismissAlert= () => {

if(this.state.showAlert){
    this.setState({
    showAlert: false
  });
}





};


  render() {
    const {onSubmit} = this.props;
    const {showAlert,textAlert,titleAlert,isErrorAlert,clienteRazonSocial, existeEmpresa, isLoading} = this.state;
    return (
      
 <Container style={[style.container]}>
 
    {isLoading && <Loader />}

       <View style={style.formView}>  
<Formik
  initialValues={{
    clienteRuc: ''
  }}
 validationSchema={ Yup.object().shape({
    clienteRuc: Yup.string().required('Campo requerido').
    min(13,'Mínimo  de caracteres 13').matches(/^[0-9\b]+$/,'Ingrese solo número').
    test("isValidEmpresaDni","El número de documento ingresado ya se encuentra registrado",
      value => {
       return existeEmpresa
      }
    )
  })
  } 

  onSubmit={(values, {resetForm,setSubmitting}) => {

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
  
  

<View style={{marginRight: 85, marginLeft: 85, marginTop:40,marginBottom: 5}}>
      <MaterialInput
        name="clienteRuc"
        autoFocus
        label='Número de documento'
        value={values.clienteRuc}
        keyboardType={'numeric'}
        maxLength={13}
        placeholder="Número de documento"
        error={ errors.clienteRuc && touched.clienteRuc?errors.clienteRuc:undefined }
        onChangeText={ e => {
            setFieldValue('clienteRuc', e);
            this.onChangeTextDniEmpresa(e);
         }}/>

    </View>
    <Text style={existeEmpresa ? style.successInfoText : style.errorInfoText}>{clienteRazonSocial}</Text>


    </View>
  )}
</Formik>



{existeEmpresa ? this.renderForm():
<View style={{padding: 5, width: '100%'}}>
<Text style={[style.textInfo,{paddingTop:10}]}>INGRESE EL RUC DE LA COMPAÑIA DE TRANSPORTE O TRANSPORTISTA, PARA QUIEN VA A CONDUCIR.</Text>
<Text style={[style.textData,{paddingTop:10}]}>Nota: La compañia de transporte o transportista bajo el cual se va a registrar ya debe estar registrado en TAS-ON. Caso contrario, primero registrar la compañia o el transportista.</Text>

<View style={{paddingTop:100}}>
<AwesomeButtonRick height={50} stretch={true} style={[style.defaultButton]} onPress={()=>Actions.pop() 
          } backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Regresar</Text></AwesomeButtonRick>
</View>

</View>
} 
</View>


{showAlert &&  <AwesomeAlert
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
confirmButtonTextStyle={isErrorAlert?style.alertConfirmButtonErrorTextStyle:style.alertConfirmButtonTextStyle}
titleStyle={style.alertTitleStyle}
onConfirmPressed={() => {
this.hideAlert();
}}
onDismiss={() => {
this.dismissAlert();
}}
/>}

      </Container>
    );
  }
}



export default reduxForm({
  form: 'EnrolamientoForm', // a unique identifier for this form
 })(NuevoConductor)


