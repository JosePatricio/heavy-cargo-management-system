import { ReactfetchApi } from "../services/api";
import {
  FETCHING_DRIVERS,FETCHING_DRIVERS_SUCCESS,FETCHING_DRIVERS_FAILURE,
  FETCHING_VEHICLES,FETCHING_VEHICLES_SUCCESS,FETCHING_VEHICLES_FAILURE
} from "./actionTypes";
import {fetchApi} from "../services/api";
import {urlClienteConductor,urlMyDriversToAprove,urlVehicles,urlAllMyDrivers,urlUpdateDriver,urlUpdateVehicle,urlCreateVehicle,urlMyDrivers,urlVehicle ,urlCreateDriver , SUCCESS_STATUS_CODE,JSON_TYPE} from "../Constants";


export const getDrivers = (idSolicitud) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
    
         dispatch({type: FETCHING_DRIVERS});
         user=(user?user:global.currentUser);
   
         const response = await fetchApi(urlMyDrivers.replace('#idSolicitud',idSolicitud), "GET", null, 200, user.token);
       
         if(response.success) {
          dispatch({type: FETCHING_DRIVERS_SUCCESS, data: response.responseBody});
        } 
        return response;
    
       } catch (e) {
        dispatch({type: FETCHING_DRIVERS});
          console.log(e);
      }
  }
}

export const getMisConductores = () => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
    
         dispatch({type: FETCHING_DRIVERS});
         user=(user?user:global.currentUser);
   
         const response = await fetchApi(urlAllMyDrivers, "GET", null, 200, user.token);
       
         if(response.success) {
          dispatch({type: FETCHING_DRIVERS_SUCCESS, data: response.responseBody});
        } 
        return response;
    
       } catch (e) {
        dispatch({type: FETCHING_DRIVERS});
          console.log(e);
      }
  }
}



export const getVehicles = (idSolicitud) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
    
         dispatch({type: FETCHING_VEHICLES});
         user=(user?user:global.currentUser);
         const response = await fetchApi(urlVehicle.replace('#idSolicitud',idSolicitud), "GET", null, 200, user.token);
       
         if(response.success) {
          dispatch({type: FETCHING_VEHICLES_SUCCESS, data: response.responseBody});
        } 
        return response;
    
       } catch (e) {
        dispatch({type: FETCHING_VEHICLES_FAILURE});
          console.log(e);
      }
  }
}


export const getMisVehiculos = () => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
         dispatch({type: FETCHING_VEHICLES});
         user=(user?user:global.currentUser);
         const response = await fetchApi(urlVehicles, "GET", null, 200, user.token);
       
         if(response.success) {
          dispatch({type: FETCHING_VEHICLES_SUCCESS, data: response.responseBody});
        } 
        return response;
    
       } catch (e) {
        dispatch({type: FETCHING_VEHICLES_FAILURE});
          console.log(e);
      }
  }
}



export const getAllUserDetailByEmpresaTipoAndStatus = (rucEmpresa,tipoUsuario,estado) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
         dispatch({type: FETCHING_DRIVERS});
         user=(user?user:global.currentUser);
         const response = await fetchApi(urlMyDriversToAprove.replace('#rucEmpresa',rucEmpresa).replace('#tipoUsuario',tipoUsuario).replace('#estado',estado), "GET", null, 200, user.token);
       
         if(response.success) {
          dispatch({type: FETCHING_DRIVERS_SUCCESS, data: response.responseBody});
        } 
        return response;
    
       } catch (e) {
        dispatch({type: FETCHING_DRIVERS_FAILURE});
          console.log(e);
      }
  }
}


export const getClienteConductor = (idClieCond) => {
  return async (dispatch, getState) => {
      const state = getState();
     let user=state.userReducer.getUser.userDetails;

      try {
         dispatch({type: FETCHING_DRIVERS});
         user=(user?user:global.currentUser);
         const response = await fetchApi(urlClienteConductor.replace('#idClieCond',idClieCond), "GET", null, 200, user.token);
       
         if(response.success) {
          dispatch({type: FETCHING_DRIVERS_SUCCESS, data: response.responseBody});
        } 
        return response;
    
       } catch (e) {
        dispatch({type: FETCHING_DRIVERS_FAILURE});
          console.log(e);
      }
  }
}



export const createDriver = (json) => {
  return async (dispatch, getState) => {
      try {
        const state = getState();
        let user=state.userReducer.getUser.userDetails;
        dispatch({type: FETCHING_DRIVERS, token :user.token});

         const response = await fetchApi(urlCreateDriver, "POST", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
          if(response.success) {
            dispatch({type: FETCHING_DRIVERS_SUCCESS, data: response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: FETCHING_DRIVERS_FAILURE});
      }
   }
}




export const updateConductorCamion = (json) => {
  return async (dispatch, getState) => {
      try {
        const state = getState();
        let user=state.userReducer.getUser.userDetails;
        dispatch({type: FETCHING_DRIVERS, token :user.token});

         const response = await fetchApi(urlUpdateDriver, "PUT", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
          if(response.success) {
            dispatch({type: FETCHING_DRIVERS_SUCCESS, data: response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: FETCHING_DRIVERS_FAILURE});
      }
   }
}



export const addVehiculo = (json) => {
  return async (dispatch, getState) => {
      try {
        const state = getState();
        let user=state.userReducer.getUser.userDetails;
        dispatch({type: FETCHING_VEHICLES, token :user.token});

         const response = await fetchApi(urlCreateVehicle, "POST", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
          if(response.success) {
            dispatch({type: FETCHING_VEHICLES_SUCCESS, data: response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: FETCHING_VEHICLES_FAILURE});
      }
   }
}


export const updateVehiculo = (json) => {
  return async (dispatch, getState) => {
      try {
        const state = getState();
        let user=state.userReducer.getUser.userDetails;
        dispatch({type: FETCHING_VEHICLES, token :user.token});

         const response = await fetchApi(urlUpdateVehicle, "PUT", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
          if(response.success) {
            dispatch({type: FETCHING_VEHICLES_SUCCESS, data: response.responseBody});
          }
          return response;
      } catch (e) {
        dispatch({type: FETCHING_VEHICLES_FAILURE});
      }
   }
}
