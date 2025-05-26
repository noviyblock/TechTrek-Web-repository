import { useEffect, useRef, useState } from "react";
import { ChevronLeft, ChevronRight } from "lucide-react";
import BuyModifier from "../molecule/BuyCard";
import { ModifierProps } from "../../shared/Types";

const BuyCardCarousel: React.FC<{ modifiers: ModifierProps[] }> = ({
  modifiers,
}) => {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [isOverflowing, setIsOverflowing] = useState(false);
  const scrollByAmount = 250;

  const scrollLeft = () => {
    scrollRef.current?.scrollBy({ left: -scrollByAmount, behavior: "smooth" });
  };

  const scrollRight = () => {
    scrollRef.current?.scrollBy({ left: scrollByAmount, behavior: "smooth" });
  };

  useEffect(() => {
    const el = scrollRef.current;
    if (!el) return;

    const checkOverflow = () => {
      setIsOverflowing(el.scrollWidth > el.clientWidth);
    };

    checkOverflow();
    window.addEventListener("resize", checkOverflow);
    return () => window.removeEventListener("resize", checkOverflow);
  }, [modifiers]);

  return (
    <div className="w-full flex items-center gap-4">
      {/* Левая кнопка */}
      {isOverflowing && (
        <button
          className="text-white hover:text-gray-400 p-2 rounded-full"
          onClick={scrollLeft}
        >
          <ChevronLeft size={28} />
        </button>
      )}

      {/* Карусель */}
      <div
        ref={scrollRef}
        className={
          "flex gap-4 overflow-x-auto scroll-smooth items-stretch w-full min-h-80 " +
          (isOverflowing ? "justify-start" : "justify-center")
        }
        style={{ scrollSnapType: "x mandatory" }}
      >
        {modifiers.map((card, index) => (
          <div
            key={index}
            className="flex-shrink-0"
            style={{ scrollSnapAlign: "start" }}
          >
            <BuyModifier {...card} />
          </div>
        ))}
      </div>

      {isOverflowing && (
        <button
          className="text-white hover:text-gray-400 p-2 rounded-full"
          onClick={scrollRight}
        >
          <ChevronRight size={28} />
        </button>
      )}
    </div>
  );
};

export default BuyCardCarousel;
