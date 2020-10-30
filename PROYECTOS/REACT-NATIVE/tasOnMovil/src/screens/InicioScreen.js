import { Container, Content } from 'native-base';
import React, { Component } from 'react';
import { Dimensions, StyleSheet, View } from 'react-native';
import { SliderBox } from "react-native-image-slider-box";
import { connect } from "react-redux";

const deviceHeigth=Dimensions.get('window').height;
const styles = StyleSheet.create({
  container : {
    backgroundColor:'white',
    flex: 1,
    alignItems:'center',
    justifyContent :'center'
  },
  textStyle: {
      color: "black",
      fontSize: 18
  },
  button: {
    width:300,
    backgroundColor:'#1c313a',
    borderRadius: 25,
    marginVertical: 10,
    paddingVertical: 13
  },
  buttonText: {
    fontSize:16,
    fontWeight:'500',
    color:'#ffffff',
    textAlign:'center'
  },
});

class InicioScreen extends Component {

  constructor(props) {
    super(props);

    this.state = {
      images: [
        //require('../images/galery1.png'),
        //require('../images/galery2.png'),
        //('../images/galery3.png')
      ]
    };
  }


	render() {

    
   	return(
			<Container >
        <Content >
<View style={{flex:1}}>
<SliderBox  images={this.state.images} sliderBoxHeight={deviceHeigth}
dotColor="#FFEE58"
inactiveDotColor="#90A4AE"   autoplay
circleLoop/>
</View>
         </Content>
			</Container>
			)
	}
}

const mapStateToProps = (state) => {
  return {
    getUser: state.userReducer.getUser,
    generalReducer: state.generalReducer,

  };
};


export default connect(mapStateToProps, null)(InicioScreen);
