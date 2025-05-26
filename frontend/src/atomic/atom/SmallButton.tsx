import { Color, TextColor } from "../../shared/Color";
import { ButtonProps } from "../../shared/Types";

const SmallButton: React.FC<ButtonProps> = ({
  children,
  onClick,
  color = "Default",
  width,
  form,
}) => (
  <button
    style={{
      background: Color[color],
      color: `${TextColor[color]}`,
    }}
    className="p-3 rounded-full font-inter text-xs w-fit max-w-[33vw]"
    form={form}
    type={form ? "submit" : "button"}
    onClick={onClick}
  >
    {children}
  </button>
);

export default SmallButton;

// 12 14 16 fonts
