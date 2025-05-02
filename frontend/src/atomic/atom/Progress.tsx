import { useEffect, useRef, useState } from "react";
import { Color } from "../../shared/Color";
import { ProgressProps } from "../../shared/Types";
import { defaultAnimationTimeMs } from "../../shared/constants";

const Progress: React.FC<ProgressProps> = ({
  current,
  end,
  color,
  changeColor = true,
}) => {
  const previous = useRef<number>(current);
  const [currentColor, setCurrentColor] = useState(color);
  useEffect(() => {
    if (changeColor) {
      if (current === previous.current) {
        setCurrentColor(color);
      } else if (current > previous.current) {
        setCurrentColor("Success");
      } else {
        setCurrentColor("Danger");
      }

      const timeout = setTimeout(() => {
        setCurrentColor(color);
      }, defaultAnimationTimeMs);

      previous.current = current;
      return () => clearTimeout(timeout);
    }
  }, [current, color, changeColor]);

  return (
    <div
      style={{ background: Color.Progress }}
      className="w-full h-4 rounded-full overflow-hidden"
    >
      <div
        className={`h-full rounded-full overflow-hidden transition-all`}
        style={{
          width: `${(current / end) * 100}%`,
          background: Color[currentColor],
          transitionDuration: `${defaultAnimationTimeMs}ms`,
        }}
      />
    </div>
  );
};

export default Progress;
