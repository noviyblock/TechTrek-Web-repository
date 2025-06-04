import { StrictMode, useEffect, useState } from "react";
import SphereSelectionPage from "./SphereSelectionPage";
import MissionPage from "./MissionPage";
import CommandNamePage from "./CommandNamePage";
import MainScreenLayout from "../template/MainScreenLayout";
import { ChoosableTextProps } from "../../shared/Types";
import { GameState, getMissions, startGame, state } from "../../api/Game";
import { nullGameState } from "../../shared/constants";
import { useLocalStorage } from "../../LocalStorage";
import { GameFields, GameService } from "../../api/services/GameService";
import { useNavigate } from "react-router-dom";

const GamePage: React.FC = () => {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useLocalStorage<Pages>(
    GameFields.gamePage,
    "spherePage"
  );
  const [sphere, setSphere] = useLocalStorage<number>(GameFields.sphere, 0);
  const [mission, setMission] = useLocalStorage<number>(GameFields.mission, 0);
  const [missions, setMissions] = useState<ChoosableTextProps[]>([]);
  const [commandName, setCommandName] = useLocalStorage<string>(
    GameFields.commandName,
    ""
  );
  const gameId = GameService.getGameId();
  const [gameIdState, setGameId] = useState(0);
  const [gameState, setGameState] = useState<GameState>(nullGameState);

  useEffect(() => {
    if (gameId === undefined) {
      GameService.reset();
      return;
    }

    const fetchGameState = async (gameId: number) => {
        const result = await state(gameId);
        setGameState(result);
    };

    if (gameId !== 0) {
      fetchGameState(gameId);
      GameService.setGameId(gameId);
      setGameId(gameId);
      setCurrentPage("mainScreen");
    }
  }, []);

  useEffect(() => {
    if (sphere === 0 || gameId) return;

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
  }, [sphere, gameId]);

  useEffect(() => {
    if (
      !mission ||
      !commandName ||
      currentPage !== "mainScreen" ||
      gameState !== nullGameState ||
      gameId
    )
      return;

    const fetchStartGame = async () => {
      const result = await startGame({
        missionId: mission,
        companyName: commandName,
      });
      setGameId(result.gameId);
      setGameState(result);
    };

    fetchStartGame();
  }, [mission, commandName, currentPage, gameId, gameState]);

  useEffect(() => {
    if (gameState === nullGameState) return;
    GameService.setGameId(gameState.gameId);
  }, [gameState]);

  const gamePages = {
    spherePage: (
      <SphereSelectionPage
        onClick={(sphere: number) => {
          setSphere(sphere);
          setCurrentPage("missionPage");
        }}
        onBack={() => {
          navigate("/profile");
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
      <MainScreenLayout game={gameState} sphere={sphere} gameId={gameIdState}>
        <></>
      </MainScreenLayout>
    ),
  };
  type Pages = keyof typeof gamePages;

  return <>{gamePages[currentPage]}</>;
};

export default GamePage;
