import Cube from "../atom/Cube";
import SmallButton from "../atom/SmallButton";

const DiceRoller: React.FC<{ onClick: () => void }> = ({ onClick }) => (
  <div className="flex flex-col gap-4 items-center">
    <div className="font-inter text-white">Влияние случая на ваше решение</div>
    <div className="grid grid-cols-2 grid-rows-1 gap-16 min-h-16">
      <div className="min-h-16 min-w-6">
        <Cube amount={Math.floor(Math.random() * 5 + 1)} rotation={33} />
      </div>
      <div className="min-h-16 min-w-6">
        <Cube amount={Math.floor(Math.random() * 5 + 1)} rotation={3} />
      </div>
    </div>
    <SmallButton onClick={onClick} color="Primary">Сделать бросок</SmallButton>
  </div>
);
export default DiceRoller;
