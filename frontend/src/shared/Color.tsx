export enum Color {
  Default = "#3F3F46",
  DefaultAccent = "#27272A",
  Primary = "#006FEE",
  Secondary = "#9353D3",
  Success = "#17C964",
  Warning = "#F5A524",
  Danger = "#F31260",
  White = "#FFFFFF",
  Black = "#000000",
  Progress = "#E4E4E7",
}

export const TextColor: Record<keyof typeof Color, string> = {
  Default: "#FFFFFF",
  DefaultAccent: "#FFFFFF",
  Primary: "#FFFFFF",
  Secondary: "#FFFFFF",
  Success: "#000000",
  Warning: "#000000",
  Danger: "#FFFFFF",
  White: "#000000",
  Black: "#FFFFFF",
  Progress: "#FFFFFF",
} as any;

export const deafultBackground = "#010000";
export const tooltipColor = "#949494";
export const strokeColor = "#3C3C3C";
export const defaultBlockColor = "#11181C";
