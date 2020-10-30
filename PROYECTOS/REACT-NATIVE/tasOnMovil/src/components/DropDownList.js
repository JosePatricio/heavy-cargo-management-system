import PropTypes from "prop-types";
import React, { Component } from "react";
import { TextInput, View, StyleSheet, Picker } from "react-native";
import {Text} from "../components/";
import ErrorHelper from '../components/InputTextMaterial/ErrorHelper'
const propTypes = {
  mapElement: PropTypes.func,
  onValueChange: PropTypes.func,
  value: PropTypes.string,
  placeholder: PropTypes.string,
  maxLength: PropTypes.number
};

const defaultProps = {
  mapElement: n => {},
  onValueChange: () => {},
  value: "",
  placeholder: "",
  borderLeftWidth: 4,
  borderRightWidth: 4
};

const styles = StyleSheet.create({
  TextViewStyle: {
    paddingVertical: 2,
 
    borderWidth: 2,
    borderRadius: 10,
    borderColor: "silver",
    backgroundColor: "#ffffff",
    paddingVertical: 2,
  },

  TextStyle: {
    padding: 5,
    marginRight: 10,
    color: "#909090",
    height: 41
  }
});

class DropDownList extends Component {
  state = {
    value: ""
  };

  componentDidMount() {

    this.setState({
      value: (this.props.selectedValue?Number(this.props.selectedValue): Number(this.props.value))
    });
  }

  onValueChange = value => {
    this.setState(
      {
        value
      },
      () => {
        this.props.onValueChange(value);
      }
    );
  };

  render() {
    const {
      placeholder,
      value,
      onValueChange,
      data,
      itemValue,
      itemLabel,itemLabel2,description,refField,error,
      errorColor,
      errorPaddingTop,
      errorFontSize
    } = this.props;
    

    let errorProps = {
      error,
      errorColor,
      errorPaddingTop,
      errorFontSize
    }

    let items = [];
   

   let data_=data?data:[];
      items = data_.map((s, i) => {
        let label1=this.props.itemLabel?s[this.props.itemLabel]:'', label2=this.props.itemLabel2?s[this.props.itemLabel2]:'';
        return (

          <Picker.Item
            key={i}
            value={s[this.props.itemValue]}
            label={label1+' '+label2}
            
          />
        );
      });
    

      
  return (
  <View style={{paddingBottom:18}}>
    {description && <Text style={{fontSize: 14, fontWeight: "normal" ,color: (error?'#fc1f4a':'#4A5F8E'),paddingBottom:5}}>{description}</Text>}
  <View style={styles.TextViewStyle}>
    
  <Picker
          style={styles.TextStyle}
          selectedValue={this.state.value}
          onValueChange={this.onValueChange}
          placeholder={placeholder}
          ref={refField}
          
        >
             <Picker.Item key={'unselectable'} value={null} label={placeholder}/>
          {items}
        </Picker>
      </View>
      {error ? <ErrorHelper {...errorProps} /> : null}  
      </View>
    );
  }
}

DropDownList.defaultProps = defaultProps;

DropDownList.propTypes = propTypes;

export default DropDownList;
