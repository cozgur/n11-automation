import React from "react";
import { View, Text } from "react-native";

export default function IsometricGarden() {
  return (
    <View className="flex-1 items-center justify-center bg-background rounded-2xl mx-4 my-4 min-h-[200px]">
      <Text className="text-gray-400 text-base">
        🌱 Your Isometric Garden
      </Text>
      <Text className="text-gray-300 text-sm mt-2">
        Complete focus sessions to grow your plants
      </Text>
    </View>
  );
}
