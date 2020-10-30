import React, { Component } from 'react';
import {StyleSheet,View,Platform,Vibration,StatusBar} from 'react-native';
import {connect} from "react-redux";
import Routes from './components/Routes';
import GeneralStatusBar from './components/GeneralStatusBar';
import {no_connected_info,connected_info} from './utils/tas-on-texts';
//import { createAppContainer } from 'react-navigation';
//import rutas from './components/ReacNavigationRouter/router';
//import Applicat from './components/ReacNavigationRouter/example';
//const AppContainer = createAppContainer(rutas);
import firebase, { Notification, RemoteMessage } from 'react-native-firebase';
import NetInfo from "@react-native-community/netinfo";
import {Toast,Button} from "native-base";
import {netWorkInfo,deviceFcmToken} from '../src/actions/general.action';
import {VIBRATION_DURATION} from '../src/Constants';

let checkNetInfo=null;
class Main extends Component {

notificationListener = () =>
  firebase.notifications().onNotification(notification => {
    const {
      notifications: {
        Android: {
          Priority: { Max,High }
        }
      }
    } = firebase;

    notification.android.setChannelId('MainChannel');
    notification.android.setPriority(High);
    notification.setData(notification.data);
    firebase.notifications().displayNotification(notification);
  });


  componentDidMount(){
    StatusBar.setHidden(false);
    StatusBar.setBackgroundColor('#1c313a');
  
    this.notificationListener();
  
    this.notificationOpenedListener();
   // BackgroundTask.schedule();


    checkNetInfo= NetInfo.addEventListener(state => {
     this.props.dispatch(netWorkInfo(state.isConnected));

    if(this.props.authData.isLoggedIn != undefined && this.props.authData.isLoggedIn == true){
      if(!state.isConnected){
         Toast.show({
          text: no_connected_info,
          position: "bottom",
          type: "danger",
          duration: 15000,
          textStyle:{textAlign:'center'},
          style: {paddingTop: 0, paddingBottom:26, height: 13}
        }) 
      }else{
        Toast.show({
           text: connected_info,
           position: "bottom",
           type: "success",
           duration: 6000,
           textStyle:{textAlign:'center'},
           style: {paddingTop: 0, paddingBottom:26, height: 13}
        }) 
      }
    }

    });
  }

  notificationOpenedListener = () =>
  firebase.notifications().onNotificationOpened(notificationOpen => {
    console.log('           --------------------                ');
   console.log('notificationOpen  dd  ',notificationOpen);
   console.log('           --------------------                ');
  });

  

   componentWillUnmount() {
    this.notificationListener();  
    this.notificationListener();
  }

 
	render() {
    const {authData:{isLoggedIn,userStatus,userRole}} = this.props;
		return(
      <View style={styless.container}>

    <StatusBar backgroundColor="#1c313a" barStyle="light-content"/>
            <Routes isLoggedIn={isLoggedIn} userStatus={userStatus} userRole={userRole}/>  
    
      </View>
  		)
  }  
}

const styless = StyleSheet.create({
  container : {
    flex: 1
  }
});


const mapStateToProps = (state) => {
  return {
    authData: state.authReducer.authData,
    generalReducer: state.generalReducer
  };
};


const mapDispatchToProps = dispatch => {
  return {
    dispatch,
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Main);

