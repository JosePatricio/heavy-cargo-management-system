import {
  Platform,
  StyleSheet,
  TouchableOpacity,
  View,StatusBar
} from "react-native";
import React from "react";
import { Actions } from "react-native-router-flux";
import DrawerContent from "../components/DrawerContent";
import { Icon } from "react-native-elements";
import {style,defaultTasonColor} from "../styles/index";
import {Text} from "../components/";
import {Icon as IconNb} from 'native-base'
import { connect } from "react-redux";
import {logoutUser} from "../actions/auth.actions";

const styles = StyleSheet.create({
  container: {
    height: Platform.OS === "ios" ? 64 : 54,
    flexDirection: "row",
    paddingTop: 10
  },
  navBarItem: {
    flex: 1,
    justifyContent: "center",
    alignItems:'center',
   // paddingLeft:10
  }
});

 class CustomNavBar extends React.Component {
  
  /* constructor(props) {
     super(props)
   } */


   
logout=()=>{

  this.props.dispatch(logoutUser());
}


  abrirMenu() {
    // Actions.get('drawer').ref.toggle();
    Actions.drawerOpen(DrawerContent);
  }

  _renderLeft() {
    if (Actions.currentScene === "Solicitudes" ||  Actions.currentScene === "MisSolicitudes") {
      return (
        <TouchableOpacity  style={[styles.navBarItem,{paddingRight: 25}]} onPress={() => this.abrirMenu()}>
         {/*  <Icon size={30} color="#007AFF" name="menu" /> */}
        </TouchableOpacity>
      );
    }else if (Actions.currentScene !='_Inicio'){
      return (
        <TouchableOpacity
          onPress={Actions.pop}
          style={[styles.navBarItem,{paddingRight: 25}]}>
      <Icon size={30} color="#537FBE" name="arrow-back" /> 
        </TouchableOpacity>
      );
    }else{
            return ( <View></View>)
          }
   } 

  _renderMiddle() {
    return (
      <View style={[styles.navBarItem,{paddingRight: 15}]}>
        <Text style={style.actionBarText}>{!this.props.childTitle?this.props.title:this.props.childTitle}</Text>
      </View>
    );
  }

  _renderRight() {
    return (
      <View
        style={[
          styles.navBarItem,
          { flexDirection: "row", justifyContent: "flex-end" }
        ]}
      >


         <TouchableOpacity
          onPress={this.logout.bind(this)}
          style={{ flexDirection:'row'}}
        >
       {/*<Text style={{paddingRight:20, color:defaultTasonColor ,  fontWeight: 'bold', }} >Salir</Text> */}
        <IconNb style={{paddingRight:20, color:defaultTasonColor ,  fontWeight: 'bold', }} name='logout' type='MaterialCommunityIcons'/>
        </TouchableOpacity>
 

      </View>
    );
  }

  render() {

    let dinamicStyle = {};
    if (Actions.currentScene === "_Inicio") {
      dinamicStyle = { backgroundColor: "#F8F8F8" };
    } else {
      dinamicStyle = { backgroundColor: "#F8F8F8" };
    }
const isHome=(Actions.currentScene === "_Iniciooooo");


if(!isHome){
    return (
        
        <View style={[styles.container, dinamicStyle]}>
           <StatusBar backgroundColor="#1c313a" barStyle="light-content"/> 
        <View style={{width:'20%'}}>
          {this._renderLeft()}
        </View>
      <View style={{width:'60%'}}>
      {this._renderMiddle()}
      </View>
      <View style={{width:'20%'}}>
        {this._renderRight()} 
      </View>
      </View> 
    );
  }else {
    return (
         <View >
      </View> 
    );
  }
  }
}



const mapStateToProps = state => {
  return {
    loginUser: state.authReducer.loginUser,
    generalReducer: state.generalReducer
  };
};

const mapDispatchToProps = dispatch => {
  return {
      dispatch
  };
};


export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomNavBar);
