import { Color } from "../../shared/Color";
import { ExtraTitledProgressProps } from "../../shared/Types";
import Progress from "../atom/Progress";

const ExtraTitledProgress: React.FC<ExtraTitledProgressProps> = ({
  current,
  end,
  color,
  title,
  textColor,
  startTitle,
  endTitle,
  changeColor,
}) => (
  <div className="flex flex-col gap-2">
    <div
      style={{ color: Color[textColor] }}
      className="font-inter flex flex-row justify-between text-sm"
    >
      <div className="">{startTitle}</div>
      <div className="">{endTitle}</div>
    </div>
    <Progress {...{ current, end, color, changeColor }} />
    <div
      style={{ color: Color[textColor] }}
      className="font-inter text-center text-sm"
    >
      {title}
    </div>
  </div>
);

export default ExtraTitledProgress;
