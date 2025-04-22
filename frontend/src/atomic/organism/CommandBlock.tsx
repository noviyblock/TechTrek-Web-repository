import { tooltipColor } from "../../shared/Color";
import Block from "../molecule/Block";
import PictureList from "../molecule/PictureList";
import { CommandBlockProps } from "../../shared/Types";

const CommandBlock: React.FC<CommandBlockProps> = ({
  commandName,
  commandPic,
  participants,
}) => (
  <Block>
    <div className="flex flex-col gap-2">
      <div className="flex flex-row justify-between content-center max-h-[3em] min-h-[3em]">
        <div className="flex flex-col">
          <div
            className="font-inter font-bold text-xs"
            style={{ color: tooltipColor }}
          >
            Команда
          </div>
          <div className="text-white font-inter text-xl"> {commandName} </div>
        </div>
        <div className="max-w-[3em] min-w-[3em] min-h-[3em] rounded-full overflow-hidden">
          <img
            className="w-full h-full object-cover"
            src={commandPic}
            alt="pic"
          ></img>
        </div>
      </div>

      <div className="flex flex-col gap-2">
        <div
          className="font-inter font-bold text-xs"
          style={{ color: tooltipColor }}
        >
          Участники
        </div>
        <PictureList
          pictures={participants.flatMap((participant) => ({
            image: participant.picture,
            rounding: 12,
          }))}
          size={50}
        />
      </div>
    </div>
  </Block>
);

export default CommandBlock;
