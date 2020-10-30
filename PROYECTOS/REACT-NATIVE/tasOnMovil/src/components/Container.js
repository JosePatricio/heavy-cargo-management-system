import React, { Component } from "react";
import {View,SafeAreaView,ScrollView,Dimensions,StyleSheet,StatusBar} from "react-native";
const { height } = Dimensions.get('window');
export default class Container extends Component {
 
  state = {
    screenHeight: height,
  };

  onContentSizeChange = (contentWidth, contentHeight) => {
    this.setState({ screenHeight: contentHeight });
  };

  render() {
    const scrollEnabled = this.state.screenHeight > height;
    return (
      <SafeAreaView style={styles.container}>
      {/*   <StatusBar barStyle="light-content" backgroundColor="#468189" />
       */}  <ScrollView
          style={{ flex: 1 }}
          contentContainerStyle={styles.scrollview}
          scrollEnabled={scrollEnabled}
          onContentSizeChange={this.onContentSizeChange}
        >
          <View style={styles.content}>
            {this.props.children}
          </View>
        </ScrollView>
      </SafeAreaView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#85D4E7",
  },
  scrollview: {
    flexGrow: 1,
  },
  content: {
    flexGrow: 1,
    backgroundColor: "white",
    flex: 1,
    flexDirection: "column"
  },
});