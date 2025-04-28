import React from "react";
import { StackedPicture } from "../../shared/Types";

const PictureListStacked: React.FC<{
  stackedPictures: StackedPicture[];
  size: number;
}> = ({ stackedPictures, size }) => {
  return (
    <div className="flex">
      {stackedPictures.map((picture, index) => (
        <div
          key={index}
          className="overflow-hidden border-2 border-white shadow"
          style={{
            borderRadius: `${picture.rounding}px`,
            width: `${size}px`,
            height: `${size}px`,
            marginLeft: index === 0 ? 0 : `-${size / 2}px`,
            zIndex: stackedPictures.length - index,
          }}
        >
          <img
            src={picture.image}
            alt={"pic"}
            className="w-full h-full object-cover"
          />
        </div>
      ))}
    </div>
  );
};

export default PictureListStacked;
