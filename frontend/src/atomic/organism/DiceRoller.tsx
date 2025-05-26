import Cube from "../atom/Cube";
import SmallButton from "../atom/SmallButton";

//Math.floor(Math.random() * 5 + 1)
const DiceRoller: React.FC<{
  onClick: () => void;
  cube1: number;
  rotation1: number;
  cube2: number;
  rotation2: number;
}> = ({ onClick, cube1, rotation1, cube2, rotation2 }) => (
  <div className="flex flex-col gap-4 items-center">
    <div className="font-inter text-white">Влияние случая на ваше решение</div>
    <div className="grid grid-cols-2 grid-rows-1 gap-16 min-h-16">
      <div className="min-h-16 min-w-6">
        <Cube amount={cube1} rotation={rotation1} />
      </div>
      <div className="min-h-16 min-w-6">
        <Cube amount={cube2} rotation={rotation2} />
      </div>
    </div>
    <SmallButton onClick={onClick} color="Primary">
      Сделать бросок
    </SmallButton>
  </div>
);
export default DiceRoller;
