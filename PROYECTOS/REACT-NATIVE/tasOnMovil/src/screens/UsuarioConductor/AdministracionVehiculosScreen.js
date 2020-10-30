import { Body, Card, CardItem, Content, Icon, ListItem } from 'native-base';
import React, { Component } from "react";
import { FlatList, Keyboard, RefreshControl, View } from "react-native";
import { Overlay } from 'react-native-elements';
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { required } from 'redux-form-validators';
import { addVehiculo, getMisVehiculos, updateVehiculo } from "../../actions/driverVehicle.action";
import { obtenerCatalogoItem } from "../../actions/general.action";
import { AwesomeAlert, AwesomeButtonRick, CheckBoxField, CustomInputComponent, DropDownList, Loader, Text } from "../../components/";
import { idArcsaAlimentos, idArcsaCosmeticos, idArcsaMedicamentos, idBasc, idCatalogoCamion, idCatalogoCarga, idCatalogoPesos, idPaseInternacional } from '../../Constants';
import { defaultTasonColor, style, styleLogin as styles } from '../../styles/';
import { onlyNums } from '../../utils/normalize';
import { no_connected_info } from '../../utils/tas-on-texts';


const utf8 = require('utf8');

const renderCheckBox = ({
  input: {value, onChange, ...input},
  meta: {touched, error},
  ...restInput
}) => {
  return (
    <View style={{padding: 5, alignItems: 'center'}}>
      <CheckBoxField {...input} onChange={onChange} />
      {touched && error && <Text style={style.errorText}>{error}</Text>}
    </View>
  );
};


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


class AdministracionVehiculosScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
     currentUser:{}
     ,password: '',
     showAlert: false,
     textAlert: "",
     titleAlert: "",
     isErrorAlert:false,
     isPostedSuccessfully: false,
     tiposLicencia: [],
     isLoading:false,
     refreshing:false,
     vehicles:[],
     modalVehicleInfoVisible:false,
     modalVehicleFormVisible:false,
     selectedVehicle:{},
     certificados:[],
     unidadesCapacidad:[],
     tiposCarga:[],
     tiposCamion:[],
    }
  }


  componentDidMount() {
   this.loadData();

  

  }
  
  loadData = async () => {
    try {
      this.setState({isLoading: true});
     
      
      const response = await this.props.dispatch(getMisVehiculos());

      const response1 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoPesos),
      );

      const response2 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoCarga),
      );

      const response3 = await this.props.dispatch(
        obtenerCatalogoItem(idCatalogoCamion),
      );

      const dropdownList = [
        {"id":idArcsaAlimentos,"itemName":"ARCSA alimentos"},
        {"id":idArcsaCosmeticos,"itemName":"ARCSA cosméticos"},
        {"id":idArcsaMedicamentos,"itemName":"ARCSA medicamentos"},
        {"id":idBasc,"itemName":"BASC"},
        {"id":idPaseInternacional,"itemName":"Pase internacional"}
      ];


      if (response && response1 && response2 && response3) {
        this.setState({isLoading:false,vehicles:response.responseBody,unidadesCapacidad:response1.responseBody ,tiposCarga:response2.responseBody,tiposCamion:response3.responseBody,certificados:dropdownList});
    

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

  
  loadVehicles = async () => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getMisVehiculos());
   
      if (response) {
        this.setState({isLoading:false,vehicles:response.responseBody});
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


  updateVehicle = async values => {
  
  this.setState({isLoading:true});
     try {
      const response = await this.props.dispatch(updateVehiculo(values));
      
      if(response.success){

        this.setState({
          showAlert: true,
          textAlert: response.responseBody.responseMessage,
          titleAlert: 'Mensaje',
          modalVehicleInfoVisible:false,
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
     
     this.setState({
        showAlert: true,
        textAlert: utf8.decode(errorText),
        titleAlert: "Error",
        isErrorAlert:true
      }); 

    } 
  };


  onEdit= values => {
   
    this.props.change('vehiculoAnio',  values.vehiculoAnio ? values.vehiculoAnio : null);
    
    if(!values.vehiculoPlaca && !values.vehiculoModelo && !values.vehiculoAnio&& !values.vehiculoTipoCarga && !values.vehiculoTipoCamion&& !values.vehiculoTipoCapacidad && !values.vehiculoCapacidad ){
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

    const {selectedVehicle}=this.state;

    let payload= {
      vehiculoId: selectedVehicle.vehiculoId,
  vehiculoUsuario: selectedVehicle.vehiculoUsuario,
  vehiculoModelo: values.vehiculoModelo,
  vehiculoAnio: Number(values.vehiculoAnio),
  vehiculoPlaca: selectedVehicle.vehiculoPlaca,
  vehiculoTipoCarga: Number(values.vehiculoTipoCarga),
  vehiculoTipoCamion: Number(values.vehiculoTipoCamion),
  vehiculoCapacidad:  Number(values.vehiculoCapacidad),
  vehiculoTipoCapacidad: Number(values.vehiculoTipoCapacidad),
  vehiculoEstado: selectedVehicle.vehiculoEstado,
  vehiculoArcsaAlimentos: values.vehiculoArcsaAlimentos,
  vehiculoArcsaCosmeticos: values.vehiculoArcsaCosmeticos,
  vehiculoArcsaMedicamentos: values.vehiculoArcsaMedicamentos,
  vehiculoBasc: values.vehiculoBasc,
  vehiculoPaseInternacional: values.vehiculoPaseInternacional,
  vehiculoCertificadoArcsa: false
    }
  
  
  console.log('payload   ',payload);

  this.updateVehicle(JSON.stringify(payload));

  };

  onSubmit = values => {
   
    this.props.change('vehiculoAnio',  values.vehiculoAnio ? values.vehiculoAnio : null);
    
    if(!values.vehiculoPlaca && !values.vehiculoModelo && !values.vehiculoAnio&& !values.vehiculoTipoCarga && !values.vehiculoTipoCamion&& !values.vehiculoTipoCapacidad && !values.vehiculoCapacidad ){
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
    vehiculoPlaca: values.vehiculoPlaca,
    vehiculoModelo: values.vehiculoModelo,
    vehiculoAnio: values.vehiculoAnio,
    vehiculoTipoCamion: String(values.vehiculoTipoCamion),
    vehiculoTipoCarga: String(values.vehiculoTipoCarga),
    vehiculoCapacidad: String(values.vehiculoCapacidad),
    vehiculoTipoCapacidad: String(values.vehiculoTipoCapacidad),
    vehiculoArcsaAlimentos: (values.vehiculoArcsaAlimentos?true:false),
    vehiculoArcsaCosmeticos: (values.vehiculoArcsaCosmeticos?true:false),
    vehiculoArcsaMedicamentos: (values.vehiculoArcsaMedicamentos?true:false),
    vehiculoBasc: (values.vehiculoBasc?true:false),
    vehiculoPaseInternacional: (values.vehiculoPaseInternacional?true:false)
  }  
  
  console.log('payload   ',payload);

  this.createVehicle(JSON.stringify(payload));

  };

  
  cancel=()=>{
    Actions.pop();
  }
  onInputSubmit = () => {
    let values={user : this.props.user, password:this.props.password }
    this.recoverPassword(values);
    };


  hideAlert = () => {
    this.setState({
      showAlert: false
    });
   
    if(this.state.isPostedSuccessfully){
    this.props.reset();
    this.loadVehicles();
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
this.loadVehicles();
}

bindVehiclesFlatList(data) {

  const { driverVehicleReducer,handleSubmit } = this.props;
  const { isLoading,tiposLicencia,unidadesCapacidad,tiposCarga,tiposCamion } = this.state;
  

  if(data.length==0){
    const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
    return (  
    <View style={[{marginBottom:20}]}>
    <FlatList
      data={emptyData}
      renderItem={({item}) => (
        <View style={style.cardStyle}>
      {(!isLoading && !driverVehicleReducer.getVehiclesReducer.isFetching) && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
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
              onPress={() => this.openModalInfoVehicle(item) }>
              <Body style={style.noPadding}>
               
              <View style={style.viewHeader}>
              <Text style={style.viewHeaderText}>{item.vehiculoPlaca}</Text>
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Marca</Text>
                  <Text style={style.textData}>
                  {item.vehiculoModelo}
                  </Text>
                </View>

                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Año</Text>
                  <Text style={style.textData}>
                  {item.vehiculoAnio} 
                  </Text>
                </View>
               
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Capacidad</Text>
                  <Text style={style.textData}>
                 {item.vehiculoCapacidad} {this.getDescriptionFromId(unidadesCapacidad,item.vehiculoTipoCapacidad)} 
                  </Text>
                </View>


                <View style={{padding: 5, width: '50%'}}>
                <Text style={style.textInfo}>Certificado</Text>
                  {item.vehiculoArcsaAlimentos && <Text style={style.textData}>{'ARCSA alimentos'}</Text>}
                  {item.vehiculoArcsaCosmeticos && <Text style={style.textData}>{'ARCSA cosméticos'}</Text>}
                  {item.vehiculoArcsaMedicamentos && <Text style={style.textData}>{'ARCSA medicamentos'}</Text>}
                  {item.vehiculoBasc && <Text style={style.textData}>{'BASC'}</Text>}
                  {item.vehiculoPaseInternacional &&<Text style={style.textData}>{'Pase internacional'}</Text>}
                
                </View>
              </View>

              <View style={style.viewInfo1}>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Tipo camión</Text>
                  <Text style={style.textData}>
             {this.getDescriptionFromId(tiposCamion,item.vehiculoTipoCamion)} 
                  </Text>
                </View>
                <View style={{padding: 5, width: '50%'}}>
                  <Text style={style.textInfo}>Tipo carga</Text>
                  <Text style={style.textData}> {this.getDescriptionFromId(tiposCarga,item.vehiculoTipoCarga)} </Text>
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
  const {tiposLicencia,unidadesCapacidad,tiposCamion,tiposCarga,  selectedVehicle}=this.state;
  return(
    <View style={[style.cardStyle,{flex: 1,
      flexDirection: 'column',
      justifyContent: 'space-between'}]}>

  <View style={{ width: '100%',paddingBottom:10}}>
                      <Text style={style.textInfoDisabled}>Placa</Text>
                      <Text style={style.textDataDisabled}>{selectedVehicle.vehiculoPlaca}</Text>
                    </View>

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



<View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.setState({modalVehicleInfoVisible:false})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.onEdit)} backgroundColor={defaultTasonColor}>
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


         
<View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.setState({modalVehicleFormVisible:false})} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Cerrar</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.onSubmit)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Guardar</Text>
             </AwesomeButtonRick>
          </View>

       

    </View>
  );
}

openModalInfoVehicle=(item)=>{
  this.setState({ modalVehicleInfoVisible: true,selectedVehicle:item })

  this.props.change('vehiculoPlaca',item.vehiculoPlaca);
  this.props.change('vehiculoModelo',item.vehiculoModelo);
  this.props.change('vehiculoAnio',item.vehiculoAnio);
  this.props.change('vehiculoTipoCarga',item.vehiculoTipoCarga);
  this.props.change('vehiculoTipoCamion',item.vehiculoTipoCamion);
  this.props.change('vehiculoTipoCapacidad',item.vehiculoTipoCapacidad);
  this.props.change('vehiculoCapacidad',item.vehiculoCapacidad);

  
  this.props.change('vehiculoArcsaAlimentos',item.vehiculoArcsaAlimentos);
  this.props.change('vehiculoArcsaCosmeticos',item.vehiculoArcsaCosmeticos);
  this.props.change('vehiculoArcsaMedicamentos',item.vehiculoArcsaMedicamentos);
  this.props.change('vehiculoBasc',item.vehiculoBasc);
  this.props.change('vehiculoPaseInternacional',item.vehiculoPaseInternacional);
  this.props.change('vehiculoCertificadoArcsa',item.vehiculoCertificadoArcsa);


}
render() {
    const { driverVehicleReducer,handleSubmit } = this.props;
    const {modalVehicleInfoVisible,modalVehicleFormVisible, vehicles,showAlert,textAlert,titleAlert,isErrorAlert,isLoading } = this.state;
 
    return (
      <View style={styles.container}>
        {isLoading && <Loader />}

        <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={() =>this.setState({modalVehicleFormVisible:true})} backgroundColor={defaultTasonColor}>
<Icon name="md-add" type="Ionicons" style={style.defaultIconButton}/>
        <Text style={style.defaultTextButton}>Nuevo</Text>
</AwesomeButtonRick>
     
     {this.bindVehiclesFlatList(vehicles)} 

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
  isVisible={modalVehicleFormVisible}
  onBackdropPress={() => this.setState({ modalVehicleFormVisible: false })}
><Content>
  {this.renderFormVehicle()}
  </Content>
</Overlay>

<Overlay width={'95%'}
  isVisible={modalVehicleInfoVisible}
  onBackdropPress={() => this.setState({ modalVehicleInfoVisible: false })}
><Content>
  {this.renderInfoDriver()}
  </Content>
</Overlay>



       
      </View>
    );
  }
}



const selector = formValueSelector('AdministracionVehiculosForm') 

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
    form: "AdministracionVehiculosForm",
    enableReinitialize: true
  })
)(AdministracionVehiculosScreen);
