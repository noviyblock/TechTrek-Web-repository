import { Color } from "../../shared/Color";
import { TitledProgressProps } from "../../shared/Types";
import Progress from "../atom/Progress";

const TitledProgress: React.FC<TitledProgressProps> = ({
  current,
  end,
  color,
  title,
  textColor,
  changeColor,
}) => (
  <div className="flex flex-col gap-2">
    <div style={{ color: Color[textColor] }} className="font-inter text-xs">
      {title}
    </div>
    <Progress {...{ current, end, color, changeColor }} />
  </div>
);

export default TitledProgress;
