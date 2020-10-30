import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form'
import authReducer from "./auth.reducer";
import userReducer from "./user.reducer";
import solicitudReducer from "./solicitud.reducer";
import misfletesReducer from "./misfletes.reducer";
import driverVehicleReducer from "./driverVehicle.reducer";
import generalReducer from "./general.reducer";
import facturaReducer from "./factura.reducer";
import empresaReducer from "./empresa.reducer";
import publicReducer from "./public.reducer";
import clienteReducer from "./cliente.reducer";


const reducers = {
    authReducer,
    userReducer,
    solicitudReducer,
    misfletesReducer,
    driverVehicleReducer,
    generalReducer,
    facturaReducer,
    empresaReducer,
    publicReducer,
    clienteReducer,
    form: formReducer
};

const appReducer = combineReducers(reducers);

const rootReducer = (state, action) => {

    if (action.type === "USER_LOGGED_OUT_SUCCESS") {
        state = {}
    }

    return appReducer(state, action);
}

export default rootReducer;
