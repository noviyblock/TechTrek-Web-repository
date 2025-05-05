import CardButton from "../atom/CardButton";
import { CardButtonProps } from "../../shared/Types";

const CardButtonTable: React.FC<{
  categories: CardButtonProps[],
  onClick: (sphere: number) => void,
}> = ({ categories, onClick }) => (
  <div className="grid grid-cols-3 gap-5">
    {categories.map((props) => (
      <CardButton
        tooltip={props.tooltip}
        name={props.name}
        color={props.color}
        imagePath={props.imagePath}
        width={"100%"}
        onClick={() => {
          onClick(props.id!);
        }}
      ></CardButton>
    ))}
  </div>
);

export default CardButtonTable;
