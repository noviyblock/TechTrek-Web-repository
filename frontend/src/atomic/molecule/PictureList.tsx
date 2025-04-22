import React from "react";
import { Picture } from "../../shared/Types";

const PictureList: React.FC<{
  pictures: Picture[];
  size: number;
}> = ({ pictures, size }) => {
  return (
    <div className="flex">
      {pictures.map((picture, index) => (
        <div
          key={index}
          className="overflow-hidden border-2 border-white shadow mr-3"
          style={{
            borderRadius: `${picture.rounding}px`,
            width: `${size}px`,
            height: `${size}px`,
            zIndex: pictures.length - index,
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

export default PictureList;
