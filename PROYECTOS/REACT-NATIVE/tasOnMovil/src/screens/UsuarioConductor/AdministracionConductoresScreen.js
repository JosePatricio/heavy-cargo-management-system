import { Body, Card, CardItem, Content, Icon } from 'native-base';
import React, { Component } from "react";
import { FlatList, Keyboard, RefreshControl, View } from "react-native";
import { Overlay } from 'react-native-elements';
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { email, length, required } from 'redux-form-validators';
import { createDriver, getMisConductores, updateConductorCamion } from "../../actions/driverVehicle.action";
import { obtenerCatalogoItem } from "../../actions/general.action";
import { AwesomeAlert, AwesomeButtonRick, CustomInputComponent, DropDownList, Loader, Text } from "../../components/";
import { idCatalogoLicencia } from '../../Constants';
import { defaultTasonColor, style, styleLogin as styles } from '../../styles/';
import { onlyNums } from '../../utils/normalize';
import { no_connected_info } from '../../utils/tas-on-texts';
import { deviceHeight } from '../../utils/util';

const utf8 = require('utf8');

const renderPicker = ({
  meta: {touched, error},
  data,
  input: {onChange, value, ...restInput},

  placeholder,
  itemValue,
  itemLabel,description,
  ...inputProps
}) => {
  return (
    <View style={{paddingBottom: 10}}>
      <DropDownList
        selectedValue={`${value}`}
        data={data}
        itemValue={itemValue}
        itemLabel={itemLabel}
        onValueChange={onChange}
        placeholder={placeholder}
        description={description}
      />
      {touched && error && <Text style={style.errorText}>{error}</Text>}
    </View>
  );
};


class AdministracionConductoresScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
     currentUser:{},
     showAlert: false,
     textAlert: "",
     titleAlert: "",
     isErrorAlert:false,
     isPostedSuccessfully: false,
     tiposLicencia: [],
     isLoading:false,
     refreshing:false,
     drivers:[],
     modalDriverInfoVisible:false,
     modalDriverFormVisible:false,
     selectedDriver:{}
    }
  }


  componentDidMount() {
   this.loadData();
  }
  
  loadData = async () => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getMisConductores());
      const response1 = await this.props.dispatch(obtenerCatalogoItem(idCatalogoLicencia));

      if (response && response1) {
        this.setState({isLoading:false,drivers:response.responseBody ,tiposLicencia:response1.responseBody});

      } else {
        throw response;
      }
    } catch (error) {
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
      });
    }
  };

  loadDrivers = async () => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getMisConductores());
   
      if (response) {
        this.setState({isLoading:false,drivers:response.responseBody});
   } else {
        throw response;
      }
    } catch (error) {
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
      });
    }
  };

 
 
  createDriver = async values => {
  
    Keyboard.dismiss();
  this.setState({isLoading:true});
     try {
      const response = await this.props.dispatch(createDriver(values));
   
      if(response.success){

        this.setState({
          showAlert: true,
          textAlert: response.responseBody.responseMessage,
          titleAlert: 'Mensaje',
          isErrorAlert: false,
          isLoading: false,
          modalDriverFormVisible:false,
          isPostedSuccessfully:
            response.responseBody.response == 'ERROR' ? false : true,
        });

      }
      else {
        throw response;
      }
    
    } catch (error) {
    console.log('ERRRO screen  ',error);
   
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
     
     this.setState({
        showAlert: true,
        textAlert: utf8.decode(errorText),
        titleAlert: "Error",
        isErrorAlert:true
      }); 

    } 
  };


  updateDriver = async values => {
  
  this.setState({isLoading:true});
     try {
      const response = await this.props.dispatch(updateConductorCamion(values));
      
      if(response.success){

        this.setState({
          showAlert: true,
          textAlert: response.responseBody.responseMessage,
          titleAlert: 'Mensaje',
          modalDriverInfoVisible:false,
          isErrorAlert: false,
          isLoading: false,
          isPostedSuccessfully:
            response.responseBody.response == 'ERROR' ? false : true,
        });
      }
      else {
        throw response;
      }
    
    } catch (error) {
    console.log('ERRRO screen  ',error);
    let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
     if(!errorText){
       errorText=JSON.String(error);
     }
     this.setState({
        showAlert: true,
        textAlert: utf8.decode(errorText),
        titleAlert: "Error",
        isErrorAlert:true,
        isLoading:false
      }); 

    } 
  };


  onEdit= values => {
   
    this.props.change('phone',  values.phone ? values.phone : null);
    
    if(!values.licenseType && !values.email && !values.phone && !values.dni){
      return;
    }
    

    Keyboard.dismiss();
    if(this.props.generalReducer.netWorkInfo.status==false){
      this.setState({
        showAlert: true,
        textAlert: no_connected_info,
        titleAlert: "Error",
        isErrorAlert:true,
        refreshing:false,
        isLoading:false,
      });
        return;
    }

    const {selectedDriver}=this.state;

    let payload= {
      conductorId: selectedDriver.conductorId,
      conductorUsuario: selectedDriver.conductorUsuario,
      conductorCedula: values.dni,
      conductorNombre: selectedDriver.conductorNombre,
      conductorApellido: selectedDriver.conductorApellido,
      conductorTipoLicencia: values.licenseType,
      conductorTelefono: values.phone,
      conductorEmail: values.email,
      conductorEstado: selectedDriver.conductorEstado
    }
  
  
  console.log('payload   ',payload);

  this.updateDriver(JSON.stringify(payload));

  };

  onSubmit = values => {
   
    this.props.change('phone',  values.name ? values.name : null);
    
    if(!values.name && !values.lastname && !values.dni && !values.licenseType && !values.email && !values.phone){
      return;
    }
    
    if(this.props.generalReducer.netWorkInfo.status==false){
      this.setState({
        showAlert: true,
        textAlert: no_connected_info,
        titleAlert: "Error",
        isErrorAlert:true,
        refreshing:false,
        isLoading:false,
      });
        return;
    }

  let payload={
  conductorCedula: values.dni,
  conductorTelefono: values.phone,
  conductorNombre: values.name,
  conductorApellido: values.lastname,
  conductorTipoLicencia: String(values.licenseType),
  conductorEmail: values.email
  }  
  this.createDriver(JSON.stringify(payload));

  };

  
  cancel=()=>{
    Actions.pop();
  }
  onInputSubmit = () => {
   
    };


  hideAlert = () => {
    this.setState({
      showAlert: false
    });
   
    if(this.state.isPostedSuccessfully){
    this.props.reset();
    this.loadDrivers();
  }
};

  dismissAlert= () => {
    if(this.state.showAlert){
  this.setState({
    showAlert: false
  });
}
 // Actions.pop();

};

getDescriptionFromId(list :[], id){
  let res='';
if(!list){
  return res;
}

  if(list.length>0){
  list.forEach(item=>{
      if(item.catalogoItemId == id){
          res=item.catalogoItemDescripcion;
      }
  });}
  else{
    return  res;    
  }
return  res;
}



onRefresh=()=>{
this.loadDrivers();
}

bindDriversFlatList(data) {

  const { driverVehicleReducer,handleSubmit } = this.props;
  const { tiposLicencia } = this.state;
  if(!data){
    return;
  }

const {isLoading}= this.state;
  if(data.length==0){
    const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
    return (  
    <View style={[{marginBottom:20}]}>
    <FlatList
      data={emptyData}
      renderItem={({item}) => (
        <View style={style.cardStyle}>
      {(!isLoading && !driverVehicleReducer.getDriversReducer.isFetching) && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
      </View>
      )
    }
    keyExtractor={ (item, index) => String(index) }
      refreshControl={
        <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
      }
      onEndReachedThreshold={(0, 4)}
    />
  </View>

    );
  }else{
  return (
    <View style={{paddingBottom:70}}>
      <FlatList
        data={data}
        renderItem={({item}) => (
          <Card collapsable={true} style={({elevation: 4}, style.cardStyle)}>
            <CardItem
              button
              style={style.noPadding}
              disabled={this.state.isButtonDisabled}
              onPress={() => this.openModalInfoDriver(item) }>
              <Body style={style.noPadding}>
               
              <View style={style.viewHeader}>
              <Text style={style.viewHeaderText}>{item.conductorNombre} {item.conductorApellido}</Text>
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '40%'}}>
                  <Text style={style.textInfo}>Cédula</Text>
                  <Text style={style.textData}>
                  {item.conductorCedula}
                  </Text>
                </View>
                <View style={{padding: 5, width: '60%'}}>
                  <Text style={style.textInfo}>Licencia</Text>
                  <Text style={style.textData}> {this.getDescriptionFromId(tiposLicencia,item.conductorTipoLicencia)} </Text>
                </View>
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '40%'}}>
                  <Text style={style.textInfo}>Teléfono</Text>
                  <Text style={style.textData}>
                  {item.conductorTelefono} 
                  </Text>
                </View>
                <View style={{padding: 5, width: '60%'}}>
                  <Text style={style.textInfo}>Correo</Text>
                  <Text style={style.textData}>
                  {item.conductorEmail} 
                  </Text>
                </View>
              </View>
              </Body>
            </CardItem>
          </Card>
        )}
        keyExtractor={ ( item, index ) => `${index}` }
        refreshControl={
          <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
        }
        onEndReachedThreshold={(0, 4)}
      />
    </View>
  );
}}


