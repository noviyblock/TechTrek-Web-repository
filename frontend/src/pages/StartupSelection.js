import React from "react";

const categories = [
  { name: "E-commerce", color: "#006FEE" },
  { name: "Fintech", color: "#F5A524" },
  { name: "Medtech", color: "#17C964" },
  { name: "GameTech", color: "#52525B" },
  { name: "EdTech", color: "#7828C8" },
  { name: "AgroTech", color: "#F31260" },
  { name: "Other...", color: "#D4D4D8" },
];

const StartupSelection = () => {
  return (
    <div className="startup-container">
      <h1>Добро пожаловать в игру для предпринимателей TechTrack!</h1>
      <div className="selection-box">
        <h2>Выберите сферу, в которой вы бы хотели запустить стартап:</h2>
        <div className="categories">
          {categories.map((category, index) => (
            <div
              key={index}
              className="category"
              style={{ backgroundColor: category.color }}
            >
              {category.name}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default StartupSelection;
