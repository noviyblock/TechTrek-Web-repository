import { useEffect, useState } from "react";
import SphereSelectionPage from "./SphereSelectionPage";
import MissionPage from "./MissionPage";
import CommandNamePage from "./CommandNamePage";
import MainScreenLayout from "../template/MainScreenLayout";
import { ChoosableTextProps } from "../../shared/Types";
import { GameState, getMissions, startGame } from "../../api/Game";
import { nullGameState } from "../../shared/constants";

const GamePage: React.FC = () => {
  const [currentPage, setCurrentPage] = useState<Pages>("spherePage");
  const [sphere, setSphere] = useState<number>(0);
  const [mission, setMission] = useState<number>(0);
  const [missions, setMissions] = useState<ChoosableTextProps[]>([]);
  const [commandName, setCommandName] = useState<string>("");
  const [gameState, setGameState] = useState<GameState>(nullGameState);

  useEffect(() => {
    const fetchMissions = async (sphere: number) => {
      const response = await getMissions(sphere);
      setMissions(
        response.map((value) => {
          return {
            color: "White",
            id: value.id,
            children: <div>{value.name}</div>,
          };
        })
      );
    };

    fetchMissions(sphere);
  }, [sphere]);

  useEffect(() => {
    if (commandName === "") return;

    const fetchStartGame = async () => {
      setGameState(
        await startGame({ missionId: mission, companyName: commandName })
      );
    };

    fetchStartGame();
  }, [commandName]);

  const gamePages = {
    spherePage: (
      <SphereSelectionPage
        onClick={(sphere: number) => {
          setSphere(sphere);
          setCurrentPage("missionPage");
        }}
      ></SphereSelectionPage>
    ),
    missionPage: (
      <MissionPage
        missions={missions}
        onClick={(mission: number) => {
          setMission(mission);
          setCurrentPage("commandName");
        }}
      ></MissionPage>
    ),
    commandName: (
      <CommandNamePage
        onClick={(commandName: string) => {
          setCommandName(commandName);
          setCurrentPage("mainScreen");
        }}
      ></CommandNamePage>
    ),
    mainScreen: (
      <MainScreenLayout game={gameState}>
        <></>
      </MainScreenLayout>
    ),
  };
  type Pages = keyof typeof gamePages;

  return <>{gamePages[currentPage]}</>;
};

export default GamePage;
