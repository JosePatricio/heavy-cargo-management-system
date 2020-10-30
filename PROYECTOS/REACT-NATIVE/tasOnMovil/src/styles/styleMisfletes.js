import { StyleSheet } from 'react-native';

export default StyleSheet.create({
  container: {
   /*  backgroundColor: "white",
      flex: 1,
      flexDirection: "column",
      padding: 3, */
      flex: 1,
      backgroundColor: "#FFFFFF"
  },
  tabContent: {
    color: '#444444',
    fontSize: 18,
    margin: 14,
  },
  Seperator: {
    marginHorizontal: -10,
    alignSelf: 'stretch',
    borderTopWidth: 1,
    borderTopColor: '#888888',
    marginTop: 24,
  },
  menuScrollStyle:{
    paddingTop: 5,
    height: '10%',
    backgroundColor: 'transparent'
  },
  tabStyle: {
    borderColor: '#D52C43',
  },
  activeTabStyle: {
    backgroundColor: '#D52C43',
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
  lengthInfoText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "normal",
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
  cityText: {
    color: "#4A5F8E",
    fontSize: 15,
    fontWeight: "bold",
    textAlign:'center'
  },
  dateText: {
    color: "#4A5F8E",
    fontSize: 10,
    fontWeight: "normal",
    textAlign:'center'// added
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
  },
  descriptionText: {
    color: "#4A5F8E",
    fontSize: 10,
    fontWeight: "normal"
  },
  informationText: {
    color: "#4A5F8E",
    fontSize: 12,
    fontWeight: "bold"
  },

facturarButton: {
  width: '45%',
  backgroundColor: '#03AADE',
  marginTop: 10,
  justifyContent: 'center',
  height: 40,
  marginBottom: 15,

},
facturacionButtonArea: {
  justifyContent: 'space-between',
  flexDirection: 'row',
  paddingBottom: 35
},
facturacionButtonText: {
  fontWeight: 'bold',
  color: 'white',
}
  });

