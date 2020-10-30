import {StyleSheet} from 'react-native';

export default StyleSheet.create({
  alertContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#fff',
  },
  alertConfirmButtonErrorTextStyle: {
    color: '#FF7F00',
    fontWeight: 'bold',
  },
  alertConfirmButtonTextStyle: {
    color: '#77C3BB',
    fontWeight: 'bold',
  },
  alertContentContainerStyle: {
    alignItems: 'flex-end',
  },
  alertConfirmButtonStyle: {
    backgroundColor: 'transparent',
  },
  alertTitleStyle: {
    fontWeight: 'bold',
    alignSelf: 'baseline',
    paddingLeft: 0,
    paddingRight: 0,
  },
  errorText: {
    fontWeight: '700',
    color: '#700',
    paddingLeft: 60,
    marginBottom: 15,
  },
  modalStyle: {
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 4,
    borderColor: 'rgba(0, 0, 0, 0.1)',
    margin: 0,
  },
  LinkButton:{
    alignItems : 'center',
    paddingTop:15,
    paddingBottom: 10
  },
  linkTextButton: {
    color: "#03AADE",
    fontSize: 13,
    fontWeight: "500"
  },
  actionBarText: {
    color: "#03AADE",
    fontSize: 13,
    fontWeight: "500",
   // marginLeft:5,
    textAlign: 'center',
  },
  cardStyle: {
    margin: 5,
    marginLeft: 20,
    marginRight: 20,
  },
  textInfo: {
    fontWeight: 'bold',
    color: '#4A5F8E',
    
  },
  textData: {
    color: '#4A5F8E',
    fontSize: 14,
  },
  imageViewerIcon:{
    backgroundColor: 'black',color:'white',padding:10
  },
  twoButtonsArea:{
    justifyContent: 'space-between',
    flexDirection: 'row',
    paddingBottom: 15,
    justifyContent:'center',
    paddingTop:10,
  
  },
  buttonInArea:{
    width: '50%',
    backgroundColor: '#03AADE',
    borderRadius: 25,
    marginTop: 15,
    alignItems: 'center',
    justifyContent: 'flex-start',
    height: 40,
  
  },
  
  collapseHeaderText: {
    color: '#4A5F8E',
    fontWeight: 'bold',
    fontSize: 14,
    alignSelf:'center'// added
  },
  collapseHeaderSeparator: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingRight: 15,
    alignItems: 'center',
  },
  successInfoText:{
    color: '#4A5F8E',textAlign: 'center',    fontWeight: 'bold',fontSize: 16,
  },
  errorInfoText:{
    color: '#DC3545',textAlign: 'center' ,    fontWeight: 'bold',fontSize: 16,
  }
  ,
  container: {
    backgroundColor: 'white',
    flex: 1,
    flexDirection: 'column',
   
  },
container2:{
  flex:1,
        flexDirection:'column',
        alignItems:'center',
        justifyContent:'center'
},
container3: {
  flex: 1,
  flexDirection: "column",
  justifyContent: "space-between",
  backgroundColor: "#ffffff"
},

  defaulFontFamily:{
    fontFamily: 'Montserrat-Regular'
  },
  defaultButton:{
    maxWidth: '50%',
    alignSelf:'center',
    marginTop:10,
    marginBottom:10
  },

  defaultTextButton:{
    fontSize: 16,
    fontWeight: "500",
    color: "#ffffff",
    textAlign :'center',
    marginTop: 0,
  },
  defaultIconButton:{
    fontSize: 15,paddingTop:5,color:'#ffff',
    padding:3
  }
,
  defaultButtonInArea1:{
    maxWidth: '45%', 
    marginRight:10
  },
  defaultButtonInArea2:{
    maxWidth: '45%', 
    marginLeft:10
  },
  
  errorText:{
    fontWeight: '600',
    fontSize:12,
    color: '#FC3A5F',
    paddingLeft: 5,
    marginBottom:15,
},
formView:{
  flex:1,paddingLeft:15,paddingRight:15
},
viewInfo: {
  paddingLeft: 15,
  paddingRight: 15,
  paddingBottom: 5,
  flexGrow: 1,
  flexDirection: 'row',
  width: '100%',
},
formTitle:{
  color: '#03AADE',
  fontWeight: 'bold',
  fontSize: 16,
  alignSelf:'center'
},
noPadding:{
  paddingBottom:0,paddingTop:0,paddingRight: 0, paddingLeft: 0
},
viewInfo1: {
  alignSelf:'stretch',
  flexDirection: "row",
  justifyContent: "space-between",
  paddingTop : 2,
  paddingBottom : 2,
  alignItems:'center'
},
flatContainer:{
  flex: 1,
  flexDirection: 'row',
  flexWrap: 'wrap',
  alignItems: 'flex-start'
},
flatContainerItem: {
  width: '50%', // is 50% of container width
  padding:5
},
viewHeader:{
    alignItems: "center",
    backgroundColor: "#03AADE",
    padding: 0,
    justifyContent:'center',
    height: 25,
    alignSelf:'stretch'
  },
 viewHeaderText:{
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "normal",
 },
 textInfoDisabled:{
color:'#4A5F8E',
fontFamily: "Montserrat-Regular",
 fontSize: 13, fontWeight: "normal" 

 },
 textDataDisabled:{
color:'#C2C2C2'
 } 
,formSection: {
  flex: 1 ,
  paddingRight: 25,
  paddingLeft: 25 
},
linkButtonOpacity:{
  alignItems : 'center',
  paddingTop:15,
  paddingBottom: 10
},
linkButtonText: {
  color: "#03AADE",
  fontSize: 15,
  fontWeight: "500"
},
});
