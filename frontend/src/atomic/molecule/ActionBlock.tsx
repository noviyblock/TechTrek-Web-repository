import { defaultBlockColor } from "../../shared/Color";
import LargeButton from "../atom/LargeButton";
import MediumButton from "../atom/MediumButton";
import SmallButton from "../atom/SmallButton";

const ActionBlock: React.FC<{ header: string; tooltip: string }> = ({
  header,
  tooltip,
}) => (
  <div
    className="flex flex-col gap-3 border-solid border-[1px] border-white p-4 w-full h-full items-center"
    style={{ background: defaultBlockColor, maxWidth: '300px'  }}
  >
    <div className="text-3xl font-inter text-white"><b>{header}</b></div>
    <div className="text-sm font-inter text-white mb-28">{tooltip}</div>

    <MediumButton color="Primary">Выбрать</MediumButton>
  </div>
);
export default ActionBlock;
