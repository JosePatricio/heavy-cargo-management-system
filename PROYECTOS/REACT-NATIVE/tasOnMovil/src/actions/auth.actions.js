import {fetchApi} from "../../src/services/api";
import {urlUpdateInfoBank,urlBancInfoUpdateReq,urlActivateUser,urlLogin,URL_ENCODED_TYPE,SUCCESS_STATUS_CODE} from "../Constants";
import {READING_USUARIO_LOADING,READING_USUARIO_SUCCESS,READING_USUARIO_FAILURE,
  UPDATING_PASSWORD_LOADING,UPDATING_PASSWORD_SUCCESS,UPDATING_PASSWORD_FAILURE,RECOVERING_PASSWORD_LOADING,RECOVERING_PASSWORD_SUCCESS,RECOVERING_PASSWORD_FAILURE} from "./actionTypes";
import { URL_UPDATE_PASSWORD,URL_RESTABLECER_PASSWORD,HEADER_PUBLIC,HEADER_BASIC ,URL_READ_USUARIO,JSON_TYPE} from "../Constants";


export const createNewUser = (payload) => {
    return async (dispatch) => {

        try {
          dispatch({
            type: "CREATE_USER_LOADING"
          });
          const response = await fetchApi("/user/create", "POST", payload, SUCCESS_STATUS_CODE,URL_ENCODED_TYPE);

          
          if(response.success) {
            dispatch({
                type: "CREAT_USER_SUCCESS"
            });
            dispatch({
                type: "AUTH_USER_SUCCESS",
                token: response.token,
                userStatus: response.estadoUsuario
            });
            dispatch({
                type: "GET_USER_SUCCESS",
                payload: response.responseBody
            });

            return response;
          } else {
            throw response;
          }

        } catch (error) {
            dispatch({
                type: "CREAT_USER_FAIL",
                payload: error.responseBody
            });
            return error;
        }
    }
}


export const loginUser = (payload) => {
    return async (dispatch) => {

        try {
          dispatch({
            type: "LOGIN_USER_LOADING"
          });

          const response = await fetchApi(urlLogin, "POST", payload, SUCCESS_STATUS_CODE,null,URL_ENCODED_TYPE);

          console.log('RESPUESTA LOGIN ',response);
          global.currentUser=response.responseBody;
         
          if(response.success && response.responseBody.response != "ERROR") {
          const username=String(payload).split('&')[0].split('=')[1];
          let serviceResponse={...response.responseBody,username:username };
          
          global.username=username;

           dispatch({
                type: "LOGIN_USER_SUCCESS",
            });
            dispatch({
                type: "AUTH_USER_SUCCESS",
                token: response.responseBody.token,
                userStatus: response.responseBody.estadoUsuario,
                userRole: response.responseBody.tipoUsuario
            });
            dispatch({
                type: "GET_USER_SUCCESS",
                payload: serviceResponse
            });
            return response;
          } else {
            throw response;
          }
       } catch (error) {
           let errorText=error.responseBody;
           if (error.message) {
             errorText = error.message;
           }
            dispatch({
                type: "LOGIN_USER_FAIL",
                payload: errorText
            });
            return error;
        }
    }
}

export const logoutUser = () => {
    return async (dispatch, getState) => {
        const state = getState();
        try {
            const {authReducer: {authData: {token}}} = state;
            console.log(token);
            const response = await fetchApi("/user/logout", "DELETE", null, SUCCESS_STATUS_CODE, token,URL_ENCODED_TYPE);
            console.log(response);
            dispatch({
                type: "USER_LOGGED_OUT_SUCCESS"
            });
        } catch (e) {
            console.log(e);
        }
    }
}


export const restablecerPassword = (email,identificador) => {
    return async (dispatch) => {
        try {
            dispatch({
                type: RECOVERING_PASSWORD_LOADING
              });
            const response = await fetchApi(URL_RESTABLECER_PASSWORD.replace("#email", email).replace("#identificador",identificador), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
          
            dispatch({type: RECOVERING_PASSWORD_SUCCESS, data :response.responseBody});
            return response;
           
        } catch (e) {
          dispatch({type: RECOVERING_PASSWORD_FAILURE});
        }
    }
  }


export const updatePassword = (json) => {
  return async (dispatch) => {
      try {
          dispatch({
              type: UPDATING_PASSWORD_LOADING
            });
          const response = await fetchApi(URL_UPDATE_PASSWORD, "POST", json, SUCCESS_STATUS_CODE, null,JSON_TYPE);

          dispatch({type: UPDATING_PASSWORD_SUCCESS, data :response.responseBody});
          dispatch({type: 'AUTH_USER_RESET'});
          return response;
         
      } catch (e) {
        dispatch({type: UPDATING_PASSWORD_FAILURE});
      }
  }
}

export const validateDocNum = (ruc) => {
  return async (dispatch) => {
      try {
          dispatch({
              type: READING_USUARIO_LOADING
            });

          const response = await fetchApi(URL_READ_USUARIO.replace("#ruc", ruc), "GET", null, SUCCESS_STATUS_CODE, HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC);
        
          dispatch({type: READING_USUARIO_SUCCESS, data :response.responseBody});
       
          if (response.response && response.response === 'ERROR') {
            return false;
          } 
          return true;
         
      } catch (e) {
        dispatch({type: READING_USUARIO_FAILURE});
      }
  }
}



export const activateClient = (payload) => {
  return async (dispatch, getState) => {
    try {
      const state = getState();
      let user=state.userReducer;

        dispatch({type: READING_USUARIO_LOADING});
        const response = await fetchApi(urlActivateUser, "POST", payload, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
      
        if(response.success) {
          dispatch({type: READING_USUARIO_SUCCESS,data: response.responseBody});

          return response;
        } else {
          throw response;
        }

      } catch (error) {
          dispatch({ type: READING_USUARIO_FAILURE, data: error.responseBody  });
          return error;
      }
  }
}





export const solicitarActualizarInformacionBancaria = (json) => {
  return async (dispatch,getState) => {
      try {
        const state = getState();
        let user=state.userReducer.getUser.userDetails;

          dispatch({
              type: READING_USUARIO_LOADING
            });

          const response = await fetchApi(urlBancInfoUpdateReq, "POST", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);

          dispatch({type: READING_USUARIO_SUCCESS, data :response.responseBody});
      
          return response;
         
      } catch (e) {
        dispatch({ type: READING_USUARIO_FAILURE, data: error.responseBody  });
        return e;
      }
  }
}



export const updateClienteInfoBancaria = (json) => {
  return async (dispatch,getState) => {
      try {
        const state = getState();
        let user=state.userReducer.getUser.userDetails;

          dispatch({
              type: READING_USUARIO_LOADING
            });
          const response = await fetchApi(urlUpdateInfoBank, "PUT", json, SUCCESS_STATUS_CODE, user.token,JSON_TYPE);
          dispatch({type: READING_USUARIO_SUCCESS, data :response.responseBody});
      
          return response;
         
      } catch (error) {
        dispatch({ type: READING_USUARIO_FAILURE, data: error  });
        return error;
      }
  }
}



