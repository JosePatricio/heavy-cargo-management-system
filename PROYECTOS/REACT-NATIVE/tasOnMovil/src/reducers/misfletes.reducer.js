import { combineReducers } from 'redux';
import { FETCHING_MISFLETES,FETCHING_MISFLETES_SUCCESS,FETCHING_MISFLETES_FAILURE
,FETCHING_IMAGES,FETCHING_IMAGES_SUCCESS,FETCHING_IMAGES_FAILURE} from '../actions/actionTypes';

const initialState ={
    data: [],
    isFetching: false,
    error: false
}

const getAllReducer = (state = initialState, action) => {
  switch (action.type) {

      case FETCHING_MISFLETES:
          return {
           ...state,
           data: [],
           data1: [],
           data2: [],
           data3: [],
           data4: [],
           isFetching : true
          }

      case FETCHING_MISFLETES_SUCCESS:
          return {
            ...state,
           data: action.data,
           data1: action.data1,
           data2: action.data2,
           data3: action.data3,
           data4: action.data4,
           data5: action.data5,
           data6: action.data6,
           data7: action.data7,
           isFetching : false
          }
    
          case FETCHING_MISFLETES_FAILURE:
            return {
              ...state,
             isFetching : false,
                error:true
            }

      default:
        return state;
    }
}

const getImagesReducer = (state = initialState, action) => {
  switch (action.type) {

      case FETCHING_IMAGES:
          return {
           ...state,
           data: [],
           isFetching : true
          }

      case FETCHING_IMAGES_SUCCESS:
          return {
            ...state,
           data: action.data,
           isFetching : false
          }
    
      case FETCHING_IMAGES_FAILURE:
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
  getAllReducer,getImagesReducer
});
