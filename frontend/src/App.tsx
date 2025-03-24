import './App.css';
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import StartupSelection from "./pages/StartupSelection";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<StartupSelection />} />
      </Routes>
    </Router>
  );
}

export default App;
