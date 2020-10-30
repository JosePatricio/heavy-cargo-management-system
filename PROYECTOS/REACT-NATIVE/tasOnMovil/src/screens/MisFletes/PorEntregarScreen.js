import { Card, Container, Content, Icon as NbIcon, Separator } from 'native-base';
import React, { Component } from 'react';
import { FlatList, Image, Linking, Modal, Platform, TouchableOpacity, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { formValueSelector, reduxForm } from 'redux-form';
import { getFoto, getImages } from '../../actions/misfletes.action';
import { getSolicitudById, updateMyOfert } from '../../actions/solicitud.action';
import { AnimatedText, AwesomeAlert, AwesomeButtonRick, Collapse, CollapseBody, CollapseHeader, ImagePicker, ImageViewer, Line, Loader, Moment, Shimmer, Text } from '../../components/';
import { ESTADO_FOTO_POR_ENTREGAR, ESTADO_FOTO_POR_RECIBIR } from '../../Constants';
import { defaultTasonColor, style, styleSolicitudOferta as styles } from '../../styles/';
import { no_connected_info } from '../../utils/tas-on-texts';

const utf8 = require('utf8');

class PorEntregarScreen extends Component {
  constructor(props) {
    super(props);

    this.imagesArray = [];

    this.state = {
      user: props.usuarioService.getUser.userDetails,
      showAlert: false,
      textAlert: '',
      titleAlert: '',
      isErrorAlert: false,
      data: {},
      isLoading: false,
      isOfferedSuccessfully: false,
      drivers: [],
      vehicles: [],
      selectedDriver: undefined,
      selectedVehicle: undefined,
      isChecked: false,
      showModal: false,
      isConfirmation: false,
      fileData: '',
      pictures: [],
      dataPictures: [],
      dataPictures2: [],
      fileUri: '',
      selectedImage: undefined,
      imagesViewer: [],
      imagesViewerIndex: undefined,
      isImagesLoading:false,
      picturesRecoleccionShimmer:[],
      picturesEntregaShimmer:[],
      showShimmer1:false,
     
      showShimmer2:false,
      hasValueShimmer1:false,
      hasValueShimmer2:false
      
    };
  }

  componentDidMount() {
    this.loadData();
    
  }

  loadImages = async () => {
    try {
      let images = [],images2 = [];
      this.setState({isImagesLoading: true});
     // const response = await this.props.dispatch(getImages(this.props.solicitud.idOferta, ESTADO_FOTO_POR_RECIBIR));
     const response = await this.props.dispatch(getImages(608, ESTADO_FOTO_POR_RECIBIR));
    
      if (!response.success) {
        throw response;
      }
      else {
        console.log('FOTOS   ',response.responseBody);

        let dataImages = response.responseBody;
        dataImages.forEach((element, index) => {
          let imageObject = {id: index, image: element};
          images.push(imageObject);
        });
      }

      if(this.props.onlyVisible){
        const response = await this.props.dispatch(getImages(this.props.solicitud.idOferta, ESTADO_FOTO_POR_ENTREGAR));
        if (!response.success) {
          throw response;
        }
        else {
          let dataImages = response.responseBody;
          dataImages.forEach((element, index) => {
            let imageObject = {id: index, image: element};
            images2.push(imageObject);
          });
        }
      }
      
      this.setState({dataPictures: images, dataPictures2 : images2,isImagesLoading:false});

    } catch (error) {
      console.log('ERROR LOAD IMGAES  ',error);
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      if (!errorText) {
        errorText = error.responseBody;
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


  getFoto = async (idFoto) => {
    try {
      const response = await this.props.dispatch(getFoto(idFoto));
    
      if (!response.success) {
        throw response;
      }
      else {
     return response.responseBody;
    }} catch (error) {
      console.log('ERROR LOAD IMGAES  ',error);
      let errorText;
      errorText = error.responseMessage;
      if (error.message) {
        errorText = error.message;
      }
      if (!errorText) {
        errorText = error.responseBody;
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


  
  loadData = async values => {
    try {
      this.setState({isLoading: true});

      const response = await this.props.dispatch(
       getSolicitudById(this.props.solicitud.idSolicitud, this.state.user),
        //getSolicitudById('TAS-ON0003669', this.state.user),
      );


      console.log('DATOS  ',response);
      if (!response.success) {
        throw response;
      } else {
        this.setState({data: response.responseBody, isLoading: false,picturesRecoleccionShimmer:response.responseBody.fotosRecoleccion
          ,picturesEntregaShimmer:response.responseBody.fotosEntrega});
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
          isOfferedSuccessfully: (response.responseBody.response== 'ERROR' ? false : true),
          isConfirmation:false
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
        isErrorAlert: true,
      });
    }
  };

  onCancel =()=>{

    Actions.pop();
  }

  onPress = values => {
    const {pictures} = this.state;
    let images = [];
    pictures.forEach(element => {
      let imageObject = {
        file: element.data,
        fileName: element.fileName,
        fileSize: element.fileSize,
        fileType: 'image/jpeg',
      };
      images.push(imageObject);
    });

    let object = {
      fotos: images,
      idOferta: String(this.props.solicitud.idOferta),
    };

    if (this.state.pictures.length == 0) {
      this.setState({
        textAlert: 'Debe tomar al menos una foto de la recolección',
        titleAlert: 'Mensaje',
        showAlert: true,
        isErrorAlert: true,
        isConfirmation:false
      });
    } else {
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
    }
  };

  hideAlert = () => {
    this.setState({
      showAlert: false,
    });

    if (this.state.isOfferedSuccessfully) {
      Actions.reset('MisSolicitudes');
    }
  };

  dismissAlert = () => {
    if (this.state.showAlert) {
      this.setState({
        showAlert: false,
      });
    }
  };

  confirmationAlert = () => {
    this.setState({
      showAlert: false,
    });
    if (this.state.isConfirmation) {
      const { pictures,selectedImage } = this.state;
 let array = [...this.state.pictures]; 
 let index = array.indexOf(this.state.selectedImage)

 if (index !== -1) {
  array.splice(index, 1);
  this.setState({ pictures : array});
  this.imagesArray=array;   
   }
    }

  };

  dialCall = number => {
    let phoneNumber = '';

    if (Platform.OS === 'android') {
      phoneNumber = 'tel:${' + number + '}';
    } else {
      phoneNumber = 'telprompt:${' + number + '}';
    }
    Linking.openURL(phoneNumber);
  };

  openMail = email => {
    Linking.openURL('mailto:' + email + '');
  };

  openImageSource() {
    let options = {
      title: 'Seleccione entrada',

      storageOptions: {
        skipBackup: true,
        path: 'images',
      },
    };
    ImagePicker.showImagePicker(options, response => {
      if (response.didCancel) {
        console.log('User cancelled image picker');
      } else if (response.error) {
        console.log('ImagePicker Error: ', response.error);
      } else {
        const source = {uri: response.uri};

        this.imagesArray.push({
          id: this.state.pictures.length,
          image: response.uri,
          data: response.data,
          fileSize: response.fileSize,
          fileName: String(Date.now()),
          fileType: 'image/jpeg',
        });
        
        this.setState({pictures: [...this.imagesArray],picturesEntregaShimmer: [...this.imagesArray]});//swede
    
    
      }
    });
  }

  imageDeleteConfirmation = item => {
    this.setState({
      showAlert: true,
      textAlert: '¿Está seguro de eliminar esta imagen?',
      titleAlert: 'Confirmación',
      isErrorAlert: false,
      isConfirmation: true,
      selectedImage: item,
    });
  };

  openImageViewer = (item,flatList) => {
    let images = [];

console.log('ITEM ID ES   ',item.id);

    if(flatList == 1){
     this.state.picturesRecoleccionShimmer.forEach((element, index) => {
      let imageObject = {
        url: 'data:image/jpeg;base64,' + element.image,
      };
      images.push(imageObject);
    });
  }
  if(flatList == 2){

    if(!this.props.onlyVisible){
     this.state.pictures.forEach((element, index) => {
      let imageObject = {
        url: 'data:image/jpeg;base64,' + element.data,
      };
      images.push(imageObject);
    });
  }else{
    this.state.picturesEntregaShimmer.forEach((element, index) => {
      let imageObject = {
        url: 'data:image/jpeg;base64,' + element.image,
      };
      images.push(imageObject);
    });
  }}

  this.setState({showModal: true, imagesViewer: images,imagesViewerIndex:item.id});
  
};




getImage = async (images,opt) => {
  try {

let imagenesShipperAux=[];
let c=0;   
if(opt==1){
this.setState({showShimmer1:true, hasValueShimmer1:(this.state.data.fotosRecoleccion.length>0 )});

for (const idFoto of images) {
      const response = await this.props.dispatch(getFoto(idFoto));
      let object={id:c,image:response.responseBody.fileContent}
      imagenesShipperAux.push(object);
      c++;
    }
this.setState({dataPictures:imagenesShipperAux,picturesRecoleccionShimmer:imagenesShipperAux,showShimmer1:false});
}
if(opt==2){

  this.setState({ hasValueShimmer2:(this.state.data.fotosEntrega.length>0 ),showShimmer2:true});
  
  for (const idFoto of images) {
        const response = await this.props.dispatch(getFoto(idFoto));
        let object={id:c,image:response.responseBody.fileContent}
        imagenesShipperAux.push(object);
        c++;
      }
  this.setState({ dataPictures2:imagenesShipperAux,picturesEntregaShimmer:imagenesShipperAux,showShimmer2:false});

}
   
  } catch (error) {
    console.log('ERROR LOAD getimage  ',error);
    let errorText;
    errorText = error.responseMessage;
    if (error.message) {
      errorText = error.message;
    }
    if (!errorText) {
      errorText = error.responseBody;
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


verFotosRecoleccion=()=>{
this.getImage(this.state.data.fotosRecoleccion,1);
}

verFotosEntregar=()=>{
this.getImage(this.state.data.fotosEntrega,2);  
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
      pictures,
      isConfirmation,
      imagesViewer,
      fileData,
      isLoading,
      dataPictures,      dataPictures2,
      isImagesLoading
      ,picturesRecoleccionShimmer,picturesEntregaShimmer
    } = this.state;

console.log('dataPictures   ',dataPictures);

    return (
      <Container>
        <Content>
          <View style={styles.container}>
          <Text style={[styles.textIdSolicitud,{alignSelf:'center'}]}>{data.idSolicitud}</Text>
          {data.offer &&  <Text style={[style.textData,{paddingRight:20,alignSelf:'flex-end'}]}>Mi oferta <Text style={style.textInfo}>{data.offer.amount}{' $'}</Text></Text>}
  
            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={this.props.onlyVisible?true:false}>
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
                      <Text style={styles.textData4}>{data.tipo}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Número de piezas</Text>
                      <Text style={styles.textData4}>{data.numeroPiezas}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Volumen Total</Text>
                      <Text style={styles.textData4}>
                        {data.volumen} {data.tipoVolumen}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                    <View style={{padding: 5, width: '50%'}}>
                      <Text style={styles.textInfo4}>Peso</Text>
                      <Text style={styles.textData4}>
                        {data.peso} {data.tipoPeso}
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
                      <Text style={styles.textData4}>{data.observaciones}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={this.props.onlyVisible?true:false}>
                <CollapseHeader>
                  <Separator style={styles.collapseHeaderSeparator}>
                    <Text style={styles.collapseHeaderText}>Origen</Text>
                  </Separator>
                </CollapseHeader>
                <CollapseBody>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Provincia</Text>
                      <Text style={styles.textData4}>
                        {data.provinciaOrigen}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Ciudad</Text>
                      <Text style={styles.textData4}>{data.origen}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Dirección</Text>
                      <Text style={styles.textData4}>{data.direccion}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Fecha de recolección</Text>
                      <Text style={styles.textData4}>
                        <Moment element={Text} format="DD/MM/YYYY hh:mm">
                          {data.fechaEntrega}
                        </Moment>
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Envía</Text>
                      <Text style={styles.textData4}>
                        {data.personaEntrega}
                      </Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Teléfono</Text>

                      <TouchableOpacity
                        onPress={() => this.dialCall(data.telefonoContacto)}
                        style={style.linkButton}>
                        <Text style={style.linkTextButton}>
                          {data.telefonoContacto}
                        </Text>
                      </TouchableOpacity>

                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Correo eletrónico</Text>
                      <TouchableOpacity
                        onPress={() => this.openMail(data.correoContacto)}
                        style={style.linkButton}>
                        <Text style={style.linkTextButton}>
                          {data.correoContacto}
                        </Text>
                      </TouchableOpacity>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <Card elevation={4} style={styles.cardStyle}>
              <Collapse isCollapsed={this.props.onlyVisible?true:false}>
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
                      <Text style={styles.textData4}>{data.destino}</Text>
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

                  <View style={styles.viewInfo3_1}>
                    <View style={{padding: 5}}>
                      <Text style={styles.textInfo4}>Recibe</Text>
                      <Text style={styles.textData4}>{data.personaRecibe}</Text>
                      <Line color="#4A5F8E" size={1} />
                    </View>
                  </View>
                </CollapseBody>
              </Collapse>
            </Card>

            <View style={[styles.viewInfo4 ,{marginTop:5}]}>
              <Text style={styles.textInfo5}> Fotos de recolección  </Text>
            </View>
  <View style={{alignItems: 'center'}} >
    

        { (dataPictures.length == 0) ?   <TouchableOpacity onPress={this.verFotosRecoleccion.bind(this)} style={style.linkButtonOpacity}>
                  <Text style={style.linkButtonText}>
                  Ver fotos {data.fotosRecoleccion?'('+data.fotosRecoleccion.length+')':''}
                  </Text>
                </TouchableOpacity> :<Text></Text>}       
    
<FlatList horizontal style={{marginTop:5}} style={{display:(this.state.hasValueShimmer1?'flex':'none')}}
            data={picturesRecoleccionShimmer}
            renderItem={({ item }) => 
            <TouchableOpacity item={item} style={{margin: 10}} onPress={() => (!this.state.showShimmer1)?this.openImageViewer(item,1):undefined} >
           {(this.state.showShimmer1) && <Shimmer autoRun={true}  style={styles.images} ></Shimmer>}
           {(!this.state.showShimmer1) && <Image source={{ uri: 'data:image/jpeg;base64,' + item.image }} style={styles.images}/> }
             
              </TouchableOpacity>
              }
            keyExtractor={( item, index ) => `${index}`} />
  </View>

            <View style={styles.viewInfo4}>
            <Text style={styles.textInfo5}>Fotos de entrega</Text>
            </View>

            <View style={[styles.viewInfo1, {alignItems: 'center'}]}>

            { (dataPictures2.length == 0) ? 
            <TouchableOpacity onPress={this.verFotosEntregar.bind(this)} style={[style.linkButtonOpacity,{display:(!this.props.onlyVisible?'none':'flex')}]}>
                  <Text style={style.linkButtonText}>
                  Ver fotos {data.fotosEntrega?'('+data.fotosEntrega.length+')':''}
                  </Text>
                </TouchableOpacity>:<Text></Text>}
  
            <AnimatedText  onPress={this.openImageSource.bind(this)}
              animation={(this.state.pictures.length == 0)?"pulse":undefined} 
              easing="ease-out" 
              iterationCount="infinite" 
              style={{ textAlign: 'center',display:(this.props.onlyVisible?'none':'flex') }}>
              <NbIcon style={{ fontSize: 50,color:'#4A5F8E'}} type="Ionicons" ios="ios-camera" android="md-camera"/>
            </AnimatedText> 
            <Text style={[styles.textInfo4,{display:(this.props.onlyVisible?'none':'flex')}]}>Agregar Foto</Text>
            
            <FlatList
                horizontal style={{marginTop:10,display:( this.props.onlyVisible?(this.state.hasValueShimmer2?'flex':'none'):undefined)}}
                data={this.props.onlyVisible?picturesEntregaShimmer:this.imagesArray}
                renderItem={({item}) => (
                  <TouchableOpacity
                    item={item} onPress={() => (!this.state.showShimmer1)?this.openImageViewer(item,2):undefined}
                    onLongPress={() => (this.props.onlyVisible?undefined:this.imageDeleteConfirmation(item))}>
                  
                  {this.props.onlyVisible?
                
               (this.state.showShimmer2?<Shimmer autoRun={true}  style={styles.images} ></Shimmer>:<Image source={{ uri: 'data:image/jpeg;base64,' + item.image }} style={styles.images}/>)
                  :
                    <Image source={{uri: ( this.props.onlyVisible?'data:image/jpeg;base64,' + item.image : item.image )}} style={styles.images} />
                  }
                  
  

                  </TouchableOpacity>
                )}
                keyExtractor={(item, index) => String(index)}
              />

            </View>

            <Line color="#4A5F8E" size={1} />

            <View style={styles.viewInfo7}>
         
              <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={handleSubmit( this.props.onlyVisible?this.onCancel:this.onPress)} backgroundColor={defaultTasonColor}>
               
               { this.props.onlyVisible &&   <NbIcon name='left' type='AntDesign' style={style.defaultIconButton}/>    }
                <Text style={style.defaultTextButton}>{this.props.onlyVisible?'REGRESAR':'ACTUALIZAR' }</Text></AwesomeButtonRick>

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
          showCancelButton={isConfirmation}
          showConfirmButton={true}
          confirmText={isConfirmation ? 'Aceptar' : 'Ok'}
          cancelText="Cerrar"
          contentContainerStyle={style.alertContentContainerStyle}
          confirmButtonStyle={style.alertConfirmButtonStyle}
          confirmButtonTextStyle={
            isErrorAlert
              ? style.alertConfirmButtonErrorTextStyle
              : style.alertConfirmButtonTextStyle
          }
          cancelButtonStyle={style.alertConfirmButtonStyle}
          cancelButtonTextStyle={
            isErrorAlert
              ? style.alertConfirmButtonErrorTextStyle
              : style.alertConfirmButtonTextStyle
          }
          titleStyle={style.alertTitleStyle}
          onConfirmPressed={() => {
            isConfirmation ? this.confirmationAlert() : this.hideAlert();
          }}
          onDismiss={() => {
            this.dismissAlert();
          }}
          onCancelPressed={() => this.hideAlert()}
        />}
        {showModal && (
          <Modal
           onRequestClose={()=> this.setState({showModal:false})}
            isVisible={showModal}
            transparent={true}>
              <Container>
              <NbIcon style={style.imageViewerIcon} onPress={()=> this.setState({showModal:false})} type="AntDesign" name="close"/>
              <ImageViewer index={this.state.imagesViewerIndex} enableSwipeDown={true} imageUrls={imagesViewer}/>
              </Container>
            </Modal>
        )}

        {isLoading && <Loader />}
      </Container>
    );
  }
}

const validate = values => {
  const errors = {};
  return errors;
};

const selector = formValueSelector('PorEntregarForm');

const mapStateToProps = state => {
  return {
    solicitudService: state.solicitudReducer,
    usuarioService: state.userReducer,
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
    form: 'PorEntregarForm',
    validate,
    enableReinitialize: true,
  }),
)(PorEntregarScreen);
