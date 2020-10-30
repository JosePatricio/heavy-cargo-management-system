import { ReactfetchApi } from "../services/api";
import {FETCHING_FACTURA,FETCHING_FACTURA_SUCCESS,FETCHING_FACTURA_FAILURE,USER_LOGGED_OUT_SUCCESS,
POSTING_FACTURA,POSTING_FACTURA_SUCCESS,POSTING_FACTURA_FAILURE} from "./actionTypes";
import {fetchApi} from "../services/api";
import { urlPreInvoiceAvailability,urlGetCodeFactura,urlCreateInvoice,urlInvoiceListByState,urlInvoiceDetailByNumber,SUCCESS_STATUS_CODE,JSON_TYPE} from "../Constants";


export const getCodeFactura = () => {
    return async (dispatch, getState) => {
        const state = getState();
       let user=state.userReducer.getUser.userDetails;
        
       try {
           dispatch({type: FETCHING_FACTURA});
           user=(user?user:global.currentUser);
           const response = await fetchApi(urlGetCodeFactura,"GET", null, 200, user.token);
           if(response.responseBody.status == 403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
           }
           if(response.success) {
            dispatch({type: FETCHING_FACTURA_SUCCESS, data: response.responseBody});
           } 
           return response;
        } catch (e) {
          dispatch({type: FETCHING_FACTURA_FAILURE});
        }
    }
  }

  export const createInvoice = (json) => {
    return async (dispatch, getState) => {
        try {
            const state = getState();
            let user=state.userReducer.getUser.userDetails;
            dispatch({type: POSTING_FACTURA, token :user.token});
            const response = await fetchApi(urlCreateInvoice, "POST", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
           
            if(response.responseBody.status == 403){
                dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
            }
            if(response.success) {
              dispatch({type: POSTING_FACTURA_SUCCESS, data: response.responseBody});
            }
            return response;
        } catch (e) {
          dispatch({type: POSTING_FACTURA_FAILURE});
        }
     }
  }


  export const getInvoiceListByState = (estado) => {
    return async (dispatch, getState) => {
        const state = getState();
       let user=state.userReducer.getUser.userDetails;
  
        try {
           dispatch({type: FETCHING_FACTURA});
           user=(user?user:global.currentUser);
           const response = await fetchApi(urlInvoiceListByState.replace('#estado',estado), "GET", null, 200, user.token);
          
           if(response.responseBody.status == 403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
           }
           if(response.success) {
            dispatch({type: FETCHING_FACTURA_SUCCESS, data: response.responseBody});
           } 
           return response;
      
         } catch (e) {
          dispatch({type: FETCHING_FACTURA_FAILURE});
        }
    }
  }


  export const getInvoiceDetailByNumber = (numbPreInv) => {
    return async (dispatch, getState) => {
        const state = getState();
       let user=state.userReducer.getUser.userDetails;
  
        try {
           dispatch({type: FETCHING_FACTURA});
           user=(user?user:global.currentUser);
           const response = await fetchApi(urlInvoiceDetailByNumber.replace("#numbPreInv", numbPreInv), "GET", null, 200, user.token);
          
           if(response.responseBody.status == 403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
           }
           if(response.success) {
            dispatch({type: FETCHING_FACTURA_SUCCESS, data: response.responseBody});
           } 
           
           return response;
      
         } catch (e) {
          dispatch({type: FETCHING_FACTURA_FAILURE});
        }
    }
  }


  export const preInvoiceAvailability = () => {
    return async (dispatch, getState) => {
        const state = getState();
       let user=state.userReducer.getUser.userDetails;
        
       try {
           dispatch({type: FETCHING_FACTURA});
           const response = await fetchApi(urlPreInvoiceAvailability,"GET", null, 200, user.token);
           if(response.responseBody.status == 403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
           }
           if(response.success) {
            dispatch({type: FETCHING_FACTURA_SUCCESS, data: response.responseBody});
           } 
           return response;
        } catch (e) {
          dispatch({type: FETCHING_FACTURA_FAILURE});
        }
    }
  }
