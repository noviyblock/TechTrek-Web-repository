import { TimeBlockProps } from "../../shared/Types";
import Block from "../molecule/Block";
import ExtraTitledProgress from "../molecule/ExtraTitledProgress";

const TimeBlock: React.FC<TimeBlockProps> = ({
  current,
  end,
  title,
  startTitle,
  endTitle,
}) => (
  <Block>
    <ExtraTitledProgress
      current={current}
      end={end}
      textColor="White"
      color="DefaultAccent"
      title={title}
      startTitle={startTitle}
      endTitle={endTitle}
      changeColor={false}
    />
  </Block>
);

export default TimeBlock;
