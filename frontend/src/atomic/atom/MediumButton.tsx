import { ButtonProps } from "../../shared/Types";
import { Color, TextColor } from "../../shared/Color";

const MediumButton: React.FC<ButtonProps> = ({
  children,
  onClick,
  color = "Default",
  width,
  form,
}) => (
  <button
    onClick={onClick}
    style={{
      background: Color[color],
      color: `${TextColor[color]}`,
      width: width ?? "66%",
    }}
    className="p-4 rounded-full font-inter text-sm"
    form={form}
    type={form ? "submit" : "button"}
  >
    {children}
  </button>
);

export default MediumButton;
