import { Card, Container, Content, Separator } from 'native-base';
import React, { Component } from 'react';
import { KeyboardAvoidingView, Platform, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { Field, formValueSelector, reduxForm } from 'redux-form';
import { getSolicitudById, sendMyOffer } from '../../actions/solicitud.action';
import { AwesomeAlert, AwesomeButtonRick, Collapse, CollapseBody, CollapseHeader, CustomInputComponent, Icon, Line, Loader, Moment, Text } from '../../components/';
import { defaultTasonColor, style, styleSolicitudOferta as styles } from '../../styles/';
import { currencyValidation, normalizeCurrency } from '../../utils/normalize';
import { no_connected_info } from '../../utils/tas-on-texts';

const utf8 = require('utf8');

class SolicitudOfertaScreen extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: props.usuarioService.getUser.userDetails,
      solicitud: {},
      isLoading: false,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert: false,
      isOfferedSuccessfully: false,
    };
  }


  componentDidMount() {
    this.props.reset();
    this.loadData();
  }

  loadData = async values => {
    try {
      this.setState({isLoading: true});
      const response = await this.props.dispatch(getSolicitudById(this.props.solicitud.idSolicitud, this.state.user));
      if (!response.success) {
        throw response;
      } else {
        this.setState({isLoading: false, solicitud: response.responseBody});
      }
    } catch (error) {
      console.log('Error loadData Solcitud espera ',error);
      let errorText;
      errorText = error.responseMessage;

      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText=error.responseBody;
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

  makeOffer = async values => {
    this.props.change('oferta ',this.props.oferta?this.props.oferta:null);  
   
     try {

if(!values.oferta){
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
     
      let object = {
        amount: this.props.oferta,
        comments: this.props.comentario ? this.props.comentario : '',
        idSolicitud: this.state.solicitud.idSolicitud,
      };
    
      console.log('payload  ',object);
      this.setState({isLoading: true, textAlert: ''});
      const response = await this.props.dispatch(
        sendMyOffer(JSON.stringify(object), this.state.user),
      ); 

      if (response.success) {
        this.setState({
          showAlert: true,
          textAlert: response.responseBody.responseMessage ,
          titleAlert: 'Mensaje',
          isErrorAlert: false,
          isLoading: false,
          isOfferedSuccessfully:
            response.responseBody.response == 'ERROR' ? false : true,
        });
     
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
        textAlert: utf8.decode(errorText),
        titleAlert: 'Error',
        isErrorAlert: true,
        isLoading: false,
      });
    } 
  };

  hideAlert = () => {
    this.setState({
      showAlert: false,
    });

    if(this.state.isOfferedSuccessfully){
      Actions.reset('Solicitudes');
    }

  };

  dismissAlert = () => {
    if(this.state.showAlert){
    this.setState({
      showAlert: false,
    });
  }
  };
  

  render() {
    const {
      handleSubmit,
    } = this.props;

    const {showAlert, titleAlert, textAlert, isErrorAlert,solicitud} = this.state;

    return (
      <Container>
        <Content>
          <View style={styles.container}>
            <View style={styles.viewInfo1}>
              <View style={styles.viewIdSolicitud}>
                <Text style={styles.textIdSolicitud}>{solicitud.idSolicitud}</Text>
              </View>
              <View style={styles.viewInfo2_2}>
                <View style={styles.viewData}>
                  <Text style={styles.textInfo}>N° Ofertas</Text>
                  <Text style={styles.textData}>{solicitud.totalOffer}</Text>
                </View>
                <View style={styles.viewData}>
                  <Text style={styles.textInfo}>Mejor Oferta</Text>
                  <Text style={styles.textData}>
                    {solicitud.bestOffer ? solicitud.bestOffer : 0}
                  </Text>
                </View>
                <View style={styles.viewData}>
                  <Text style={styles.textInfo}>Promedio</Text>
                  <Text style={styles.textData}>
                    {solicitud.averageOffer ? solicitud.averageOffer : 0}
                  </Text>
                </View>
              </View>
            </View>

            <Card  elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={true}>
                <CollapseHeader>
                  <Separator style={styles.collapseHeaderSeparator}>
                    <Text style={styles.collapseHeaderText}>Datos del envío</Text>
                  </Separator>
                </CollapseHeader>
                <CollapseBody>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Producto</Text>
                      <Text style={styles.textData4}>{solicitud.tipo}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Número de piezas</Text>
                      <Text style={styles.textData4}>{solicitud.numeroPiezas}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Volumen Total</Text>
                      <Text style={styles.textData4}>
                        {solicitud.volumen} {solicitud.tipoVolumen}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Peso</Text>
                      <Text style={styles.textData4}>
                        {solicitud.peso} {solicitud.tipoPeso}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Días de pago</Text>
                      <Text style={styles.textData4}>{solicitud.diasPago}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Estibadores</Text>
                      <Text style={styles.textData4}>
                        {solicitud.numeroEstibadores}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
               
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Observaciones</Text>
                      <Text style={styles.textData4}>{solicitud.observaciones}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={true}>
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
                        {solicitud.provinciaOrigen}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Ciudad</Text>
                      <Text style={styles.textData4}>{solicitud.origen}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Dirección</Text>
                      <Text style={styles.textData4}>{solicitud.direccion}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Fecha de recolección</Text>
                      <Text style={styles.textData4}>
                        <Moment element={Text} format="DD/MM/YYYY hh:mm">
                          {solicitud.fechaEntrega}
                        </Moment>
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={true}>
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
                        {solicitud.provinciaDestino}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Ciudad</Text>
                      <Text style={styles.textData4}>{solicitud.destino}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Dirección</Text>
                      <Text style={styles.textData4}>
                        {solicitud.direccionEntrega}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '100%'}}>
                      <Text style={styles.textInfo4}>Fecha de entrega</Text>
                      <Text style={styles.textData4}>
                        <Moment element={Text} format="DD/MM/YYYY hh:mm">
                          {solicitud.fechaEntrega}
                        </Moment>
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <View style={styles.viewInfo4}>
              <Text style={styles.textInfo5}>Mi oferta</Text>
            </View>
            <Text style={[styles.textData4,{paddingTop:5,alignSelf: 'center'}]}>Ingrese el costo por el traslado de toda la carga</Text>
                
            <KeyboardAvoidingView
              behavior={Platform.select({android: undefined, ios: 'padding'})}
              style={{flex: 1}}
              keyboardVerticalOffset={10}>
              <View style={styles.viewInfo5}>
                <Icon customIcon="dollar" iconReference="FontAwesome" />
                <View style={{paddingLeft: 10, width: '50%'}}>
                  <Field
                    component={CustomInputComponent}
                    name="oferta"
                    type="text"
                   // placeholder="00.00"
                    keyboardType={'numeric'}
                    fontSize={29}
                    normalize={normalizeCurrency}
                    validate={[currencyValidation]}
                    maxLength={7}
                    hideLabel={true}
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
         
            <View style={styles.viewInfo7}>
             
              <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={handleSubmit(this.makeOffer)} backgroundColor={defaultTasonColor}><Text style={style.defaultTextButton}>Ofertar</Text></AwesomeButtonRick>
      
            </View>



          </View>
        </Content>

        {showAlert &&<AwesomeAlert
          show={showAlert}
          showProgress={false}
          title={titleAlert}
          message={textAlert}
          closeOnTouchOutside={!this.state.isOfferedSuccessfully}
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
        {this.state.isLoading && <Loader />}
      </Container>
    );
  }
}

const validate = values => {
  const errors = {};
  if (!values.oferta) {
    errors.oferta = 'Su oferta es requerido';
  }
  return errors;
};

const mapStateToProps = state => {
  return {
    solicitudService: state.solicitudReducer,
    usuarioService: state.userReducer,
    generalReducer: state.generalReducer,    
    oferta: selector(state, 'oferta'),
    comentario: selector(state, 'comentario'),
  };
};

const mapDispatchToProps = dispatch => {
  return {
    dispatch,
  };
};
const selector = formValueSelector('solicitudOferta'); // <== Redux Form Name

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({
    form: 'solicitudOferta',
    validate,
    fields: ['oferta', 'comentario'],
    enableReinitialize: true,
  }),
)(SolicitudOfertaScreen);
