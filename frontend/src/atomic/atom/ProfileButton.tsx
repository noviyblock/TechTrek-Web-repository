import { ButtonProps } from "../../shared/Types";
import { Color, TextColor } from "../../shared/Color";

const ProfileButton: React.FC<ButtonProps> = ({
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
      height: height,
      color: `${TextColor[color]}`,
      width: width ?? "66%",
    }}
    className="rounded-full font-inter text-sm"
    form={form}
    type={form ? "submit" : "button"}
  >
    {children}
  </button>
);

export default ProfileButton;
