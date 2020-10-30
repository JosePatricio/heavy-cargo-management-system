import { Card, Container, Content, Icon, Separator } from 'native-base';
import React, { Component } from "react";
import { View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { formValueSelector, reduxForm } from "redux-form";
import { getInvoiceDetailByNumber } from "../../actions/factura.action";
import { AwesomeAlert, AwesomeButtonRick, Collapse, CollapseBody, CollapseHeader, Icon as Icono, Line, Loader, Text } from '../../components/';
import { FACT_NORMAL, FACT_RAPIDA } from '../../Constants';
import { defaultTasonColor, style, styleSolicitudOferta as styles } from "../../styles/";

const utf8 = require('utf8');


class PorEntregarFacturaScreen extends Component {
  constructor(props) {
    super(props);

  
    this.state = {
      user: props.usuarioService.getUser.userDetails,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert:false,
      isConfirmation:false,
      cliente:{},
      empresa:{},
      codeFactura:undefined,
      isLoading:false,
      isOfferedSuccessfully:false,
      totalFact : 0.0,
      COMISION : 0.0,
      subtotal: 0.0,
      total: 0.0,
      offersId:[],
      solicitudes:[]
    };
  }

  componentDidMount() {
    this.loadInvoicetData();
    
  }

 setGridValues( cliente, empresa ,codigoFactura){
  let totalFact = 0.0;
  let COMISION = 0.0
  let offersId=[];

  console.log('SOLICITUDES  ',this.state.solicitudes);
  this.state.solicitudes.forEach((element, index) => {
    totalFact = totalFact + Number(element.amount);
    COMISION = COMISION + Number(element.descuento);
    offersId.push(element.idOferta);
  });
 
  const totalFactFormat=totalFact.toFixed(2);
  let descuento;
  let subTotal;
  let total;
  if(this.props.tipoPago == FACT_RAPIDA){
    descuento=COMISION.toFixed(2);
    subTotal= (Number(totalFactFormat)- Number(descuento)).toFixed(2);
    total= (Number(totalFactFormat)- Number(descuento)).toFixed(2);
  }
  if(this.props.tipoPago == FACT_NORMAL){
    descuento=' - ';
    subTotal= (Number(totalFactFormat)).toFixed(2);
    total= (Number(totalFactFormat)).toFixed(2);
  }
   
  this.setState({ cliente:cliente,empresa:empresa,codeFactura:codigoFactura.code,totalFact:totalFactFormat,COMISION:descuento,
  subtotal:totalFactFormat,subTotal:subTotal,total:total,offersId:offersId,isLoading:false });

}


loadInvoicetData = async () => {
  try {
    this.setState({isLoading:true});
    const response1 = await this.props.dispatch(getInvoiceDetailByNumber(this.props.factura.invoiceId));
    if (!response1.success) {
      throw response1;
    }
    let cliente= response1.responseBody.client;
    cliente= {clienteRazonSocial:cliente.razonSocial, clienteRuc:cliente.ruc, clienteDireccion:cliente.direccion}
    let tason=response1.responseBody.tason;
    tason= {clienteRazonSocial:tason.razonSocial, clienteRuc:tason.ruc, clienteDireccion:tason.direccion}
    let factura=response1.responseBody;
    factura={code:factura.invoiceId};
    
    this.setState({solicitudes:response1.responseBody.offers});
  
    this.setGridValues(cliente,tason,factura);

  } catch (error) {
    let errorText;
    errorText = error.responseMessage;
    if (error.message) {
      errorText = error.message;
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



  hideAlert = () => {
    this.setState({
      showAlert: false
    });
   
  
  }

  confirmationAlert = () => {
    this.setState({
      showAlert: false
    });
  if(this.state.isConfirmation){
  this.cancelOffer();
  }  
  }

  dismissAlert= () => {
   if(this.state.showAlert){
    this.setState({
      showAlert: false
    });
   }
  }

  
  cancel = () => {
    Actions.pop();
  };

  render() {
  
    const { handleSubmit } = this.props;
    const {showAlert,titleAlert,isErrorAlert,textAlert,cliente,empresa,codeFactura,isConfirmation,totalFact,totalFactFormat,COMISION,subTotal,total} = this.state
    return (
      <Container>
      <Content>
       
      <View style={styles.container}>
       
    
          <Card elevation={4} style={styles.cardStyle}>
            <Collapse isCollapsed={false}>
              <CollapseHeader>
                <Separator style={styles.collapseHeaderSeparator}>
                  <Text style={styles.collapseHeaderText}>
                    Datos compañía
                  </Text>
                  <Icono
                    iconReference="MaterialCommunityIcons"
                    customIcon="minus-circle-outline"
                  />
                </Separator>
              </CollapseHeader>
              <CollapseBody>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Razón social</Text>
                    <Text style={styles.textData4}>
                     {cliente.clienteRazonSocial}
                    </Text>
                  </View>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>RUC</Text>
                    <Text style={styles.textData4}>
                    {cliente.clienteRuc}
                    </Text>
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>No. factura</Text>
                    <Text style={styles.textData4}>
                    -
                    </Text>
                  </View>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Número de autorización</Text>
                    <Text style={styles.textData4}>
                    -
                    </Text>
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Dirección</Text>
                    <Text style={styles.textData4}>
                    {cliente.clienteDireccion}
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
                  <Text style={styles.collapseHeaderText}>
                    Datos TAS-ON
                  </Text>
                  <Icono
                    iconReference="MaterialCommunityIcons"
                    customIcon="minus-circle-outline"
                  />
                </Separator>
              </CollapseHeader>
              <CollapseBody>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Razón social</Text>
                    <Text style={styles.textData4}>
                     {empresa.clienteRazonSocial}
                    </Text>
                  </View>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>RUC</Text>
                    <Text style={styles.textData4}>
                    {empresa.clienteRuc}
                    </Text>
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Fecha de emisión</Text>
                    <Text style={styles.textData4}>
                    -
                    </Text>
                  </View>
                  <View style={{padding: 5, width: '50%'}}>
                    <Text style={styles.textInfo4}>Guía de remisión</Text>
                    <Text style={styles.textData4}>
                    -
                    </Text>
                  </View>
                </View>
                <View style={styles.viewInfo3_1}>
                  <View style={{padding: 5, width: '100%'}}>
                    <Text style={styles.textInfo4}>Dirección</Text>
                    <Text style={styles.textData4}>
                    {empresa.clienteDireccion}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>
              </CollapseBody>
            </Collapse>
          </Card>

          <View style={[styles.cardStyle,{paddingTop:15}]}>
          <Text style={[styles.formTitle,{paddingBottom:5}]}>
                Detalle
              </Text>
          </View>

<View style={{paddingLeft:20}}>
<View style={{ flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
            <View style={{ flexGrow: 1, width:'30%' }} ><Text>Cantidad</Text></View>
            <View style={{ flexGrow: 1, width:'45%' }} ><Text style={{textAlign:'left'}}>Descripción</Text></View>
            <View style={{ flexGrow: 1, width:'25%' }} ><Text>Precio Unitario</Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}>1</Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{display:(codeFactura?'flex':'none')}}>Código de documento: {codeFactura} , transporte de mercadería pesada.</Text></View>
    <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}>{totalFact}</Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}></Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{textAlign:'right'}}>Subtotal 12%</Text></View>
          <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}>-</Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}></Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{textAlign:'right'}}>Subtotal 0%</Text></View>
    <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}>{totalFactFormat}</Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}></Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{textAlign:'right'}}>DESCUENTO</Text></View>
          <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}> {COMISION} </Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}></Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{textAlign:'right'}}>Subtotal</Text></View>
    <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}>{subTotal}</Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}></Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{textAlign:'right'}}>IVA (12%)</Text></View>
          <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}>-</Text></View>
</View>
<View style={{ padding:10,flex: 1, alignSelf: 'stretch', flexDirection: 'row' }}>
          <View style={{ flexGrow: 1,width:'15%'}} ><Text style={{textAlign:'center'}}></Text></View>
          <View style={{ flexGrow: 1,width:'60%'}} ><Text style={{textAlign:'right'}}>Total</Text></View>
    <View style={{ flexGrow: 1,width:'25%'}} ><Text style={{textAlign:'center'}}>{total}</Text></View>
</View>


</View>
      
    <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={()=>this.cancel()} backgroundColor={defaultTasonColor}>
            <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Regresar</Text>
       </AwesomeButtonRick>

        </View>
  

   </Content>
    
   {showAlert &&<AwesomeAlert
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


const selector = formValueSelector('PorEntregarFacturaForm');

const mapStateToProps = state => {
  return {
    usuarioService: state.userReducer,
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
    form: "PorEntregarFacturaForm",
    enableReinitialize: true
  })
)(PorEntregarFacturaScreen);
