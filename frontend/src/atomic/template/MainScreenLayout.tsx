import { Color } from "../../shared/Color";
import Block from "../molecule/Block";
import CommandBlock from "../organism/CommandBlock";
import Statistics from "../organism/StatisticsBlock";
import TimeBlock from "../organism/TimeBlock";
import banana from "../../shared/banana.jpeg";
import {
  actives,
  defaultPicture,
  employees,
  modificators,
  participants,
} from "../../shared/constants";
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
import DecisionField from "../atom/DecisionField";
import { useEffect, useState } from "react";
import DiceRoller from "../organism/DiceRoller";
import DiceResult from "../organism/DiceResult";
import Overlay from "../molecule/Overlay";
import { useLocalStorage } from "../../LocalStorage";
import Market from "../organism/Market";
import CrisisDecision from "../molecule/CrisisDecision";
import { GameFields } from "../../api/services/GameService";

const MainScreenLayout: React.FC<{
  game: GameState;
  children: React.ReactNode;
}> = ({ game, children }) => {
  let prevStage = game.stage;
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
  const [decision, setDecision] = useLocalStorage<DecisionResponse | undefined>(
    GameFields.decision,
    undefined
  );
  const [gameState, setGameState] = useLocalStorage<GameState>(
    GameFields.gameState,
    game
  );
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
      const result = await generateCrisis(gameId);
      setCrisis(result.description);
    };

    if (showMarket === false && currentStage === "emptyScreen") {
      setCurrentStage("crisis");
      fetchGeneratedCrisis(game.gameId);
      
    }
  }, [showMarket]);

  useEffect(() => {
    setGameState(game);
    console.log(game.stage, prevStage);
  }, [game]);

  useEffect(() => {
    setShowMarket(currentStage === "emptyScreen");
  }, [currentStage]);

  const stage = {
    emptyScreen: <></>,
    crisis: (
      <div className="flex flex-col">
        <HeadedBlock header="Кризис">{crisis}</HeadedBlock>
        <CrisisDecision
          inputValue={crisisDecision}
          inputOnChange={setCrisisDecision}
          onClick={async () => {
            setDecision(await evaluateDesicion(game.gameId, crisisDecision));
            setGameState(await state(game.gameId));
            setCurrentStage("diceRoll");
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
            //xddddddddddd
            game.juniors = gameState.juniors;
            game.middles = gameState.middles;
            game.money = gameState.money;
            game.monthsPassed = gameState.monthsPassed;
            game.motivation = gameState.motivation;
            game.numberOfOffices = gameState.numberOfOffices;
            game.productReadiness = gameState.productReadiness;
            game.stage = gameState.stage;
            game.turnNumber = gameState.turnNumber;
            game.technicReadiness = gameState.technicReadiness;
            game.superEmployees = gameState.superEmployees;
            game.seniors = gameState.seniors;

            let count = 0;
            const interval = setInterval(() => {
              count++;
              console.log(`Событие ${count}`);
              setCube1(Math.floor(Math.random() * 5 + 1));
              setCube2(Math.floor(Math.random() * 5 + 1));
              setRotation1(rotation1 + Math.floor(Math.random() * 150 + 1));
              setRotation2(rotation1 + Math.floor(Math.random() * 150 + 1));

              if (count == 3) {
                setCube1(decision?.roll.firstCubeRoll!);
                setCube2(decision?.roll.secondCubeRoll!);
              }

              if (count >= 4) {
                clearInterval(interval);
                setCurrentStage("diceResult");
              }
            }, 800);
          }}
        />
      </div>
    ),
    diceResult: (
      <div className="flex flex-col gap-8">
        <HeadedBlock header="Ваше решение">
          {gameState.situationText}
        </HeadedBlock>
        <DiceResult
          cube1={decision?.roll.firstCubeRoll!}
          cube2={decision?.roll.secondCubeRoll!}
          text="Круто!"
          diceResult={decision?.roll.diceTotal ?? 0}
          onClick={async () => {
            if (game.stage != prevStage) {
              setCurrentStage("presentation");
            } else {
              setCurrentStage("emptyScreen");
            }
            prevStage = game.stage;
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
              await evaluatePresentation(game.gameId, crisisDecision)
            );
            setGameState(await state(game.gameId));

            if (game.stage === 3) {
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
      style={{ background: Color.DefaultAccent }}
    >
      <div className="flex flex-col gap-1 basis-[20%] shrink-0 min-h-0 overflow-auto min-w-[185px]">
        <CommandBlock
          commandName={gameState.companyName}
          commandPic={banana}
          participants={participants}
        />
        <TimeBlock
          current={gameState.monthsPassed}
          end={30}
          title="Время"
          startTitle={`${gameState.monthsPassed} мес.`}
          endTitle="30 мес."
        />
        <Statistics
          techReadiness={gameState.technicReadiness}
          productReadiness={gameState.productReadiness}
          motivation={gameState.motivation}
          reputation={0}
          modificators={gameState.superEmployees.map(() => {
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
                tooltip={gameState.money}
              ></GameFieldValue>
              <GameFieldValue
                inCircle={`${gameState.stage}/3`}
                tooltip="Этап"
              ></GameFieldValue>
            </div>
          </div>
        </Block>
      </div>
      {showMarket && (
        <Overlay
          setOpen={() => setShowMarket(false)}
          color="#171719"
          strokeColor="#171719"
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
              setGameState(await state(game.gameId));
              game = gameState;
            }}
            gameId={game.gameId}
            stage={game.stage}
            onClick={() => setShowMarket(false)}
          ></Market>
        </Overlay>
      )}
      {showEnd && (
        <Overlay
          setOpen={() => setShowEnd(false)}
          color={Color["Success"]}
          strokeColor={Color["Success"]}
        >
          <div>{gameState.finalScore.totalScore} / 100</div>
        </Overlay>
      )}
    </div>
  );
};
export default MainScreenLayout;
