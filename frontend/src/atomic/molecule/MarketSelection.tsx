import { on } from "events";
import { defaultBlockColor } from "../../shared/Color";
import LargeButton from "../atom/LargeButton";
import MediumButton from "../atom/MediumButton";
import ProfileButtonBorder from "../atom/ProfileButtonBorder";
import SmallButton from "../atom/SmallButton";
import SectionBuyCard, { sectionType } from "./SectionBuyCard";

const MarketSelection: React.FC<{
  onClickDeveloper: React.MouseEventHandler<HTMLButtonElement>;
  onClickCLevel: React.MouseEventHandler<HTMLButtonElement>;
  onClickOffice: React.MouseEventHandler<HTMLButtonElement>;
  onClickExit: React.MouseEventHandler<HTMLButtonElement>;
}> = ({ onClickDeveloper, onClickCLevel, onClickOffice, onClickExit }) => (
  <div className="flex flex-col gap-3 font-inter text-white items-center">
    <div className="text-center text-3xl">Выберите действие</div>
    <div className="flex flex-row gap-5">
      <SectionBuyCard
        section={sectionType.DEVELOPER}
        onClick={onClickDeveloper}
      ></SectionBuyCard>
      <SectionBuyCard
        section={sectionType.C_LEVEL}
        onClick={onClickCLevel}
      ></SectionBuyCard>
      <SectionBuyCard
        section={sectionType.OFFICE}
        onClick={onClickOffice}
      ></SectionBuyCard>
    </div>
    <ProfileButtonBorder height={30} width="190px" color="Primary" onClick={onClickExit}>
      Выйти
    </ProfileButtonBorder>
  </div>
);

export default MarketSelection;
