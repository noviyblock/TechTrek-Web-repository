import { Color, deafultBackground, topBackground } from "../../shared/Color";
import Block from "../molecule/Block";
import CommandBlock from "../organism/CommandBlock";
import Statistics from "../organism/StatisticsBlock";
import TimeBlock from "../organism/TimeBlock";
import banana from "../../shared/banana.jpeg";
import { defaultPicture } from "../../shared/constants";
import {
  DecisionResponse,
  GameState,
  evaluateDesicion,
  evaluatePresentation,
  generateCrisis,
  state,
} from "../../api/Game";
import GameFieldValue from "../atom/GameFieldValue";
import HeadedBlock from "../atom/HeadedBlock";
import { useEffect, useRef, useState } from "react";
import DiceRoller from "../organism/DiceRoller";
import DiceResult from "../organism/DiceResult";
import Overlay from "../molecule/Overlay";
import { useLocalStorage } from "../../LocalStorage";
import Market from "../organism/Market";
import CrisisDecision from "../molecule/CrisisDecision";
import { GameFields, GameService } from "../../api/services/GameService";

const MainScreenLayout: React.FC<{
  game: GameState;
  children: React.ReactNode;
  sphere: number;
  gameId: number;
}> = ({ game, children, sphere, gameId }) => {
  let prevStage = useRef(game.stage);
  const [crisisDecision, setCrisisDecision] = useLocalStorage<string>(
    GameFields.crisisDecision,
    ""
  );
  const [presentationDecision, setPresentationDecision] =
    useLocalStorage<string>("presentationDecision", "");
  const [currentStage, setCurrentStage] = useLocalStorage<Stages>(
    GameFields.currentStage,
    "emptyScreen"
  );

  console.log(currentStage);
  const [decision, setDecision] = useLocalStorage<DecisionResponse | undefined>(
    GameFields.decision,
    undefined
  );
  const [gameState, setGameState] = useLocalStorage<GameState>(
    GameFields.gameState,
    game
  );
  const [shownGame, setShownGame] = useState<GameState>(game);

  function setNewGameState(gameState: GameState) {
    prevStage.current = gameState.stage;
    setGameState(gameState);
  }

  const [crisis, setCrisis] = useLocalStorage<string>("crisis", "");

  const [cube1, setCube1] = useState<number>(Math.floor(Math.random() * 5 + 1));
  const [rotation1, setRotation1] = useState<number>(
    Math.floor(Math.random() * 30 + 1)
  );
  const [cube2, setCube2] = useState<number>(Math.floor(Math.random() * 5 + 1));
  const [rotation2, setRotation2] = useState<number>(
    Math.floor(Math.random() * 30 + 1)
  );

  const [showMarket, setShowMarket] = useState(currentStage === "emptyScreen");
  const [showEnd, setShowEnd] = useState(false);

  useEffect(() => {
    const fetchGeneratedCrisis = async (gameId: number) => {
      if (gameId !== 0) {
        const result = await generateCrisis(gameId);
        setCrisis(result.description);
      }
    };

    if (showMarket === false) {
      setCurrentStage("crisis");
      fetchGeneratedCrisis(gameId);
    }
  }, [showMarket]);

  useEffect(() => {
    setShowMarket(currentStage === "emptyScreen");
  }, [currentStage]);

  useEffect(() => {
    if (gameId !== 0) {
      const wrap = async () => {
        setShownGame(await state(gameId));
      };

      wrap();
    }
  }, [gameId]);

  const [showPresentation, setShowPresentation] = useState(false);

  useEffect(() => {
    console.log(prevStage.current);
    if (
      prevStage.current !== 0 &&
      prevStage.current !== 1 &&
      prevStage.current !== undefined
    )
      setShowPresentation(true);
  }, [prevStage.current]);

  const stage = {
    emptyScreen: <></>,
    crisis: (
      <div className="flex flex-col">
        <HeadedBlock header="Кризис">{crisis}</HeadedBlock>
        <CrisisDecision
          inputValue={crisisDecision}
          inputOnChange={setCrisisDecision}
          onClick={async () => {
            setDecision(await evaluateDesicion(gameId, crisisDecision));
            setCrisisDecision("");
            setNewGameState(await state(gameId));
            setCurrentStage("diceRoll");
            if (gameState.finalScore !== null) {
              setShowEnd(true);
            }
          }}
        />
      </div>
    ),
    diceRoll: (
      <div className="flex justify-center items-center w-full h-full">
        <DiceRoller
          cube1={cube1}
          cube2={cube2}
          rotation1={rotation1}
          rotation2={rotation2}
          onClick={() => {
            let count = 0;
            const interval = setInterval(() => {
              count++;
              console.log(`Событие ${count}`);
              setCube1(Math.floor(Math.random() * 5 + 1));
              setCube2(Math.floor(Math.random() * 5 + 1));
              setRotation1(rotation1 + Math.floor(Math.random() * 150 + 1));
              setRotation2(rotation1 + Math.floor(Math.random() * 150 + 1));

              if (count === 3) {
                setCube1(decision?.roll.firstCubeRoll!);
                setCube2(decision?.roll.secondCubeRoll!);
              }

              if (count >= 4) {
                clearInterval(interval);
                setCurrentStage("diceResult");
                setShownGame(gameState);
              }
            }, 800);
          }}
        />
      </div>
    ),
    diceResult: (
      <div className="flex flex-col gap-8">
        <HeadedBlock header="Ваше решение">
          {shownGame.situationText}
        </HeadedBlock>
        <DiceResult
          color={
            decision?.roll.diceTotal! <= 4
              ? Color.Danger
              : decision?.roll.diceTotal! >= 8
              ? Color.Success
              : Color.Warning
          }
          cube1={decision?.roll.firstCubeRoll!}
          cube2={decision?.roll.secondCubeRoll!}
          text="Круто!"
          diceResult={decision?.roll.diceTotal ?? 0}
          onClick={async () => {
            if (showPresentation || gameState.turnNumber === 18) {
              setCurrentStage("presentation");
              const result = await generateCrisis(gameId);
              setCrisis(result.description);
              setShowPresentation(false);
            } else {
              setCurrentStage("emptyScreen");
            }
          }}
        />
      </div>
    ),
    presentation: (
      <div className="flex flex-col content-center max-h-96">
        <HeadedBlock header="Презентация">
          Презентуйте себя и свой проект инвесторам!
        </HeadedBlock>
        <CrisisDecision
          inputValue={presentationDecision}
          inputOnChange={setPresentationDecision}
          onClick={async () => {
            setDecision(
              await evaluatePresentation(gameId, presentationDecision)
            );
            setNewGameState(await state(gameId));

            if (gameState.finalScore !== null) {
              setShowEnd(true);
            }

            setCurrentStage("emptyScreen");
          }}
        />
      </div>
    ),
  };
  type Stages = keyof typeof stage;

  return (
    <div
      className="flex flex-row gap-1 h-screen w-screen"
      style={{ background: deafultBackground }}
    >
      <div className="flex flex-col gap-1 basis-[20%] shrink-0 min-h-0 overflow-auto min-w-[185px]">
        <CommandBlock
          commandName={shownGame.companyName}
          commandPic={banana}
          mission={
            "Упростить управление личными финансами через инновационные цифровые решения"
          }
          sphere={sphere}
        />
        <TimeBlock
          current={shownGame.monthsPassed}
          end={30}
          title="Время"
          startTitle={`${shownGame.monthsPassed} мес.`}
          endTitle="30 мес."
        />
        <Statistics
          techReadiness={shownGame.technicReadiness}
          productReadiness={shownGame.productReadiness}
          motivation={shownGame.motivation}
          reputation={0}
          modificators={shownGame.superEmployees.map(() => {
            return { picture: defaultPicture };
          })}
          employees={[]}
          actives={[]}
        />
      </div>
      <div className="flex w-full h-full">
        <Block grow={1}>
          <div className="flex flex-row gap-10 pl-10">
            <div className="flex-grow overflow-hidden">
              {stage[currentStage]}
            </div>
            <div className="flex flex-col gap-3 flex-none">
              <GameFieldValue
                inCircle="$"
                tooltip={shownGame.money}
              ></GameFieldValue>
              <GameFieldValue
                inCircle={`${shownGame.stage}/3`}
                tooltip="Этап"
              ></GameFieldValue>
            </div>
          </div>
        </Block>
      </div>
      {showMarket && (
        <Overlay
          setOpen={() => setShowMarket(false)}
          color={topBackground}
          strokeColor="#27262A"
        >
          {/* <div className="flex flex-col gap-4 font-inter">
            <div className="text-2xl font-extrabold">
              Ваш проект нравится инвесторам!
            </div>
            <div className="text-base font-extrabold">Вы получаете:</div>
            <div>
              Кризис показал, что команда умеет быстро реагировать и брать
              ответственность. Пользователи оценили честность, доверие удалось
              сохранить. Внутри команды стали строже относиться к проверкам и
              тестированию.
            </div>
          </div> */}
          <Market
            updateState={async () => {
              if (gameId !== 0) {
                setNewGameState(await state(gameId));
                game = { ...gameState };
              }
            }}
            gameId={gameId}
            stage={shownGame.stage}
            onClick={() => setShowMarket(false)}
          ></Market>
        </Overlay>
      )}
      {showEnd && (
        <Overlay
          setOpen={() => setShowEnd(false)}
          color={
            gameState.finalScore.totalScore > 60
              ? Color["Success"]
              : Color["Warning"]
          }
          strokeColor={
            gameState.finalScore.totalScore > 60
              ? Color["Success"]
              : Color["Warning"]
          }
        >
          <div>{gameState.finalScore.totalScore} / 100</div>
        </Overlay>
      )}
    </div>
  );
};
export default MainScreenLayout;
