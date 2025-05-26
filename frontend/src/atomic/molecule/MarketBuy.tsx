import { ModifierProps, CardButtonProps } from "../../shared/Types";
import ProfileButton from "../atom/ProfileButton";
import SmallButton from "../atom/SmallButton";
import { sectionType } from "../molecule/SectionBuyCard";
import BuyCardCarousel from "../organism/BuyCardCarousel";

const MarketBuy: React.FC<{
  section: sectionType;
  modifiers: ModifierProps[];
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  updateState: () => void;
}> = ({ section, modifiers, onClick, updateState }) => (
  <div className="flex flex-col gap-3 text-white font-inter">
    <div className="text-2xl text-center">Нанять {section}</div>
    <BuyCardCarousel
      modifiers={modifiers}
      updateState={updateState}
    ></BuyCardCarousel>
    <div className="flex flex-row gap-3 justify-center">
      <ProfileButton height={35} width="33%" onClick={onClick}>
        Назад
      </ProfileButton>
      {/* <ProfileButton height={35} color="Primary">Оплатить</ProfileButton> */}
    </div>
  </div>
);

export default MarketBuy;
