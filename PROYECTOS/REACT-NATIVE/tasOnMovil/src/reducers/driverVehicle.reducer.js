import { combineReducers } from 'redux';
import { FETCHING_DRIVERS,FETCHING_DRIVERS_SUCCESS,FETCHING_DRIVERS_FAILURE,
  FETCHING_VEHICLES,FETCHING_VEHICLES_SUCCESS,FETCHING_VEHICLES_FAILURE} from '../actions/actionTypes';

const initialState ={
    data: [],
    isFetching: false,
    error: false
}

const getDriversReducer = (state = initialState, action) => {
  switch (action.type) {

      case FETCHING_DRIVERS:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_DRIVERS_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
      case FETCHING_DRIVERS_FAILURE:
            return {
              ...state,
             isFetching : false,
                error:true
            }

      default:
        return state;
    }
}


const getVehiclesReducer = (state = initialState, action) => {
  switch (action.type) {

      case FETCHING_VEHICLES:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_VEHICLES_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
      case FETCHING_VEHICLES_FAILURE:
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
  getDriversReducer,getVehiclesReducer
});
