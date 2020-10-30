/**
 * @format
 */

import {AppRegistry ,YellowBox} from 'react-native';

import App from './App';
import {name as appName} from './app.json';
import bgMessaging from './src/bgMessaging ';

YellowBox.ignoreWarnings([
    'DrawerLayoutAndroid drawerPosition',    // WebView uri: result.url and url failing to load - "bloomberg suneq" https://github.com/facebook/react-native/issues/7839#issuecomment-224111608
'componentWillReceiveProps' ,
'componentWillMount','Request Timeout', 
'CheckBox','CheckBox has been extracted',   
])

/* 
<RootStack.Screen name={'MainStack'} initialParams={{swede:44}}>
        {() => (
          <UserContext.Provider value={state.user}>
            <ProductContext.Provider value={products}>
            <MainStackNavigator />
            </ProductContext.Provider>
          </UserContext.Provider>
        )}
      </RootStack.Screen> */

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerHeadlessTask('RNFirebaseBackgroundMessage', () => bgMessaging);
