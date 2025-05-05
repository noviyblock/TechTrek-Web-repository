import { Color, TextColor, deafultBackground } from "../../shared/Color";
import { CardButtonProps } from "../../shared/Types";
import Logo from "../../shared/banana.jpeg";

const GameFieldValue: React.FC<{
  inCircle: string;
  tooltip: string | number;
}> = ({ inCircle, tooltip }) => (
  <div
    className="flex flex-col rounded-lg w-min p-3 gap-1 px-5"
    style={{ background: "#27272A" }}
  >
    <div
      className="rounded-full self-center text-center w-9 h-9"
      style={{ background: "#D9D9D9" }}
    >
      <div className="mt-1.5">{inCircle}</div>
    </div>
    <div className="self-center text-sm text-center text-white min-w-[4em]">{tooltip}</div>
  </div>
);

export default GameFieldValue;
