import React, { Component } from "react";
import {defaultTextFamily} from "../styles/index"
import {Text} from "react-native";

export default props => <Text {...props} style={[{fontFamily: defaultTextFamily}, props.style]}>{props.children}</Text>