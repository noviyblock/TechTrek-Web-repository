import ChoosableText from "../atom/ChoosableText";
import { ChoosableTextProps } from "../../shared/Types";

const ChoosableTextList: React.FC<{ choosableList: ChoosableTextProps[], onClick: (mission: number) => void}> = ({
  choosableList,
  onClick
}) => (
  <div className="flex flex-col gap-3 items-center">
    {choosableList.map((props) => (
      <ChoosableText {...props} onClick={() => { onClick(props.id!) }}>{props.children}</ChoosableText>
    ))}
  </div>
);

export default ChoosableTextList;
