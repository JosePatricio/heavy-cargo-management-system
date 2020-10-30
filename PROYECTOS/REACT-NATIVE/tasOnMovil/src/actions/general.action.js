import {NET_IS_OFF,NET_IS_ON,FETCHING_FCMTOKEN_SUCCESS_DEVICE,FETCHING_NOTIFICATIONS_SUCCESS,
  FETCHING_UTIL_DATA,FETCHING_UTIL_DATA_SUCCESS,FETCHING_UTIL_DATA_FAILURE} from "./actionTypes";
import { URL_GET_CATALOGO_ITEM,SUCCESS_STATUS_CODE,HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC } from "../Constants";
import {fetchApi} from "../../src/services/api";


export const netWorkInfo = (isOnline) => {
    return  (dispatch, getState) => {
        try {
            let action=isOnline?NET_IS_ON:NET_IS_OFF;  
            dispatch({type: action});
            return null;
        } catch (e) {
          console.log('  error from action ',e);
        }
    }
  }
  

  export const deviceFcmToken = (token) => {
    return  (dispatch, getState) => {
        try {
            dispatch({type: FETCHING_FCMTOKEN_SUCCESS_DEVICE, data: token});
            return null;
        } catch (e) {
          console.log('  error from deviceFcmToken action ',e);
        }
    }
  }

  export const notifications = (notifications) => {
    return  (dispatch, getState) => {
        try {
            dispatch({type: FETCHING_NOTIFICATIONS_SUCCESS, data: notifications});
            return null;
        } catch (e) {
          console.log('  error from deviceFcmToken action ',e);
        }
    }
  }

  export const obtenerCatalogoItem = (idCatalogo) => {
    return async (dispatch, getState) => {
        try {
          dispatch({
            type: FETCHING_UTIL_DATA
          });
            const response = await fetchApi(URL_GET_CATALOGO_ITEM.replace("#idCatalogo", idCatalogo), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
            dispatch({type: FETCHING_UTIL_DATA_SUCCESS, data :response.responseBody});
            return response;
        } catch (e) {
          dispatch({type: FETCHING_UTIL_DATA_FAILURE});
        }
    }
  }
  

  

  