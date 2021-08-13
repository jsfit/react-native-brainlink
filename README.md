
# react-native-brainlink

## Getting started

`$ npm install react-native-brainlink --save`

### Mostly automatic installation

`$ react-native link react-native-brainlink`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-brainlink` and add `RNBrainlink.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNBrainlink.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNBrainlinkPackage;` to the imports at the top of the file
  - Add `new RNBrainlinkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-brainlink'
  	project(':react-native-brainlink').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-brainlink/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-brainlink')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNBrainlink.sln` in `node_modules/react-native-brainlink/windows/RNBrainlink.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Brainlink.RNBrainlink;` to the usings at the top of the file
  - Add `new RNBrainlinkPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNBrainlink from 'react-native-brainlink';

// TODO: What to do with the module?
RNBrainlink;
```
  