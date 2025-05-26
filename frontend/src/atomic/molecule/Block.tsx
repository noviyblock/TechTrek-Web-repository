import { defaultBlockColor } from "../../shared/Color";

const Block: React.FC<{ children: React.ReactNode, grow: number, h?: string }> = ({ children, grow, h }) => (
  <div
    className={`rounded-2xl p-[15px] w-full`}
    style={{ background: defaultBlockColor, flexGrow: grow }}
  >
    {children}
  </div>
);
export default Block;
