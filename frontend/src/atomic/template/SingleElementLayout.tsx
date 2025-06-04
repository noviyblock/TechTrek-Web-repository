import { deafultBackground } from "../../shared/Color";
import { containerBorderColor, containerColor } from "../../shared/constants";

const SingleElementLayoutTemplate: React.FC<{
  children?: React.ReactNode;
  backable?: boolean;
  regenable?: boolean;
  width?: string;
  minWidth?: string;
  height?: string;
  onBack?: () => void;
  onRegen?: () => void;
}> = ({
  children,
  backable = false,
  regenable = false,
  width,
  minWidth,
  height,
  onBack,
  onRegen,
}) => (
  <div
    style={{ background: deafultBackground }}
    className="absolute inset-0 flex w-fit min-w-full h-fit min-h-full items-center justify-center"
  >
    <div
      style={{
        background: containerColor,
        borderRadius: 21,
        borderStyle: "solid",
        borderColor: containerBorderColor,
        borderWidth: 3,
        width: width,
        minWidth: minWidth,
        height: height,
      }}
      className="relative overflow-hidden"
    >
      <div
        className="absolute top-0 left-0 p-3 text-white cursor-pointer"
        style={{ display: !backable ? "none" : "block" }}
        onClick={onBack}
      >
        Back
      </div>
      <div
        className="absolute top-0 right-0 p-3 text-white cursor-pointers"
        style={{ display: !regenable ? "none" : "block" }}
        onClick={onRegen}
      >
        Regen
      </div>

      {children}
    </div>
  </div>
);

export default SingleElementLayoutTemplate;
