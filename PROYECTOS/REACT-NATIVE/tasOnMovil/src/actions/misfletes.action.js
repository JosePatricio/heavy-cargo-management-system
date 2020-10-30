import { ReactfetchApi } from "../services/api";
import {USER_LOGGED_OUT_SUCCESS,
 FETCHING_MISFLETES,FETCHING_MISFLETES_SUCCESS,FETCHING_MISFLETES_FAILURE,
 FETCHING_IMAGES,FETCHING_IMAGES_SUCCESS,FETCHING_IMAGES_FAILURE
} from "./actionTypes";
import {fetchApi} from "../services/api";
import {URL_GET_IMAGE,urlMyOferts,urlInvoiceListByState,urlgetFotoPorId,
  MY_OFERTS,
  MY_OFERTS_APPROVED,
  MY_OFERTS_TO_RECIEVE,
  MY_OFERTS_TO_DELIVER,
  MY_OFERTS_GENERATE_INVOICE,
  MY_OFERTS_DELIVER_INVOICE,
  MY_OFERTS_FINISHED,
  MY_OFERTS_CANCELED,
  MY_OFERTS_COLLECT
} from "../Constants";

export const getMisFletes = (status) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

let url=urlMyOferts;

if(status == MY_OFERTS_DELIVER_INVOICE){
  url=urlInvoiceListByState.replace('#estado','');
}

      try {
         dispatch({type: FETCHING_MISFLETES});
         user=(user?user:global.currentUser);
   
         const response = await fetchApi(url+status, "GET", null, 200, user.token);

         if(!response.success){
          throw response;
         }
         if(response.responseBody.status == 403){
          dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
         }
         if(MY_OFERTS==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: response.responseBody ,data1: null,data2: null,data3: null,data4: null});
         }
         if(MY_OFERTS_APPROVED==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1: response.responseBody,data2: null,data3: null,data4: null});
         }
          if(MY_OFERTS_TO_RECIEVE==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1:null ,data2: response.responseBody,data3: null,data4: null});
         }
         if(MY_OFERTS_TO_DELIVER==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1:null ,data2: null,data3: response.responseBody,data4: null});
         }
         if(MY_OFERTS_GENERATE_INVOICE==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1:null ,data2: null,data3: null,data4: response.responseBody});
         }
         if(MY_OFERTS_DELIVER_INVOICE==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1:null ,data2: null,data3: null,data4:null,data5: response.responseBody });
         }
         if(MY_OFERTS_COLLECT==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1:null ,data2: null,data3: null,data4:null,data5:null,data6: response.responseBody });
         }
         if(MY_OFERTS_FINISHED==status){
          dispatch({type: FETCHING_MISFLETES_SUCCESS, data: null ,data1:null ,data2: null,data3: null,data4:null,data5:null,data6:null,data7: response.responseBody });
         }
         return response;
    
       } catch (e) {
        dispatch({type: FETCHING_MISFLETES_FAILURE});
console.log('EL CATCH ES ',e);

        throw e;
      }
  }
}



export const getImages = (idOffer,status) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
         dispatch({type: FETCHING_IMAGES_SUCCESS});
         user=(user?user:global.currentUser);
         const response = await fetchApi(URL_GET_IMAGE+idOffer+'/'+status, "GET", null, 200, user.token);
        
         if(response.responseBody.status == 403){
          dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
         }
         if(response.success) {
          dispatch({type: FETCHING_IMAGES_SUCCESS, data: response.responseBody});
         } 
         
         return response;
    
       } catch (e) {
        dispatch({type: FETCHING_IMAGES_FAILURE});
      }
  }
}



export const getFoto = (fotoId) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
         dispatch({type: FETCHING_IMAGES_SUCCESS});
         user=(user?user:global.currentUser);
         const response = await fetchApi(urlgetFotoPorId.replace('#fotoId',fotoId), "GET", null, 200, user.token);
        
         if(response.responseBody.status == 403){
          dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
         }
         if(response.success) {
          dispatch({type: FETCHING_IMAGES_SUCCESS, data: response.responseBody});
         } 
         
         return response;
    
       } catch (e) {
        dispatch({type: FETCHING_IMAGES_FAILURE});
      }
  }
}