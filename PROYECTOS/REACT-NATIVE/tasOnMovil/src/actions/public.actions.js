import {fetchApi} from "../../src/services/api";
import {HEADER_PUBLIC,SUCCESS_STATUS_CODE,JSON_TYPE,HEADER_BASIC,URL_GET_LOCALIDAD_BY_PADRE,URL_READ_EMPRESA_TRANSP
,URL_CREA_USUARIO,URL_READ_USUARIO_BY_USERNAME,URL_READ_USUARIO_BY_EMAIL,URL_READ_USUARIO,URL_READ_EMPRESA,URL_CREA_EMPRESA} from "../Constants";
import {FETCHING_PUBLIC_DATA,FETCHING_PUBLIC_DATA_SUCCESS,FETCHING_PUBLIC_DATA_FAILURE} from "./actionTypes";






export const createEmpresaCliente = (json) => {
  return async (dispatch, getState) => {
      try {

        dispatch({type: FETCHING_PUBLIC_DATA});
         const response = await fetchApi(URL_CREA_EMPRESA, "POST", json, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
      
          if(response.success) {
            dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
   }
}

export const createUsuarioEmpresa = (json) => {
  return async (dispatch, getState) => {
      try {

        dispatch({type: FETCHING_PUBLIC_DATA});
         const response = await fetchApi(URL_CREA_USUARIO, "POST", json, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
      
          if(response.success) {
            dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
   }
}

export const getEmpresaTransporteByRuc = (ruc) => {
  return async (dispatch, getState) => {
      try {
        dispatch({
          type: FETCHING_PUBLIC_DATA
        }); 
        const response = await fetchApi(URL_READ_EMPRESA_TRANSP.replace("#ruc", ruc), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
        dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
        return response;
      } catch (e) {
        console.log(e);
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
  }
}

export const getLocalidadByPadre = (idLocalidadPadre,estado) => {
  return async (dispatch, getState) => {
      try {
        dispatch({type: FETCHING_PUBLIC_DATA });
          const response = await fetchApi(URL_GET_LOCALIDAD_BY_PADRE.replace("#idLocalidadPadre", idLocalidadPadre).replace("#estado", estado), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
          dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
          return response;
      } catch (e) {
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
  }
}

export const userNameUsuarioValidator = (username) => {
  return async (dispatch, getState) => {
      try {
        dispatch({type: FETCHING_PUBLIC_DATA });
          const response = await fetchApi(URL_READ_USUARIO_BY_USERNAME.replace("#username", username), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
          if(response){
            dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
              return {
                  valid: (null != response.responseBody && response.responseBody.response === 'ERROR')
              };
          }
       
      } catch (e) {
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
  }
}


export const emailUsuarioValidator = (username) => {
  return async (dispatch, getState) => {
      try {
        dispatch({type: FETCHING_PUBLIC_DATA });
          const response = await fetchApi(URL_READ_USUARIO_BY_EMAIL.replace("#email", username), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
          if(response){
            dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
              return {
                  valid: (null != response.responseBody && response.responseBody.response === 'ERROR')
              };
          }
      } catch (e) {
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
  }
}

export const numdocUsuarioValidator = (ruc) => {
  return async (dispatch, getState) => {
      try {
        dispatch({type: FETCHING_PUBLIC_DATA });
          const response = await fetchApi(URL_READ_USUARIO.replace("#ruc", ruc), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
          if(response){
            dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
              return {
                  valid: (null != response.responseBody && response.responseBody.response === 'ERROR')
              };
          }
         
      } catch (e) {
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
  }
}


export const rucEmpresaValidator = (ruc) => {
  return async (dispatch, getState) => {
      try {
        dispatch({
          type: FETCHING_PUBLIC_DATA
        }); 
        const response = await fetchApi(URL_READ_EMPRESA.replace("#ruc", ruc), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
        if(response){
          dispatch({type: FETCHING_PUBLIC_DATA_SUCCESS, data :response.responseBody});
            return {
                valid: (null != response.responseBody && response.responseBody.response === 'ERROR')
            };
        }
        return response;
      } catch (e) {
        console.log(e);
        dispatch({type: FETCHING_PUBLIC_DATA_FAILURE});
      }
  }
}
