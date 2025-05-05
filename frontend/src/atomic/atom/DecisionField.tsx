import { LinkProps } from "../../shared/Types";
import InputField from "./InputField";
import SmallButton from "./SmallButton";

const DecisionField: React.FC<{
  inputValue: string;
  inputOnChange: (event: string) => void;
  submit: () => void;
}> = ({ inputValue, inputOnChange, submit }) => (
  <div
    className="flex flex-col gap-3 p-3 items-center"
    style={{ background: "#18181B" }}
  >
    <div className="font-inter text-white">
      Укажите свое решение текстом или голосом
    </div>
    <InputField value={inputValue} onChange={inputOnChange} />
    <SmallButton color="Primary" onClick={submit}>Отправить</SmallButton>
  </div>
);
export default DecisionField;
