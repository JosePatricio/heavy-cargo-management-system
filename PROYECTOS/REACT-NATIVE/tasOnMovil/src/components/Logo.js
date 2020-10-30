import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
   Image 
} from 'react-native';


export default class Logo extends Component {
	render(){
		return(
			<View style={styles.container}>
				<Image  style={{width:130, height: 110}}
          			source={require('../../src/images/logo2.png')}/>
  			</View>
			)
	}
}


const styles = StyleSheet.create({
  container : { flex: 1,  
	alignItems: 'center',
	justifyContent: 'space-around',
	marginTop :20
  }
});