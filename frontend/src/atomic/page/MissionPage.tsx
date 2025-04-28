import ChoosableTextList from "../molecule/ChoosableTextList";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";
import { choosableList } from "../../shared/constants";

const MissionPage: React.FC = () => (
  <SingleElementLayoutTemplate
    backable={true}
    regenable={true}
    width="75%"
    minWidth="700px"
  >
    <div className="flex flex-col px-20 p-8 gap-14 ">
      <div className="text-white font-inter text-center">
        Отлично! Вы вбрали сферу “Fintech”.
        <br />
        Теперь выберите ваши цель и миссию
      </div>
      <ChoosableTextList choosableList={choosableList} />
    </div>
  </SingleElementLayoutTemplate>
);

export default MissionPage;
