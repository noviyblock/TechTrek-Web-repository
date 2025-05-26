import { purchaseModifier } from "../../api/Game";
import { Color } from "../../shared/Color";
import { ModifierProps } from "../../shared/Types";
import ProfileButton from "../atom/ProfileButton";

const buyModifierDescription: Record<string, string> = {
  JUNIOR_DEV: "Выполняет простые задачи, быстро обучается и помогает команде",
  MIDDLE_DEV: "Уверенно решает задачи средней сложности и повышает эффективность",
  SENIOR_DEV: "Архитектор решений и наставник, отвечает за ключевые фичи",
  CEO: "Отвечает за общее руководство и стратегическое направление организации",
  CTO: "Руководит технической стратегией и развитием продукта",
  CMO: "Продвигает продукт на рынке и отвечает за рост аудитории",
  CPO: "Формирует продуктовую стратегию и управляет фичами",
  COWORKING: "Бюджетное рабочее место для старта проекта",
  SMALL_OFFICE: "Уютное пространство для небольшой команды",
  TECH_HUB: "Современный офис с ресурсами для роста и инноваций",
};

const BuyModifier: React.FC<ModifierProps> = ({ name, purchaseCost, upkeepCost, id, gameId, updateState}) => (
    <div
      className="flex flex-col gap-3 font-inter text-white rounded-3xl p-4 overflow-hidden h-full"
      style={{ background: Color["Default"], width: 238 }}
    >
      <div className="text-lg">{name.length === 3 ? name : (name.toLocaleUpperCase()[0] + name.slice(1).toLocaleLowerCase()).replaceAll("_", " ")}</div>

      <div className="text-sm flex-grow">{buyModifierDescription[name]}</div>

      <div className="flex flex-row gap-3">
        <div className="flex-grow">
          <div className="text-sm">Покупка</div>
          <div className="text-sm">{purchaseCost}</div>
        </div>
        <div className="flex-grow">
          <div className="text-sm">Содержание</div>
          <div className="text-sm">{upkeepCost}</div>
        </div>
      </div>

      <div className="flex flex-row gap-3">
        <ProfileButton height={30} color="LightGray">
          Уволить
        </ProfileButton>
        <ProfileButton height={30} color="Primary" onClick={async () => {await purchaseModifier(gameId, id); updateState()}}>
          Купить
        </ProfileButton>
      </div>
    </div>
);
export default BuyModifier;
