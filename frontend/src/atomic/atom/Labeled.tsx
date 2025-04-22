import { tooltipColor } from "../../shared/Color";

const Labeled: React.FC<{ children: React.ReactNode, label: string }> = ({ children, label }) => 
    <div className="flex flex-col gap-2">
        {children}
        <div className="font-inter text-xs" style={{color: tooltipColor}}>{label}</div>
    </div>
export default Labeled;