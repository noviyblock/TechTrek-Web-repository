import { ReactNode, useEffect, useRef, useState } from "react";
import { ModifierProps, CardButtonProps } from "../../shared/Types";
import ProfileButton from "../atom/ProfileButton";
import SmallButton from "../atom/SmallButton";
import { sectionType } from "../molecule/SectionBuyCard";
import BuyCardCarousel from "../organism/BuyCardCarousel";

const MarketBuy: React.FC<{
  section: sectionType;
  buyNode?: ReactNode;
  modifiers: ModifierProps[];
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  updateState: () => void;
}> = ({ section, modifiers, buyNode, onClick, updateState }) => {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [isOverflowing, setIsOverflowing] = useState(false);
  useEffect(() => {
    const el = scrollRef.current;
    if (!el) return;

    const checkOverflow = () => {
      setIsOverflowing(el.scrollWidth > el.clientWidth);
    };

    checkOverflow();
    window.addEventListener("resize", checkOverflow);
    return () => window.removeEventListener("resize", checkOverflow);
  }, []);

  return (
    <div className="flex flex-col gap-3 text-white font-inter">
      <div
        className={
          "text-2xl text-center flex flex-row gap-2 overflow-auto " +
          (isOverflowing ? "justify-start" : "justify-center")
        }
        ref={scrollRef}
      >
        {section === sectionType.OFFICE ? "Приобрести" : "Нанять"} {buyNode}
      </div>
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
};

export default MarketBuy;
