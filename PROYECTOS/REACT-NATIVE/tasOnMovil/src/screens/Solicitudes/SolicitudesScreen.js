import { Body, Card, CardItem, Container } from 'native-base';
import React from 'react';
import { FlatList, RefreshControl, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import { getSolicitudes } from '../../actions/solicitud.action';
import { AwesomeAlert, Icon as Icono, Loader, Moment, Text } from '../../components/';
import { style, styleSolicitud as styles } from '../../styles/';
import { no_connected_info } from '../../utils/tas-on-texts';

const maxLocationLength = 20;
class SolicitudesScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: props.usuario.getUser.userDetails,
      isButtonDisabled: false,
      isLoading:false,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert: false,
      solicitudes:[],
      refreshing:false,
    };
  }

  

  load = () => {
   if (!this.state.solicitudes && this.state.solicitudes.length != 0){
    this.setState({isLoading:false});
   }else{
    this.setState({isButtonDisabled: false});
   }
 }

 
  componentDidMount() {
     this.load();
    this.props.navigation.addListener('willFocus', this.load);
    this.getFletes(); 
   
  }


  getFletes = async values => {
    try {
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

      const response = await this.props.dispatch(getSolicitudes());

      if (response.success) {
        this.setState({isLoading : false, refreshing : false, solicitudes:response.responseBody});
      }else{
        throw response;
      }
    } catch (error) {
      console.log('ERROR getFletes  ',error);
      let errorText;
      errorText = error.responseMessage;
      if (error.responseBody.message) {
        errorText = error.responseBody.message;
      }
      if(!errorText){
        errorText=JSON.stringify(error);
      }
      this.setState({
        showAlert: true,
        textAlert: errorText,
        titleAlert: "Error",
        isErrorAlert:true,
        isLoading:false,
        refreshing:false
      });
    }
  };


 
  onPress =  item => {
 
if(this.props.generalReducer.netWorkInfo.status==false){
  this.setState({
    showAlert: true,
    textAlert: no_connected_info,
    titleAlert: "Error",
    isErrorAlert:true,
  });
    return;
}

  this.setState({isButtonDisabled: true,isLoading:true});
    setTimeout(function () {
      Actions.SolicitudOferta({solicitud: item});
      this.setState({isLoading:false});
    }.bind(this),10); 

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
} 
}
  
  onRefresh() {
    this.setState({refreshing:true});
  
    this.getFletes();
  }


  bindSolicitudes() {

const {refreshing,solicitudes} = this.state;

if(solicitudes.length==0){
  const emptyData=[{id:0,text:'Sin resultados'},{id:1,text:'Búsqueda no generó resultados'}];
  return (  
  <View style={styles.container}>
  <FlatList
    data={emptyData}
    renderItem={({item}) => (
      <View style={style.cardStyle}>
    {!this.props.solicitudesService.getAllReducer.isFetching && <Text style={(item.id==0?style.textInfo:style.textData)} >{item.text}</Text> }   
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
}
    return (
      <View style={styles.container}>
        <FlatList
          data={solicitudes}
          renderItem={({item}) => (
            <Card collapsable={true} style={({elevation: 4}, styles.cardStyle)}>
              <CardItem button  style={styles.noPadding} disabled={this.state.isButtonDisabled}  onPress={()=>this.onPress(item)}>
            <Body style={styles.noPadding}>
              <View style={styles.button}>
              <Text style={styles.lengthInfoText}>{item.idSolicitud}</Text>
              </View>
           
               <View style={styles.viewInfo1}>
                <View style={styles.viewInfo1_1}>
                  <Text style={styles.cityText}>{' '}{item.origen.length >= maxLocationLength? item.provinciaOrigen: item.origen}{' '}</Text>
                  <Text style={styles.dateText}><Moment element={Text} format="DD/MM/YYYY hh:mm">{item.fechaEntrega}</Moment></Text>
                </View>

                <View style={styles.viewInfo1_2}>
                  <Icono
                    customIcon="arrowright"
                    iconReference="AntDesign"
                    size={25}
                  />
                </View>

                <View style={styles.viewInfo1_3}>
                  <Text style={styles.cityText}>{item.destino.length >= maxLocationLength? item.provinciaDestino: item.destino}</Text>
                  <Text style={styles.dateText}><Moment element={Text} format="DD/MM/YYYY hh:mm">{item.fechaRecepcion}</Moment></Text>
                </View>

                <View style={styles.viewInfo1_4}>
                  <Text style={styles.descriptionText}>{item.numeroPiezas} piezas</Text>
                  <Text style={styles.informationText}>{item.peso} {item.medida}</Text>

                  <Text style={styles.descriptionText}>pago en</Text>
                  <Text style={styles.informationText}>{item.diasPagos} días</Text>
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
         onEndReachedThreshold={0,4}
        />
      </View>
    );
  }

  render() {

  const {showAlert,titleAlert,textAlert,isErrorAlert,solicitudes,refreshing} = this.state
  
  return (
    <Container>
      <View style={styles.container}>
        {this.bindSolicitudes()}
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
            confirmButtonTextStyle={isErrorAlert?style.alertConfirmButtonErrorTextStyle:style.alertConfirmButtonTextStyle}
            titleStyle={style.alertTitleStyle}
            onConfirmPressed={() => {
              this.hideAlert();
            }}
            onDismiss={() => {
              this.dismissAlert();
            }}
          />}

      </View>

      {(this.props.solicitudesService.getAllReducer.isFetching || this.state.isLoading) && (
          <Loader hideIndicator={refreshing} />
        )}
      </Container>
    );
  }
}

const mapStateToProps = state => {
  return {
    solicitudesService: state.solicitudReducer,
    usuario: state.userReducer,
    generalReducer: state.generalReducer,
    loginUser: state.authReducer
  };
};

const mapDispatchToProps = dispatch => {
  return {
    dispatch,
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(SolicitudesScreen);
