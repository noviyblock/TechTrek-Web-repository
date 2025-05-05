import Cube from "../atom/Cube";
import SmallButton from "../atom/SmallButton";

const Overlay: React.FC<{
  children: React.ReactNode;
  color: string;
  strokeColor: string;
  setOpen: (open: boolean) => void;
}> = ({ children, setOpen, color, strokeColor }) => (
  <div
    className="fixed inset-0 opacity-80 flex items-center justify-center z-50 text-white font-inter"
    style={{ background: "#24242D" }}
  >
    <div
      className="flex flex-col p-6 rounded-lg opacity-1 shadow-lg z-60 gap-6 border-solid border-2 max-w-[600px]"
      style={{ background: color, borderColor: strokeColor }}
    >
      <button className="ml-auto" onClick={() => setOpen(false)}>
        X
      </button>
      <div>{children}</div>
    </div>
  </div>
);
export default Overlay;
