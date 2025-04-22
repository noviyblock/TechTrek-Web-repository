import { Color, TextColor } from "../../shared/Color";
import { CardButtonProps } from "../../shared/Types";
import Logo from "../../shared/banana.jpeg";

const CardButton: React.FC<CardButtonProps> = ({
  tooltip,
  name,
  onClick,
  imagePath = Logo,
  color = "Default",
  width = "33%",
}) => (
  <div
    onClick={onClick}
    className="border-[1px] border-solid border-white hover:cursor-pointer p-4 min-w-[250px] overflow-auto"
    style={{ background: Color[color], width: width }}
  >
    <div style={{ color: `${TextColor[color]}` }} className="font-inter p-3">
      <div className="opacity-60 text-xs">{tooltip}</div>
      <div className="text-3xl font-medium">{name}</div>
    </div>
    <img src={imagePath} className="rounded-3xl p-3" alt={name} />
  </div>
);

export default CardButton;
