import { Body, Button, Card, Container, Content, Icon as IconNb, ListItem, Separator } from 'native-base';
import React, { Component } from 'react';
import { Keyboard, Linking, Modal, Platform, TouchableHighlight, TouchableOpacity, View } from 'react-native';
import { Overlay } from 'react-native-elements';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { Field, formValueSelector, reduxForm } from 'redux-form';
import { email, length, required } from 'redux-form-validators';
import { addVehiculo, createDriver, getDrivers, getVehicles } from '../../actions/driverVehicle.action';
import { obtenerCatalogoItem } from "../../actions/general.action";
import { getSolicitudById, updateMyOfert } from '../../actions/solicitud.action';
import { AwesomeAlert, AwesomeButtonRick, CheckBox, Collapse, CollapseBody, CollapseHeader, CustomInputComponent, DropDownList, HTML, Line, Loader, Moment, Text } from '../../components/';
import { idCatalogoCamion, idCatalogoCarga, idCatalogoLicencia, idCatalogoPesos, MY_OFERTS_CANCELED, MY_OFERTS_TO_RECIEVE } from '../../Constants';
import { defaultTasonColor, style, styleSolicitudOferta as styles } from '../../styles/';
import { onlyNums } from '../../utils/normalize';
import { no_connected_info, TERMINOS_CONDICIONES } from '../../utils/tas-on-texts';

const utf8 = require('utf8');

const renderPicker = ({
  meta: {touched, error},
  data,
  input: {onChange, value, ...restInput},

  placeholder,
  itemValue,
  itemLabel,itemLabel2,description,
  ...inputProps
}) => {
  return (
    <View style={{paddingBottom: 10}}>
      <DropDownList
        selectedValue={`${value}`}
        data={data}
        itemValue={itemValue}
        itemLabel={itemLabel}
        itemLabel2={itemLabel2}
        onValueChange={onChange}
        placeholder={placeholder}
        description={description}

      />
      {touched && error && <Text style={style.errorText}>{error}</Text>}
    </View>
  );
};


const renderCheckBox = ({
  input: {value, onChange, ...input},checked,
  meta: {touched, error},
  ...restInput
}) => {
  return (
    <View style={{padding: 5, alignItems: 'center'}}>
      <CheckBox {...input} checked={checked} onChange={onChange} />
      {touched && error && <Text style={style.errorText}>{error}</Text>}
    </View>
  );
};

