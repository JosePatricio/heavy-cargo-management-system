import { combineReducers } from 'redux';
import { FETCHING_CLIENTE_DATA,FETCHING_CLIENTE_DATA_SUCCESS,FETCHING_CLIENTE_DATA_FAILURE} from '../actions/actionTypes';
  


const initialState ={
    status: true,
    data:null,
    isFetching: false,
    error: false
    }
   
const getClienteData = (state = initialState, action) => {
        switch (action.type) {
      
            case FETCHING_CLIENTE_DATA:
                return {
                 ...state,
                 data: [],
                 isFetching : true
                }
      
            case FETCHING_CLIENTE_DATA_SUCCESS:
                return {
                  ...state,
                 data: action.data,
                 isFetching : false
                }
          
                case FETCHING_CLIENTE_DATA_FAILURE:
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
     getClienteData
});
