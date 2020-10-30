import React, { Component } from "react";

import { Icon } from 'native-base';
class Icono extends Component {

  componentDidMount() {

  }

  render() {
    const {
      focused,
      title,
      customIcon,
      iconReference,
      customColor,
      customSize
    } = this.props;


    let iconStyle = {fontSize: 25};
    if (!customColor) {
      iconStyle = { color: focused ? "#0E4EF8" : "#17233D" };
    } else {
      iconStyle =  { color: customColor };
    }

        if (!customSize) {
          iconStyle={fontSize: customSize}
        } else{
          iconStyle={fontSize: 25}
        }
        return <Icon type={iconReference} name={customIcon} style={iconStyle}/>;
  }
}

export default Icono;
