import { InputProps } from "../../shared/Types";

const InputField: React.FC<InputProps> = ({ children, placeholder, type }) => (
  <input
    placeholder={placeholder}
    className="rounded-xl p-1 font-inter h-10 w-4/5"
    type={type}
  >
    {children}
  </input>
);

export default InputField;
