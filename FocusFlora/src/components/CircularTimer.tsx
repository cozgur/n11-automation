import React from "react";
import { View, Text, TouchableOpacity } from "react-native";
import Svg, { Circle } from "react-native-svg";

const RADIUS = 100;
const STROKE_WIDTH = 10;
const CIRCUMFERENCE = 2 * Math.PI * RADIUS;

interface CircularTimerProps {
  /** Remaining seconds */
  remaining: number;
  /** Total duration in seconds */
  total: number;
  /** Whether the timer is running */
  isRunning: boolean;
  onToggle: () => void;
  onReset: () => void;
}

function formatTime(seconds: number): string {
  const m = Math.floor(seconds / 60);
  const s = seconds % 60;
  return `${m.toString().padStart(2, "0")}:${s.toString().padStart(2, "0")}`;
}

export default function CircularTimer({
  remaining,
  total,
  isRunning,
  onToggle,
  onReset,
}: CircularTimerProps) {
  const progress = total > 0 ? remaining / total : 0;
  const strokeDashoffset = CIRCUMFERENCE * (1 - progress);

  return (
    <View className="items-center justify-center py-8">
      <View className="items-center justify-center">
        <Svg
          width={RADIUS * 2 + STROKE_WIDTH * 2}
          height={RADIUS * 2 + STROKE_WIDTH * 2}
        >
          {/* Background circle */}
          <Circle
            cx={RADIUS + STROKE_WIDTH}
            cy={RADIUS + STROKE_WIDTH}
            r={RADIUS}
            stroke="#E0E0E0"
            strokeWidth={STROKE_WIDTH}
            fill="none"
          />
          {/* Progress circle */}
          <Circle
            cx={RADIUS + STROKE_WIDTH}
            cy={RADIUS + STROKE_WIDTH}
            r={RADIUS}
            stroke="#4CAF50"
            strokeWidth={STROKE_WIDTH}
            fill="none"
            strokeDasharray={`${CIRCUMFERENCE}`}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={`rotate(-90, ${RADIUS + STROKE_WIDTH}, ${RADIUS + STROKE_WIDTH})`}
          />
        </Svg>
        <Text className="absolute text-4xl font-bold text-gray-800">
          {formatTime(remaining)}
        </Text>
      </View>

      <View className="flex-row mt-6 gap-4">
        <TouchableOpacity
          onPress={onToggle}
          className="bg-primary px-8 py-3 rounded-full"
        >
          <Text className="text-white font-semibold text-lg">
            {isRunning ? "Pause" : "Start"}
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={onReset}
          className="bg-gray-300 px-8 py-3 rounded-full"
        >
          <Text className="text-gray-700 font-semibold text-lg">Reset</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
