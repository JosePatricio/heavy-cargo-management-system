import React, { Component } from "react";
import { View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { AwesomeButtonRick, Loader, Text } from "../../components/";
import { defaultTasonColor, style } from '../../styles/';






class MenuAdministacionScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
     currentUser:{},
     showAlert: false,
     textAlert: "",
     titleAlert: "",
     isErrorAlert:false,
     isPostedSuccessfully: false,
     isLoading:false,
     
    }
  }


  componentDidMount() {

  }

redirect=(key)=>{
switch (key) {
    case 1:
        Actions.adminAprobacionConductores();
        break;
    case 2:
        Actions.adminConductores();
        break;
    case 3:
        Actions.adminVehiculos();
        break;
    case 4:
        Actions.adminInformacionBancaria();
      break;
    
    default:
        break;
}
}

render() {
    const { handleSubmit } = this.props;
    const { showAlert,textAlert,titleAlert,isErrorAlert,isLoading } = this.state;
    return (
      <View style={style.container2}>
        {isLoading && <Loader />}
     
      <AwesomeButtonRick height={50} stretch={true} style={{ maxWidth:'60%',marginBottom: 15}} onPress={()=>this.redirect(1)} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Aprobar nuevos usuarios</Text></AwesomeButtonRick>

            <AwesomeButtonRick height={50} stretch={true} style={{ maxWidth:'60%',marginBottom: 15}} onPress={()=>this.redirect(2)} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Conductores</Text></AwesomeButtonRick>
            
            <AwesomeButtonRick height={50} stretch={true} style={{ maxWidth:'60%',marginBottom: 15}} onPress={()=>this.redirect(3)} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Vehículos</Text></AwesomeButtonRick>

            <AwesomeButtonRick height={50} stretch={true} style={{ maxWidth:'60%'}} onPress={()=>this.redirect(4)} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Información bancaria</Text></AwesomeButtonRick>
    
         
       
      </View>
    );
  }
}



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
  )
  
)(MenuAdministacionScreen);
