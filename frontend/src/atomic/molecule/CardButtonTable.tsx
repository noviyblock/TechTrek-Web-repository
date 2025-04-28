import CardButton from "../atom/CardButton";
import { CardButtonProps } from "../../shared/Types";

const CardButtonTable: React.FC<{ categories: CardButtonProps[] }> = ({
  categories,
}) => (
  <div className="grid grid-cols-3 gap-5">
    {categories.map((props) => (
      <CardButton
        tooltip={props.tooltip}
        name={props.name}
        color={props.color}
        imagePath={props.imagePath}
        width={"100%"}
      ></CardButton>
    ))}
  </div>
);

export default CardButtonTable;
