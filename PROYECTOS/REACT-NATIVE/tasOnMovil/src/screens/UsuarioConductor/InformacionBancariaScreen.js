import { Card, Content,Container, Icon } from 'native-base';
import React, { Component } from "react";
import { KeyboardAvoidingView, Platform, ScrollView, View } from "react-native";
import { Overlay, Input } from 'react-native-elements';
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { required } from 'redux-form-validators';
import { solicitarActualizarInformacionBancaria, updateClienteInfoBancaria } from "../../actions/auth.actions";
import { getClienteInfoBancaria } from "../../actions/cliente.actions";
import { obtenerCatalogoItem } from "../../actions/general.action";
import { KeyboardAwareScrollView,MaterialInput,AwesomeAlert, AwesomeButtonRick, CustomInputComponent, DropDownList, Loader, Moment, Text } from "../../components/";
import { idCatalogoBanco, idCatalogoDocumento, idCatalogoTipoCuenta, ID_RUC } from '../../Constants';
import { defaultTasonColor, style } from '../../styles/';
import { onlyNums } from '../../utils/normalize';
import { no_connected_info } from '../../utils/tas-on-texts';
import { deviceHeight } from '../../utils/util';
import * as Yup from 'yup';
import { Formik } from 'formik';

const utf8 = require('utf8');

const noData='Sin registrar';
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
      <View style={{paddingBottom: 10}}>
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

  
class InformacionBancariaScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
     currentUser:{}
     ,password: '',
     showAlert: false,
     textAlert: "",
     titleAlert: "",
     isErrorAlert:false,
     isPostedSuccessfully: false,
     bancos:[],tiposCuenta:[],tiposIdentificacion:[],
     infoBancaria:{},
     modalPasswordVisible:false,
     isRequestApproved:false,
     loadDefaultData:false,
     dniRequiredLength:10,

     clienteBanco: {},
     clienteTipoCuenta: {},
     clienteCuenta: {},
     tipoIdentificacion:{},
     identificacion: {},
     clienteNombres: {}

    }

    this.clienteCuenta= React.createRef();

  }

  

  componentDidMount() {
  this.loadUserBankData();
 
  }

  loadUserBankData = async () => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getClienteInfoBancaria());

      console.log('response ',response)
      if ( response
      ) {
        this.setState({
          isLoading: false,
          infoBancaria:response.responseBody,
          
          clienteBanco: String(response.responseBody.banco),
          clienteTipoCuenta: String(response.responseBody.tipoCuenta),
          clienteCuenta: response.responseBody.numeroCuenta,
          tipoIdentificacion:String(response.responseBody.tipoIdentificacion),
          identificacion: response.responseBody.identificacion,
          clienteNombres: response.responseBody.nombres
        });
  
    
      console.log(response.responseBody);

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


  loadDefaultData = async () => {
    try {
     this.setState({isLoading:true});
      const response = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoTipoCuenta),
      );
      const response1 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoBanco),
      ); 

      const response2 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoDocumento),
      );
     

      if ( response && response1 && response2
      ) {
        this.setState({
          isLoading: false,
          tiposCuenta: response.responseBody,
          bancos: response1.responseBody,
          tiposIdentificacion:response2.responseBody,
        });

    
      
        this.props.change('clienteBanco',String(this.state.infoBancaria.banco));
        this.props.change('clienteTipoCuenta',String(this.state.infoBancaria.tipoCuenta));
        this.props.change('clienteCuenta',this.state.infoBancaria.numeroCuenta);
        this.props.change('tipoIdentificacion',String(this.state.infoBancaria.tipoIdentificacion));
        this.props.change('identificacion',this.state.infoBancaria.identificacion);
    

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



  onSubmit = values => {
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
 this.props.change('clienteNombres',values.clienteNombres?values.clienteNombres:null);  
   
if(!values.clienteBanco && !values.clienteTipoCuenta &&!values.clienteCuenta&& !values.tipoIdentificacion &&!values.identificacion ){
  return;
}

const {bancos,tiposCuenta,tiposIdentificacion}= this.state;


 let payload= {
    banco: Number(values.clienteBanco),
    tipoCuenta: Number(values.clienteTipoCuenta),
    numeroCuenta: values.clienteCuenta,
    tipoIdentificacion: Number(values.tipoIdentificacion),
    identificacion: values.identificacion,
    nombres: values.clienteNombres
  } 
  
  console.log('pay;;opda  ',payload);
this.updateBankInfo(payload);
};








openPasswordModal=()=>{
    this.setState({modalPasswordVisible:true});
}


updateBankInfo = async values => {

    try {
     this.setState({isLoading:true});
     const response = await this.props.dispatch(updateClienteInfoBancaria(JSON.stringify(values)));
  
      if(response.success){
  
      this.setState({
        showAlert: true,
        isRequestApproved:false,
        textAlert: response.responseBody.responseMessage,
        titleAlert: 'Mensaje',
        isErrorAlert: (response.responseBody.response == 'ERROR'),
        isLoading: false,
        modalDriverFormVisible:false,
        isPostedSuccessfully: response.responseBody.response == 'ERROR' ? false : true,
          isConfirmation:false,
          loadDefaultData:false
      });
    
  
    }
    else {
      throw response;
    } 
    
  
  }catch (error) {
      console.log('CATCH ERROR updateBankInfo ',error)
       let errorText;
      errorText = JSON.stringify(error);
      if (error.responseBody) {
        errorText = error.responseBody.responseMessage;
      }
     
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: "Error",
        isErrorAlert:true,
        isLoading:false,
        loadDefaultData:false,
        isPostedSuccessfully:false
      }); 
  
    }
   
  }

  

