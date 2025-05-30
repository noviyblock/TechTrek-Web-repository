import { Color, TextColor } from "../../shared/Color";
import { ButtonProps } from "../../shared/Types";

const LargeButton: React.FC<ButtonProps> = ({
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
    }}
    className="p-5 rounded-full font-inter text-base w-fit max-w-[99vw]"
    form={form}
    type={form ? "submit" : "button"}
  >
    {children}
  </button>
);

export default LargeButton;
