import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import Brainlink from 'react-native-brainlink';
import { NativeEventEmitter } from 'react-native';

const eventEmitter = new NativeEventEmitter(Brainlink);

export default function App() {
  const [result, setResult] = React.useState<any>();
  const resultRef = React.useRef(result)
  resultRef.current = result


  React.useEffect(() => {
    Brainlink.isBluetoothOn((res: boolean) => {
      console.log("isBluetoothOn", res)
    })

    Brainlink.start();
    eventEmitter.addListener('Connection', (r => {
      if (resultRef.current?.state != r.state) {
        setResult(r)
      }
    }));

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