class AprobadasScreen extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: props.usuarioService.getUser.userDetails,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert: false,
      data: {},
      isLoading: false,
      isPostedSuccessfully: false,
      drivers: [],
      vehicles: [],
      selectedDriver: undefined,
      selectedVehicle: undefined,
      isChecked: false,
      showModal: false,
      disabled:true,
      modalDriverFormVisible:false,
      tiposLicencia:[],
      isPostedDrivers:false,
      isPostedVehicles:false,
      errorMessage:'',
      unidadesCapacidad:[],tiposCarga:[] ,tiposCamion:[],
      modalVehicleFormVisible:false
    };
  }



  componentDidMount() {
     this.loadData();
     this.props.change('acceptance',true);
  }

  
  loadDataNewDriver = async () => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(obtenerCatalogoItem(idCatalogoLicencia));

      if (response) {
        return response.responseBody;
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


  loadDataVehicles = async () => {
    try {
      this.setState({isLoading: true});
     
      
      const response1 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoPesos),
      );

      const response2 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoCarga),
      );

      const response3 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoCamion),
      );

      
      if ( response1 && response2 && response3) {
        this.setState({isLoading:false,unidadesCapacidad:response1.responseBody ,tiposCarga:response2.responseBody,tiposCamion:response3.responseBody});
    
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


  loadDrivers = async values => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        getDrivers(this.props.solicitud.idSolicitud, this.state.user)
       // getDrivers('TAS-ON0003881', this.state.user),
      );
      if (response.success) {
        this.setState({drivers: response.responseBody, isLoading: false});
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
        isLoading: false,
      });
    }
  };

  loadVehicles = async values => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        getVehicles(this.props.solicitud.idSolicitud, this.state.user)
       // getVehicles('TAS-ON0003881', this.state.user),
      );

      if (response.success) {
        this.setState({vehicles: response.responseBody, isLoading: false});
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



  loadData = async values => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getSolicitudById(this.props.solicitud.idSolicitud, this.state.user));

      const response1 = await this.props.dispatch(getDrivers(this.props.solicitud.idSolicitud, this.state.user));

      const response2 = await this.props.dispatch(getVehicles(this.props.solicitud.idSolicitud, this.state.user));
   
      if (!response.success && !response1.success && !response2.success) {
        throw response;
      } else {
        this.setState({data: response.responseBody,drivers: response1.responseBody,vehicles: response2.responseBody, isLoading: false});
      }
    } catch (error) {
      console.log('ERRROrrrr loadData',error);
      let errorText;
      errorText = error.responseMessage;


      console.log('errorText  ',errorText);
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText = JSON.stringify(error.responseBody);
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: 'Error',
        isErrorAlert: true,
        isLoading: false,
      });
    }
  };


  updateOfferService = async values => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        updateMyOfert(JSON.stringify(values), this.state.user),
      );
      if (response.success) {
     
        this.setState({textAlert: response.responseBody.responseMessage,
          titleAlert:'Mensaje',
          isLoading:false,
          showAlert:true,
          isErrorAlert:false,
          isPostedSuccessfully: (response.responseBody.response== 'ERROR' ? false : true),
          isConfirmation:false,
          isPostedVehicles:false,
          isPostedDrivers:false
        });
       
      } else {
        throw response;
      }
    } catch (error) {
      console.log('ERROR updateOfferService ',error);
      let errorText;
      if (error.message) {
        errorText = error.message;
      }
      errorText = error.responseBody;
      this.setState({
        textAlert: errorText,
        titleAlert: 'Error',
        showAlert: true,
        isLoading: false,
        isErrorAlert:true
      });
    }
  };

  updateOffer = values => {
    this.props.change('driver',values.driver?values.driver:null);  
  
console.log('values   ',values);
    
    let object = {
      idConductor: String(values.driver),
      idOferta: String(this.state.data.offer.idOferta),
      idVehiculo: String(values.vehicle),
      state: String(MY_OFERTS_TO_RECIEVE),
    };

    if(!object.idConductor || !object.idVehiculo){
      return;
    }
   const {driver,vehicle,acceptance} = this.props;
   this.setState({user:this.state.user});

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

   this.updateOfferService(object);

  };

  hideAlert = () => {
    this.setState({
      showAlert: false,
    });


    console.log('hideAlert  ',this.state.isPostedSuccessfully,this.state.isPostedDrivers,this.state.isPostedVehicles);
    if(this.state.isPostedSuccessfully){
    
      if(this.state.isPostedDrivers){
        this.setState({  modalDriverFormVisible:true});
        this.loadDrivers();
      }
      if(this.state.isPostedVehicles){
        this.setState({  modalVehicleFormVisible:true});
        this.loadVehicles();
      }

      if(!this.state.isPostedVehicles && !this.state.isPostedDrivers){
        Actions.reset('MisSolicitudes');
      }

     
    }
  };

  dismissAlert = () => {
    if(this.state.showAlert){
    this.setState({
      showAlert: false,
    });
  }
  };

  cancelOfferService = async values => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(
        updateMyOfert(JSON.stringify(values), this.state.user),
      );
      if (response.success) {
        this.setState({
          textAlert: response.responseBody.responseMessage,
          titleAlert: 'Mensaje',
          isLoading: false,
          showAlert: true,
          isPostedSuccessfully:
            response.responseBody.response == 'ERROR' ? false : true,
        });
      } else {
        throw response;
      }
    } catch (error) {
      let errorText;
      if (error.message) {
        errorText = error.message;
      }
      errorText = error.responseBody;

      this.setState({
        textAlert: errorText,
        titleAlert: 'Error',
        showAlert: true,
      });
    }
  };

  cancelOffer = values => {
    let object = {
      idOferta: this.state.data.offer.idOferta,
      state: MY_OFERTS_CANCELED,
    };

    this.cancelOfferService(object);
  };
  onDriverValueChange(value: string) {
    
    this.setState({
      selectedDriver: value,
      disabled: (this.state.selectedDriver == undefined && this.state.selectedVehicle == undefined )?true:false
    });
  }
  onVehicleValueChange(value: string) {
    this.setState({
      selectedVehicle: value,
    });
  }
  openModal = values => {
    this.setState({showModal: true});
  };
  hideModal = () => {
    this.setState({showModal: false});
  };

  dialCall = number => {
     let phoneNumber = '';

    if (Platform.OS === 'android') {
      phoneNumber = 'tel:${'+number+'}'; 
    }
    else {
      phoneNumber = 'telprompt:${'+number+'}';
    }
    Linking.openURL(phoneNumber); 
  };

  openMail = email => {
   Linking.openURL('mailto:'+email+''); 
  };

  newDriver = () => {
    this.loadDataNewDriver().then(res=>{
      this.setState({isLoading:false,tiposLicencia:res,modalDriverFormVisible:true});
    });
   };
 
   newVehicle = () => {
    this.loadDataVehicles().then(res=>{
      this.setState({modalVehicleFormVisible:true});
    });
   };
   




   createDriver = async values => {
  
    Keyboard.dismiss();
  this.setState({isLoading:true,modalDriverFormVisible:false});
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
          isPostedSuccessfully: response.responseBody.response == 'ERROR' ? false : true,
          isPostedDrivers:true,
          isPostedVehicles:false

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

  onAllSubmit = (foo, data)=> {
    // do your async behavior and after it returns
    console.log('STUFFF  ',foo);
     if (foo) {

     }
   }

   createNewDriver=()=>{
    
    
    if(this.props.name==undefined || this.props.lastname==undefined || this.props.dni==undefined || this.props.licenseType==undefined || this.props.email==undefined || this.props.phone==undefined){
     this.setState({errorMessage:'Todos los campos son requeridos'});
      return;
    }else{
      this.setState({errorMessage:''});
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
  conductorCedula: this.props.dni,
  conductorTelefono: this.props.phone,
  conductorNombre: this.props.name,
  conductorApellido: this.props.lastname,
  conductorTipoLicencia: String(this.props.licenseType),
  conductorEmail: this.props.email
  }  
  this.createDriver(JSON.stringify(payload)).then(res=>{

    this.props.change('dni','');
    this.props.change('phone','');
    this.props.change('name','');
    this.props.change('lastname','');
    this.props.change('licenseType',null);
    this.props.change('email','');
  }); 
   }
   

   createVehicle = async values => {
  
    Keyboard.dismiss();
  this.setState({isLoading:true});
     try {
      const response = await this.props.dispatch(addVehiculo(values));
   
      if(response.success){

        this.setState({
          showAlert: true,
          textAlert: response.responseBody.responseMessage,
          titleAlert: 'Mensaje',
          isErrorAlert: false,
          isLoading: false,
          modalVehicleFormVisible:false,
          isPostedSuccessfully:
            response.responseBody.response == 'ERROR' ? false : true,
            isPostedDrivers:false,
            isPostedVehicles:true
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


   addVehicle=()=>{
   
    if(!this.props.vehiculoPlaca || !this.props.vehiculoModelo || !this.props.vehiculoAnio || !this.props.vehiculoTipoCarga || !this.props.vehiculoTipoCamion || !this.props.vehiculoTipoCapacidad || !this.props.vehiculoCapacidad ){
      this.setState({errorMessage:'Todos los campos son requeridos'});
      return;
    }else{
      this.setState({errorMessage:''});
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
    vehiculoPlaca: this.props.vehiculoPlaca,
    vehiculoModelo: this.props.vehiculoModelo,
    vehiculoAnio: this.props.vehiculoAnio,
    vehiculoTipoCamion: String(this.props.vehiculoTipoCamion),
    vehiculoTipoCarga: String(this.props.vehiculoTipoCarga),
    vehiculoCapacidad: String(this.props.vehiculoCapacidad),
    vehiculoTipoCapacidad: String(this.props.vehiculoTipoCapacidad),
    vehiculoArcsaAlimentos: (this.props.vehiculoArcsaAlimentos?true:false),
    vehiculoArcsaCosmeticos: (this.props.vehiculoArcsaCosmeticos?true:false),
    vehiculoArcsaMedicamentos: (this.props.vehiculoArcsaMedicamentos?true:false),
    vehiculoBasc: (this.props.vehiculoBasc?true:false),
    vehiculoPaseInternacional: (this.props.vehiculoPaseInternacional?true:false)
  }  
  
this.createVehicle(JSON.stringify(payload)).then(res=>{
  this.props.change('vehiculoPlaca',null);
  this.props.change('vehiculoModelo',null);
  this.props.change('vehiculoAnio',null);
  this.props.change('vehiculoTipoCamion',null);
  this.props.change('vehiculoTipoCarga',null);
  this.props.change('vehiculoCapacidad',null);
  this.props.change('vehiculoArcsaAlimentos',null);
  this.props.change('vehiculoArcsaCosmeticos',null);
  this.props.change('vehiculoArcsaMedicamentos',null);
  this.props.change('vehiculoBasc',null);
  this.props.change('vehiculoPaseInternacional',null);
 
}); 
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
                itemValue="catalogoItemId"
                itemLabel="catalogoItemDescripcion"
                placeholder="Seleccione"
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

              <Text style={[style.errorText,{alignSelf:'center'}]}>{this.state.errorMessage}</Text>
  <View style={ style.twoButtonsArea}>
            <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.setState({modalDriverFormVisible:false})} backgroundColor={defaultTasonColor}>
              <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
            <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} 
             onPress={()=>this.createNewDriver()} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Guardar</Text>
               </AwesomeButtonRick>
            </View>
  
         
  
      </View>
    );
  }
   



  renderFormVehicle(){
    const { handleSubmit } = this.props;
    const {tiposLicencia,unidadesCapacidad,tiposCamion,tiposCarga}=this.state;
    return(
      <View style={[style.cardStyle,{flex: 1,
        flexDirection: 'column',
        justifyContent: 'space-between'}]}>
  
  <Text style={[style.collapseHeaderText, {paddingBottom: 10}]}>
            Datos del vehículo
          </Text>
  
          <Field
                component={CustomInputComponent}
                name="vehiculoModelo"
                type="text"
                maxLength={20}
                autoCapitalize={'words'}
                placeholder="Marca"
                validate={[required({msg: 'Campo requerido'})]}/>
  
             <Field
                component={CustomInputComponent}
                name="vehiculoAnio"
                type="text" 
                maxLength={4}
                keyboardType={'numeric'}
                normalize={onlyNums}
                placeholder="Año"
                validate={[required({msg: 'Campo requerido'})]}/>
  
               <Field
                component={CustomInputComponent}
                name="vehiculoPlaca"
                type="text"
                maxLength={8}
                placeholder="Placa"
                validate={[required({msg: 'Campo requerido'})]}/>
  
              
                
          <View style={{flexDirection:'row',justifyContent:'space-between'}}>
           <View style={{width:'30%',paddingTop:15}}>
             <Field
                component={CustomInputComponent}
                name="vehiculoCapacidad"
                type="text"
                maxLength={6}
                keyboardType={'numeric'}
                normalize={onlyNums}
                placeholder="Capacidad"
                validate={[required({msg: 'Campo requerido'})]}/>
                </View>
              <View style={{width:'60%'}}>
              <Field
                name="vehiculoTipoCapacidad"
                component={renderPicker}
                data={unidadesCapacidad}
                itemValue="catalogoItemId"
                itemLabel="catalogoItemDescripcion"
                placeholder="Seleccione"
                description={'Unidad de capacidad'}
                validate={[required({msg: 'Campo requerido'})]}
              /> 
             </View>
             </View>
        
              
            
              <Field
                name="vehiculoTipoCarga"
                component={renderPicker}
                data={tiposCarga}
                itemValue="catalogoItemId"
                itemLabel="catalogoItemDescripcion"
                placeholder="Seleccione"
                description={'Seleccione tipo de carga'}
                validate={[required({msg: 'Campo requerido'})]}
              />
     
              <Field
                name="vehiculoTipoCamion"
                component={renderPicker}
                data={tiposCamion}
                itemValue="catalogoItemId"
                itemLabel="catalogoItemDescripcion"
                placeholder="Seleccione"
                description={'Seleccione tipo camión'}
                validate={[required({msg: 'Campo requerido'})]}
              />


       <View style={{paddingLeft:-10}}>
  
       <Text style={style.textInfoDisabled}>Certificados</Text>
  
       <ListItem>
                  <Field name="vehiculoArcsaAlimentos" type="checkbox" component={renderCheckBox}/>
              <Body><Text>ARCSA alimentos</Text></Body>
            </ListItem>
  
            <ListItem>
                  <Field name="vehiculoArcsaCosmeticos" type="checkbox" component={renderCheckBox}/>
              <Body><Text>ARCSA cosméticos</Text></Body>
            </ListItem>
  
            <ListItem>
                  <Field name="vehiculoArcsaMedicamentos" type="checkbox" component={renderCheckBox}/>
              <Body><Text>ARCSA medicamentos</Text></Body>
            </ListItem>
  
            <ListItem>
                  <Field name="vehiculoBasc" type="checkbox" component={renderCheckBox}/>
              <Body><Text>BASC</Text></Body>
            </ListItem>
  
            <ListItem>
                  <Field name="vehiculoPaseInternacional" type="checkbox" component={renderCheckBox}/>
              <Body><Text>Pase internacional</Text></Body>
            </ListItem>
          
       </View> 
  
       <Text style={[style.errorText,{alignSelf:'center'}]}>{this.state.errorMessage}</Text>
           
  <View style={ style.twoButtonsArea}>
            <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.setState({modalVehicleFormVisible:false})} backgroundColor={defaultTasonColor}>
              <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
            <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={()=>this.addVehicle()} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Guardar</Text>
               </AwesomeButtonRick>
            </View>
  
         
  
      </View>
    );
  }


  ontoggleAcordion=()=>{

    console.log('SWEDE');
  }
  render() {
    const {handleSubmit} = this.props;
    const {
      showAlert,
      titleAlert,
      isErrorAlert,  
      textAlert,
      data,
      showModal,
      disabled,modalDriverFormVisible,modalVehicleFormVisible
    } = this.state;
   

    return (
      <Container>
        <Content>
          <View style={styles.container}>

          <Text style={[styles.textIdSolicitud,{alignSelf:'center'}]}>{data.idSolicitud}</Text>
          {data.offer &&  <Text style={[style.textData,{paddingRight:20,alignSelf:'flex-end'}]}>Mi oferta <Text style={style.textInfo}>{data.offer.amount}{' $'}</Text></Text>}
  
            <Card elevation={4} style={styles.cardStyle} >
           
              <Collapse isCollapsed={false} >
             
                <CollapseHeader> 
               
                  <Separator  style={[styles.collapseHeaderSeparator]}>
                    <Text style={styles.collapseHeaderText}>Datos del envío</Text>
                   
                  </Separator>
                
                </CollapseHeader>
                
                <CollapseBody>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Producto</Text>
                      <Text style={styles.textData4}>{data.tipo}</Text>
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Número de piezas</Text>
                      <Text style={styles.textData4}>{data.numeroPiezas}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Volumen Total</Text>
                      <Text style={styles.textData4}>{data.volumen} {data.tipoVolumen}</Text>
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Peso</Text>
                      <Text style={styles.textData4}>{data.peso} {data.tipoPeso}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Estibadores</Text>
                      <Text style={styles.textData4}>{data.numeroEstibadores}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Observaciones</Text>
                      <Text style={styles.textData4}>{data.observaciones}</Text>
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
           
            </Card>

            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={false}>
                <CollapseHeader>
                  <Separator style={styles.collapseHeaderSeparator}>
                    <Text style={styles.collapseHeaderText}>Origen</Text>
                   </Separator>
                </CollapseHeader>
                <CollapseBody>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Provincia</Text>
                      <Text style={styles.textData4}>{data.provinciaOrigen}</Text>
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Ciudad</Text>
                      <Text style={styles.textData4}>{data.origen}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Dirección</Text>
                      <Text style={styles.textData4}>{data.direccion}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Fecha de recolección</Text>
                      <Text style={styles.textData4}><Moment element={Text} format="DD/MM/YYYY hh:mm">{data.fechaEntrega}</Moment></Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Envía</Text>
                      <Text style={styles.textData4}>{data.personaEntrega}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Teléfono</Text>
                     
                      <TouchableOpacity
                        onPress={() => this.dialCall(data.telefonoContacto)}
                        style={style.linkButton}>
                        <Text style={style.linkTextButton}>{data.telefonoContacto}</Text>
                      </TouchableOpacity>

                    </View>
                  </View>
                  
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Correo eletrónico</Text>
                      <TouchableOpacity
                        onPress={() => this.openMail(data.correoContacto)}
                        style={style.linkButton}>
                        <Text style={style.linkTextButton}>{data.correoContacto}</Text>
                      </TouchableOpacity>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={false}>
                <CollapseHeader>
                  <Separator style={styles.collapseHeaderSeparator}>
                    <Text style={styles.collapseHeaderText}>Destino</Text>
                  </Separator>
                </CollapseHeader>
                <CollapseBody>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Provincia</Text>
                      <Text style={styles.textData4}>{data.provinciaDestino}</Text>
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Ciudad</Text>
                      <Text style={styles.textData4}>{data.destino}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Dirección</Text>
                      <Text style={styles.textData4}>{data.direccionEntrega}</Text>
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Fecha de entrega</Text>
                      <Text style={styles.textData4}><Moment element={Text} format="DD/MM/YYYY hh:mm">
                          {data.fechaEntrega}
                        </Moment></Text>
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Recibe</Text>
                      <Text style={styles.textData4}>{data.personaRecibe}</Text>
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <View style={styles.viewInfo4}>
              <Text style={styles.textInfo5}> Configurar viaje </Text>
            </View>

            <View style={styles.viewInfo1}>
              <Card elevation={4}>
                <View style={{padding: 5, width: '100%'}}>
               
           <View style={{alignContent:'center', justifyContent:'center',flexDirection: "row"}} >
               
               <View style={{width: '70%'}}>
               <Field
                    name="driver"
                    component={renderPicker}
                    data={this.state.drivers}
                    itemValue="conductorId"
                    itemLabel="conductorNombre"
                    itemLabel2="conductorApellido"
                    placeholder="Seleccione"
                    description={'Conductor'}
                  />
               </View>
            
             <View style={{paddingTop:20,width: '30%', flexDirection:'row'}}>
               
               <Button iconLeft transparent  onPress={()=>this.newDriver()}>
                  <IconNb name='plus' type='AntDesign' style={[{fontSize: 15,paddingTop:5,color:'#03AADE',  padding:3, paddingRight:25,fontWeight: 'bold',fontSize: 25}]}/>
                </Button>
              </View>
              
             </View>
                  
                </View>

                <View style={{padding: 5, width: '100%'}}>
        <View style={{alignContent:'center', justifyContent:'center',flexDirection: "row"}} >
        <View style={{width: '70%'}}>
                  <Field
                    name="vehicle"
                    component={renderPicker}
                    data={this.state.vehicles}
                    type="select"
                    itemValue="vehiculoId"
                    itemLabel="vehiculoPlaca"
                    placeholder="Seleccione"
                    description={'Vehiculo'}
                  />
                </View> 

                <View style={{paddingTop:20,width: '30%', flexDirection:'row'}}>
               
               <Button iconLeft transparent  onPress={()=>this.newVehicle()}>
                  <IconNb name='plus' type='AntDesign' style={[{fontSize: 15,paddingTop:5,color:'#03AADE',  padding:3, paddingRight:25,fontWeight: 'bold',fontSize: 25}]}/>
                </Button>
              </View>

                </View>
                </View>

                <View style={styles.viewInfo3_1}>
                  <View
                    style={{alignContent: 'center', padding: 5, width: '100%'}}>
                    <Text style={{color: '#4A5F8E', textAlign: 'center'}}>Aceptar términos y condiciones</Text>
                    <Field
                      name="acceptance"
                      type="checkbox"
                      component={renderCheckBox}
                      checked={true}
                    />
                    <Text
                      style={{color: 'blue', textAlign: 'center'}}
                      onPress={() => this.openModal()}>Términos y condiciones</Text>
                  </View>
                </View>
              </Card>
            </View>
            
            <View style={styles.viewInfo7}>
            

              <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} 
              onPress={handleSubmit(this.updateOffer.bind(this))} 
              
              backgroundColor={defaultTasonColor}><Text style={style.defaultTextButton}>Actualizar</Text></AwesomeButtonRick>
      

            </View>
          </View>
        </Content>

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
          confirmButtonTextStyle={
            isErrorAlert
              ? style.alertConfirmButtonErrorTextStyle
              : style.alertConfirmButtonTextStyle
          }
          titleStyle={style.alertTitleStyle}
          onConfirmPressed={() => {
            this.hideAlert();
          }}
          onDismiss={() => {
            this.dismissAlert();
          }}
        />}
        {showModal && (
          <Modal
            style={style.modalStyle}
            isVisible={showModal}
            backdropColor={'white'}
            backdropOpacity={1}
            animationIn={'slideInLeft'}
            animationOut={'slideOutRight'}>
            <Content
              style={{marginLeft: 15, marginBottom: 15}}
              contentContainerStyle={{alignItems: 'center'}}>
              <HTML html={TERMINOS_CONDICIONES} />
              <TouchableHighlight
                style={styles.button2}
                onPress={() => this.hideModal()}
                underlayColor={'gray'}>
                <Text style={styles.button2TextStyle}>Cerrar</Text>
              </TouchableHighlight>
            </Content>
          </Modal>
        )}

<Overlay width={'95%'}
  isVisible={modalDriverFormVisible}
  onBackdropPress={() => this.setState({ modalDriverFormVisible: false })}
><Content>
   {this.renderFormDriver()} 
  </Content>
</Overlay>

<Overlay width={'95%'}
  isVisible={modalVehicleFormVisible}
  onBackdropPress={() => this.setState({ modalVehicleFormVisible: false })}
><Content>
   {this.renderFormVehicle()}
  </Content>
</Overlay>

        {this.state.isLoading && <Loader />}
      </Container>
    );
  }
}