requestBankInfoEdit = async values => {

  try {
   this.setState({isLoading:true,modalPasswordVisible:false});
   const response = await this.props.dispatch(solicitarActualizarInformacionBancaria(values));

    if(response.success){

      console.log('RESPUESTA  ',response);
       /*   this.setState({
            showAlert: true,
            isRequestApproved:(response.responseBody.response != 'ERROR'),
            textAlert: response.responseBody.responseMessage,
            titleAlert: 'Mensaje',
            isErrorAlert: (response.responseBody.response == 'ERROR'),
            isLoading: false,
            modalDriverFormVisible:false,
            isPostedSuccessfully:
              response.responseBody.response == 'ERROR' ? false : true,
              isConfirmation:false,
              loadDefaultData:true
              
          });  */

this.setState({isLoading: false,showAlert: true,textAlert: response.responseBody.responseMessage,
  isPostedSuccessfully: (response.responseBody.response !== 'ERROR'),
  isErrorAlert: (response.responseBody.response == 'ERROR'),
  loadDefaultData:true,titleAlert: 'Mensaje',
  isRequestApproved:(response.responseBody.response != 'ERROR'),
});

          return response;
     

  }
  else {
    throw response;
  } 
  

}catch (error) {
    console.log('CATCH ERROR requestBankInfoEdit ',error)
     let errorText;
    errorText = JSON.stringify(error);
    if (error.responseBody) {
      errorText = error.responseBody.responseMessage;
    }
   
    this.setState({
      showAlert: true,
      textAlert: errorText,
      titleAlert: "Error",
      isErrorAlert:true,
      isLoading:false
    }); 

  }
 
}


state = {
  selected: 'null',
};

onTipoDocumentoValueChange = selected => {
  this.setState({
    selected,
    dniRequiredLength: ID_RUC == selected ? 13 : 10,
  });
};

  cancel=()=>{
    Actions.pop();
  }

  hideAlert = () => {
    this.setState({
      showAlert: false
    });

    if(this.state.isPostedSuccessfully){
    if(this.state.loadDefaultData == true){
    this.loadDefaultData();
      }
    else{
        Actions.pop();
      }
    }
  };

  dismissAlert= () => {
    if(this.state.showAlert){
  this.setState({
    showAlert: false
  });
}
};




