import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import Brainlink from 'react-native-brainlink';
import { NativeEventEmitter } from 'react-native';

const eventEmitter = new NativeEventEmitter(Brainlink);

export default function App() {
  const [result, setResult] = React.useState<any>();

  React.useEffect(() => {

    Brainlink.start();
    eventEmitter.addListener('Connection', setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result?.state}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
