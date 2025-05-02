import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import RegistrationPage from "../atomic/page/RegistrationPage";
import SphereSelectionPage from "../atomic/page/SphereSelectionPage";
import MissionPage from "../atomic/page/MissionPage";
import CommandNamePage from "../atomic/page/CommandNamePage";
import MainScreenLayout from "../atomic/template/MainScreenLayout";
import TestPage from "../atomic/page/TestPage";
import ActionBlock from "../atomic/molecule/ActionBlock";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element={<RegistrationPage />} />
        <Route path="/select" element={<SphereSelectionPage />} /> 
        <Route path="/mission" element={<MissionPage />} />
        <Route path="/command_name" element={<CommandNamePage />} />
        <Route path="/main" element={<MainScreenLayout />} />

        <Route path="/test" element={<ActionBlock header="Нанять ключевого специалиста" tooltip="Уменьшает время разработки, ускоряет техническую и продуктовую готовность" />}/>
      </Routes>
    </Router>
  );
}

export default App;
