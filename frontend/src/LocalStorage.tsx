import { useState } from "react";

export function useLocalStorage<T>(key: string, initialValue: T): [T, (val: T) => void] {
    const [storedValue, setStoredValue] = useState<T>(() => {
      try {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : initialValue;
      } catch (error) {
        console.warn("useLocalStorage error:", error);
        return initialValue;
      }
    });
  
    const setValue = (value: T) => {
      try {
        setStoredValue(value);
        localStorage.setItem(key, JSON.stringify(value));
      } catch (error) {
        console.warn("useLocalStorage error:", error);
      }
    };
  
    return [storedValue, setValue];
  }