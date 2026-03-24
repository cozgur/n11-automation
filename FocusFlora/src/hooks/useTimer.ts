import { useState, useEffect, useRef, useCallback } from "react";

const DEFAULT_DURATION = 25 * 60; // 25 minutes in seconds

export function useTimer(duration: number = DEFAULT_DURATION) {
  const [remaining, setRemaining] = useState(duration);
  const [isRunning, setIsRunning] = useState(false);
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const clear = useCallback(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
  }, []);

  useEffect(() => {
    if (isRunning && remaining > 0) {
      intervalRef.current = setInterval(() => {
        setRemaining((prev) => {
          if (prev <= 1) {
            clear();
            setIsRunning(false);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    } else {
      clear();
    }
    return clear;
  }, [isRunning, remaining, clear]);

  const toggle = useCallback(() => {
    if (remaining > 0) {
      setIsRunning((prev) => !prev);
    }
  }, [remaining]);

  const reset = useCallback(() => {
    clear();
    setIsRunning(false);
    setRemaining(duration);
  }, [duration, clear]);

  return { remaining, isRunning, total: duration, toggle, reset };
}
