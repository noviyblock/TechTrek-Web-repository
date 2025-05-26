import { tooltipColor } from "../../shared/Color";

const TitledText: React.FC<{ header: string; text: string }> = ({
  header,
  text,
}) => (
  <div className="flex-col font-inter">
    <div className="text-gray-600 text-sm">{header}</div>
    <div className="text-white text-xl">{text}</div>
  </div>
);
export default TitledText;
