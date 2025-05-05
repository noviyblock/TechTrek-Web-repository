import Cube from "../atom/Cube";
import SmallButton from "../atom/SmallButton";

const DiceResult: React.FC<{ diceResult: number; onClick: () => void }> = ({
  diceResult,
  onClick,
}) => (
  <div className="flex flex-col p-5 gap-4 items-center font-inter rounded-2xl bg-green-400">
    <div className="flex flex-row justify-between w-full">
      <div className="flex flex-col">
        <div>Ваш бросок</div>
        <div>{diceResult}</div>
      </div>
      <div className="grid grid-cols-2 grid-rows-1 gap-6 min-h-16">
        <div className="min-h-16 min-w-12">
          <Cube amount={5} rotation={0} />
        </div>
        <div className="min-h-16 min-w-12">
          <Cube amount={5} rotation={0} />
        </div>
      </div>
    </div>
    <div>
      <b>{diceResult}</b>
    </div>
    <div>
      Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
      tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
      veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
      commodo consequat. Duis aute irure dolor in reprehenderit in voluptate
      velit esse cillum dolore eu fugiat nulla pariatur.
    </div>
    <div>Buffs</div>
    <SmallButton color="Primary" onClick={onClick}>Окей</SmallButton>
  </div>
);
export default DiceResult;
