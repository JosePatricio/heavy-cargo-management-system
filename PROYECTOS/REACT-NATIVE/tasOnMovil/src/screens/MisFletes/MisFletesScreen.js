import React, {Component} from 'react';
import {View,TouchableHighlight,FlatList,TouchableOpacity,RefreshControl,Dimensions} from 'react-native';
import {connect} from 'react-redux';
import {Actions} from 'react-native-router-flux';
import {Field, reduxForm} from 'redux-form';
import {compose} from 'redux';
import {Container, Content, Card, CardItem, Body, Icon as NbIcon} from 'native-base';
import {getMisFletes} from '../../actions/misfletes.action';
import { ScrollableTabView,TabBar,AwesomeButtonRick ,Text,Icon, Loader, Moment, SegmentedControlTab, AwesomeAlert} from '../../components/';
import {styleMisfletes as styles, style,defaultTasonColor} from '../../styles/';
import {List, ListItem} from 'react-native-elements';
import {FACT_RAPIDA,FACT_NORMAL} from '../../Constants';
import {no_connected_info} from '../../utils/tas-on-texts';

import {
  MY_OFERTS,
  MY_OFERTS_APPROVED,
  MY_OFERTS_TO_RECIEVE,
  MY_OFERTS_TO_DELIVER,
  MY_OFERTS_GENERATE_INVOICE,
  MY_OFERTS_DELIVER_INVOICE,
  MY_OFERTS_FINISHED,
  MY_OFERTS_CANCELED,
  MY_OFERTS_COLLECT,
} from '../../Constants';
const maxLocationLength = 20;
const deviceHeigth=Dimensions.get('window').height;


const Page = ({label}) => (
  <View style={styles.container}>
    <Text style={styles.welcome}>
      {label}
    </Text>
    <Text style={styles.instructions}>
      To get started, edit index.ios.js
    </Text>
    <Text style={styles.instructions}>
      Press Cmd+R to reload,{'\n'}
      Cmd+D or shake for dev menu
    </Text>
  </View>
);

class MisFletesScreen extends Component {
 

  constructor(props) {
    super(props);

    this.state = {
      user: props.usuarioService.getUser.userDetails,
      selectedIndex: 0,
      customStyleIndex: 0,
      dataList: [],
      selectedDataList: [],
      isLoading: false,
      refreshing:false,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert: false,
      isProcessing: false
    };
  }

componentDidMount(){
this.getMisFletes(MY_OFERTS);
}


  getMisFletes = async status => {
    try {
      
  if(this.props.generalReducer.netWorkInfo.status == true){
    const response = await this.props.dispatch(getMisFletes(status));

    if (response.success) {
     
      this.setState({
        dataList: response.responseBody,
        isLoading: false,
        refreshing:false,
        isProcessing:false
      });
    } else {
      throw response;
    }
  }else{
    throw {responseBody: {message : no_connected_info}};
  }
    } catch (error) {
      console.log('error screen getMisFletes ',error);
        let errorText;
      errorText = error.responseBody.message;
      if (error.message) {
        errorText = error.message;
      }
      if(!errorText){
        errorText=error.responseBody;
      }
     
       this.setState({textAlert:errorText,
        titleAlert:'Error',
        showAlert:true,
        isErrorAlert:true,
        isLoading: false,
        refreshing: false
      }); 
    }
  };

  

