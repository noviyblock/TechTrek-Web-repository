import { defaultBlockColor } from "../../shared/Color";

const Block: React.FC<{ children: React.ReactNode }> = ({ children }) => 
    <div className="rounded-2xl p-[15px] w-full h-full" style={{background: defaultBlockColor}}> 
        {children}
    </div>
export default Block;