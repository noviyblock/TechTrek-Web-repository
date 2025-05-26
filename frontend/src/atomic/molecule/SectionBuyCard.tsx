import { ReactElement } from "react";
import { Color, deafultBackground } from "../../shared/Color";
import ProfileButton from "../atom/ProfileButton";
import MediumButton from "../atom/MediumButton";

export enum sectionType {
  DEVELOPER,
  C_LEVEL,
  OFFICE
}

const sectionDescriptions = {
  [sectionType.DEVELOPER]: "Уменьшает время разработки, ускоряет техническую и продуктовую готовность",
  [sectionType.C_LEVEL]: "Влияет на стратегические показатели и усиливает команду в критических ситуациях",
  [sectionType.OFFICE]: "Долгосрочное вложение — улучшает инфраструктуру и открывает новые возможности",
};

const sectionName = {
  [sectionType.DEVELOPER]: "Нанять сотрудника в штат",
  [sectionType.C_LEVEL]: "Нанять ключевого специалиста",
  [sectionType.OFFICE]: "Приобрести актив",
}

const SectionBuyCard: React.FC<{
  section: sectionType;
  onClick: React.MouseEventHandler<HTMLButtonElement>;
}> = ({ section, onClick }) => 
<div className="flex flex-col justify-between font-inter text-white p-4 max-w-[15rem] items-center min-h-80 border-solid border-white border-[1px]" style={{background: Color["DefaultAccent"]}}>
  <div className="flex flex-col gap-2">
  <div className="text-2xl">{sectionName[section]}
  </div>
  <div className="text-sm">{sectionDescriptions[section]}</div>
  </div>
  <ProfileButton height={30} color="Primary" onClick={onClick}>Выбрать</ProfileButton>
</div>;
export default SectionBuyCard;
