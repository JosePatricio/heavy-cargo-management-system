import { Icon, onlyNums } from 'native-base';
import React, { Component } from "react";
import { Keyboard, KeyboardAvoidingView, Platform, ScrollView, View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { length, required } from 'redux-form-validators';
import { restablecerPassword } from "../actions/auth.actions";
import { AwesomeAlert, AwesomeButtonRick, CustomInputComponent, Loader, Logo, Text } from "../components/";
import { defaultTasonColor, style, styleLogin as styles } from '../styles/';
import { no_connected_info } from '../utils/tas-on-texts';

const utf8 = require('utf8');

class RecuperarClaveScreen extends Component {
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

  

  componentDidMount() {
   /*  this.props.change('dni','1724449432001');
    this.props.change('email','patricioestocolmo@gmail.com');
   */
  }


  
 
  recoverPassword = async values => {
    
    Keyboard.dismiss();
  
     try {
      const response = await this.props.dispatch(restablecerPassword(values.email,values.dni));
   
      
      if(response.success){

        if(response.responseBody.response == 'ERROR'){
          this.setState({
            showAlert: true,
            textAlert: response.responseBody.responseMessage,
            titleAlert: "Error",
            isErrorAlert:true,
            isLoading: false
          }); 
        }else{
          this.setState({
            showAlert: true,
            textAlert: response.responseBody.responseMessage,
            titleAlert: 'Mensaje',
            isErrorAlert: false,
            isLoading: false,
            isPostedSuccessfully:
              response.responseBody.response == 'ERROR' ? false : true,
          });
      }
      }
      else {
        throw response;
      }
    
    } catch (error) {
    console.log('ERRROR recoverPassword  ',error);
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
     
     this.setState({
        showAlert: true,
        textAlert: utf8.decode(errorText),
        titleAlert: "Error",
        isErrorAlert:true,
        isLoading: false
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
  this.recoverPassword(values);
  };

  cancel=()=>{
    Actions.reset('login');
  }
  onInputSubmit = () => {
    let values={user : this.props.user, password:this.props.password }
    this.recoverPassword(values);
    };


  hideAlert = () => {
    this.setState({
      showAlert: false
    });
    if(this.state.isPostedSuccessfully){
      Actions.reset('login');
    }
};

  dismissAlert= () => {
    if(this.state.showAlert){
  this.setState({
    showAlert: false
  });}
};




render() {
    const { handleSubmit, restablecerPassword } = this.props;
    const { showAlert,textAlert,titleAlert,isErrorAlert } = this.state;
    return (
      <View style={styles.container}>
        {restablecerPassword && restablecerPassword.isLoading && <Loader />}
     
        <View style={styles.logoSection}>
          <Logo />
        </View>


        <KeyboardAvoidingView style={{ flex: 1,paddingTop:15, flexDirection: 'column',justifyContent: 'center',}} behavior={Platform.select({android: undefined, ios: 'padding'})} enabled  >
    <ScrollView keyboardShouldPersistTaps='always'>
        <View style={styles.formSection}>

        
            <Field
            component={CustomInputComponent}
            name="email"
            type="text"
            placeholder="Correo electrónico"
            onSubmitEditing={() => {
            //  this.password.focus();
            }}
            securefield={false}
            autoFocus 
            validate={[required({msg: 'Campo requerido'})]}
          />

          <Field
            component={CustomInputComponent}
            name="dni"
            type="text"
            placeholder="Documento de identidad"
            securefield={false}
            normalize={onlyNums}
            keyboardType={'numeric'}
            maxLength={13}
            validate={[length({
            msg: 'Mínimo de caracteres 13',
            min: 13,
            max: 13,
            }),required({msg: 'Campo requerido'})]}

            refField={input => {
              this.password = input;
            }}
            onSubmitEditing={() => {
              this.onInputSubmit(this)
              }}
              validate={[required({msg: 'Campo requerido'})]}
          />   
       
       <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.cancel()} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Anterior</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.onSubmit)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Siguiente</Text>
             <Icon name='right' type='AntDesign' style={style.defaultIconButton}/>
             </AwesomeButtonRick>
          </View>


           
        </View>
    </ScrollView>
</KeyboardAvoidingView>


{showAlert &&<AwesomeAlert
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
       
      </View>
    );
  }
}



const selector = formValueSelector('RecuperarClaveForm') 

const mapStateToProps = state => {
  return {
    restablecerPassword: state.authReducer.recoverPassword,
    generalReducer: state.generalReducer
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
    form: "RecuperarClaveForm",
    fields: ["email","dni"],
    enableReinitialize: true
  })
)(RecuperarClaveScreen);
