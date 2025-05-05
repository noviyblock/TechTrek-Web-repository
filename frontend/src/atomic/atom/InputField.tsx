import { InputProps } from "../../shared/Types";

const InputField: React.FC<InputProps> = ({ children, placeholder, type, value, onChange }) => (
  <input
    placeholder={placeholder}
    className="rounded-xl p-1 font-inter h-10 w-4/5"
    type={type}
    value={value}
    onChange={(e) => onChange(e.target.value)}
    required
  >
    {children}
  </input>
);

export default InputField;
