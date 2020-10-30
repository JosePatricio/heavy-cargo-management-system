import {StyleSheet,Dimensions} from 'react-native';

export default StyleSheet.create({
  container: {
    backgroundColor: 'white',
    flex: 1,
    flexDirection: 'column',
  },

  containerStyle: {
    backgroundColor: 'white',
    borderWidth: 1,
    borderRadius: 2,
    borderColor: '#ddd',
    borderBottomWidth: 0,
    shadowColor: '#000',
    shadowOffset: {width: 0, height: 2},
    shadowOpacity: 0.8,
    shadowRadius: 2,
    elevation: 1,
    marginLeft: 10,
    marginRight: 10,
  },

  cardStyle: {
    margin: 5,
    marginLeft: 20,
    marginRight: 20,
  },

  cityText: {
    color: '#4A5F8E',
    fontSize: 15,
    fontWeight: 'bold',
    textAlign: 'center', // added
  },
  dateText: {
    color: '#4A5F8E',
    fontSize: 10,
    fontWeight: 'normal',
    textAlign: 'center', // added
  },

  viewInfo1_1: {
    width: '46%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
  viewInfo1_2: {
    width: '8%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
  viewInfo1_3: {
    width: '46%',
    height: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
  viewInfo: {
    flex: 1,
    margin: 10,
    flexDirection: 'column',
    justifyContent: 'flex-start',
  },

  button1: {
    marginVertical: 10,
    paddingVertical: 13,
    paddingRight: 20,
    backgroundColor: 'transparent',
  },
  button2: {
    width: '60%',
    backgroundColor: '#03AADE',
    borderRadius: 25,
    marginTop: 15,
    alignItems: 'center',
    justifyContent: 'center',
    height: 40,
  },
  button2_1: {
    width: '40%',
    backgroundColor: '#03AADE',
    borderRadius: 25,
    marginTop: 15,
    alignItems: 'center',
    justifyContent: 'center',
    height: 40,
  },
  button1TextStyle: {
    color: 'red',
    fontWeight: 'bold',
  },
  button2TextStyle: {
    fontWeight: 'bold',
    color: '#FFFFFF',
  },

  textNsolicitud: {
    color: '#03AADE',
    fontWeight: 'bold',
    fontSize: 27,
  },

  textInfoNSolicitud: {
    color: '#4A5F8E',
    fontWeight: 'normal',
    fontSize: 13,
    paddingLeft: 5,
  },
  textInfo: {
    color: '#4A5F8E',
    fontWeight: 'normal',
    fontSize: 14,
    paddingBottom: 5,
    textAlign:'center'
  },
  textTipo: {
    color: '#4A5F8E',
    fontWeight: 'normal',
    fontSize: 14,
    paddingRight: 15,
  },
  textIdSolicitud: {
    color: '#03AADE',
    fontWeight: 'bold',
    fontSize: 16,
    paddingBottom: 5,
    paddingTop: 5,
  },
  viewIdSolicitud: {
    alignItems: 'center',
    padding: 5,
  },
  formTitle:{
    color: '#03AADE',
    fontWeight: 'bold',
    fontSize: 16,
    paddingBottom: 15
  },
  viewInfo1: {
    padding: 5,
    paddingLeft: 20,
    paddingRight: 20,
  },
  viewInfo1_1: {
    flex: 1,
    flexDirection: 'row',
    padding: 5,
  },

  viewInfo2: {
    flexGrow: 1,
    flexDirection: 'column',
    alignItems: 'center',
  },
  viewInfo2_2: {
    flexDirection: 'row',
  justifyContent:'space-between'
  },
  viewInfo2_3: {
    flex: 1,
    flexDirection: 'row',
    width: '100%',
    padding: 15,
  },

  viewInfo2_3_1: {
    alignItems: 'flex-end',
    width: '25%',
    marginRight: 12,
  },
  verticalLine: {
    marginBottom: 5,
    color: '#4A5F8E',
    fontWeight: 'bold',
    paddingRight: 10,
  },

  viewInfo2_3_2: {
    width: '75%',
  },
  viewInfo3: {
    padding: 15,
  },
  infoPadding: {
    padding: 15,
  },
  textInfo4: {
    fontWeight: 'bold',
    color: '#4A5F8E',
  },
  textData4: {
    color: '#4A5F8E',
    fontSize: 14,
  },
  viewInfo3_1: {
    paddingLeft: 15,
    paddingRight: 15,
    paddingBottom: 5,
    flexGrow: 1,
    flexDirection: 'row',
    width: '100%',
  },
  viewInfo4: {
    backgroundColor: '#4A5F8E',
    alignItems: 'center',
    height: 30,
    justifyContent: 'center',
  },
  textInfo5: {
    color: '#FFFFFF',
    fontWeight: 'bold',
    fontSize: 23,
    padding: 10,
  },

  viewInfo5: {
    alignItems: 'center',
    flexDirection: 'row',
    justifyContent: 'center',
    marginRight: 25,
    marginLeft: 25,
  },

  viewInfo6: {
    marginTop: 25,
    marginRight: 25,
    marginLeft: 25,
  },

  viewData: {
  },
  textData: {
    color: '#4A5F8E',
    fontWeight: 'bold',
    fontSize: 19,
    textAlign:'center'
  },
  textInfo2: {
    color: '#4A5F8E',
    fontWeight: 'bold',
    fontSize: 23,
    paddingBottom: 15,
  },
  textData2: {
    color: '#4A5F8E',
    fontWeight: 'normal',
    fontSize: 23,
  },
  textInfo3: {
    color: '#03AADE',
    fontWeight: 'normal',
    fontSize: 16,
    padding: 15,
  },
  viewInfo7: {
    justifyContent: 'center',
    flexDirection: 'row',
    marginBottom: 35,
  marginRight:20,
  marginLeft:20,
  marginTop:5
  },

  errorText: {
    fontWeight: '700',
    color: '#700',
    paddingLeft: 10,
    marginBottom: 15,
  },

  collapseHeaderText: {
    color: '#4A5F8E',
    fontWeight: 'bold',
    fontSize: 14,
  },
  collapseHeaderSeparator: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingRight: 15,
    alignItems: 'center',
  },

  preview: {
    width: '100%',
    height: '100%',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  capture: {
    flex: 0,
    backgroundColor: '#fff',
    borderRadius: 5,
    padding: 15,
    paddingHorizontal: 20,
    alignSelf: 'center',
    margin: 20,
  },


  body: {
    backgroundColor: 'white',
    justifyContent: 'center',
    borderColor: 'black',
    borderWidth: 1,
    height: Dimensions.get('screen').height - 20,
    width: Dimensions.get('screen').width
  },
  ImageSections: {
    display: 'flex',
    flexDirection: 'row',
    paddingHorizontal: 8,
    paddingVertical: 8,
    justifyContent: 'center'
  },
  images: {
    width: 150,
    height: 150,
    borderColor: 'transparent',
    borderWidth: 1,
    marginHorizontal: 3
  },
  btnSection: {
    width: 225,
    height: 50,
    backgroundColor: '#DCDCDC',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    marginBottom:10
  },
  btnText: {
    textAlign: 'center',
    color: 'gray',
    fontSize: 14,
    fontWeight:'bold'
  },
  facturacionButtonArea: {
    justifyContent: 'space-between',
    flexDirection: 'row',
    paddingBottom: 35
  },
});
