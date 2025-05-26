import { useEffect, useState } from "react";
import SphereSelectionPage from "./SphereSelectionPage";
import MissionPage from "./MissionPage";
import CommandNamePage from "./CommandNamePage";
import MainScreenLayout from "../template/MainScreenLayout";
import { ChoosableTextProps } from "../../shared/Types";
import { GameState, getMissions, startGame } from "../../api/Game";
import { nullGameState } from "../../shared/constants";
import { useLocalStorage } from "../../LocalStorage";

const GamePage: React.FC = () => {
  const [currentPage, setCurrentPage] = useLocalStorage<Pages>(
    "game_currentPage",
    "spherePage"
  );
  const [sphere, setSphere] = useLocalStorage<number>("game_sphere", 0);
  const [mission, setMission] = useLocalStorage<number>("game_mission", 0);
  const [missions, setMissions] = useState<ChoosableTextProps[]>([]);
  const [commandName, setCommandName] = useLocalStorage<string>(
    "game_commandName",
    ""
  );
  const [gameState, setGameState] = useLocalStorage<GameState>("game_state", nullGameState);

  useEffect(() => {
    if (sphere === 0) return;
  
    const fetchMissions = async (sphere: number) => {
      const response = await getMissions(sphere);
      setMissions(
        response.map((value) => ({
          color: "White",
          id: value.id,
          children: <div>{value.name}</div>,
        }))
      );
    };
  
    fetchMissions(sphere);
  }, [sphere]);

  useEffect(() => {
    if (!mission || !commandName || currentPage !== "mainScreen" || gameState !== nullGameState) return;
  
    const fetchStartGame = async () => {
      const result = await startGame({ missionId: mission, companyName: commandName });
      setGameState(result);
    };
  
    fetchStartGame();
  }, [mission, commandName, currentPage]);

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
