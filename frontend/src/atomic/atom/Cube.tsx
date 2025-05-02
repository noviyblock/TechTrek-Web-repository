const Cube: React.FC<{ amount: number, rotation: string }> = ({ amount, rotation }) => {
  return (
    <div className={`absolute m-32 p-32 transiotion-all rotate-[0deg] duration-700 bg-red-600`}>
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-5 w-5 ${
            amount === 1 ? "left-1/2 top-1/2" : 
            amount === 2 ? "left-full top-0" : 
            amount === 3 ? "left-full top-full" : 
            amount === 4 ? "left-0 top-full" : 
            amount === 5 ? "left-0 top-0" : 
            amount === 6 ? "left-full top-1/2" : 
            "hidden"        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-5 w-5 ${
            amount === 1 ? "left-1/2 top-1/2" : 
            amount === 2 ? "left-0 top-full" : 
            amount === 3 ? "left-1/2 top-1/2" : 
            amount === 4 ? "left-0 top-0" : 
            amount === 5 ? "left-1/2 top-1/2" : 
            amount === 6 ? "left-full top-full" : 
            "hidden"
        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-5 w-5 ${
            amount === 1 ? "left-1/2 top-1/2" : 
            amount === 2 ? "left-full top-0" : 
            amount === 3 ? "left-1/2 top-1/2" : 
            amount === 4 ? "left-full top-full" : 
            amount === 5 ? "left-0 top-full" : 
            amount === 6 ? "left-0 top-0" : 
            "hidden"        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-5 w-5 ${
            amount === 1 ? "left-1/2 top-1/2" : 
            amount === 2 ? "left-0 top-full" : 
            amount === 3 ? "left-0 top-0" : 
            amount === 4 ? "left-full top-0" : 
            amount === 5 ? "left-full top-full" : 
            amount === 6 ? "top-1/2 left-0" : 
            "hidden"        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-5 w-5 ${
            amount === 1 ? "left-1/2 top-1/2" : 
            amount === 2 ? "left-0 top-full" : 
            amount === 3 ? "left-0 top-0" : 
            amount === 4 ? "left-full top-full" : 
            amount === 5 ? "left-full top-0" : 
            amount === 6 ? "left-0 top-full" : 
            "hidden"        }`}
      />
      <div
        className={`absolute transition-all duration-700 rounded-full bg-black h-5 w-5 ${
            amount === 1 ? "left-1/2 top-1/2" : 
            amount === 2 ? "left-full top-0" : 
            amount === 3 ? "left-full top-full" : 
            amount === 4 ? "left-0 top-full" : 
            amount === 5 ? "left-0 top-0" : 
            amount === 6 ? "left-full top-0" : 
            "hidden"    }`}
      />
    </div>
  );
};

export default Cube;
