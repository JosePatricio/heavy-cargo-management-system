import { combineReducers } from 'redux';
import {UPDATING_PASSWORD_LOADING,UPDATING_PASSWORD_SUCCESS,UPDATING_PASSWORD_FAILURE,RECOVERING_PASSWORD_LOADING,RECOVERING_PASSWORD_SUCCESS,RECOVERING_PASSWORD_FAILURE} from '../actions/actionTypes';

const authData = (state = {}, action) => {
    switch (action.type) {
        case "AUTH_USER_SUCCESS":
            return {
              token: action.token,
              isLoggedIn: true,
              userStatus: action.userStatus,
              userRole: action.userRole
            }
        case "AUTH_USER_FAIL":
            return {
              token: null,
              isLoggedIn: false
            }
        case "AUTH_USER_RESET":
            return {
                token: null,
                isLoggedIn: false,
                userStatus: null
            }
        default:
          return state;
    }
}

const createUser = (state = {}, action) => {
    switch (action.type) {

      case "CREATE_USER_LOADING":
          return {
              isLoading: true,
              isError: false,
              isSuccess: false,
              errors: null,

          }

      case "CREAT_USER_SUCCESS":
          return {
              isLoading: false,
              isError: false,
              isSuccess: true,
              errors: null
          }

      case "CREAT_USER_FAIL":
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


const loginUser = (state = {}, action) => {
    switch (action.type) {

      case "LOGIN_USER_LOADING":
          return {
              isLoading: true,
              isError: false,
              isSuccess: false,
              errors: null
          }

      case "LOGIN_USER_SUCCESS":
          return {
              isLoading: false,
              isError: false,
              isSuccess: true,
              errors: null,
          }

      case "LOGIN_USER_FAIL":
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


const recoverPassword = (state = {}, action) => {
    
    switch (action.type) {
      case RECOVERING_PASSWORD_LOADING:
        return {
              isLoading: true,
              isError: false,
              isSuccess: false,
              errors: null
          }

      case RECOVERING_PASSWORD_SUCCESS:
          return {
              isLoading: false,
              isError: false,
              isSuccess: true,
              data:action.data,
              errors: null,
          }

      case RECOVERING_PASSWORD_FAILURE:
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


const updatePassword = (state = {}, action) => {
    switch (action.type) {
      case UPDATING_PASSWORD_LOADING:
        return {
              isLoading: true,
              isError: false,
              isSuccess: false,
              errors: null
          }

      case UPDATING_PASSWORD_SUCCESS:
          return {
              isLoading: false,
              isError: false,
              isSuccess: true,
              data:action.data,
              errors: null,
          }

      case UPDATING_PASSWORD_FAILURE:
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
  createUser,
  loginUser,
  authData,
  recoverPassword,
  updatePassword
});