const validate = values => {
  const errors = {};

  if (!values.driver) {
    errors.driver = 'Debe seleccionar un conductor';
  }
  if (!values.vehicle) {
    errors.vehicle = 'Debe seleccionar un vehiculo';
  }
  if (!values.acceptance) {
    errors.acceptance = 'Debe aceptar los términos y condiciones';
  }
  return errors;
};


const selector = formValueSelector('AprobadasForm');

const mapStateToProps = state => {
  return {
    solicitudService: state.solicitudReducer,
    usuarioService: state.userReducer,
    driver: selector(state, 'driver'),
    vehicle: selector(state, 'vehicle'),
    acceptance: selector(state, 'acceptance'),


    name: selector(state, 'name'),
    lastname: selector(state, 'lastname'),
    dni: selector(state, 'dni'),
    licenseType: selector(state, 'licenseType'),
    email: selector(state, 'email'),
    phone: selector(state, 'phone'),



    vehiculoPlaca: selector(state, 'vehiculoPlaca'),
    vehiculoModelo: selector(state, 'vehiculoModelo'),
    vehiculoAnio: selector(state, 'vehiculoAnio'),
    vehiculoTipoCamion: selector(state, 'vehiculoTipoCamion'),
    vehiculoTipoCarga: selector(state, 'vehiculoTipoCarga'),
    vehiculoCapacidad: selector(state, 'vehiculoCapacidad'),
    vehiculoTipoCapacidad: selector(state, 'vehiculoTipoCapacidad'),
    vehiculoArcsaAlimentos: selector(state, 'vehiculoArcsaAlimentos'),
    vehiculoArcsaCosmeticos: selector(state, 'vehiculoArcsaCosmeticos'),
    vehiculoArcsaMedicamentos: selector(state, 'vehiculoArcsaMedicamentos'),
    vehiculoBasc: selector(state, 'vehiculoBasc'),
    vehiculoPaseInternacional: selector(state, 'vehiculoPaseInternacional'),

    generalReducer: state.generalReducer
  };
};

const mapDispatchToProps = dispatch => {
  return {
    dispatch,
  };
};

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({
    form: 'AprobadasForm',
    validate,
    fields: ['driver', 'vehicle', 'acceptance'],
    enableReinitialize: true,
    keepDirtyOnReinitialize:true
  }),
)(AprobadasScreen);
