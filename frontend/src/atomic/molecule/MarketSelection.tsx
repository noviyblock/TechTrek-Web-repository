import { on } from "events";
import { defaultBlockColor } from "../../shared/Color";
import LargeButton from "../atom/LargeButton";
import MediumButton from "../atom/MediumButton";
import ProfileButtonBorder from "../atom/ProfileButtonBorder";
import SmallButton from "../atom/SmallButton";
import SectionBuyCard, { sectionType } from "./SectionBuyCard";
import { useEffect, useRef, useState } from "react";

const MarketSelection: React.FC<{
  onClickDeveloper: React.MouseEventHandler<HTMLButtonElement>;
  onClickCLevel: React.MouseEventHandler<HTMLButtonElement>;
  onClickOffice: React.MouseEventHandler<HTMLButtonElement>;
  onClickExit: React.MouseEventHandler<HTMLButtonElement>;
}> = ({ onClickDeveloper, onClickCLevel, onClickOffice, onClickExit }) => {
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
    <div className="flex flex-col gap-3 font-inter text-white items-center">
      <div className="w-full overflow-x-auto">

      <div className="text-center text-3xl whitespace-nowrap">
        Сделайте свободное действие
      </div>
      </div>
      <div
        className={
          "flex flex-row gap-5 overflow-x-auto max-w-full " +
          (isOverflowing ? "justify-start" : "justify-center")
        }
        ref={scrollRef}
      >
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
      <ProfileButtonBorder
        height={30}
        width="190px"
        color="Primary"
        onClick={onClickExit}
      >
        Выйти
      </ProfileButtonBorder>
    </div>
  );
};

export default MarketSelection;