renderInfoDriver(){
  const { handleSubmit } = this.props;
  const {tiposLicencia,selectedDriver}=this.state;
  return(
    <View style={[style.cardStyle,{flex: 1,
      flexDirection: 'column',
      justifyContent: 'space-between'}]}>

<Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
          Datos del conductor
        </Text>

                    <View style={{ width: '100%',paddingBottom:10}}>
                      <Text style={style.textInfoDisabled}>Nombres</Text>
                      <Text style={style.textDataDisabled}>{selectedDriver.conductorNombre} {selectedDriver.conductorApellido}</Text>
                    </View>
        

         {selectedDriver.conductorCedula? <View style={{ width: '100%',paddingBottom:10}}>
                      <Text style={style.textInfoDisabled}>Cédula</Text>
                      <Text style={style.textDataDisabled}>{selectedDriver.conductorCedula}</Text>
                    </View>:<Field
            component={CustomInputComponent}
            name="dni"
            type="text"
            placeholder="Cédula"
            normalize={onlyNums}
            maxLength={10}
            autoFocus
            validate={length({
              msg: 'Mínimo de caracteres 10',
              min: 10,
              max: 10,
            })}
            maxLength={10}
            keyboardType={'numeric'}
          />}
                   
              <Field
              name="licenseType"
              component={renderPicker}
              data={tiposLicencia}
              itemValue="catalogoItemId"
              placeholder=""
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione tipo de licencia'}
              validate={[required({msg: 'Campo requerido'})]}/> 
            
            <Field
            component={CustomInputComponent}
            name="email"
            type="text"
            placeholder="Correo electrónico"
            validate={[
              required({msg: 'Campo requerido'}),
              email({msg: 'La dirección ingresada no es un correo valido'}),
            ]}
          />
           
           <Field
              component={CustomInputComponent}
              name="phone"
              type="text"
              placeholder="Teléfono"
              keyboardType={'numeric'}
              normalize={onlyNums}
              maxLength={10}
              validate={[length({
              msg: 'Mínimo de caracteres 6',
              min: 6,
              max: 10,
              }),required({msg: 'Campo requerido'})]}
              />

<View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.setState({modalDriverInfoVisible:false})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.onEdit)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Guardar</Text>
             </AwesomeButtonRick>
          </View>

       

    </View>
  );
}




