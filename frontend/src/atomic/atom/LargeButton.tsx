import { Color, TextColor } from "../../shared/Color";
import { ButtonProps } from "../../shared/Types";

const LargeButton: React.FC<ButtonProps> = ({
  children,
  onClick,
  color = "Default",
  width,
}) => (
  <button
    onClick={onClick}
    style={{
      background: Color[color],
      color: `${TextColor[color]}`,
      width: width ?? "99%",
    }}
    className="p-5 rounded-full font-inter text-base"
  >
    {children}
  </button>
);

export default LargeButton;
