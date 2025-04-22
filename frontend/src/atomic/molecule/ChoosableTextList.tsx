import ChoosableText from "../atom/ChoosableText";
import { ChoosableTextProps } from "../../shared/Types";

const ChoosableTextList: React.FC<{ choosableList: ChoosableTextProps[] }> = ({
    choosableList,
}) => (
  <div className="flex flex-col gap-3 items-center">
    {choosableList.map((props) => (
      <ChoosableText
        {...props}
      >{props.children}</ChoosableText>
    ))}
  </div>
);

export default ChoosableTextList;
