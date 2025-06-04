import { ReactNode } from "react";
import { Color, TextColor } from "../../shared/Color";

const ColoredText: React.FC<{ children: ReactNode; background: keyof typeof Color; className?: string; px?: string}> = ({
  children,
  background,
  className,
  px = "12px"
}) => (
  <div className={className + ` font-inter rounded-2xl w-fit`} style={{backgroundColor: Color[background], color: TextColor[background], paddingLeft: px, paddingRight: px}}>{children}</div>
);

export default ColoredText;
