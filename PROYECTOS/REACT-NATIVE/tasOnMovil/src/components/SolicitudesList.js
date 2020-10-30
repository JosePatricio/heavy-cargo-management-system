import React, { Component } from "react";
import { View, FlatList,  TouchableHighlight } from "react-native";
import Moment from "react-moment";
import Icono from "../components/Icon";
import styles from "../styles/styleSolicitud";
import {Text} from "../components/";

class SolicitudesList extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {}

  setSelection(item) {
    this.props.onItemPress(item);
  }
 
  render() {
    if (this.props.data && this.props.data.length > 0) {
      return (
        <View style={styles.container}>
          <FlatList
            data={this.props.data}
            renderItem={({ item }) => (
              <View style={(styles.viewInfo, styles.containerStyle)}>
                <View style={{ alignItems: "center", padding: 10 }}>
                  <Text
                    style={{
                      color: "#03AADE",
                      fontWeight: "bold",
                      fontSize: 16
                    }}
                  >
                    {item.idSolicitud}
                  </Text>
                </View>

                <View style={styles.viewInfo1}>
                  <View style={(styles.viewInfo1_1, { width: "46%" })}>
                    <Text style={styles.cityText}>{item.origen}</Text>
                    <Text style={styles.dateText}>
                      <Moment element={Text} format="DD/MM/YYYY hh:mm">
                        {item.fechaEntrega}
                      </Moment>
                    </Text>
                  </View>

                  <View style={(styles.viewInfo1_2, { width: "8%" })}>
                    <Icono customIcon="arrowright" iconReference="AntDesign" />
                  </View>

                  <View style={(styles.viewInfo1_3, { width: "46%" })}>
                    <Text style={styles.cityText}>{item.destino}</Text>
                    <Text style={styles.dateText}>
                      <Moment element={Text} format="DD/MM/YYYY hh:mm">
                        {item.fechaRecepcion}
                      </Moment>
                    </Text>
                  </View>
                </View>
                <TouchableHighlight
                  style={styles.button}
                  onPress={() => this.setSelection(item)}
                  key={item.idSolicitud}
                  underlayColor={"gray"}
                >
                  <View style={styles.button_1}>
                    <Text style={styles.lengthInfoText}>
                      {item.numeroPiezas} piezas
                    </Text>
                  </View>
                </TouchableHighlight>
              </View>
            )}
            keyExtractor={item => item.idSolicitud}
          />
        </View>
      );
    } else {
      return <Text></Text>;
    }
  }
}


export default SolicitudesList;