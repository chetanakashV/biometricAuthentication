import { NativeModules, Platform } from 'react-native';
const { BiometricModule } = NativeModules;

const checkBiometrics = async () => {
  try {
    const isAvailable = await BiometricModule.isBiometricAvailable();
    if (!isAvailable) {
      console.log("Biometrics not available");
      return;
    }

    const authenticated = await BiometricModule.authenticate();
    if (authenticated) {
      console.log("Authentication successful!");
    }
  } catch (error) {
    console.error("Authentication failed:", error);
  }
};

export default checkBiometrics;