renderFormPassword() {
    const {handleSubmit} = this.props;
   

    return (
      <Container>
         
<Card elevation={4} >
          <View style={style.cardStyle}>

          <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Ingrese su contraseña
            </Text>

          <ScrollView keyboardShouldPersistTaps='always'>
<KeyboardAwareScrollView keyboardShouldPersistTaps="always"
            contentContainerStyle={style.container}>
        

            <Formik
              initialValues={{
                clientePassword: ''
              }}
         
             validationSchema={ Yup.object().shape({
              clientePassword: Yup.string().required('Campo requerido'),
              })
              } 

              onSubmit={(values, {resetForm,setSubmitting}) => {
            
                this.requestBankInfoEdit(values.clientePassword).then(res=>{
                  console.log(res);
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
                <View style={style.formView}>




          <MaterialInput
          label='Contraseña'
          name="clientePassword"
          value={values.clientePassword}
          placeholder="Contraseña"
          error={!values.clientePassword && errors.clientePassword && touched.clientePassword?errors.clientePassword:undefined }
          autoFocus
          securefield={true}
          onBlur={handleBlur('clientePassword')}
          onChangeText={handleChange('clientePassword')}
        />
     
              
      
     <View style={ [style.twoButtonsArea,{paddingBottom:10}]}>
          <AwesomeButtonRick height={50} stretch={true} style={[style.defaultButtonInArea1]} onPress={() => this.setState({ modalPasswordVisible: false })} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Cerrar</Text>
             </AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={[style.defaultButtonInArea2]} onPress={handleSubmit} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Aceptar</Text></AwesomeButtonRick>
        </View>



        </View>


    
              )}
            </Formik>
      
      
          </KeyboardAwareScrollView> 
        
          </ScrollView>        
          </View>
          </Card>
      
      </Container>
     



    );
  }

  

render() {
    const { handleSubmit, updatePassword,loginUser } = this.props;
    const {  clienteBanco, clienteTipoCuenta,  clienteCuenta,  tipoIdentificacion, identificacion,
      clienteNombres , dniRequiredLength,modalPasswordVisible,isRequestApproved,showAlert,textAlert,
      titleAlert,isErrorAlert ,isLoading,infoBancaria,bancos,tiposCuenta,tiposIdentificacion} = this.state;
   
    return (
   <Container>
        {(updatePassword && updatePassword.isLoading) || isLoading && <Loader />}
   
          <ScrollView keyboardShouldPersistTaps='always'>

         <KeyboardAwareScrollView keyboardShouldPersistTaps="always"
            contentContainerStyle={style.container}>
            <Formik
              enableReinitialize={true}
              initialValues={infoBancaria}

           
             validationSchema={ Yup.object().shape({
              banco: Yup.string().required('Campo requerido').nullable(true),
              tipoCuenta: Yup.string().required('Campo requerido').nullable(true),
              numeroCuenta: Yup.string().required('Campo requerido'),
              tipoIdentificacion: Yup.string().required('Campo requerido').nullable(true),
              identificacion: Yup.string().required('Campo requerido'),
              nombres: Yup.string().required('Campo requerido'),
            
              //clienteBanco: Yup.string().required('Campo requerido').nullable(true),
              //clienteTipoCuenta: Yup.string().required('Campo requerido').nullable(true),
             // clienteCuenta: Yup.string().required('Campo requerido'),
              //tipoIdentificacion:Yup.string().required('Campo requerido').nullable(true),
             // identificacion: Yup.string().required('Campo requerido'),
            //  clienteNombres: Yup.string().required('Campo requerido')
              })
              } 

              onSubmit={(values, {resetForm,setSubmitting}) => {
    
             
        let payload={clienteBanco: values.banco,
              clienteTipoCuenta: values.tipoCuenta,
              clienteCuenta: values.numeroCuenta,
              tipoIdentificacion: values.tipoIdentificacion,
              identificacion: values.identificacion,
              clienteNombres: values.nombres
            };

            setSubmitting(true);
                this.updateBankInfo(values).then(res=>{
setSubmitting(false);
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
                <View style={style.formView}>

    {isRequestApproved? 
            <DropDownList  name="banco"
              data={bancos}
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              error={ errors.banco && touched.banco?errors.banco:undefined }
              value={String(values.banco)}
              selectedValue={infoBancaria.banco}
              placeholder="Seleccione banco"
              description={'Bancos'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('banco',itemValue);
              }
            }/> 
            : <View style={{padding: 5, width: '100%'}}>
            <Text style={style.textInfo}>Banco</Text>
            <Text style={style.textData}>{infoBancaria.bancoDesc?infoBancaria.bancoDesc:noData}</Text>
          </View> }
 
          {isRequestApproved?
            <DropDownList  name="tipoCuenta"
              data={tiposCuenta}
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              selectedValue={infoBancaria.tipoCuenta}
              value={String(values.tipoCuenta)}
              error={ errors.tipoCuenta && touched.tipoCuenta?errors.tipoCuenta:undefined }
              placeholder="Seleccione"
              description={'Tipos de cuenta'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('tipoCuenta',itemValue);
              }
            }/>
         : <View style={{padding: 5, width: '100%'}}>
            <Text style={style.textInfo}>Tipo de cuenta</Text>
            <Text style={style.textData}>{infoBancaria.tipoCuentaDesc?infoBancaria.tipoCuentaDesc:noData}</Text>
          </View> }


 
     {isRequestApproved?
          <MaterialInput
          label='Número de cuenta'
          name="numeroCuenta"
          keyboardType={'numeric'}
          maxLength={20}
          refField={input => {this.clienteCuenta = input;  }}  
          value={values.numeroCuenta}
          defaultValue={infoBancaria.numeroCuenta}
          placeholder="Número de cuenta"
          error={errors.numeroCuenta && touched.numeroCuenta?errors.numeroCuenta:undefined }
          autoFocus
          onBlur={handleBlur('numeroCuenta')}
          onChangeText={handleChange('numeroCuenta')}
        />
      : <View style={{padding: 5, width: '100%'}}>
          <Text style={style.textInfo}>Cuenta</Text>
          <Text style={style.textData}>{infoBancaria.numeroCuenta?infoBancaria.numeroCuenta:noData}</Text>
        </View> }



        {isRequestApproved?
            <DropDownList  name="tipoIdentificacion"
              data={tiposIdentificacion}
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              value={String(values.tipoIdentificacion)}
              selectedValue={infoBancaria.tipoIdentificacion}
              error={ errors.tipoIdentificacion && touched.tipoIdentificacion?errors.tipoIdentificacion:undefined }
              placeholder="Tipos de identificación"
              description={'Seleccione tipo de identificación'}
              onValueChange={(itemValue, itemIndex) => {
                setFieldValue('tipoIdentificacion',itemValue);
                this.onTipoDocumentoValueChange(itemValue);
              }
            }/>
            : <View style={{padding: 5, width: '100%'}}>
            <Text style={style.textInfo}>Tipo de identificación</Text>
            <Text style={style.textData}>{infoBancaria.tipoIdentificacionDesc?infoBancaria.tipoIdentificacionDesc:noData}</Text>
          </View> }


          {isRequestApproved? 
           <MaterialInput
           label='Número identificación'
           name="identificacion"
           keyboardType={'numeric'}
           maxLength={dniRequiredLength}
           value={values.identificacion}
           defaultValue={infoBancaria.numeroCuenta}
           placeholder="Número identificación"
           error={!values.identificacion && errors.identificacion && touched.identificacion?errors.identificacion:undefined }
           maxLength={dniRequiredLength}
           onBlur={handleBlur('identificacion')}
           onChangeText={handleChange('identificacion')}
         />
           : <View style={{padding: 5, width: '100%'}}>
           <Text style={style.textInfo}>Identificación</Text>
           <Text style={style.textData}>{infoBancaria.identificacion?infoBancaria.identificacion:noData}</Text>
         </View> }

         {isRequestApproved?
           <MaterialInput
           label='Nombres'
           name="nombres"
           autoCapitalize={'words'}
           value={values.nombres}
           defaultValue={infoBancaria.nombres}
           placeholder="Nombres"
           error={!values.nombres && errors.nombres && touched.nombres?errors.nombres:undefined }
           onBlur={handleBlur('nombres')}
           onChangeText={handleChange('nombres')}
         />
           : <View style={{padding: 5, width: '100%'}}>
           <Text style={style.textInfo}>Nombres</Text>
           <Text style={style.textData}>{infoBancaria.nombres?infoBancaria.nombres:noData}</Text>
         </View> }

 
    <View style={{padding: 5, width: '100%'}}>
                    <Text style={style.textInfo}>Última actualización</Text>
                    <Text style={style.textData}>
                        {infoBancaria.ultimaActualizacion?
                          <Moment element={Text} format="DD/MM/YYYY hh:mm">
                            {infoBancaria.ultimaActualizacion}
                          </Moment>:<Text style={style.textData}>{noData}</Text>}
                        </Text>
                  </View>

                  {!isRequestApproved?<Text></Text>: <View style={{padding: 5, width: '100%'}}>
           <Text style={[style.textInfo,{paddingTop:10}]}>* Estimado usuario, revise que la información se encuentra ingresada correctamente ya que la misma será utilizada para el pago de los servicios brindados por su empresa.</Text>
         </View> }
     
            
       <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.cancel()} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
           <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2}
           onPress={ isRequestApproved?handleSubmit:this.openPasswordModal.bind(this)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Actualizar</Text>
             </AwesomeButtonRick> 


          </View>
 
        </View>


    
              )}
            </Formik>
          </KeyboardAwareScrollView> 
     
          </ScrollView>
          <Overlay width={'95%'} height={(deviceHeight/3)}
  isVisible={modalPasswordVisible}
  onBackdropPress={() => this.setState({ modalPasswordVisible: false })}
>
  
  {this.renderFormPassword()}
  
</Overlay>
    


{showAlert &&  <AwesomeAlert
            show={showAlert}
            showProgress={false}
            title={titleAlert}
            message={textAlert}
            closeOnTouchOutside={!this.state.isPostedSuccessfully}
            closeOnHardwareBackPress={false}
            showCancelButton={false}
            showConfirmButton={true}
            confirmText="Aceptar"
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


const validate = values => {
  const errors = {};
 
  if (!values.oldPassword) {
    errors.oldPassword = "Campo requerido";
   }
  if (!values.newPassword) {
    errors.newPassword = "Campo requerido";
  }
  if (!values.confirmPassword) {
    errors.confirmPassword = "Campo requerido";
  }
  if (values.confirmPassword && values.newPassword != values.confirmPassword) {
    errors.confirmPassword = "Contraseñas no coinciden";
  }
  return errors;
};


const selector = formValueSelector('InformacionBancariaForm') 

const mapStateToProps = state => {
  return {
    updatePassword: state.authReducer.updatePassword,
    generalReducer: state.generalReducer,
    usuario: state.userReducer.getUser.userDetails,
    loginUser: state.authReducer.loginUser,
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
    form: "InformacionBancariaForm",
    validate,
    enableReinitialize: true
  })
)(InformacionBancariaScreen);