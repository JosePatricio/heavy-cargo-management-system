import { StyleSheet } from "react-native";

export default StyleSheet.create({
  container: {
    flex: 1,
   // marginTop: 5,
    backgroundColor: "#FFFFFF"
  },
  item: {
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16
  },
  title: {
    fontSize: 15
  },
  cardStyle:{
    margin: 5,
    marginLeft:20,
    marginRight: 20 
  },
  noPadding:{
    paddingBottom:0,paddingTop:0,paddingRight: 0, paddingLeft: 0
  },
  
  button: {
    alignItems: "center",
    backgroundColor: "#03AADE",
    padding: 0,
    justifyContent:'center',
    height: 25,
    alignSelf:'stretch'
  },
  button_1: {
    flex:1,flexDirection: 'row', alignItems:'flex-end',justifyContent: 'flex-end'
  },

  cityText: {
    color: "#4A5F8E",
    fontSize: 15,
    fontWeight: "bold",
    textAlign:'center'// added
  },
  dateText: {
    color: "#4A5F8E",
    fontSize: 10,
    fontWeight: "normal",

    textAlign:'center'// added
  },
  descriptionText: {
    color: "#4A5F8E",
    fontSize: 10,
    fontWeight: "normal"
  },
  informationText: {
    color: "#4A5F8E",
    fontSize: 12,
    fontWeight: "bold",
    textAlign:'center'
  },
  lengthInfoText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "normal",
  },

  viewInfo: {
    flex: 1,
    margin: 10,
    flexDirection: "column",
    justifyContent: "flex-start"
  },
  viewInfo1: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "flex-start",
    paddingTop : 5,
    paddingBottom : 5
  },
  viewInfo1_1: {
    width: "30%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  },
  viewInfo1_2: {
    width: "7%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  },
  viewInfo1_3: {
    width: "30%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  },
  viewInfo1_4: {
    width: "33%",
    height: "100%",
    alignItems: "center",
    justifyContent: "center"
  }
});
