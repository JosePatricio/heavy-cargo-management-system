import { Formik } from 'formik';
import { Container, Toast } from 'native-base';
import React, { Component } from 'react';
import { Keyboard, TouchableOpacity, View,ScrollView } from 'react-native';
import firebase from 'react-native-firebase';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import * as Yup from 'yup';
import { deviceFcmToken } from '../../src/actions/general.action';
import { loginUser, logoutUser } from '../actions/auth.actions';
import { AwesomeAlert, AwesomeButtonRick,MaterialInput, KeyboardAwareScrollView, Loader, Logo, Text } from '../components/';
import { defaultTasonColor, style, styleLogin as styles } from '../styles/';
import { no_connected_info } from '../utils/tas-on-texts';
import { encodedUrl, isValidRole } from '../utils/util';

const utf8 = require('utf8');

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: {},
      password: '',
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert: false,
      value: '',
      stopButtonAnimation:undefined
    };

    this.password = React.createRef();
  }

  componentDidMount() {
    /*  this.props.change("user","empcli1");
  this.props.change("password","51rxyPi.");  */
  }

  signup() {
    Actions.signup();
  }

  logoutUser = () => {
    this.props.dispatch(logoutUser());
  };

  loginUser = async (values, resetForm) => {
    try {

      if (this.props.generalReducer.netWorkInfo.status == false) {
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

      Keyboard.dismiss();

      const fcmToken = await firebase.messaging().getToken();

      let formData = {
        username: values.user,
        password: values.password,
        fcmToken: fcmToken,
      };

      console.log('MI TOKEN FCM ES  ',fcmToken);

      const loginData = encodedUrl(formData);
      const response = await this.props.dispatch(loginUser(loginData));
      console.log('response  ', response);
      this.state.stopButtonAnimation();
      if (response) {

        if (response.responseBody.response == 'ERROR') {
          this.setState({
            showAlert: true,
            textAlert: response.responseBody.responseMessage,
            titleAlert: 'Error',
            isErrorAlert: true,
          });
         
        //  resetForm();
          return;
        } else {
          if (isValidRole(Number(response.responseBody.tipoUsuario))) {
            if (response.responseBody.estadoUsuario !== '5') {
              this.props.dispatch(deviceFcmToken(fcmToken));
              Toast.show({
                text: 'Bienvenido ' + String(response.responseBody.nombres),
                duration: 3000,
                position: 'bottom',
                textStyle: {textAlign: 'center'},
              });
            }
          } else {
            Toast.show({
              text:
                'Lo sentimos: Este tipo de usuario no esta habilitado para usar la aplicación.',
              duration: 5000,
              position: 'top',
              textStyle: {textAlign: 'center'},
            });

            this.logoutUser();
          }
        }
      } else {
        this.setState({
          showAlert: true,
          textAlert: response,
          titleAlert: 'Error',
          isErrorAlert: true,
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

 

  onRecoverPassword = () => {
    Actions.RecuperarClave();
  };

  onInputSubmit = () => {
    if (this.props.generalReducer.netWorkInfo.status == false) {
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
    let values = {user: this.props.user, password: this.props.password};
    this.loginUser(values);
  };

  onRegister = () => {
    Actions.Enrolamiento();
  };

  hideAlert = () => {
    this.setState({
      showAlert: false,
    });
  };

  dismissAlert = () => {
    if (this.state.showAlert) {
      this.setState({
        showAlert: false,
      });
    }
  };

  render() {
    const {handleSubmit, loginUser} = this.props;
    const {showAlert, textAlert, titleAlert, isErrorAlert} = this.state;

    return (
<Container style={[styles.container]}>      
          {loginUser && loginUser.isLoading && <Loader hideIndicator={true} />}

          <View
            style={[styles.logoSection, {paddingBottom: 80, paddingTop: 80}]}>
            <Logo />
          </View>
          <ScrollView keyboardShouldPersistTaps='always'>
          <KeyboardAwareScrollView keyboardShouldPersistTaps="always"
            contentContainerStyle={style.container}>
            <Formik
              initialValues={{
                user: '',
                password: '',
              }}
              validationSchema={ Yup.object().shape({
                user: Yup.string().required('Campo requerido'),
                password: Yup.string().required('Campo requerido'),
              })
              }
              
           
        
              onSubmit={(values, {resetForm,setSubmitting}) => {
                
                setSubmitting(true);
                this.loginUser(values, resetForm).then(res=>{
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
                isValid
              }) => (
                <View style={styles.formSection}>
                  <MaterialInput
                    name="user"
                    autoFocus
                    label='Usuario'
                    value={values.user}
                    placeholder="Usuario"
                    error={!values.user && errors.user && touched.user?errors.user:undefined }
                    onSubmitEditing={() => {
                      this.password.focus();
                    }}
                    onChangeText={handleChange('user')}
                    onEndEditing={()=>{console.log('TERMINO EDICION')}}
                  />
                  {!values.user && errors.user && touched.user?this.state.stopButtonAnimation():undefined}
                  
                  <MaterialInput
                  label='Contraseña'
                    name="password"
                    value={values.password}
                    placeholder="Contraseña"
                    error={!values.password && errors.password && touched.password?errors.password:undefined }
                    refField={input => {
                      this.password = input;
                    }}
                    securefield={true}
                    onChangeText={handleChange('password')}
                    onSubmitEditing={handleSubmit}
                  />
                  {!values.password && errors.password && touched.password?this.state.stopButtonAnimation():undefined}
                  <View style={{paddingTop: 15}} pointerEvents={ isSubmitting?'none':'auto'}>
                  
                    <AwesomeButtonRick
                    height={50}
                    progress={isValid ? true : false}
                    
                    stretch={true}
                    progressLoadingTim={15000}  
                    onPress={next =>
                     {
                       if(isValid){
                        this.setState({stopButtonAnimation:next});
                        handleSubmit();
                       }
                    }}
                    backgroundColor={defaultTasonColor}>
                      <Text style={style.defaultTextButton}>
                        Iniciar sesión
                      </Text>
                    </AwesomeButtonRick>
                  
                    <TouchableOpacity
                      onPress={this.onRecoverPassword.bind(this)}
                      style={styles.forgotPasswordButton}>
                      <Text style={styles.linkButton}>
                        Recuperar contraseña
                      </Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                      onPress={this.onRegister.bind(this)}
                      style={styles.forgotPasswordButton}>
                      <Text style={styles.linkButton}>Registrarse</Text>
                    </TouchableOpacity>
                  </View>
                </View>
              )}
            </Formik>
          </KeyboardAwareScrollView>
          </ScrollView>
        
          {showAlert && (
            <AwesomeAlert
              show={showAlert}
              showProgress={false}
              title={titleAlert}
              message={textAlert}
              closeOnTouchOutside={!this.state.isOfferedSuccessfully}
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
         
      </Container>
    );
  }
}


const mapStateToProps = state => {
  return {
    loginUser: state.authReducer.loginUser,
    generalReducer: state.generalReducer,
  };
};

const mapDispatchToProps = dispatch => {
  return {
    dispatch,
  };
};


export default connect(mapStateToProps, mapDispatchToProps)(Login);
