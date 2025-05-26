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

const MainScreenLayout: React.FC<{
  game: GameState;
  children: React.ReactNode;
}> = ({ game, children }) => {
  const gameS: GameState = {
    gameId: 42,
    companyName: "BugHunters Inc.",
    stage: 1,
    turnNumber: 7,
    monthsPassed: 14,
    missionId: 3,

    money: 1000,
    technicReadiness: 55,
    productReadiness: 64,
    motivation: 70,

    juniors: 2,
    middles: 3,
    seniors: 1,

    superEmployees: ["Антон Архитектор"],
    numberOfOffices: 1,

    situationText:
      "Тесты прошли, демо выжило. Но на проде — апокалипсис. MVP взорвался у первых реальных пользователей. Ошибка в логике, баг в форме заказа — и теперь всё, что вы обещали, звучит как шутка. Надо чинить… и как можно быстрее.",
  };
  const ans = {
    motivationDelta: +5,
    newMotivation: 75,

    technicalDelta: +7,
    newTechnicalReadiness: 62,

    productDelta: +4,
    newProductReadiness: 68,

    moneyDelta: -3,
    newMoney: 97,

    textToPlayer:
      "Команда справилась с багом быстро и честно, что укрепило доверие пользователей. Вы потеряли немного денег из-за временной приостановки, но избежали репутационной катастрофы. Code review теперь стали не рутиной, а страховкой от повторения такого фокапа.",

    qualityScore: 82,
    roll: "10",
  };
  const [showOverlay, setShowOverlay] = useState(false);
  const [crisisDecision, setCrisisDecision] = useLocalStorage<string>(
    "crisisDecision",
    ""
  );
  const [presentationDecision, setPresentationDecision] =
    useLocalStorage<string>("presentationDecision", "");
  const [currentStage, setCurrentStage] = useLocalStorage<Stages>(
    "currentStage",
    "crisis"
  );
  const [decision, setDecision] = useLocalStorage<DecisionResponse | undefined>(
    "decision",
    undefined
  );
  const [newGame, setNewGame] = useLocalStorage<GameState>("newGame", game);

  const stage = {
    crisis: (
      <div>
        <HeadedBlock header="Кризис">{gameS.situationText}</HeadedBlock>
        <DecisionField
          inputValue={crisisDecision}
          inputOnChange={setCrisisDecision}
          submit={async () => {
            setDecision(await evaluateDesicion(game.gameId, crisisDecision));
            setNewGame(await state(game.gameId));

            setCurrentStage("diceRoll");
          }}
        ></DecisionField>
      </div>
    ),
    diceRoll: (
      <div className="flex justify-center items-center w-full h-full">
        <DiceRoller
          onClick={() => {
            //xddddddddddd
            game.juniors = newGame.juniors;
            game.middles = newGame.middles;
            game.money = newGame.money;
            game.monthsPassed = newGame.monthsPassed;
            game.motivation = newGame.motivation;
            game.numberOfOffices = newGame.numberOfOffices;
            game.productReadiness = newGame.productReadiness;
            game.stage = newGame.stage;
            game.turnNumber = newGame.turnNumber;
            game.technicReadiness = newGame.technicReadiness;
            game.superEmployees = newGame.superEmployees;
            game.seniors = newGame.seniors;
            setCurrentStage("diceResult");
          }}
        />
      </div>
    ),
    diceResult: (
      <div className="flex flex-col gap-8">
        <HeadedBlock header="Кризис">{gameS.situationText}</HeadedBlock>
        <DiceResult
          diceResult={decision?.roll.diceTotal ?? 0}
          onClick={async () => {
            setCurrentStage("presentation");
          }}
        />
      </div>
    ),
    presentation: (
      <div className="flex flex-col content-center">
        <HeadedBlock header="Презентация">
          Презентуйте себя и свой проект инвесторам!
        </HeadedBlock>
        <DecisionField
          inputValue={presentationDecision}
          inputOnChange={setPresentationDecision}
          submit={() => {
            setShowOverlay(true);
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
          commandName={game.companyName}
          commandPic={banana}
          participants={participants}
        />
        <TimeBlock
          current={game.monthsPassed}
          end={30}
          title="Время"
          startTitle={`${game.monthsPassed} мес.`}
          endTitle="30 мес."
        />
        <Statistics
          techReadiness={game.technicReadiness}
          productReadiness={game.productReadiness}
          motivation={game.motivation}
          reputation={0}
          modificators={game.superEmployees.map(() => {
            return { picture: defaultPicture };
          })}
          employees={[]}
          actives={[]}
        />
      </div>
      <div className="flex w-full h-full">
        <Block grow={1}>
          <div className="flex flex-row gap-10 pl-10">
            <div className="flex-grow">{stage[currentStage]}</div>
            <div className="flex flex-col gap-3 flex-none">
              <GameFieldValue
                inCircle="$"
                tooltip={game.money}
              ></GameFieldValue>
              <GameFieldValue
                inCircle={`${game.stage}/3`}
                tooltip="Этап"
              ></GameFieldValue>
            </div>
          </div>
        </Block>
      </div>
      {showOverlay && (
        <Overlay
          setOpen={() => setShowOverlay(false)}
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
          <Market gameId={game.gameId} stage={game.stage} onClick={() => setShowOverlay(false)}></Market>
        </Overlay>
      )}
    </div>
  );
};
export default MainScreenLayout;
