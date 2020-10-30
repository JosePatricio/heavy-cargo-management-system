import PropTypes from "prop-types";
import React, { Component } from "react";
import {View, StyleSheet ,Text} from "react-native";
import {Radio} from 'native-base'
import {defaultTextFamily} from '../styles/index'
//import {Text} from '../components/Text'
import { RadioButton } from 'react-native-paper';

const propTypes = {
  mapElement: PropTypes.func,
  onValueChange: PropTypes.func,
  value: PropTypes.bool,
  placeholder: PropTypes.string,
  maxLength: PropTypes.number
};

const defaultProps = {
  mapElement: n => {},
  onValueChange: () => {},
  value: false,

  borderLeftWidth: 4,
  borderRightWidth: 4
};

const styles = StyleSheet.create({
  TextViewStyle: {
   flexDirection:'row',
  },
  TextStyle: {
      fontFamily:defaultTextFamily,
    paddingTop:10
   }
});

class RadioButon extends Component {
  

  componentDidMount() {
  }


  onPress (onChange, val, changeState) {
    onChange(val)
    changeState()
  }


  
  render() {
    const {
      value,status,onPress,label
    } = this.props;
  
    return (
      <View style={styles.TextViewStyle}>
        <Text style={styles.TextStyle}>{label}</Text>
        <RadioButton 
         value={value}
         status={status}
         onPress={onPress}
         color={'#03AADE'}
         
          />

      </View>
    );
  }
}

RadioButon.defaultProps = defaultProps;

RadioButon.propTypes = propTypes;

export default RadioButon;
