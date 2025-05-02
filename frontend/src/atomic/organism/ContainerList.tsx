import { useState } from "react";
import { ContrainerListProps } from "../../shared/Types";
import { containerColor } from "../../shared/constants";
import { Color, defaultBorderColor } from "../../shared/Color";

const ContainerList: React.FC<ContrainerListProps> = ({ elements }) => {
  const [activeIndex, setActiveIndex] = useState(0);
  return (
    <div className="flex flex-col justify-between p-2 h-52 overflow-auto" style={{ background: containerColor }}>
      <div>{elements[activeIndex]}</div>
      <div className="flex gap-2 justify-center">
        {elements.map((_, i) => (
          <button
            key={i}
            onClick={() => setActiveIndex(i)}
            className="w-4 h-4 rounded-full border-r"
            style={{
              background: activeIndex === i ? Color.Primary : Color.Default,
              borderWidth: 1,
              borderColor: activeIndex === i ? Color.White : defaultBorderColor
            }}
          />
        ))}
      </div>
    </div>
  );
};

export default ContainerList;
