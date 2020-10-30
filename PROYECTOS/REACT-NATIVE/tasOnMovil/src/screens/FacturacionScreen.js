import React, { Component } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { connect } from "react-redux";
import { logoutUser } from "../actions/auth.actions";


const styles = StyleSheet.create({
  container : {
    backgroundColor:'white',
    flex: 1,
    alignItems:'center',
    justifyContent :'center'
  },
  textStyle: {
      color: "black",
      fontSize: 18
  },
  button: {
    width:300,
    backgroundColor:'#1c313a',
    borderRadius: 25,
    marginVertical: 10,
    paddingVertical: 13
  },
  buttonText: {
    fontSize:16,
    fontWeight:'500',
    color:'#ffffff',
    textAlign:'center'
  },
});

class SolicitudesScreen extends Component {

  logoutUser = () => {
      this.props.dispatch(logoutUser());
  }

	render() {
   
		return(
			<View style={styles.container}>
			     <Text style={styles.textStyle}>Facturacion</Text>
         
			</View>
			)
	}
}

const mapStateToProps = (state) => {
  return {
    getUser: state.userReducer.getUser
  };
};


export default connect(mapStateToProps, null)(SolicitudesScreen);
