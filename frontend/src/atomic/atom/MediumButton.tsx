import { ButtonProps } from "../../shared/Types";
import { Color, TextColor } from "../../shared/Color";

const MediumButton: React.FC<ButtonProps> = ({
  children,
  onClick,
  color = "Default",
  width,
  form,
  height=4,
}) => (
  <button
    onClick={onClick}
    style={{
      background: Color[color],
      color: `${TextColor[color]}`,
    }}
    className="p-4 rounded-full font-inter text-sm max-w-[66vw] w-2/3"
    form={form}
    type={form ? "submit" : "button"}
  >
    {children}
  </button>
);

export default MediumButton;
