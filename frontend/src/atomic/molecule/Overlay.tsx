import Cube from "../atom/Cube";
import SmallButton from "../atom/SmallButton";

const Overlay: React.FC<{
  children: React.ReactNode;
  color: string;
  strokeColor: string;
  setOpen: (open: boolean) => void;
}> = ({ children, setOpen, color, strokeColor }) => (
  <div
    className="fixed inset-0 flex items-center justify-center z-50 text-white font-inter"
    style={{ background: "rgba(36, 36, 45, 0.8)" }}
    onClick={() => setOpen(false)}
  >
    <div
      className="flex flex-col p-6 rounded-lg opacity-100 shadow-lg z-60 gap-6 border-solid border-2 w-4/5"
      style={{ background: color, borderColor: strokeColor }}
      onClick={(e) => e.stopPropagation()}
    >
      {children}
    </div>
  </div>
);
export default Overlay;
