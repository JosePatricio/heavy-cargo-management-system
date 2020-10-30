import { ReactfetchApi } from "../services/api";
import { FETCHING_EMPRESA,FETCHING_EMPRESA_SUCCESS,FETCHING_EMPRESA_FAILURE,USER_LOGGED_OUT_SUCCESS} from './actionTypes';
import {fetchApi} from "../services/api";
import {urlReadEmpre,urlClienteByToken} from "../Constants";


export const getClienteByToken = () => {
    return async (dispatch, getState) => {
        const state = getState();
       let user=state.userReducer.getUser.userDetails;
  
        try {
           dispatch({type: FETCHING_EMPRESA});
           user=(user?user:global.currentUser);
           const response = await fetchApi(urlClienteByToken, "GET", null, 200, user.token);
          
           if(response.responseBody.status == 403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
           }
           if(response.success) {
            dispatch({type: FETCHING_EMPRESA_SUCCESS, data: response.responseBody});
           } 
           return response;
         } catch (e) {
          dispatch({type: FETCHING_EMPRESA_FAILURE});
        }
    }
  }


export const readEmpresa = (rucEmpresa) => {
    return async (dispatch, getState) => {
        const state = getState();
       let user=state.userReducer.getUser.userDetails;
  
        try {
           dispatch({type: FETCHING_EMPRESA});
           user=(user?user:global.currentUser);
           const response = await fetchApi(urlReadEmpre.replace('#rucEmpresa',rucEmpresa), "GET", null, 200, user.token);
          
           if(response.responseBody.status == 403){
            dispatch({ type: USER_LOGGED_OUT_SUCCESS}); 
           }
           if(response.success) {
            dispatch({type: FETCHING_EMPRESA_SUCCESS, data: response.responseBody});
           } 
           return response;
         } catch (e) {
          dispatch({type: FETCHING_EMPRESA_FAILURE});
        }
    }
  }


