import { useEffect, useState } from "react";
import MarketBuy from "../molecule/MarketBuy";
import MarketSelection from "../molecule/MarketSelection";
import { sectionType } from "../molecule/SectionBuyCard";
import { ModifierProps } from "../../shared/Types";
import { modifiers } from "../../api/Game";
import ColoredText from "../atom/ColoredText";
import { Color } from "../../shared/Color";

// todo stage
const Market: React.FC<{
  gameId: number;
  stage: number;
  onClick: React.MouseEventHandler<HTMLButtonElement>;
  updateState: () => void;
}> = ({ gameId, stage, onClick, updateState }) => {
  const [modifierList, setModifiers] = useState<ModifierProps[]>([]);

  useEffect(() => {
    const fetchModifiers = async (gameId: number) => {
      const response = await modifiers(gameId);

      setModifiers(
        response.map((value) => {
          return { updateState, gameId, ...value };
        })
      );
    };

    if (gameId !== undefined) {
      fetchModifiers(gameId);
    }
  }, [gameId]);

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
        buyNode={<ColoredText background={"Danger"}>разработчика</ColoredText>}
        updateState={updateState}
        onClick={() => {
          setCurrentScreen("main");
        }}
        section={sectionType.DEVELOPER}
        modifiers={modifierList
          .filter(
            (value) => value.type !== "OFFICE" && value.type !== "C_LEVEL"
          )
          .map((value) => {
            return { ...value, gameId: gameId };
          })}
      ></MarketBuy>
    ),
    [sectionType.C_LEVEL]: (
      <MarketBuy
        buyNode={
          <ColoredText background={"Primary"}>
            ключевого специалиста
          </ColoredText>
        }
        updateState={updateState}
        onClick={() => {
          setCurrentScreen("main");
        }}
        section={sectionType.C_LEVEL}
        modifiers={modifierList
          .filter((value) => value.type === "C_LEVEL")
          .map((value) => {
            return { ...value, gameId: gameId };
          })}
      ></MarketBuy>
    ),
    [sectionType.OFFICE]: (
      <MarketBuy
        buyNode={<ColoredText background={"Warning"}>актив</ColoredText>}
        updateState={updateState}
        onClick={() => {
          setCurrentScreen("main");
        }}
        section={sectionType.OFFICE}
        modifiers={modifierList
          .filter((value) => value.type === "OFFICE")
          .map((value) => {
            return { ...value, gameId: gameId };
          })}
      ></MarketBuy>
    ),
  };
  const [currentScreen, setCurrentScreen] =
    useState<keyof typeof screens>("main");

  return screens[currentScreen];
};

export default Market;
