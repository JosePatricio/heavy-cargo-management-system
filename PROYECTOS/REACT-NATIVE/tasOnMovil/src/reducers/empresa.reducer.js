import { combineReducers } from 'redux';
import { FETCHING_EMPRESA,FETCHING_EMPRESA_SUCCESS,FETCHING_EMPRESA_FAILURE} from '../actions/actionTypes';


const initialState ={
    data: [],
    isFetching: false,
    error: false
}

const getFacturaReducer = (state = initialState, action) => {
  switch (action.type) {
      case FETCHING_EMPRESA:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_EMPRESA_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
      case FETCHING_EMPRESA_FAILURE:
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
