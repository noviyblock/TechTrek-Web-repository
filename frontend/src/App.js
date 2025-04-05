import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import StartupSelection from "./pages/StartupSelection";


function App() {
  console.log("App загружен");
  return (
    <Router>
      <Routes>
        <Route path="/" element={<StartupSelection />} />
        <Route path="/startup/:sector" element={<h1>Страница стартапа</h1>} />
      </Routes>
    </Router>
  );
}


export default App;
