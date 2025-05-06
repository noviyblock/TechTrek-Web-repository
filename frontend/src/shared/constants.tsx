import { ReactNode } from "react";
import { Active, Employee, Modificator } from "./GameTypes";
import {
  CardButtonProps,
  ChoosableTextProps,
  Participant,
  Picture,
  StackedPicture,
} from "./Types";
import Logo from "./banana.jpeg";
import { GameState } from "../api/Game";

export const containerColor = "#121212";
export const containerBorderColor = "#3C3C3C";

export const defaultAnimationTimeMs = 700;
export const defaultPicture = Logo;

export const categories: CardButtonProps[] = [
  {
    name: "E-commerce",
    tooltip: (
      <div>
        СОЗДАЙ СТАРТАП В СФЕРЕ
        <br />
        ЭЛЕКТРОННОЙ ТОРГОВЛИ
      </div>
    ),
    imagePath: Logo,
    color: "Primary",
  },
  {
    name: "Fintech",
    tooltip: (
      <div>
        ПОЗНАКОМЬСЯ С ВЫЗОВАМИ
        <br />В ФИНАНСОВОМ СЕКТОРЕ
      </div>
    ),
    imagePath: Logo,
    color: "Warning",
  },
  {
    name: "Gamedev",
    tooltip: (
      <div>
        ПОЗНАКОМЬСЯ С ВЫЗОВАМИ
        <br />В ИГРОВОМ СЕКТОРЕ
      </div>
    ),
    imagePath: Logo,
    color: "Default",
  },
  {
    name: "MedTech",
    tooltip: (
      <div>
        СОЗДАЙ СТАРТАП В СФЕРЕ
        <br />
        МЕДИЦИНЫ ТОРГОВЛИ
      </div>
    ),
    imagePath: Logo,
    color: "Danger",
  },
  {
    name: "EdTech",
    tooltip: (
      <div>
        ПОЗНАКОМЬСЯ С ВЫЗОВАМИ
        <br />В СФЕРЕ ОБРАЗОВАНИЯ
      </div>
    ),
    imagePath: Logo,
    color: "Secondary",
  },
  {
    name: "AgroTech",
    tooltip: (
      <div>
        ПОЗНАКОМЬСЯ С ВЫЗОВАМИ
        <br />В АГРАРНОМ СЕКТОРЕ
      </div>
    ),
    imagePath: Logo,
    color: "Success",
  },
];

export const backendURL = "";

export const choosableList: ChoosableTextProps[] = [
  {
    color: "White",
    children: (
      <div>
        Упростить управление личными финансами через инновационные цифровые
        решения
      </div>
    ),
  },
  {
    color: "White",
    children: (
      <div>
        Создать стартап по управлению личными финансами (AI-финансовый
        ассистент)
      </div>
    ),
  },
  {
    color: "White",
    children: (
      <div>
        Платформа для P2P-кредитования (децентрализованные финансы — DeFi)
      </div>
    ),
  },
  {
    color: "DefaultAccent",
    children: <div>Свой вариант стартапа</div>,
    borderColor: "White",
  },
];

export const pictures: Picture[] = [
  {
    image: Logo,
    rounding: 12,
  },
  {
    image: Logo,
    rounding: 12,
  },
  {
    image: Logo,
    rounding: 12,
  },
  {
    image: Logo,
    rounding: 12,
  },
];

export const stackedPictures: StackedPicture[] = [
  {
    image: Logo,
    rounding: 12,
  },
  {
    image: Logo,
    rounding: 12,
  },
  {
    image: Logo,
    rounding: 12,
  },
  {
    image: Logo,
    rounding: 12,
  },
];

export const participants: Participant[] = [
  {
    name: "1",
    picture: Logo,
  },
  {
    name: "2",
    picture: Logo,
  },
  {
    name: "3",
    picture: Logo,
  },
];

export const modificators: Modificator[] = [
  {
    picture: Logo,
  },
  {
    picture: Logo,
  },
  {
    picture: Logo,
  },
];

export const employees: Employee[] = [
  {
    picture: Logo,
  },
  {
    picture: Logo,
  },
  {
    picture: Logo,
  },
];

export const actives: Active[] = [
  {
    picture: Logo,
  },
];

export const elements: ReactNode[] = [
  <div className="text-white font-inter">
    <b>Целевая аудитория</b>
    <ul className="">
      <li>
        <b>Миллениалы и Gen Z:</b> Техно-подкованные, хотят управлять деньгами
        без сложностей.
      </li>
      <li>
        <b>Фрилансеры:</b> Важен контроль финансов и планирование налогов.
      </li>
      <li>
        <b>Молодые семьи:</b> Совместный учёт, накопления и оптимизация бюджета.
      </li>
      <li>
        <b>Малый бизнес:</b> Учёт финансов, автоматизация, банки.
      </li>
      <li>
        <b>Финграмотные:</b> Контроль денег, инвестиции, обучение.
      </li>
    </ul>
  </div>,
  <div className="text-white font-inter">
    <ul className="">
      <li>Удобное управление деньгами.</li>
      <li>Автоматизация бюджета и расходов.</li>
      <li>Накопление и инвестирование.</li>
      <li>Интеграция с банками и сервисами.</li>
      <li>Финансовое образование.</li>
    </ul>
  </div>,
];

export const nullGameState: GameState = {
  gameId: 0,
  companyName: "",
  stage: 0,
  turnNumber: 0,
  monthsPassed: 0,
  missionId: 0,
  money: 0,
  technicReadiness: 0,
  productReadiness: 0,
  motivation: 0,
  juniors: 0,
  middles: 0,
  seniors: 0,
  superEmployees: [],
  numberOfOffices: 0,
  situationText: ""
}
