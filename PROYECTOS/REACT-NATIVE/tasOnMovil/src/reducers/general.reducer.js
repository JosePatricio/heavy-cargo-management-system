import { combineReducers } from 'redux';
import { NET_IS_OFF,NET_IS_ON,FETCHING_FCMTOKEN_SUCCESS_DEVICE,FETCHING_NOTIFICATIONS_SUCCESS} from '../actions/actionTypes';
  

const initialState ={
    status: true,
    data:null,
    isFetching: false,
    error: false
    }
   
const netWorkInfo = (state = initialState, action) => {
    //console.log('GENERAL  REDUCER TIPO  ',action.type);
  switch (action.type) {

      case NET_IS_ON:
          return {
            status: true,
          }

      case NET_IS_OFF:
          return {
            status: false,
          }
      default:
        return state;
    }
}



const deviceFcmToken = (state = initialState, action) => {
  switch (action.type) {

       case FETCHING_FCMTOKEN_SUCCESS_DEVICE:
          return {
            data: action.data,
          }
      default:
        return state;
    }
}

const notifications = (state = initialState, action) => {
  switch (action.type) {

       case FETCHING_NOTIFICATIONS_SUCCESS:
          return {
            data: action.data,
          }
      default:
        return state;
    }
}



export default combineReducers({
    netWorkInfo,deviceFcmToken,notifications
});
