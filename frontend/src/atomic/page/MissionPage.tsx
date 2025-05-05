import ChoosableTextList from "../molecule/ChoosableTextList";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";
import { ChoosableTextProps } from "../../shared/Types";


const MissionPage: React.FC<{missions: ChoosableTextProps[], onClick: (mission: number) => void}> = ({missions, onClick}) => { 
  return (
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
      <ChoosableTextList choosableList={missions} onClick={onClick} />
    </div>
  </SingleElementLayoutTemplate>
);
}

export default MissionPage;