  onPress = item => {
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

    console.log('swede OPCION  ',this.state.customStyleIndex);
    this.setState({isLoading:true});
    switch (this.state.customStyleIndex) {
      case 0:
          setTimeout(function () {
            Actions.EnEspera({solicitud: item});
            this.setState({isLoading:false});
          }.bind(this),0);
        break;
      case 1:
      setTimeout(function () {
        Actions.Aprobadas({solicitud: item});
        this.setState({isLoading:false});
      }.bind(this),0);
        break;
      case 2:
          setTimeout(function () {
            Actions.PorRecibir({solicitud: item});
            this.setState({isLoading:false});
          }.bind(this),0);
        break;
      case 3:
          setTimeout(function () {
            Actions.PorEntregar({solicitud: item});
            this.setState({isLoading:false});
          }.bind(this),0);
        break;
     
      case 5:
      
     setTimeout(function () {
      Actions.PorEntregarFactura({factura:item,tipoPago: FACT_RAPIDA, solicitudes: this.state.selectedDataList});
      this.setState({isLoading:false});
    }.bind(this),0);

        break;
     case 6:
          setTimeout(function () {
           Actions.PorEntregar({solicitud: item,onlyVisible:true});
           this.setState({isLoading:false});
         }.bind(this),0); 
   
           break;
      case 7:
          setTimeout(function () {
            Actions.PorEntregar({solicitud: item,onlyVisible:true});
            this.setState({isLoading:false});
          }.bind(this),0); 
        break;
      default:
        break;
    }

    
  }

  loadListData(status) {


      if (this.state.dataList.length == 0) {

        if(this.props.generalReducer.netWorkInfo.status == true){
          this.getMisFletes(status);
       }else{
       this.bindSolicitudesFlatList(this.state.dataList) ;
      return;
      }
      }
     else {
      return <Text></Text>;
    }
  }

  onRefresh() {
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


    this.setState({refreshing:true});
    switch (this.state.customStyleIndex) {
      case 0:
        this.getMisFletes(MY_OFERTS);
        break;
      case 1:
        this.getMisFletes(MY_OFERTS_APPROVED);
        break;
      case 2:
        this.getMisFletes(MY_OFERTS_TO_RECIEVE);
        break;
      case 3:
        this.getMisFletes(MY_OFERTS_TO_DELIVER);
        break;
      case 4:
        this.getMisFletes(MY_OFERTS_GENERATE_INVOICE);
        break;
      case 5:
          this.getMisFletes(MY_OFERTS_DELIVER_INVOICE);
        break;
      case 6:
          this.getMisFletes(MY_OFERTS_COLLECT);
        break;
      case 7:
          this.getMisFletes(MY_OFERTS_FINISHED);
        break;
    
      default:
        break;
    }

   
  }

  hideAlert = () => {
    this.setState({
      showAlert: false
    });
  }
  
  dismissAlert= () => {
  if(this.state.showAlert){
  this.setState({
    showAlert: false
  });
  return;
} 
  }


  press = (hey) => {
    this.state.dataList.map((item) => {
      if (item.idSolicitud === hey.idSolicitud) {
        item.check = !item.check
        if (item.check === true) {
          this.state.selectedDataList.push(item);
          console.log('selected:' + item.idSolicitud);
        } else if (item.check === false) {
          const i = this.state.selectedDataList.indexOf(item)
          if (1 != -1) {
            this.state.selectedDataList.splice(i, 1)
            console.log('unselect:' + item.id)
            return this.state.selectedDataList
          }
        }
      }
    })
    console.log('selected ',this.state.selectedDataList.length);
    this.setState({dataList: this.state.dataList})
  }

  pagoInmediato=()=>{

    if(this.state.selectedDataList == 0){
    this.setState({
      showAlert: true,
      textAlert: 'Debe seleccionar al menos una solicitud',
      titleAlert: "Mensaje",
      isErrorAlert:true,
      refreshing:false,
      isLoading:false,
    });
  }else{
    
    Actions.PorGenerarFactura({tipoPago: FACT_RAPIDA, solicitudes: this.state.selectedDataList});
  }
  }

  normal=() =>{
    if(this.state.selectedDataList == 0){
      this.setState({
        showAlert: true,
        textAlert: 'Debe seleccionar al menos una solicitud',
        titleAlert: "Alerta",
        isErrorAlert:true,
        refreshing:false,
        isLoading:false,
      });
    }else{
    Actions.PorGenerarFactura({tipoPago: FACT_NORMAL ,solicitudes: this.state.selectedDataList});
    }}

