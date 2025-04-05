import React from "react";
import { useNavigate } from "react-router-dom";
import "../App.css";

const sectors = [
  { name: "E-commerce", color: "#007bff" },
  { name: "Fintech", color: "#ffbf00" },
  { name: "Medtech", color: "#28a745" },
  { name: "GameTech", color: "#6c757d" },
  { name: "EdTech", color: "#6f42c1" },
  { name: "AgroTech", color: "#dc3545" },
];

const StartupSelection = () => {
  console.log("StartupSelection загружен");
  const navigate = useNavigate();

  
  

  return (
    <div className="startup-container">
      <h1>Добро пожаловать в TechTrack!</h1>
      <div className="selection-box">
        <h2>Выберите сферу, в которой вы хотите запустить стартап:</h2>
        <div className="categories">
          {sectors.map((sector) => (
            <button
              key={sector.name}
              className="category"
              style={{ backgroundColor: sector.color }}
              onClick={() => navigate(`/startup/${sector.name.toLowerCase()}`)}
            >
              {sector.name}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default StartupSelection;
