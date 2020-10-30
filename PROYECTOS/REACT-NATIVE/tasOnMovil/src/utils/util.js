import { Platform, StyleSheet } from "react-native";
import {PUBLIC_USER,PUBLIC_PASSWORD,ID_USUARIO_CONDUCTOR,ID_USUARIO_CONDUCTOR_CLIENTE} from '../Constants'
import {Dimensions} from "react-native";


export const deviceHeight=Dimensions.get('window').height;
export const deviceWidth=Dimensions.get('window').width;

export function cardHelper(state) {
  let style = null;

  if (Platform.OS == "android") {
    if (state.isLoading) {
      style = { style: styles.backgroundColor };
    }
  }
  return style;
}

const styles = StyleSheet.create({
  backgroundColor: {
    backgroundColor: "#999999"
  }
});



export function  getPublicHeader() {
  let headers = {};
  
  headers['Content-Type']= 'application/json';
  headers["Accept"]='application/json';  
 // headers["Authorization"]='Basic ' + window.btoa(PUBLIC_USER + ':' + appConfig.PUBLIC_PASSWORD) ;
 headers["Authorization"]='Basic ' + String(PUBLIC_USER + ':' + PUBLIC_PASSWORD) ;
return headers;

}


export function encodedUrl(formData){
  let formBody = [];
  for (let key in formData) {
    const encodedKey = encodeURIComponent(key);
    const encodedValue = encodeURIComponent(formData[key]);
    formBody.push(encodedKey + "=" + encodedValue);
  }
  formBody = formBody.join("&");
  return formBody;
}

export function isValidRole(rol){
 
  let roles=[ID_USUARIO_CONDUCTOR,ID_USUARIO_CONDUCTOR_CLIENTE];
  return roles.some(v => (v === rol ));

}
