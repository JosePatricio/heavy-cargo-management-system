import {FETCHING_CLIENTE_DATA,FETCHING_CLIENTE_DATA_SUCCESS,FETCHING_CLIENTE_DATA_FAILURE} from "./actionTypes";
  import { urlInfoBancaria,SUCCESS_STATUS_CODE,HEADER_PUBLIC,JSON_TYPE,HEADER_BASIC } from "../Constants";
  import {fetchApi} from "../../src/services/api";
  
  
  
    export const getClienteInfoBancaria = (idCatalogo) => {
      return async (dispatch, getState) => {
          
         const state = getState();
         let user=state.userReducer.getUser.userDetails;

      try {
            dispatch({
              type: FETCHING_CLIENTE_DATA
            });
        
            const response = await fetchApi(urlInfoBancaria, "GET", null, 200, user.token);
        
              dispatch({type: FETCHING_CLIENTE_DATA_SUCCESS, data :response.responseBody});
              return response;
          } catch (e) {
            dispatch({type: FETCHING_CLIENTE_DATA_FAILURE});
          }
      }
    }
    
  
    
  
    