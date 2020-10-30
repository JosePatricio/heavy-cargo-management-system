import React, { Component } from "react";
import {View} from "react-native";
class Line extends Component {
 
  componentDidMount() {
    this.setState({
      value: this.props.value
    });
  }


  render() {
  const {
     color,size
    } = this.props;
  
    let defaultColor='red';
    if(color){
      defaultColor=color;
  }

  
    let style={borderBottomColor: defaultColor, borderBottomWidth: 2};
    
    if(size){
      style={borderBottomColor: defaultColor, borderBottomWidth: size};
    }

 return <View style={{borderBottomColor:'gray', borderBottomWidth: 0}}/>
  }
}

export default Line;
