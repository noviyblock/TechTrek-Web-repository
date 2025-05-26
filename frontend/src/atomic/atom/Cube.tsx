const Cube: React.FC<{ amount: number; rotation: number }> = ({
  amount,
  rotation,
}) => {
  return (
    <div
      className={`absolute p-6 mt-0 ml-0 transition-all duration-700 border-solid border-2 bg-white h-fit`}
      style={{transform: `rotate(${rotation}deg)`}}
    >
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-3 w-3 ${
          amount === 1
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 2
            ? "left-full top-0 -translate-x-3"
            : amount === 3
            ? "left-full top-full -translate-x-3 -translate-y-3"
            : amount === 4
            ? "left-0 top-full -translate-y-3"
            : amount === 5
            ? "left-0 top-0"
            : amount === 6
            ? "left-full top-1/2 -translate-x-3 -translate-y-1.5"
            : "hidden"
        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-3 w-3 ${
          amount === 1
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 2
            ? "left-0 top-full -translate-y-3"
            : amount === 3
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 4
            ? "left-0 top-0"
            : amount === 5
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 6
            ? "left-full top-full -translate-x-3 -translate-y-3"
            : "hidden"
        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-3 w-3 ${
          amount === 1
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 2
            ? "left-full top-0 -translate-x-3"
            : amount === 3
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 4
            ? "left-full top-full -translate-x-3 -translate-y-3"
            : amount === 5
            ? "left-0 top-full -translate-y-3"
            : amount === 6
            ? "left-0 top-0"
            : "hidden"
        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-3 w-3 ${
          amount === 1
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 2
            ? "left-0 top-full -translate-y-3"
            : amount === 3
            ? "left-0 top-0"
            : amount === 4
            ? "left-full top-0 -translate-x-3"
            : amount === 5
            ? "left-full top-full -translate-x-3 -translate-y-3"
            : amount === 6
            ? "top-1/2 left-0 -translate-y-1.5"
            : "hidden"
        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-3 w-3 ${
          amount === 1
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 2
            ? "left-0 top-full -translate-y-3"
            : amount === 3
            ? "left-0 top-0"
            : amount === 4
            ? "left-full top-full -translate-x-3 -translate-y-3"
            : amount === 5
            ? "left-full top-0 -translate-x-3"
            : amount === 6
            ? "left-0 top-full -translate-y-3"
            : "hidden"
        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-3 w-3 ${
          amount === 1
            ? "left-1/2 top-1/2 -translate-x-1.5 -translate-y-1.5"
            : amount === 2
            ? "left-full top-0 -translate-x-3"
            : amount === 3
            ? "left-full top-full -translate-x-3 -translate-y-3"
            : amount === 4
            ? "left-0 top-full -translate-y-3"
            : amount === 5
            ? "left-0 top-0"
            : amount === 6
            ? "left-full top-0 -translate-x-3"
            : "hidden"
        }`}
      />
      </div>
  );
};

export default Cube;
