import { combineReducers } from 'redux';
import { FETCHING_FACTURA,FETCHING_FACTURA_SUCCESS,FETCHING_FACTURA_FAILURE} from '../actions/actionTypes';


const initialState ={
    data: [],
    isFetching: false,
    error: false
}


const getFacturaReducer = (state = initialState, action) => {
  switch (action.type) {
      case FETCHING_FACTURA:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_FACTURA_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
      case FETCHING_FACTURA_FAILURE:
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
    getFacturaReducer
});
