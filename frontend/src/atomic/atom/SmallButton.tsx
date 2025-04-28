import { Color, TextColor } from "../../shared/Color";
import { ButtonProps } from "../../shared/Types";

const SmallButton: React.FC<ButtonProps> = ({
  children,
  onClick,
  color = "Default",
  width,
}) => (
  <button
    style={{
      background: Color[color],
      color: `${TextColor[color]}`,
      width: width ?? "33%",
    }}
    className="p-3 rounded-full font-inter text-xs"
  >
    {children}
  </button>
);

export default SmallButton;

// 12 14 16 fonts
