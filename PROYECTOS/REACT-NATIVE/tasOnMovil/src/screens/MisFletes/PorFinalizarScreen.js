import { Icon } from 'native-base';
import React, { Component } from "react";
import { KeyboardAvoidingView, ScrollView, View } from "react-native";
import { Actions } from "react-native-router-flux";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, reduxForm } from "redux-form";
import { getSolicitudById } from "../../actions/solicitud.action";
import { AwesomeButtonRick, Icono, InputText, Line, Loader, SolicitudItem, Text } from "../../components/";
import { defaultTasonColor, style, styleSolicitudOferta as styles } from '../../styles/';


class AprobadasScreen extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: props.usuarioService.getUser.userDetails,
      isLoading: false
    };

    console.log("PROPSSS ", this.props);
  }

  componentDidMount() {
    this.loadData();
  }

  loadData = async values => {
    try {
      const response = await this.props.dispatch(
        getSolicitudById(this.props.solicitud.idSolicitud, this.state.user)
      );

      if (!response.success) {
        throw response;
      }
    } catch (error) {
      console.log("ERROR SCREEN loadData   ", error);
    }
  };

  renderTextInput = field => {
    const {
      meta: { touched, error },
      label,
      secureTextEntry,
      maxLength,
      keyboardType,
      placeholder,
      onSubmitEditing,
      multiline,
      color,
      fontSize,
      fontWeight,
      input: { onChange, ...restInput }
    } = field;
    return (
      <View style={{ padding: 5, width: "40%" }}>
        <InputText
          onChangeText={onChange}
          maxLength={maxLength}
          placeholder={placeholder}
          onSubmitEditing={onSubmitEditing}
          keyboardType={keyboardType}
          secureTextEntry={secureTextEntry}
          label={label}
          multiline={multiline}
          color={color}
          fontSize={fontSize}
          fontWeight={fontWeight}
          {...restInput}
        />
        {touched && error && <Text style={styles.errorText}>{error} </Text>}
      </View>
    );
  };

  renderTextInputArea = field => {
    const {
      meta: { touched, error },
      label,
      secureTextEntry,
      maxLength,
      keyboardType,
      placeholder,
      onSubmitEditing,
      input: { onChange, ...restInput }
    } = field;
    return (
      <View style={{ padding: 5, height: 90 }}>
        <InputText
          onChangeText={onChange}
          maxLength={maxLength}
          placeholder={placeholder}
          onSubmitEditing={onSubmitEditing}
          keyboardType={keyboardType}
          secureTextEntry={secureTextEntry}
          label={label}
          multiline={true}
          {...restInput}
        />
        {touched && error && <Text style={styles.errorText}>{error}</Text>}
      </View>
    );
  };

 

  onCancel = () => {
  Actions.pop();
  };


  render() {
    const {
      solicitudService: {
        getByIdReducer: { data }
      }
    } = this.props;

    const { handleSubmit } = this.props;

    this.state.solicitud = data;
  
    if (this.state.isLoading) {
      return <Loader />;
    } else {
      return (
        <ScrollView>
          <View style={styles.container}>
            <View style={styles.viewInfo1}>
              <View style={{ alignItems: "center", padding: 5 }}>
                <Text
                  style={{
                    color: "#03AADE",
                    fontWeight: "bold",
                    fontSize: 16,
                    paddingBottom: 15
                  }}
                >
                  {this.state.solicitud.idSolicitud}
                </Text>
              </View>
              <View style={styles.viewInfo2_2}>
                <View style={styles.viewData}>
                  <Text style={styles.textInfo}> N° Ofertas </Text>
                  <Text style={styles.textData}>
                    {this.state.solicitud.totalOffer}
                  </Text>
                </View>
                <View style={styles.viewData}>
                  <Text style={styles.textInfo}> Mejor Oferta </Text>
                  <Text style={styles.textData}>
                    {this.state.solicitud.bestOffer
                      ? this.state.solicitud.bestOffer
                      : 0}
                  </Text>
                </View>
                <View style={styles.viewData}>
                  <Text style={styles.textInfo}> Promedio </Text>
                  <Text style={styles.textData}>
                    {this.state.solicitud.averageOffer
                      ? this.state.solicitud.averageOffer
                      : 0}
                  </Text>
                </View>
              </View>
            </View>

            <View style={styles.viewInfo1}>
              <SolicitudItem item={this.state.solicitud} />
            </View>

            <View style={styles.viewInfo1}>
              <View style={(styles.viewInfo, styles.containerStyle)}>
                <View style={styles.viewInfo3_1}>
                  <View style={{ padding: 5, width: "50%" }}>
                    <Text style={styles.textInfo4}>Producto</Text>
                    <Text style={styles.textData4}>
                      {this.state.solicitud.tipo}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                  <View style={{ padding: 5, width: "50%" }}>
                    <Text style={styles.textInfo4}>Número de piezas</Text>
                    <Text style={styles.textData4}>
                      {this.state.solicitud.numeroPiezas}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>

                <View style={styles.viewInfo3_1}>
                  <View style={{ padding: 5, width: "50%" }}>
                    <Text style={styles.textInfo4}>Volumen Total</Text>
                    <Text style={styles.textData4}>
                      {this.state.solicitud.volumen}  {this.state.solicitud.tipoVolumen}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                  <View style={{ padding: 5, width: "50%" }}>
                    <Text style={styles.textInfo4}>Peso</Text>
                    <Text style={styles.textData4}>
                    {this.state.solicitud.peso} {this.state.solicitud.tipoPeso}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>

                <View style={styles.viewInfo3_1}>
                <View style={{ padding: 5, width: "50%" }}>
                    <Text style={styles.textInfo4}>Días de pago</Text>
                    <Text style={styles.textData4}>
                         {this.state.solicitud.diasPago}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                  <View style={{ padding: 5, width: "50%" }}>
                    <Text style={styles.textInfo4}>Estibadores</Text>
                    <Text style={styles.textData4}>
                      {this.state.solicitud.numeroEstibadores}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                </View>

            
                <View style={styles.viewInfo3_1}>
                <View style={{ padding: 5, width: "100%" }}>
                    <Text style={styles.textInfo4}>Observación</Text>
                    <Text style={styles.textData4}>
                      {this.state.solicitud.observaciones}
                    </Text>
                    <Line color="#4A5F8E" size={1} />
                  </View>
                  </View>
             
              </View>
            </View>

            <Line color="#4A5F8E" />

            <View style={styles.viewInfo4}>
              <Text style={styles.textInfo5}> Mi Oferta </Text>
            </View>

            <KeyboardAvoidingView
              behavior="padding"
              style={{ flexGrow: 1 }}
              keyboardVerticalOffset={10}
            >
              <View style={styles.viewInfo5}>
                <Icono customIcon="dollar" iconReference="FontAwesome" />
                <Field
                  name="oferta"
                  component={this.renderTextInput}
                  keyboardType={"numeric"}
                  color="#4A5F8E"
                  fontSize={29}
                  fontWeight="bold"
                />
                <Icono customIcon="edit" iconReference="Entypo" />
              </View>
              <Line color="#4A5F8E" size={1} />
              <View style={styles.viewInfo6}>
                <Text>Comentario</Text>
                <Field
                  name="comentario"
                  placeholder=""
                  component={this.renderTextInputArea}
                />
              </View>

              <Line color="#4A5F8E" size={1} />

            
              <View style={styles.viewInfo7}>
             

              <AwesomeButtonRick height={50} stretch={true} style={style.defaultButton} onPress={()=>this.onCancel()} backgroundColor={defaultTasonColor}>
              <Icon name='left' type='AntDesign' style={style.defaultIconButton}/>
                <Text style={style.defaultTextButton}>Regresar</Text>
                </AwesomeButtonRick>

              </View>
            </KeyboardAvoidingView>
          </View>
        </ScrollView>
      );
    }
  }
}

const validate = values => {
  const errors = {};
  if (!values.oferta) {
    errors.oferta = "Su oferta es requerido";
  }
  return errors;
};

const mapStateToProps = state => {
  return {
    solicitudService: state.solicitudReducer,
    usuarioService: state.userReducer
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
    form: "solicitudOferta",
    validate
  })
)(AprobadasScreen);
