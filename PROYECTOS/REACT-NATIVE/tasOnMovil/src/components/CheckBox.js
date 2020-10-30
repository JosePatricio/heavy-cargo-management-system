import PropTypes from "prop-types";
import React, { Component } from "react";
import {View, StyleSheet, CheckBox } from "react-native";

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
    paddingVertical: 2,
   // width : 250
  },

  TextStyle: {
    padding: 5,
    marginLeft: 10,
    marginRight: 10,
    color: "#4A5F8E",
    height: 41,
    backgroundColor: "#ffffff",
    borderRadius: 5,
    shadowRadius: 2
  }
});

class CheckBoxField extends Component {
  state = {
    value: false
  };

  
  componentDidMount() {
    console.log('this.props  ',this.props.checked);
    this.setState({
      value: this.props.checked
    });
  }

  onChange = value => {
    this.setState(
      {
        value
      },
      () => {
        this.props.onChange(value);
      }
    );
  };

  render() {
    const {
      onChange,
    } = this.props;
    return (
      <View style={styles.TextViewStyle}>
        <CheckBox onValueChange={this.onChange}  value={!!this.state.value}/>
      </View>
    );
  }
}

CheckBoxField.defaultProps = defaultProps;

CheckBoxField.propTypes = propTypes;

export default CheckBoxField;
