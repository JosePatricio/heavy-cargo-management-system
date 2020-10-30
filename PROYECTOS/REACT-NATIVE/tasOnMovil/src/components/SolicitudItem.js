import React, { Component } from "react";
import { View, FlatList, TouchableHighlight,StyleSheet } from "react-native";
import Moment from "react-moment";
import Icono from "./Icon";
import {Text} from "../components/";

const styles= StyleSheet.create({
  container: {
    flex: 1,
    marginTop: 5,
    backgroundColor: "#FFFFFF"
  },
  item: {
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16
  },
  title: {
    fontSize: 15
  },
  containerStyle: {
    backgroundColor: "white",
    borderWidth: 1,
    borderRadius: 2,
    borderColor: "#ddd",
    borderBottomWidth: 0,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 1,
    marginLeft: 10,
    marginRight: 10
  },
  button: {
    alignItems: "center",
    backgroundColor: "#ffffff",
    padding: 5
  },
  button_1: {
    flex:1,flexDirection: 'row', alignItems:'flex-end',justifyContent: 'flex-end'
  },

  cityText: {
    color: "#4A5F8E",
    fontSize: 15,
    fontWeight: "bold",
    textAlign:'center'// added
  },
  dateText: {
    color: "#4A5F8E",
    fontSize: 10,
    fontWeight: "normal",
    textAlign:'center'// added
  },
  descriptionText: {
    color: "#4A5F8E",
    fontSize: 10,
    fontWeight: "normal"
  },
  informationText: {
    color: "#4A5F8E",
    fontSize: 12,
    fontWeight: "bold"
  },
  lengthInfoText: {
    color: "#4A5F8E",
    fontSize: 16,
    fontWeight: "normal"
  },
  textInfo: {
    color: "#4A5F8E",
    fontWeight: "normal",
    fontSize: 14,
    paddingBottom: 5,
    paddingTop: 5,
    paddingLeft: 15
  },
  textTipo: {
    color: "#4A5F8E",
    //fontWeight: "bold",
    fontSize: 14,
    paddingRight: 15
  },
  viewInfo: {
/*     flex: 1,
    flexDirection: "column",
    justifyContent: "flex-start" */
  },
  viewInfo1: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "flex-start"
  },
  viewInfo1_1: {
    width: "46%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  },
  viewInfo1_2: {
    width: "8%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  },
  viewInfo1_3: {
    width: "46%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  },

});

class SolicitudItem extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {}

 
  render() {
    const { item }=this.props;
      return (
        <View>
              <View style={styles.viewInfo1}>
                <View style={styles.viewInfo1_1}>
                  <Text style={styles.cityText}>{item.origen}</Text>
                  <Text style={styles.dateText}>
                    <Moment element={Text} format="DD/MM/YYYY hh:mm">
                      {item.fechaEntrega}
                    </Moment>
                  </Text>
                </View>

                <View style={styles.viewInfo1_2}>
                  <Icono customIcon="arrowright" iconReference="AntDesign" customSize={20}/>
                </View>

                <View style={styles.viewInfo1_3}>
                  <Text style={styles.cityText}>{item.destino}</Text>
                  <Text style={styles.dateText}><Moment element={Text} format="DD/MM/YYYY hh:mm">{item.fechaRecepcion}</Moment></Text>
                </View>

              </View>
           
             <View style={{marginTop:15}}>
             <Text style={styles.textInfo}> Envía:  <Text style={styles.textTipo}> {item.personaEntrega} </Text> </Text>
              <Text style={styles.textInfo}> Dirección:  <Text style={styles.textTipo}> {item.direccion} </Text> </Text>
              <Text style={styles.textInfo}> Dirección de entrega:  <Text style={styles.textTipo}> {item.direccionEntrega} </Text> </Text>
           
             </View>
            </View> 
        
      );
   
  }
}


export default SolicitudItem;
