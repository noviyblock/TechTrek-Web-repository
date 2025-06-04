import { Color } from "../../shared/Color";
import Cube from "../atom/Cube";
import SmallButton from "../atom/SmallButton";

const DiceResult: React.FC<{
  diceResult: number;
  onClick: () => void;
  text: string;
  cube1: number;
  cube2: number;
  color: Color;
}> = ({ diceResult, onClick, text, cube1, cube2, color }) => (
  <div className="flex flex-col p-5 gap-4 items-center font-inter rounded-2xl" 
  style={{
    backgroundColor: color
  }}>
    <div className="flex flex-row justify-between w-full">
      <div className="flex flex-col">
        <div>Ваш бросок</div>
        <div>{diceResult}</div>
      </div>
      <div className="grid grid-cols-2 grid-rows-1 gap-6 min-h-16">
        <div className="min-h-16 min-w-12">
          <Cube amount={cube1} rotation={0} />
        </div>
        <div className="min-h-16 min-w-12">
          <Cube amount={cube2} rotation={0} />
        </div>
      </div>
    </div>
    <div>
      <b>{diceResult}</b>
    </div>
    <div>{text}</div>
    <div>Buffs</div>
    <SmallButton color="Primary" onClick={onClick}>
      Окей
    </SmallButton>
  </div>
);
export default DiceResult;
