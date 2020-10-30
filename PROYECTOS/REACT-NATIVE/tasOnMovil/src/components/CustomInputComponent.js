import PropTypes from "prop-types";
import React, { Component, Fragment } from "react";
import { View, StyleSheet, Platform, TextInput as TextoCampo } from "react-native";
import { connect } from "react-redux";
import { compose } from "redux";
import { Field, reduxForm, reset, change } from "redux-form";
import TextInput from "./InputTextMaterial/Input";
import {Icon} from "native-base"
import {style} from "../styles/index"

const styles = StyleSheet.create({
  container: {
    marginTop: Platform.select({ ios: 8, android: 0 }),
    flex: 1,
  /*   paddingRight: 50,
    paddingLeft: 50 */
   },


  icon: {
    position: "absolute",
    top: 20,
    right: 0,
    paddingRight: 5,
    color:'#9E9E9E'
  }
});

const propTypes = {
  mapElement: PropTypes.func,
  onSubmitEditing: PropTypes.func,
  onChangeText: PropTypes.func,
  value: PropTypes.string,
  placeholder: PropTypes.string,
  maxLength: PropTypes.number,
  keyboardType: PropTypes.string,
  secureTextEntry: PropTypes.bool,
  label: PropTypes.string,
};

const defaultProps = {
  mapElement: n => {},
  onSubmitEditing: () => {},
  onChangeText: () => {},
  value: "",
  placeholder: "",
  keyboardType: "default",
  multiline: false,
  label: ""
};


class MaterialInputText extends Component {
  state = {
    value: "",
    hidePassword: true,
    icEye: "visibility-off"
  };

  changePwdType = () => {
    if (this.state.hidePassword) {
      this.setState({
        icEye: "visibility",
        hidePassword: false,
        password: this.state.password
      });
    } else {
      this.setState({
        icEye: "visibility-off",
        hidePassword: true,
        password: this.state.password
      });
    }
  };
  onChangeText = value => {
    this.setState(
      {
        value
      },
      () => {
        this.props.input.onChange(value);
      }
    );
  };
  componentDidMount() {
    this.setState({
      value: this.props.value
    });
  }

  render() {
    const {
      placeholder,securefield,multiline,keyboardType,fontSize,maxLength,
      input: {  value },input,
      meta: { invalid, touched, error },
      refField,editable,
      onSubmitEditing,autoFocus,hideLabel,onEndEditing,onBlur,onKeyPress,onChangeText,autoCapitalize,
      onChange
    } = this.props;
    const defaultAutoCapitalize=(!autoCapitalize?'none':autoCapitalize);

    return (
      <View style={styles.container}>
        <TextInput
          //placeholderColor={'green'}
          onKeyPress={onKeyPress}
          autoFocus={autoFocus}
          //value={ value?this.state.value:value.toString()  }
          value={value.toString()}
          maxLength={maxLength}
          onChangeText={this.onChangeText} 
          labelColor="gray"
          underlineActiveColor="#4A5E8A"
          labelActiveColor="#4A5E8A"
          label={!hideLabel?placeholder:undefined}
          placeholder={placeholder}
          keyboardType={keyboardType}
          fontSize={fontSize}
          ref={refField}
          onSubmitEditing={onSubmitEditing}
          autoCapitalize={defaultAutoCapitalize}
          securefield={ securefield==true?this.state.hidePassword:false }
         
          error={ (touched && error)? error:null}
        
          errorColor={'#FC3A5F'}
          errorFontSize={12}
        
          multiline={multiline}
          editable={editable}
          onEndEditing={onEndEditing}
          fontFamily={'Montserrat-Regular'}
          onChange={onChange}
        /> 
   {securefield && <Icon color="#9E9E9E" onPress={this.changePwdType} fontSize={30} style={styles.icon} type="MaterialIcons" name={this.state.icEye}/>} 

        
      </View>
    );
  }
}

MaterialInputText.defaultProps = defaultProps;

MaterialInputText.propTypes = propTypes;

export default MaterialInputText;