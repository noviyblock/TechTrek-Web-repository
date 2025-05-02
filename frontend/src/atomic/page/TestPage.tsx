import { useEffect, useState } from "react";
import Cube from "../atom/Cube";

const TestPage: React.FC = () => {
    const [currentNumber, setNumber] = useState<number>(1);
    const [currentRotation, setRotation] = useState<string>('rotate-180');

    useEffect(() => {
        const timeout = setTimeout(() => {
            setNumber(currentNumber + 1 === 7 ? 1 : currentNumber + 1);
            setRotation(currentRotation === 'rotate-180' ? '-rotate-180' : 'rotate-180');
          }, 800);
    
          return () => clearTimeout(timeout);
    });

    return <Cube amount={currentNumber} rotation={currentRotation}></Cube>
};

export default TestPage;