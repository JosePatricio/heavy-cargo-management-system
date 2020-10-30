import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
export default class SettingsScreen extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      images: [
        //require('../images/galery1.png'),
        //require('../images/galery2.png'),
        //require('../images/galery3.png')
      ]
    };
  }

  render() {
    return (
      <View style={styles.container}>
       
        <Text style={styles.text}>Home</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
  /*   flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center', */
  },
  text: {
    fontSize: 30,
  }
});
