import { ReactfetchApi } from "../../src/services/api";
import {
  FETCHING_DATA,
  FETCHING_DATA_SUCCESS,
  FETCHING_DATA_FAILURE,
  FETCHING_DATA_SOLICITUD,
  FETCHING_DATA_SUCCESS_SOLICITUD,
  FETCHING_DATA_FAILURE_SOLICITUD,
  POSTING_SOLICITUD_OFERTA,
  POSTING_SOLICITUD_OFERTA_SUCCESS,
  POSTING_SOLICITUD_OFERTA_FAILURE,
  USER_LOGGED_OUT_SUCCESS
} from "../actions/actionTypes";
import {fetchApi} from "../../src/services/api";

import { urlFletesList ,urlFleteDetail,urlMakeAnOfert,urlUpdateOfert,SUCCESS_STATUS_CODE,JSON_TYPE } from "../Constants";


export const getSolicitudes = () => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;
      try {
        dispatch({type: FETCHING_DATA});
        user=(user?user:global.currentUser);

        const response = await fetchApi(urlFletesList.replace("#idState", "22"), "GET", null, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
        if(response.responseBody.status==403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
          }
          dispatch({type: FETCHING_DATA_SUCCESS, data: response.responseBody});
        
          return response;
        
      } catch (e) {
        dispatch({type: FETCHING_DATA_FAILURE});
        console.log(e);
        throw e;
      }
  }
}



export const getSolicitudById = (id,user) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;
      try {

          dispatch({type: FETCHING_DATA_SOLICITUD});
          user=(user?user:global.currentUser);
          const response = await fetchApi(urlFleteDetail+id, "GET", null, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
        
          dispatch({type: FETCHING_DATA_SUCCESS_SOLICITUD, data :response.responseBody});
          return response;
         
      } catch (e) {
        dispatch({type: FETCHING_DATA_FAILURE});
        throw e;
      }
  }
}



export const sendMyOffer = (json,user) => {
  return async (dispatch, getState) => {
      try {
        dispatch({type: POSTING_SOLICITUD_OFERTA, token :user.token});
         const response = await fetchApi(urlMakeAnOfert, "POST", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
          if(response.success) {
            dispatch({type: POSTING_SOLICITUD_OFERTA_SUCCESS, data: response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: POSTING_SOLICITUD_OFERTA_FAILURE});
      }
   }
}

export const updateMyOfert = (json,user) => {
  return async (dispatch, getState) => {
    const state = getState();
      try {
         dispatch({type: POSTING_SOLICITUD_OFERTA, token :user.token});
         const response = await fetchApi(urlUpdateOfert, "POST", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
          if(response.success) {
            dispatch({type: POSTING_SOLICITUD_OFERTA_SUCCESS, data: response.responseBody});
          } 
          return response; 
      } catch (e) {
        dispatch({type: POSTING_SOLICITUD_OFERTA_FAILURE});
      }
    }
}





