import { Color, TextColor } from "../../shared/Color";
import { ChoosableTextProps } from "../../shared/Types";

const ChoosableText: React.FC<ChoosableTextProps> = ({
  children,
  color,
  borderColor,
  onClick,
  width,
}) => (
  <div
    style={{ background: Color[color], color: TextColor[color], border: borderColor === undefined ? 'none' : 'solid', borderWidth: 1, borderColor: Color[borderColor ?? 'Default']}}
    className="grid place-items-center w-10/12 text-center font-inter hover:cursor-pointer rounded-xl px-5 min-h-[4em] max-h-[4em]"
  >
    {children}
  </div>
);

export default ChoosableText;
