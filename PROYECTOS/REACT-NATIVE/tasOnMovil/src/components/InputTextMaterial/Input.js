import React, { Component } from 'react'
import { View, TextInput, Platform ,StyleSheet} from 'react-native'
import PropTypes from 'prop-types'
import Label from './Label'
import Placeholder from './Placeholder'
import Underline from './Underline'
import ErrorHelper from './ErrorHelper'
import {Icon} from "native-base"


const styles = StyleSheet.create({
  container: {
    marginTop: Platform.select({ ios: 8, android: 0 }),
    flex: 1
   },


  icon: {
    position: "absolute",
    top: 20,
    right: 0,
    paddingRight: 5,
    color:'#9E9E9E'
  }
});


export default class extends Component {
  static propTypes = {
    ...TextInput.PropTypes,
    ...ErrorHelper.PropTypes,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    onChangeText: PropTypes.func,
    onContentSizeChange: PropTypes.func,
    minHeight: PropTypes.number,
    height: PropTypes.number,
    maxHeight: PropTypes.number,
    marginTop: PropTypes.number,
    marginRight: PropTypes.number,
    marginBottom: PropTypes.number,
    marginLeft: PropTypes.number,
    paddingTop: PropTypes.number,
    paddingRight: PropTypes.number,
    paddingBottom: PropTypes.number,
    paddingLeft: PropTypes.number,
    color: PropTypes.string,
    activeColor: PropTypes.string,
    fontFamily: PropTypes.string,
    fontSize: PropTypes.number,
    fontWeight: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
  }

  static defaultProps = {
    ...ErrorHelper.defaultProps,
    onFocus: () => {},
    onBlur: () => {},
    onChangeText: () => {},
    onContentSizeChange: () => {},
    onSubmitEditing: () => {},
    value: null,
    marginBottom: 8,
    paddingTop: 20,
    paddingRight: 0,
    paddingBottom: 8,
    paddingLeft: 0,
    color: 'black',
    fontSize: 15,
    fontWeight: 'normal'
  }

  constructor(props) {
    super(props)

    this.state = {
      value: '',
      focused: false,
      height: props.fontSize * 1.5,
      hidePassword: true,
      icEye: "visibility-off"
    }
  }


 
  
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
    console.log('onchange  ',value);
    this.setState(
      {
        value
      },
      () => {
        this.props.input.onChange(value);
      }
    );
  };

  render() {
    let { focused, height } = this.state
    let value = this.props.value != null ? this.props.value : this.state.value
    let hasValue = value && value.length > 0
    let active = focused || hasValue
    let {
      minHeight,
      maxHeight,
      marginTop,
      marginRight,
      marginBottom,
      marginLeft,
      paddingTop,
      paddingRight,
      paddingBottom,
      paddingLeft,
      color,
      activeColor,
      fontFamily,
      fontSize,
      fontWeight,
      label,
      labelDuration,
      labelColor,
      labelActiveTop,
      labelActiveColor,
      labelActiveScale,
      placeholder,
      placeholderColor,
      underlineDuration,
      underlineHeight,
      underlineColor,
      underlineActiveColor,
      underlineActiveHeight,
      error,
      errorColor,
      errorPaddingTop,
      errorFontSize,
      onSubmitEditing,onChangeText,onEndEditing,
      refField,
      hidden,
      onBlur,
      defaultValue,onChange,
      ...props
    } = this.props
    let labelProps = {
      paddingTop,
      paddingRight,
      paddingBottom,
      paddingLeft,
      activeColor,
      fontFamily,
      fontSize,
      fontWeight,
      label,
      labelDuration,
      labelColor,
      labelActiveTop,
      labelActiveColor,
      labelActiveScale,
      focused,
      hasValue,
      error,
      errorColor
    }
    let placeholderProps = {
      paddingTop,
      paddingRight,
      paddingBottom,
      paddingLeft,
      fontFamily,
      fontSize,
      fontWeight,
      placeholder,
      placeholderColor,
      focused,
      hasValue
    }
    let underlineProps = {
      activeColor,
      underlineDuration,
      underlineHeight,
      underlineColor,
      underlineActiveColor,
      underlineActiveHeight,
      focused,
      error,
      errorColor
    }
    let containerStyle = {
      marginTop,
      marginRight,
      marginBottom,
      marginLeft
    }
    if (props.multiline && props.height) {
      // Disable autogrow if height prop
      height = props.height
    }
    let inputStyle = {
      minHeight,
      maxHeight,
      paddingTop,
      paddingRight,
      paddingBottom,
      paddingLeft,
      color,
      fontFamily,
      fontSize,
      fontWeight,
      ...Platform.select({
        ios: { height: paddingTop + paddingBottom + (props.multiline ? height : fontSize * 1.5) },
        android: {
          height: props.multiline ? height : fontSize * 1.5 + paddingTop + paddingBottom,
          textAlignVertical: 'top'
        }
      })
    }
    let errorProps = {
      error,
      errorColor,
      errorPaddingTop,
      errorFontSize
    }
    const {securefield,keyboardType,maxLength,autoCapitalize} = this.props;
    const defaultAutoCapitalize=(!autoCapitalize?'none':autoCapitalize);

   
    return (
      <View style={[containerStyle,{display:(hidden === true ? 'none':'flex')}]}>
        <Label {...labelProps} />
     {defaultValue?undefined:(placeholder ? <Placeholder {...placeholderProps} /> : null)}   
     
   
        <TextInput
          {...props}
          style={inputStyle}
          underlineColorAndroid="transparent"
          onFocus={this._handleFocus}
          onBlur={onBlur}
          onChangeText={onChangeText} 
          onChange={onChange} 
          onContentSizeChange={this._handleContentSizeChange}
          onSubmitEditing={onSubmitEditing}
          onEndEditing={onEndEditing}
          value={value}
          defaultValue={defaultValue}
          secureTextEntry={ securefield==true?this.state.hidePassword:false }
          returnKeyType="next"
          ref={refField}
          placeholder={defaultValue?undefined:placeholder}
          autoCapitalize={defaultAutoCapitalize}
          
        />
        <Underline {...underlineProps} />
         {error ? <ErrorHelper {...errorProps} /> : null} 

        {securefield && <Icon color="#9E9E9E" onPress={this.changePwdType} fontSize={30} style={styles.icon} type="MaterialIcons" name={this.state.icEye}/>} 

      </View>
    )
  }

  _handleFocus = (...args) => {
    let { onFocus } = this.props
    this.setState({ focused: true })
    onFocus(...args)
  }

  _handleBlur = (...args) => {
    let { onBlur } = this.props
    this.setState({ focused: false })
    onBlur(...args)
  }

  
  

  _handleContentSizeChange = event => {
    let { onContentSizeChange, fontSize } = this.props
    let { height } = event.nativeEvent.contentSize

    this.setState({
      height: Math.max(fontSize * 1.5, Math.ceil(height))
    })

    onContentSizeChange(event)
  }
}
