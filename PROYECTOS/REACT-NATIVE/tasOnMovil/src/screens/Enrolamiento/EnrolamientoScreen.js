import React, { Component } from "react";
import { Text, View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { AwesomeButtonRick, DropDownList, Loader } from "../../components";
import { idEmpresaType, idTipoClienteEmpresaTransportista, idUsuarioConductor } from '../../Constants';
import { style, styleLogin as styles } from '../../styles';
import { defaultTasonColor } from '../../styles';




const utf8 = require('utf8');



class EnrolamientoScreen extends Component {
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
     typesUser:[],
     selectedUserType:0,
     page: 1,
     isLoading: false,
    }


  }

  


  componentDidMount() {


  }


  
render() {
    const { handleSubmit, updatePassword,loginUser  } = this.props;
    const { showAlert,textAlert,titleAlert,isErrorAlert , selectedUserType ,isLoading} = this.state;

    return (


      <View style={style.container}>
      {updatePassword && updatePassword.isLoading && <Loader />}
   
      <View style={{alignItems:'center', paddingTop: 30}}>
        {/*  <Image  style={{width:70, height: 60}}
              source={require('../../images/images/logo2.png')}/> */}
      </View>


<View style={{flex:1,justifyContent:'center',alignItems:'center'}}>

<AwesomeButtonRick height={70} stretch={true} style={{ maxWidth:'60%',marginBottom: 15}} 
onPress={()=>Actions.NuevaEmpresa({'props':this.props})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Empresa cliente</Text></AwesomeButtonRick>

            <AwesomeButtonRick height={70} stretch={true} style={{ maxWidth:'60%',marginBottom: 15}}
             onPress={()=>Actions.NuevoConductor({'props':this.props})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Conductor de compañía de transporte</Text></AwesomeButtonRick>
            
            <AwesomeButtonRick height={70} stretch={true} style={{ maxWidth:'60%',marginBottom: 15}} onPress={()=>Actions.NuevaCompaniaTransporte({'props':this.props})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Registrar compañía de transporte</Text></AwesomeButtonRick>

</View>

<View style={ style.twoButtonsArea}  >

  
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={()=>Actions.pop() 
          } backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Regresar</Text></AwesomeButtonRick>
       
          </View>
     
    </View>
 

 
      );
  }
}





const selector = formValueSelector('EnrolamientoForm') 

const mapStateToProps = state => {
  return {
    updatePassword: state.authReducer.updatePassword,
    generalReducer: state.generalReducer,
    usuario: state.userReducer.getUser.userDetails,
    loginUser: state.authReducer.loginUser,
    dni: selector(state, 'dni'),
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
    form: "EnrolamientoForm",
    enableReinitialize: true
  })
)(EnrolamientoScreen);
