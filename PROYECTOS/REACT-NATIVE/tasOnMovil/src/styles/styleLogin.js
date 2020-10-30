import { StyleSheet } from 'react-native';

export default StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: "column",
        justifyContent: "space-between",
        backgroundColor: "#ffffff"
      },
      logoSection: {
        height: "30%"
      },
      formSection: {
        height: "70%",  
        flex: 1 ,
        paddingRight: 50,
        paddingLeft: 50 
      },
      noAccountSection: {
      marginTop:10
       
      },
      linkButton: {
        color: "#03AADE",
        fontSize: 15,
        fontWeight: "500"
      },
      signupText: {
        color: "#4A5F8E",
        fontSize: 19,
        marginTop: 20,
        marginBottom: 10,
        
      }, 
      signUpButton: {
        backgroundColor: "#7FBDE9",
      
        alignItems:'center'
      },
      buttonText: {
        fontSize: 16,
        fontWeight: "500",
        color: "#ffffff",
        textAlign :'center',
        marginTop: 0,
        width: '100%',
       
      },
      loginButton: {
        backgroundColor: '#03AADE',
        
         alignItems:'center',
         marginTop:25
      },
      forgotPasswordButton:{
        alignItems : 'center',
        paddingTop:15,
        paddingBottom: 10
      },
      errorText:{
          fontWeight: '700',
          color: '#FC3A5F',
          paddingLeft: 10,
          marginBottom:15,
      }

  });

