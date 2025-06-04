import { tooltipColor } from "../../shared/Color";
import Block from "../molecule/Block";
import { CommandBlockProps } from "../../shared/Types";
import ColoredText from "../atom/ColoredText";
import { categories } from "../../shared/constants";

const CommandBlock: React.FC<CommandBlockProps> = ({
  commandName,
  commandPic,
  mission,
  sphere,
}) => (
  <Block grow={0}>
    <div className="flex flex-col gap-2">
      <div className="flex flex-row justify-between content-center max-h-[3em] min-h-[3em]">
        <div className="flex flex-col font-inter">
          <div
            className="font-inter font-bold text-xs"
            style={{ color: tooltipColor }}
          >
            Компания
          </div>
          <div className="flex flex-row gap-2 items-center">
            <div className="text-white font-inter"> {commandName} </div>
            {/* TODO onclick */}
            <div className="rounded-full text-xs text-white w-4 h-4 text-center mt-[2px] cursor-pointer" style={{backgroundColor: "#3F3F46"}}>i</div>
          </div>
        </div>
        <div className="max-w-[3em] min-w-[3em] min-h-[3em] rounded-2xl overflow-hidden">
          <img
            className="w-full h-full object-cover"
            src={commandPic}
            alt="pic"
          ></img>
        </div>
      </div>

      <ColoredText background={categories[sphere].color ?? "Warning"} className="text-xs text-center" px="8px">
      {categories[sphere].name}
      </ColoredText>

      <div className="flex flex-col gap-2 font-inter">
        <div
          className="font-extrabold text-xs"
          style={{ color: tooltipColor }}
        >
          Миссия и цель
        </div>
        <div className="text-xs text-white">
          {mission}
        </div>
      </div>
    </div>
  </Block>
);

export default CommandBlock;