  bindSolicitudesFlatList(data,label) {

      if(data.length==0){
        const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
        return (  
        <View tabLabel={{label: label}} style={styles.container}>
        <FlatList
          data={emptyData}
          renderItem={({item}) => (
            <View style={style.cardStyle}>
          {!this.props.misfletesService.getAllReducer.isFetching && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
          </View>
          )
        }
        keyExtractor={ ( item, index ) => `${index}` }
          refreshControl={
            <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
          }
          onEndReachedThreshold={(0, 4)}
        />
      </View>
  
        );
      }else{
      return (
        <View tabLabel={{label: label}} style={styles.container}>
          <FlatList
            data={data}
            renderItem={({item}) => (
              <Card collapsable={true} style={({elevation: 4}, styles.cardStyle)}>
                <CardItem
                  button
                  style={styles.noPadding}
                  disabled={this.state.isButtonDisabled}
                  onPress={() => this.onPress(item)}>
                  <Body style={styles.noPadding}>
                    <View style={styles.button}>
                      <Text style={styles.lengthInfoText}>
                        {item.idSolicitud}
                      </Text>
                    </View>
  
                    <View style={styles.viewInfo1}>
                      <View style={styles.viewInfo1_1}>
                        <Text style={styles.cityText}>
                          {item.origen && (item.origen.length >= maxLocationLength)
                            ? item.provinciaOrigen
                            : item.origen}
                        </Text>
                        <Text style={styles.dateText}>
                          <Moment element={Text} format="DD/MM/YYYY hh:mm">
                            {item.fechaEntrega}
                          </Moment>
                        </Text>
                      </View>
  
                      <View style={styles.viewInfo1_2}>
                        <Icon
                          customIcon="arrowright"
                          iconReference="AntDesign"
                          size={25}
                        />
                      </View>
  
                      <View style={styles.viewInfo1_3}>
                        <Text style={styles.cityText}>
                          {item.destino && (item.destino.length >= maxLocationLength)
                        ? item.provinciaDestino
                        : item.destino}
                        </Text>
                        <Text style={styles.dateText}>
                          <Moment element={Text} format="DD/MM/YYYY hh:mm">
                            {item.fechaRecepcion}
                          </Moment>
                        </Text>
                      </View>
  
                      <View style={styles.viewInfo1_4}>
                        <Text style={styles.descriptionText}>
                        piezas
                        </Text>
                        <Text style={styles.informationText}>
                        {item.numeroPiezas}
                        </Text>
  
                        <Text style={styles.descriptionText}>peso</Text>
                        <Text style={styles.informationText}>
                        {item.peso} {item.tipoPeso}
                        </Text>
                      </View>
                    </View>
                  </Body>
                </CardItem>
              </Card>
            )}
            keyExtractor={item => item.idSolicitud}
            refreshControl={
              <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
            }
            onEndReachedThreshold={(0, 4)}
          />
        </View>
      );
    }}
  
  bindItem(item, Checked){
    return (  <Card collapsable={true} style={[styles.cardStyle,{margin:undefined,width:'100%',elevation: 4 ,alignSelf: 'stretch'}]}>
           <View style={styles.button}>
             <Text style={styles.lengthInfoText}>
               {item.idSolicitud}
             </Text>
           </View>
           <View style={[styles.viewInfo1,{flex:undefined}]}>
           <View style={{width : '35%'}}>
           <Text style={[styles.cityText,{ fontWeight: (Checked?'bold':'normal')}]}>
              {item.origen && (item.origen.length >= maxLocationLength)
                ? item.provinciaOrigen
                : item.origen}
            </Text>
            </View>
            <View style={{width : '10%'}}>
            <Icon
              customIcon="arrowright"
              iconReference="AntDesign"
              size={25}
            />
            </View>
            <View style={{width : '35%'}}>
            <Text style={[styles.cityText,{ fontWeight: (Checked?'bold':'normal')}]}>
              {item.destino && (item.destino.length >= maxLocationLength)
            ? item.provinciaDestino
            : item.destino}
            </Text> 
            </View>
            <View style={{width : '20%'}}>
            <Text style={[styles.descriptionText,{textAlign:'center',fontWeight: (Checked?'bold':'normal')}]}>
              Valor
            </Text>
            <Text style={[styles.informationText,{textAlign:'center',fontWeight: (Checked?'bold':'normal')}]}>
            {item.amount}
            </Text>
           </View>
           </View>
          
 
  </Card>);
   }
 
  bindSolicitudesFlatListCheckBox(data,label) {
    if(data.length==0){
      const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
      return (  
      <View tabLabel={{label: label}} style={styles.container}>
      <FlatList
        data={emptyData}
        renderItem={({item}) => (
          <View style={style.cardStyle}>
        {!this.props.misfletesService.getAllReducer.isFetching && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
        </View>
        )
      }
      keyExtractor={ ( item, index ) => `${index}` }
        refreshControl={
          <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
        }
        onEndReachedThreshold={(0, 4)}
      />
    </View>
      );
    }else{
    return (
      <Container tabLabel={{label: label}}>
       
          <Text style={[style.textInfo,{paddingTop:10,paddingBottom:10,fontSize:15,textAlign:'center'}]}>
             Seleccione solicitudes 
           </Text>
<View style={{height: deviceHeigth-(deviceHeigth/2.1)}}>

<FlatList data={data} keyExtractor={( item, index ) => `${index}`}
    refreshControl={<RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />}
    onEndReachedThreshold={(0, 4)}
    extraData={this.state} renderItem={({item}) => {
            return <TouchableOpacity style={{
              flexDirection: 'row',
              paddingLeft:20,
              paddingRight:40,
              borderBottomWidth: 1,
              borderStyle: 'solid',
              borderColor: '#ecf0f1'
            }} onPress={() => {
              this.press(item)
            }}>
              <View style={{
                flex: 3,
                alignItems: 'flex-start',
                justifyContent: 'center'
              }}>
                {this.bindItem(item,item.check)}
              </View>
              <View style={{
                flex: 1,
                alignItems: 'flex-end',
                justifyContent: 'center'
              }}>
                {item.check
                  ? (
                    <NbIcon name="ios-checkbox" style={{fontSize:40,color:'#03AADE'}}  type="Ionicons"></NbIcon>
                  )
                  : (
                    <NbIcon name="ios-square-outline" style={{fontSize:40,color:'#03AADE'}} type="Ionicons"></NbIcon>
                  )}
              </View>
            </TouchableOpacity>
          }}/>
</View>

<Content>
               <Text style={[style.textInfo,{paddingTop:5,textAlign:'center'}]}>
                    Tipo de facturación
               </Text>
     
               <View style={ [style.twoButtonsArea, {paddingTop:5}]}>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea1} onPress={this.normal.bind(this)} backgroundColor={defaultTasonColor}>
          <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
            <Text style={style.defaultTextButton}>Normal</Text></AwesomeButtonRick>
          <AwesomeButtonRick height={50} stretch={true} style={style.defaultButtonInArea2} onPress={this.pagoInmediato.bind(this)} backgroundColor={defaultTasonColor}>
          <Text style={style.defaultTextButton}>Pago inmediato</Text>
             <Icon name='right' type='AntDesign' style={style.defaultIconButton}/>
             </AwesomeButtonRick>
          </View>


        



         </Content>
     </Container>
    );
  }}
 
  bindFacturasPorEntregarFlatList(data,label) {

    if(data.length==0){
      const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
      return (  
      <View tabLabel={{label: label}} style={styles.container}>
      <FlatList
        data={emptyData}
        renderItem={({item}) => (
          <View style={style.cardStyle}>
        {!this.props.misfletesService.getAllReducer.isFetching && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
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
      <View tabLabel={{label: label}} style={styles.container}>
        <FlatList
          data={data}
          renderItem={({item}) => (
            <Card collapsable={true} style={({elevation: 4}, styles.cardStyle)}>
              <CardItem
                button
                style={styles.noPadding}
                disabled={this.state.isButtonDisabled}
                onPress={() => this.onPress(item)}>
                <Body style={styles.noPadding}>
                 
                <View style={styles.viewInfo1}>
                  <View style={{padding: 5, width: '40%'}}>
                    <Text style={[style.textInfo,{textAlign:'center'}]}>Prefactura</Text>
                    <Text style={style.textData}>
                    {item.invoiceId}
                    </Text>
                  </View>
                  <View style={{padding: 5, width: '25%'}}>
                    <Text style={style.textInfo}>Fecha fact.</Text>
                    <Text style={style.textData}>
                          <Moment element={Text} format="DD/MM/YYYY">
                            {item.invoiceDatePrefactura}
                          </Moment>
                        </Text>
                 
                  </View>
                  <View style={{padding: 5, width: '25%'}}>
                    <Text style={style.textInfo}>Monto fact.</Text>
                    <Text style={style.textData}>
                    {item.invoiceAmount?item.invoiceAmount.toFixed(2):''} 
                    </Text>
                  </View>
                </View>


                
                </Body>
              </CardItem>
            </Card>
          )}
          keyExtractor={item => item.idSolicitud}
          refreshControl={
            <RefreshControl refreshing={this.state.refreshing} onRefresh={this.onRefresh.bind(this)} />
          }
          onEndReachedThreshold={(0, 4)}
        />
      </View>
    );
  }}

  onChangeTab= value => {

this.setState({customStyleIndex:value.i,isProcessing:true,dataList:[]});
     switch (value.i) {
      case 0:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS):null;
        break;
      case 1:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_APPROVED):null;
        break;
      case 2:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_TO_RECIEVE):null;
        break;
      case 3:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_TO_DELIVER):null;
        break;
      case 4:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_GENERATE_INVOICE):null;
        break;
      case 5:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_DELIVER_INVOICE):null;
        break;
      case 6:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_COLLECT):null;
        break;
      case 7:
        !this.state.isProcessing?this.getMisFletes(MY_OFERTS_FINISHED):null;
        break;
    
      default:
        break;
    }
   

    }



  render() {
    const {
      selectedIndex,
      selectedIndices,
      customStyleIndex,
      dataList,
      refreshing,
      isLoading,
      showAlert,textAlert,titleAlert,isErrorAlert
    } = this.state;
  

    return (
      <Container>
      {(this.props.misfletesService.getAllReducer.isFetching || isLoading) && (
          <Loader hideIndicator={refreshing} />
        )}


   <ScrollableTabView onChangeTab={this.onChangeTab} initialPage={0} 
              tabBarActiveTextColor={defaultTasonColor}
              tabBarInactiveTextColor="#4A5F8E"
              renderTabBar={() => <TabBar underlineColor={defaultTasonColor} />}>
          
          {this.bindSolicitudesFlatList(dataList,'En espera')}
          {this.bindSolicitudesFlatList(dataList,'Aprobadas')}
          {this.bindSolicitudesFlatList(dataList,'Por recibir')}
          {this.bindSolicitudesFlatList(dataList,'Por entregar')}
          {this.bindSolicitudesFlatListCheckBox(dataList,'Por generar Factura')}
          {this.bindFacturasPorEntregarFlatList(dataList,'Por entregar Factura')}
          {this.bindSolicitudesFlatList(dataList,'Por Cobrar')}
          {this.bindSolicitudesFlatList(dataList,'Finalizadas')}
     

          </ScrollableTabView>

{showAlert &&<AwesomeAlert
            show={showAlert}  
            showProgress={false}
            title={titleAlert}
            message={textAlert}
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
      </Container>
      
    );
  }


}

const mapStateToProps = state => {
  return {
    misfletesService: state.misfletesReducer,
    usuarioService: state.userReducer,
    generalReducer: state.generalReducer
  };
};

const mapDispatchToProps = dispatch => {
  return {
    dispatch,
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(MisFletesScreen);
