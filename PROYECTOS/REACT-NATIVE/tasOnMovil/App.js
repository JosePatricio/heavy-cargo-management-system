

import React, {Component} from 'react';
import {StyleSheet, View, StatusBar,Text} from 'react-native';
import {Provider} from "react-redux";
import { PersistGate } from 'redux-persist/integration/react'
import {Root} from "native-base";
import Main from "./src/Main";
import persist from "./src/config/store";
import SplashScreen from 'react-native-splash-screen'

const persistStore = persist();
export default class App extends Component {


  componentDidMount(){
    SplashScreen.hide();
  }
  

  render() {
    return (
      <Root>
      <Provider store={persistStore.store}>
        <PersistGate loading={null} persistor={persistStore.persistor}>
          <Main />
        </PersistGate>
      </Provider>
      </Root>
    );
  }
}