import React, {Component} from 'react';
import {StyleSheet, View} from 'react-native';
import Spinner from 'react-native-loading-spinner-overlay';
export default class Loader extends Component {
  render() {
    const {hideIndicator,visible} = this.props;
    if (hideIndicator) {
      return <View style={styles.container}></View>;
    }
    return <Spinner  visible={true} />;
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'rgba(0, 0, 0, 0.4)',
    position: 'absolute',
    width: '100%',
    height: '100%',
    zIndex: 99,
    justifyContent: 'center',
  },
});
