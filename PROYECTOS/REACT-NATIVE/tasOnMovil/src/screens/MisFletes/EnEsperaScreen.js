import { Card, Container, Content, Separator } from 'native-base';
import React, { Component } from "react";
import { KeyboardAvoidingView, View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { getSolicitudById, updateMyOfert } from "../../actions/solicitud.action";
import { AwesomeAlert, AwesomeButtonRick, Collapse, CollapseBody, CollapseHeader, CustomInputComponent, Icon as Icono, Line, Loader, Moment, Text } from '../../components/';
import { MY_OFERTS_CANCELED } from "../../Constants";
import { defaultTasonColor, style, styleSolicitudOferta as styles } from "../../styles/";
import { currencyValidation } from '../../utils/normalize';
import { no_connected_info } from '../../utils/tas-on-texts';


const utf8 = require('utf8');

class EnEsperaScreen extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: props.usuarioService.getUser.userDetails,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert:false,
      isConfirmation:false,
      data:{},
      isLoading:false,
      isOfferedSuccessfully:false
    };
  }

  componentDidMount() {
    this.loadData();
  }

  loadData = async values => {
    try {
      this.setState({isLoading:true});
      const response = await this.props.dispatch(
       getSolicitudById(this.props.solicitud.idSolicitud, this.state.user)
      );
   
      if (!response.success) {
        throw response;
      }else{
        this.setState({data:response.responseBody,isLoading:false });
      }
    } catch (error) {
      console.log('ERROR loadData  en espera ',error);
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText=response.responseBody;
      }
     
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: "Error",
        isErrorAlert:true,
        isLoading:false
      });
    }
  };


  updateOfferService = async values => {
    try {
      this.setState({isLoading:true});
      const response = await this.props.dispatch(
        updateMyOfert(JSON.stringify(values), this.state.user)
      );
      console.log('RESPUESTA  ',response);
      if (response.success) {
        this.setState({
          //textAlert: response.responseBody.responseMessage,
          textAlert: response.responseBody.responseMessage,
          titleAlert:'Mensaje',
          isLoading:false,
          showAlert:true,
          isErrorAlert:(response.responseBody.response == 'ERROR'),
          isOfferedSuccessfully: (response.responseBody.response != 'ERROR'),
          isConfirmation:false
        });
        
      } else {
        throw response;
      }
    } catch (error) {
      console.log('ERROR  updateOfferService ',error);
     let errorText;
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
      errorText = error.responseBody;
    }
      this.setState({textAlert:errorText,
        titleAlert:'Error',
        showAlert:true,
        isErrorAlert:true,
        isLoading:false
      });

    }
  }
  
  reOffer = values => {

    let object = {
      amount: values.oferta,
      comments: values.comentario ? values.comentario : "",
      idSolicitud: this.state.data.idSolicitud,
      idOferta: this.state.data.offer.idOferta
    };

  this.props.change('amount',object.amount?object.amount:null);  

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
  

  
  dismissAlert= () => {
    if(this.state.showAlert){
     this.setState({
       showAlert: false
     });
    }
   }
   
  hideAlert = () => {
    this.setState({
      showAlert: false
    });
   
    if(this.state.isOfferedSuccessfully){
      Actions.pop();
    }
  }

  confirmationAlert = () => {
    this.setState({
      showAlert: false
    });
  if(this.state.isConfirmation){
  this.cancelOffer();
  }  
  }


  openCancelConfirmation = () => {
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

    this.setState({
      showAlert: true,
      textAlert: '¿Está seguro de cancelar la oferta?',
      titleAlert: 'Confirmación',
      isErrorAlert: false,
      isConfirmation:true
    });
  };

  cancelOfferService = async values => {
    try {
      this.setState({isLoading:true});
      const response = await this.props.dispatch(
        updateMyOfert(JSON.stringify(values), this.state.user)
      );
       if (response.success) {
        this.setState({textAlert:response.responseBody.responseMessage,
          titleAlert:'Mensaje',
          isLoading:false,
          showAlert:true,
          isOfferedSuccessfully: (response.responseBody.response== 'ERROR' ? false : true),
          isConfirmation:false
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

      this.setState({textAlert:errorText,
        titleAlert:'Error',
        showAlert:true
      });
    }
  }

  cancelOffer = () => {
    let object = {
      idOferta: this.state.data.offer.idOferta,
      state:MY_OFERTS_CANCELED
    }; 
    
    this.cancelOfferService(object);
  };

  render() {
  
    const { handleSubmit } = this.props;
    const {showAlert,titleAlert,isErrorAlert,textAlert,data,isConfirmation} = this.state
    
    return (
      <Container>
      <Content>
        <View style={styles.container}>
          <View style={styles.viewInfo1}>
            <View style={{alignItems: 'center', padding: 5}}>
              <Text
                style={styles.formTitle}>
                {data.idSolicitud}
              </Text>
            </View>
           
            <View style={styles.viewInfo2_2}>
              <View style={styles.viewData}>
                <Text style={styles.textInfo}> N° Ofertas </Text>
                <Text style={styles.textData}>
                  {data.totalOffer}
                </Text>
              </View>

              <View style={styles.viewData}>
                <Text style={styles.textInfo}> Mejor Oferta </Text>
                <Text style={styles.textData}>
                  {data.bestOffer
                    ? data.bestOffer
                    : 0}
                </Text>
              </View>
              <View style={styles.viewData}>
                <Text style={styles.textInfo}> Promedio </Text>
                <Text style={styles.textData}>
                  {data.averageOffer
                    ? data.averageOffer
                    : 0}
                </Text>
              </View>
            </View>
          </View>

          <Card elevation={4} style={styles.cardStyle}>
            <Collapse isCollapsed={false}>
              <CollapseHeader>
                <Separator style={styles.collapseHeaderSeparator}>
                  <Text style={styles.collapseHeaderText}>
                    Datos del envío
                  </Text>
                </Separator>
              </CollapseHeader>
              <CollapseBody>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Producto</Text>
                    <Text style={styles.textData4}>
                      {data.tipo}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Número de piezas</Text>
                    <Text style={styles.textData4}>
                      {data.numeroPiezas}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Volumen Total</Text>
                    <Text style={styles.textData4}>
                      {data.volumen}{' '}
                      {data.tipoVolumen}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Peso</Text>
                    <Text style={styles.textData4}>
                      {data.peso}{' '}
                      {data.tipoPeso}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                 
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Estibadores</Text>
                    <Text style={styles.textData4}>
                      {data.numeroEstibadores}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Observaciones</Text>
                    <Text style={styles.textData4}>
                      {data.observaciones}
                    </Text>
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
                  <Text style={styles.collapseHeaderText}>Origen</Text>
                </Separator>
              </CollapseHeader>
              <CollapseBody>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Provincia</Text>
                    <Text style={styles.textData4}>
                      {data.provinciaOrigen}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>

                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Ciudad</Text>
                    <Text style={styles.textData4}>
                      {data.origen}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Dirección</Text>
                    <Text style={styles.textData4}>
                      {data.direccion}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>
                      Fecha de recolección
                    </Text>
                    <Text style={styles.textData4}>
                      <Moment element={Text} format="DD/MM/YYYY hh:mm">
                        {data.fechaEntrega}
                      </Moment>
                    </Text>
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
                    <Text style={styles.textData4}>
                      {data.provinciaDestino}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>

                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Ciudad</Text>
                    <Text style={styles.textData4}>
                      {data.destino}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Dirección</Text>
                    <Text style={styles.textData4}>
                      {data.direccionEntrega}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Fecha de entrega</Text>
                    <Text style={styles.textData4}>
                      <Moment element={Text} format="DD/MM/YYYY hh:mm">
                        {data.fechaEntrega}
                      </Moment>
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
              </CollapseBody>
            </Collapse>
          </Card>

          <View style={styles.viewInfo4}>
            <Text style={styles.textInfo5}> Mi oferta </Text>
          </View>

          <KeyboardAvoidingView
            behavior={Platform.select({android: undefined, ios: 'padding'})}
            style={{ flex: 1}}
            keyboardVerticalOffset={10}>
            <View style={styles.viewInfo5}>
              <Icono customIcon="dollar" iconReference="FontAwesome" />
             
             <View style={{paddingLeft:10 ,width: '50%'}}>
               <Field
                component={CustomInputComponent}
                name="oferta"
                type="text"
                placeholder=""
                keyboardType={'numeric'}
                fontSize={29} 
                validate={[currencyValidation]}
                maxLength={7}
                /> 
             </View>
            
            
            </View>
            <Line color="#4A5F8E" size={1} />
            <View style={styles.viewInfo6}>
              <Text>Comentario</Text>
               <Field
                component={CustomInputComponent}
                name="comentario"
                type="text"
                placeholder=""
                multiline={true}
              /> 
            </View>
            </KeyboardAvoidingView>



            <View style={ style.twoButtonsArea}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={()=>this.openCancelConfirmation()} backgroundColor={defaultTasonColor}>
            <Text style={style.defaultTextButton}>Cancelar Oferta</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={handleSubmit(this.reOffer)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Ofertar</Text>
             </AwesomeButtonRick>
          </View>

         
        </View>
      </Content>
    
    {showAlert &&  <AwesomeAlert
        show={showAlert}
        showProgress={false}
        title={titleAlert}
        message={textAlert}
        closeOnTouchOutside={!this.state.isOfferedSuccessfully}
        closeOnHardwareBackPress={false}
        showCancelButton={isConfirmation}
        showConfirmButton={true}
        confirmText= {isConfirmation?"Aceptar":"Ok"} 
        cancelText="Cerrar"
        contentContainerStyle={style.alertContentContainerStyle}
        confirmButtonStyle={style.alertConfirmButtonStyle}
        confirmButtonTextStyle={isErrorAlert?style.alertConfirmButtonErrorTextStyle:style.alertConfirmButtonTextStyle}
        cancelButtonStyle={style.alertConfirmButtonStyle}
        cancelButtonTextStyle={isErrorAlert?style.alertConfirmButtonErrorTextStyle:style.alertConfirmButtonTextStyle}
        titleStyle={style.alertTitleStyle}
        onConfirmPressed={() => {
          isConfirmation? this.confirmationAlert() : this.hideAlert()
        }}
        onDismiss={() => {
          this.dismissAlert();
        }}
        onCancelPressed={() => this.hideAlert()}
      />}
   {this.state.isLoading && <Loader />}
    </Container>

      );
  
  }
}

const validate = values => {
  const errors = {};
  if (!values.oferta) {
    errors.oferta = "Su oferta es requerido";
  }
  return errors;
};

const selector = formValueSelector('EnEsperaForm');

const mapStateToProps = state => {
  return {
    solicitudService: state.solicitudReducer,
    usuarioService: state.userReducer,
    initialValues: {
        oferta: (state.solicitudReducer.getByIdReducer.data.offer != undefined)
        ? state.solicitudReducer.getByIdReducer.data.offer.amount
        : null,  
       comentario: (state.solicitudReducer.getByIdReducer.data.offer != undefined)
        ? state.solicitudReducer.getByIdReducer.data.offer.comments
        : null 
    },  
    oferta: selector(state, 'oferta'),
    comentario: selector(state, 'comentario'),
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
    form: "EnEsperaForm",
    validate,
    fields: ["oferta","comentario"],
    enableReinitialize: true
  })
)(EnEsperaScreen);