renderFormDriver(){
  const { handleSubmit } = this.props;
  const {tiposLicencia}=this.state;
  return(
    <View style={[style.cardStyle,{flex: 1,
      flexDirection: 'column',
      justifyContent: 'space-between'}]}>

<Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
          Datos del conductor
        </Text>

          <Field
            component={CustomInputComponent}
            name="name"
            type="text"
            placeholder="Nombres"
            autoCapitalize={'words'}
            autoFocus 
            validate={[required({msg: 'Campo requerido'})]}
          />

            <Field
            component={CustomInputComponent}
            name="lastname"
            autoCapitalize={'words'}
            type="text"
            placeholder="Apellidos"
            validate={[required({msg: 'Campo requerido'})]}
          />

            <Field
            component={CustomInputComponent}
            name="dni"
            type="text"
            placeholder="Cédula"
            normalize={onlyNums}
            maxLength={10}
            validate={length({
              msg: 'Mínimo de caracteres 10',
              min: 10,
              max: 10,
            })}
            maxLength={10}
            keyboardType={'numeric'}
          />


    <Field
              name="licenseType"
              component={renderPicker}
              data={tiposLicencia}
              placeholder=""
              itemValue="catalogoItemId"
              itemLabel="catalogoItemDescripcion"
              description={'Seleccione tipo de licencia'}
              validate={[required({msg: 'Campo requerido'})]}
              /> 
            
            <Field
            component={CustomInputComponent}
            name="email"
            type="text"
            placeholder="Correo electrónico"
            validate={[
              required({msg: 'Campo requerido'}),
              email({msg: 'La dirección ingresada no es un correo valido'}),
            ]}
          />
           
           <Field
              component={CustomInputComponent}
              name="phone"
              type="text"
              placeholder="Teléfono"
              keyboardType={'numeric'}
              normalize={onlyNums}
              maxLength={10}
              validate={[length({
              msg: 'Mínimo de caracteres 6',
              min: 6,
              max: 10,
              }),required({msg: 'Campo requerido'})]}
              />

<View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.setState({modalDriverFormVisible:false})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.onSubmit)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Guardar</Text>
             </AwesomeButtonRick>
          </View>

       

    </View>
  );
}

openModalInfoDriver=(item)=>{
  this.setState({ modalDriverInfoVisible: true,selectedDriver:item })

  this.props.change('email',item.conductorEmail);
  this.props.change('phone',item.conductorTelefono);
  this.props.change('licenseType',item.conductorTipoLicencia);
  this.props.change('dni',item.conductorCedula);

}
render() {
    const { driverVehicleReducer,handleSubmit } = this.props;
    const {modalDriverInfoVisible,modalDriverFormVisible, drivers,showAlert,textAlert,titleAlert,isErrorAlert,isLoading } = this.state;
 
    return (
      <View style={styles.container}>
        {isLoading && <Loader />}

        <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={() =>this.setState({modalDriverFormVisible:true})} backgroundColor={defaultTasonColor}>
<Icon name="md-add" type="Ionicons" style={style.defaultIconButton}/>
        <Text style={style.defaultTextButton}>Nuevo</Text>
</AwesomeButtonRick>
     
     {this.bindDriversFlatList(drivers)} 

     {showAlert &&<AwesomeAlert
            show={showAlert}
            showProgress={false}
            title={titleAlert}
            message={textAlert}
            closeOnTouchOutside={!this.state.isPostedSuccessfully}
            closeOnHardwareBackPress={false}
            showCancelButton={false}
            showConfirmButton={true}
            confirmText="Ok"
            contentContainerStyle={style.alertContentContainerStyle}
            confirmButtonStyle={style.alertConfirmButtonStyle}
            confirmButtonTextStyle={isErrorAlert?style.alertConfirmButtonErrorTextStyle:style.alertConfirmButtonTextStyle}
            titleStyle={style.alertTitleStyle}
            onConfirmPressed={() => {
              this.hideAlert();
            }}
            onDismiss={() => {
              this.dismissAlert();
            }}
          />}

<Overlay width={'95%'}
  isVisible={modalDriverFormVisible}
  onBackdropPress={() => this.setState({ modalDriverFormVisible: false })}
><Content>
  {this.renderFormDriver()}
  </Content>
</Overlay>

<Overlay width={'95%'} height={deviceHeight/1.6}
  isVisible={modalDriverInfoVisible}
  onBackdropPress={() => this.setState({ modalDriverInfoVisible: false })}
><Content>
  {this.renderInfoDriver()}
  </Content>
</Overlay>



       
      </View>
    );
  }
}



const selector = formValueSelector('AdministracionConductoresForm') 

const mapStateToProps = state => {
  return {
    driverVehicleReducer: state.driverVehicleReducer,
    generalReducer: state.generalReducer
  };
};

const mapDispatchToProps = dispatch => {
  return {
      dispatch
  };
};

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  reduxForm({
    form: "AdministracionConductoresForm",
    fields: ["email","dni"],
    enableReinitialize: true
  })
)(AdministracionConductoresScreen);
