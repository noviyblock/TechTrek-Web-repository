import { Color } from "../../shared/Color";
import Block from "../molecule/Block";
import CommandBlock from "../organism/CommandBlock";
import Statistics from "../organism/StatisticsBlock";
import TimeBlock from "../organism/TimeBlock";
import banana from "../../shared/banana.jpeg";
import {
  actives,
  employees,
  modificators,
  participants,
} from "../../shared/constants";

const MainScreenLayout: React.FC = () => (
  <div
    className="flex flex-row gap-1 h-screen w-screen"
    style={{ background: Color.DefaultAccent }}
  >
    <div className="flex flex-col gap-1 basis-[20%] shrink-0 min-h-0 overflow-auto min-w-[185px]">
      <CommandBlock
        commandName="Lazarus"
        commandPic={banana}
        participants={participants}
      />
      <TimeBlock
        current={6}
        end={30}
        title="Время"
        startTitle="6 мес."
        endTitle="30 мес."
      />
      <Statistics
        techReadiness={30}
        productReadiness={40}
        motivation={80}
        reputation={60}
        modificators={modificators}
        employees={employees}
        actives={actives}
      />
    </div>
    <div className="flex-1 w-full h-full">
      <Block>
        <div className=""></div>
      </Block>
    </div>
  </div>
);

export default MainScreenLayout;
