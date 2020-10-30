import { combineReducers } from 'redux';
import {FETCHING_PUBLIC_DATA,FETCHING_PUBLIC_DATA_SUCCESS,FETCHING_PUBLIC_DATA_FAILURE} from "../actions/actionTypes";


const publicData = (state = {}, action) => {
    
    switch (action.type) {
      case FETCHING_PUBLIC_DATA:
        return {
              isLoading: true,
              isError: false,
              isSuccess: false,
              errors: null
          }

      case FETCHING_PUBLIC_DATA_SUCCESS:
          return {
              isLoading: false,
              isError: false,
              isSuccess: true,
              data:action.data,
              errors: null,
          }

      case FETCHING_PUBLIC_DATA_FAILURE:
          return {
              isLoading: false,
              isError: true,
              isSuccess: false,
              errors: action.payload
          }

      default:
        return state;
    }
}


export default combineReducers({
    publicData
});
