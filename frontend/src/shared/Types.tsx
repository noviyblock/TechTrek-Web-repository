import { Color } from "./Color";
import { Active, Employee, Modificator } from "./GameTypes";

export interface ButtonProps {
  children?: React.ReactNode;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  color?: keyof typeof Color;
  width?: number;
}

export interface InputProps {
  children?: React.ReactNode;
  placeholder?: string;
  type?: string;
}

export interface LinkProps {
  children?: React.ReactNode;
  href?: string;
  color?: keyof typeof Color;
}

export interface CardButtonProps {
  tooltip: React.ReactNode;
  name: string;
  onClick?: React.MouseEventHandler<HTMLDivElement>;
  imagePath?: string;
  color?: keyof typeof Color;
  width?: string;
}

export interface ChoosableTextProps {
  children?: React.ReactNode;
  color: keyof typeof Color;
  borderColor?: keyof typeof Color;
  onClick?: React.MouseEventHandler<HTMLDivElement>;
  width?: string;
}

export interface ProgressProps {
  end: number;
  current: number;
  color: keyof typeof Color;
  changeColor?: boolean;
}

export interface TitledProgressProps extends ProgressProps {
  title: string;
  textColor: keyof typeof Color;
}

export interface ExtraTitledProgressProps extends TitledProgressProps {
  startTitle: string;
  endTitle: string;
}

export interface Picture {
  image: string;
  rounding: number;
}

export interface StackedPicture extends Picture {}

export interface Participant {
  name: string;
  picture: string;
}

export interface CommandBlockProps {
  commandName: string;
  commandPic: string;
  participants: Participant[];
}

export interface TimeBlockProps {
  current: number;
  end: number;
  title: string;
  startTitle: string;
  endTitle: string;
}

export interface StatisticsProps {
  /**
   * value [0; 100]
   */
  techReadiness: number;
  productReadiness: number;
  motivation: number;
  reputation: number;

  modificators: Modificator[];
  employees: Employee[];
  actives: Active[];
}

export interface ContrainerListProps {
  elements: React.ReactNode[];
}