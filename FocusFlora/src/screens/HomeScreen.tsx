import React from "react";
import { View, Text, SafeAreaView, ScrollView } from "react-native";
import CircularTimer from "../components/CircularTimer";
import IsometricGarden from "../components/IsometricGarden";
import { useTimer } from "../hooks/useTimer";

export default function HomeScreen() {
  const { remaining, isRunning, total, toggle, reset } = useTimer(25 * 60);

  return (
    <SafeAreaView className="flex-1 bg-white">
      <ScrollView contentContainerStyle={{ flexGrow: 1 }}>
        <View className="pt-8 px-4">
          <Text className="text-2xl font-bold text-center text-primary">
            FocusFlora
          </Text>
          <Text className="text-sm text-center text-gray-500 mt-1">
            Grow your garden with focus
          </Text>
        </View>

        <CircularTimer
          remaining={remaining}
          total={total}
          isRunning={isRunning}
          onToggle={toggle}
          onReset={reset}
        />

        <IsometricGarden />
      </ScrollView>
    </SafeAreaView>
  );
}
