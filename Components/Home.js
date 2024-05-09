import React from "react";
import { Button, Text, View } from "react-native";
import checkBiometrics from "./Bio";


const Home = () => {
    return(
    <View>
        <Text>Working</Text>
        <Button
            title = "Check BioMetric"
            onPress = {checkBiometrics}
        />
    </View>
    )
}

export default Home; 