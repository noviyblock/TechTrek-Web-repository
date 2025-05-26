import { useEffect, useState } from "react";
import MarketBuy from "../molecule/MarketBuy";
import MarketSelection from "../molecule/MarketSelection";
import { sectionType } from "../molecule/SectionBuyCard";
import { ModifierProps } from "../../shared/Types";
import { modifiers } from "../../api/Game";

// todo stage
const Market: React.FC<{ gameId: number; stage: number; onClick: React.MouseEventHandler<HTMLButtonElement>; }> = ({
  gameId,
  stage,
  onClick
}) => {
  const [modifierList, setModifiers] = useState<ModifierProps[]>([]);

  useEffect(() => {
    const fetchModifiers = async (gameId: number) => {
      const response = await modifiers(gameId);
      
      setModifiers(response.map(value => {return {gameId, ...value}}));
    };

    fetchModifiers(gameId);
  }, []);

  const screens = {
    main: (
      <MarketSelection
        onClickDeveloper={() => {
          setCurrentScreen(sectionType.DEVELOPER);
        }}
        onClickCLevel={() => {
          setCurrentScreen(sectionType.C_LEVEL);
        }}
        onClickOffice={() => {
          setCurrentScreen(sectionType.OFFICE);
        }}
        onClickExit={onClick}
      ></MarketSelection>
    ),
    [sectionType.DEVELOPER]: (
      <MarketBuy
        onClick={() => {
          setCurrentScreen("main");
        }}
        section={sectionType.DEVELOPER}
        modifiers={modifierList.filter(
          (value) => value.type !== "OFFICE" && value.type !== "C_LEVEL"
        )}
      ></MarketBuy>
    ),
    [sectionType.C_LEVEL]: (
      <MarketBuy
        onClick={() => {
          setCurrentScreen("main");
        }}
        section={sectionType.C_LEVEL}
        modifiers={modifierList.filter((value) => value.type === "C_LEVEL")}
      ></MarketBuy>
    ),
    [sectionType.OFFICE]: (
      <MarketBuy
        onClick={() => {
          setCurrentScreen("main");
        }}
        section={sectionType.OFFICE}
        modifiers={modifierList.filter((value) => value.type === "OFFICE")}
      ></MarketBuy>
    ),
  };
  const [currentScreen, setCurrentScreen] =
    useState<keyof typeof screens>("main");

  return screens[currentScreen];
};

export default Market;
