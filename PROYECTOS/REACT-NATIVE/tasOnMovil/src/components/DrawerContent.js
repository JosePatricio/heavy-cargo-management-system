import React, { Fragment } from "react";
import { View, Text, StyleSheet,TouchableOpacity } from "react-native";
import { Actions } from "react-native-router-flux";

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#03AADE"
  },
  drawerText: {
    color: "#fff"
  },
  topDrawer: {
    flex: 1,
    backgroundColor: "#03AADE",
    justifyContent: "center",
    alignContent: "center",
    alignItems: "center"
  },
  bottomDrawer: {
    flex: 2,
    backgroundColor: "#fff",
    paddingHorizontal: 15,
    paddingVertical: 15
  }
});

export default class SettingsScreen extends React.Component {
  componentDidMount() {
 
    Actions.refresh({ key: "drawer", ref: this.refs.navigation });
  }

  render() {
    return (
      <Fragment>
        <View style={styles.container}>
          <View style={styles.topDrawer}>
            <Text style={styles.drawerText}>User</Text>
          </View>
          <View style={styles.bottomDrawer}>
            <Text>Home</Text>
          
            <TouchableOpacity onPress={Actions.profile}>
                    <Text >Perfil</Text>
                </TouchableOpacity>

          </View>
        </View>
      </Fragment>
    );
  }
}
