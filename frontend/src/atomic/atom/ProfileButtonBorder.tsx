import { ButtonProps } from "../../shared/Types";
import { Color } from "../../shared/Color";

const ProfileButtonBorder: React.FC<ButtonProps> = ({
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
      height: height,
      color: Color[color],
      width: width ?? "66%",
      borderColor: Color[color],
    }}
    className="border-solid border-[1px] rounded-full font-inter text-sm"
    form={form}
    type={form ? "submit" : "button"}
  >
    {children}
  </button>
);

export default ProfileButtonBorder;
