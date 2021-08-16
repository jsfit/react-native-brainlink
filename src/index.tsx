import { NativeModules } from 'react-native';

type BrainlinkType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Brainlink } = NativeModules;

export default Brainlink as BrainlinkType;
