import { Icon, Toast } from 'native-base';
import React, { Component } from "react";
import { KeyboardAvoidingView, Platform, ScrollView, View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { required } from 'redux-form-validators';
import { logoutUser, updatePassword } from "../actions/auth.actions";
import { AwesomeAlert, AwesomeButtonRick, CustomInputComponent, Loader, Logo, Text } from "../components/";
import { SERVICE_ERROR_STATUS } from '../Constants';
import { defaultTasonColor, style, styleLogin as styles } from '../styles/';
import { no_connected_info } from '../utils/tas-on-texts';
const utf8 = require('utf8');

class CambioDeClaveScreen extends Component {
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
    }


  }

  
  logoutUser = () => {
    this.props.dispatch(logoutUser());
}


  componentDidMount() {
  this.props.change('username',global.username);
 
  }



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
 this.props.change('username',values.username?values.username:null);  
   
if(!values.oldPassword && !values.newPassword &&!values.confirmPassword ){
  return;
}

  this.updatePassword(values);


  };



updatePassword = async values => {

  try {
  const newPassword=JSON.stringify(values);
   const response = await this.props.dispatch(updatePassword(newPassword));

   if (response.success) {
    
    Toast.show({
      text: response.responseBody.responseMessage,
      duration: 3000,
      position: "top",
      textStyle:{textAlign:'center'}
    });

 } else {
     if(response.responseBody && response.responseBody.status == SERVICE_ERROR_STATUS){
       throw response.responseBody;
      }else{
        throw response;
      }
  }
  

}catch (error) {
    console.log('CATCH ERROR  ',error)
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
 }

  
  cancel=()=>{
    this.logoutUser();
    Actions.reset('root');
  }
  onInputSubmit = () => {
    let values={username : this.props.username, password:this.props.password }
    this.updatePassword(values);
    };


  hideAlert = () => {
    this.setState({
      showAlert: false
    });
    Actions.reset('root');
  };

  dismissAlert= () => {
    if(this.state.showAlert){
  this.setState({
    showAlert: false
  });
}
};




render() {
    const { handleSubmit, updatePassword,loginUser } = this.props;
    const { showAlert,textAlert,titleAlert,isErrorAlert } = this.state;

    return (
      <View style={styles.container}>
        {updatePassword && updatePassword.isLoading && <Loader />}
     
        <View style={styles.logoSection}>
          <Logo />
        </View>

        <KeyboardAvoidingView style={{ flex: 1,paddingTop:15, flexDirection: 'column',justifyContent: 'center'}} behavior={Platform.select({android: undefined, ios: 'padding'})} enabled  >
    <ScrollView keyboardShouldPersistTaps='always'>
        <View style={styles.formSection}>

          
        <Field
            component={CustomInputComponent}
            name="username"
            type="text"
            placeholder="Usuario"
            editable={false}
            securefield={false}
            validate={[required({msg: 'Campo requerido'})]}
   
          />

            <Field
            component={CustomInputComponent}
            name="oldPassword"
            type="text"
            placeholder="Contraseña actual"
            securefield={true}
            autoFocus 
            validate={[required({msg: 'Campo requerido'})]}
          />

          <Field
            component={CustomInputComponent}
            name="newPassword"
            type="text"
            placeholder="Nueva contraseña"
            securefield={true}
            validate={[required({msg: 'Campo requerido'})]}
   
          />

          <Field
            component={CustomInputComponent}
            name="confirmPassword"
            type="text"
            placeholder="Confirmar contraseña"
            securefield={true}
            validate={[required({msg: 'Campo requerido'})]}
            refField={input => {
              this.password = input;
            }}
            onSubmitEditing={() => {
              this.onInputSubmit(this)
              }}
          />   
       
              
       <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.cancel()} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.onSubmit)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Actualizar</Text>
             </AwesomeButtonRick>
          </View>

        </View>
    </ScrollView>
</KeyboardAvoidingView>


{showAlert && <AwesomeAlert
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
          />
          }
      </View>
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


const selector = formValueSelector('RecuperarClaveForm') 

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
    form: "CambioDeClaveForm",
    fields: ["username","oldPassword","newPassword","confirmPassword"],
    validate,
    enableReinitialize: true
  })
)(CambioDeClaveScreen);
