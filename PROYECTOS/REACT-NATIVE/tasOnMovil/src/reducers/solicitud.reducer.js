import { combineReducers } from 'redux';
import {FETCHING_DATA,FETCHING_DATA_SUCCESS,FETCHING_DATA_FAILURE,
  FETCHING_DATA_SOLICITUD,
FETCHING_DATA_SUCCESS_SOLICITUD,
FETCHING_DATA_FAILURE_SOLICITUD,
  POSTING_SOLICITUD_OFERTA,
  POSTING_SOLICITUD_OFERTA_SUCCESS,
  POSTING_SOLICITUD_OFERTA_FAILURE} from '../actions/actionTypes';

const initialState ={
    data: [],
    isFetching: false,
    error: false
}

const getAllReducer = (state = initialState, action) => {
  switch (action.type) {

      case FETCHING_DATA:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_DATA_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
          case FETCHING_DATA_FAILURE:
            return {
              ...state,
             isFetching : false,
                error:true
            }

      default:
        return state;
    }
}



const getByIdReducer = (state = initialState, action) => {
  switch (action.type) {

      case FETCHING_DATA_SOLICITUD:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_DATA_SUCCESS_SOLICITUD:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
          case FETCHING_DATA_FAILURE_SOLICITUD:
            return {
              ...state,
             isFetching : false,
                error:true
            }
           
      default:
        return state;
    }
}

const makeOffertReducer = (state = initialState, action) => {
  switch (action.type) {

      case POSTING_SOLICITUD_OFERTA:
          return {
           ...state,
           data: [],
           isFetching : true,
           token:{}
          }

      case POSTING_SOLICITUD_OFERTA_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
          case POSTING_SOLICITUD_OFERTA_FAILURE:
            return {
              ...state,
             isFetching : false,
                error:true
            }
           
      default:
        return state;
    }
}


export default combineReducers({
  getAllReducer,getByIdReducer,makeOffertReducer
});
